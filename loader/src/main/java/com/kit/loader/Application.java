package com.kit.loader;

import com.kit.loader.gui.LoadingFrame;
import com.kit.loader.pack.Downloader;
import com.kit.loader.pack.Loader;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class Application {

    private Downloader downloader;
    private LoadingFrame loadingFrame;

    public static Image ICON_IMAGE;

    static {
        try {
            ICON_IMAGE = ImageIO.read(Application.class.getResourceAsStream("/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Application(Downloader downloader, LoadingFrame loadingFrame) {
        this.downloader = downloader;
        this.loadingFrame = loadingFrame;
    }

    public void load() {
        try {
            JarFile jar = new JarFile(downloader.downloadLatestPack());
            Map<String, byte[]> resources = new HashMap<>();
            Enumeration<JarEntry> enumeration = jar.entries();

            while (enumeration.hasMoreElements()) {
                JarEntry next = enumeration.nextElement();
                InputStream inputStream = jar.getInputStream(next);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                if (bytes != null) {
                    String name = next.getName();
                    if (name.endsWith(".class")) {
                        name = name.replaceAll("/", ".").replaceAll(".class", "");
                    }
                    resources.put(name, bytes);
                }
            }
            Loader loader = new Loader(resources);
            Class<?> clazz = loader.loadClass("com.kit.Application");
            Method main = clazz.getMethod("main", String[].class);

            if (main != null) {
                main.invoke(null, (Object) new String[]{});
            } else {
                loadingFrame.setLoadingText("Error loading client [ErrorCode: 16E]");
                throw new RuntimeException("Error loading client");
            }

            loadingFrame.dispose();
        } catch (Exception e1) {
            e1.printStackTrace();
            loadingFrame.setLoadingText("Error loading client [ErrorCode: 16A]");
            throw new RuntimeException("Error loading client");
        }
    }

    private static class InputStreamConsumer implements Runnable {
        private final InputStream stream;

        public InputStreamConsumer(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public void run() {
            final BufferedReader input = new BufferedReader(new
                    InputStreamReader(stream));
            String buf = "";
            try {
                while ((buf = input.readLine()) != null) {
                    System.out.println(buf);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        setOSXDockIcon();
        LoadingFrame frame = new LoadingFrame();
        Downloader downloader = new Downloader(frame);

        Application application = new Application(
                downloader, frame
        );

        application.load();

    }

    private static void setOSXDockIcon() {
        if (System.getProperty("os.name").contains("OS X")) {
            try {
                Object applicationObject = Class.forName("com.apple.eawt.Application")
                        .getDeclaredMethod("getApplication", new Class[]{})
                        .invoke(null, new Class[]{});
                Class<?> applicationClass = applicationObject.getClass();

                Method setDockIconImage = applicationClass.getDeclaredMethod("setDockIconImage", new Class[]{Image.class});
                setDockIconImage.invoke(applicationObject, (Object) ICON_IMAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
