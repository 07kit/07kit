package com.kit.game;

import com.kit.Application;
import com.kit.core.Session;
import com.kit.game.exception.ConfigurationFailedException;
import com.kit.game.impl.FakeAppletStub;
import com.kit.Application;
import com.kit.core.Session;
import com.kit.game.engine.IClient;
import com.kit.game.exception.ConfigurationFailedException;
import com.kit.game.impl.FakeAppletStub;
import com.kit.game.transform.model.Hooks;
import org.apache.log4j.Logger;
import com.kit.game.impl.FakeAppletStub;

import java.applet.Applet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * A loader class responsible for loading
 * the Oldschool RuneScape applet.
 */
public final class AppletLoader implements Callable<Applet> {
    private static final Logger logger = Logger.getLogger(AppletLoader.class);
    private final AppletConfiguration configuration;
    private final FakeAppletStub appletStub;
    private final Session session;
    private Applet applet;
    private File gamepack;

    public AppletLoader(Session session) {
        this.session = session;
        configuration = new AppletConfiguration();
        appletStub = new FakeAppletStub(configuration);
        gamepack = new File(System.getProperty("user.home") + "/07kit/gamepack.jar");
        try {
            configuration.load();
        } catch (IOException e) {
            throw new ConfigurationFailedException(e);
        }
    }

    private int getLocalHash() {
        try {
            if (!gamepack.exists()) {
                return -1;
            }
            URL url = gamepack.toURI().toURL();
            try (JarInputStream stream = new JarInputStream(url.openStream())) {
                return stream.getManifest().hashCode();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getRemoteHash(String jarUrl) {
        try {
            URL url = new URL(jarUrl);
            try (JarInputStream stream = new JarInputStream(url.openStream())) {
                return stream.getManifest().hashCode();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Applet call() throws Exception {
        String documentBase = configuration.getDocumentBase();
        String archiveName = configuration.getArchiveName();
        String mainClass = configuration.getMainClass();
        String jarUrlString = documentBase + archiveName;

        int remoteHash = getRemoteHash(jarUrlString);
        int localHash = getLocalHash();

        if (remoteHash == -1) {
            throw new RuntimeException("Error loading client, unable to get RS version");
        }

        final URL jarURL = new URL("jar:" + jarUrlString + "!/");
        JarFile jar;

        if (remoteHash == localHash) {
            jar = new JarFile(gamepack);
            logger.info("Found up-to-date cached gamepack: " + gamepack.getAbsolutePath());
        } else {
            logger.info("Found no up-to-date cached gamepacks, downloading...");
            JarURLConnection connection = (JarURLConnection) jarURL.openConnection();
            jar = connection.getJarFile();
            saveJar(jar);
        }

        try {
            if (Hooks.getHooks() == null) {
                logger.info("Hooks not loaded - loading now.");
                Path localPath = Paths.get(System.getProperty("07kit.hooks", Hooks.DEFAULT_PATH));
                if (!Hooks.loadFromFile(localPath) && !Hooks.loadFromUrl(Hooks.DEFAULT_URL)) {
                    logger.error("Couldn't load hooks - most features will not work properly!");
                }
            }

            AppletClassLoader classLoader = new AppletClassLoader(jar);
            classLoader.preload();
            if (!Application.outdated) {
                Class<?> client = classLoader.loadClass(mainClass);
                applet = (Applet) client.newInstance();
                appletStub.getAppletContext().setApplet(applet);
                applet.setStub(appletStub);
                session.client().setEventBus(Session.get().getEventBus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applet;
    }

    public void start() {
        applet.init();
        applet.start();
        session.onAppletLoaded();
    }

    private void saveJar(JarFile jar) {
        File dest = new File(System.getProperty("user.home") + "/07kit/gamepack.jar");
        logger.info("Caching " + jar.getName() + " to " + dest.getAbsolutePath());

        try {
            JarOutputStream out = new JarOutputStream(new FileOutputStream(dest));
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                out.putNextEntry(entry);

                InputStream in = jar.getInputStream(entry);
                while (in.available() > 0) {
                    out.write(in.read());
                }

                out.closeEntry();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AppletConfiguration getConfiguration() {
        return configuration;
    }

    public FakeAppletStub getAppletStub() {
        return appletStub;
    }

    public Applet getApplet() {
        return applet;
    }
}
