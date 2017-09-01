package com.kit;

import com.kit.api.wrappers.Skill;
import com.kit.core.Session;
import com.kit.core.model.Property;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.Appender;
import com.kit.gui.controller.*;
import com.kit.gui.laf.ColourScheme;
import com.kit.gui.laf.DarkColourScheme;
import com.kit.gui.view.AppletView;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Application entry point
 * ttttt
 */
public class Application {
    public static final ColourScheme COLOUR_SCHEME;
    public static boolean outdated;
    public static boolean devMode;

    public static Image ICON_IMAGE;

    public static final Map<Skill, Image> SKILL_IMAGE_ICONS = new HashMap<>();
    public static final Map<String, Image> FLAG_IMAGES = new HashMap<>();

    static {
        IconFontSwing.register(FontAwesome.getIconFont());
        COLOUR_SCHEME = new DarkColourScheme();
        System.setProperty("sun.java2d.opengl", "True");
        try {
            ICON_IMAGE = ImageIO.read(Application.class.getResourceAsStream("/icon.png"));

            FLAG_IMAGES.put("Germany", ImageIO.read(Application.class.getResourceAsStream("/flag_DE.png")));
            FLAG_IMAGES.put("United Kingdom", ImageIO.read(Application.class.getResourceAsStream("/flag_GB.png")));
            FLAG_IMAGES.put("United States", ImageIO.read(Application.class.getResourceAsStream("/flag_US.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (final Skill s : Skill.values()) {
            try {
                if (s.getIndex() > Skill.CONSTRUCTION.getIndex()) {
                    break;
                }
                SKILL_IMAGE_ICONS.put(s, ImageIO.read(Application.class.getResourceAsStream("/" + s.name().toLowerCase() + ".gif")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().addAppender(new Appender(new SimpleLayout()));
        final Logger logger = Logger.getLogger(Application.class);
        try {
            if (args.length > 0 && args[0] != null && args[0].trim().equals("-dev")) {
                devMode = true;
            }

            setOSXDockIcon();
            prepareEnvironment();

            SwingUtilities.invokeAndWait(() -> {
                IconFontSwing.register(FontAwesome.getIconFont());
                COLOUR_SCHEME.init();
                new SidebarController();
                //new MainController();
                new SettingsDebugController();
                new WidgetDebugController();
                new SettingsController();
                new GalleryController();

                Session.get().onAuthenticated();
                //ControllerManager.get(MainController.class).show();
                Session.get().getFrame().present();
            });
        } catch (Throwable t) {
            logger.error("Initialization failed.", t);
        }
    }

    private static void prepareEnvironment() {
        final File uids = new File(System.getProperty("user.home") + "/07kit/uids/");
        if (uids.exists()) {
            for (File uid : uids.listFiles()) {
                uid.delete();
            }
        } else {
            uids.mkdirs();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> Property.getContainer().save()));


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            Logger logger = Logger.getLogger("EXCEPTIONS");

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logger.error("Exception on thread " + t.getName(), e);
            }
        });

        Property.getContainer().load();
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
