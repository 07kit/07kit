package com.kit.api.impl.game;


import com.kit.api.event.MessageEvent;
import com.kit.api.event.ScreenshotEvent;
import com.kit.core.Session;
import com.kit.game.engine.extension.CanvasExtension;
import org.apache.log4j.Logger;
import com.kit.api.Screen;
import com.kit.api.MethodContext;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.ScreenshotEvent;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.engine.extension.CanvasExtension;
import com.kit.api.MethodContext;
import com.kit.game.engine.extension.CanvasExtension;
import com.kit.plugins.quickchat.QuickChatPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class ScreenImpl implements Screen {
    private final Logger logger = Logger.getLogger(getClass());
    private final MethodContext ctx;


    public ScreenImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point> findColor(Color color, int tolerance) {
        List<Point> matches = new ArrayList<Point>();
        if (ctx.client().getCanvas() instanceof CanvasExtension) {
            CanvasExtension ext = (CanvasExtension) ctx.client().getCanvas();
            BufferedImage buffer = ext.getGameBuffer().getSnapshot();
            for (int x = 0; x < ext.getWidth(); x++) {
                for (int y = 0; y < ext.getHeight(); y++) {
                    Color colorAtPoint = new Color(buffer.getRGB(x, y));
                    if (Math.abs(colorAtPoint.getRed() - color.getRed()) < tolerance &&
                            Math.abs(colorAtPoint.getGreen() - color.getGreen()) < tolerance &&
                            Math.abs(colorAtPoint.getBlue() - color.getBlue()) < tolerance) {
                        matches.add(new Point(x, y));
                    }
                }
            }
        }
        return matches;
    }

    @Override
    public Path capture(String fileName, boolean generateEvent) {
        if (ctx.client().getCanvas() instanceof CanvasExtension) {
            String loginNamePath = getCharacterFolder();
            CanvasExtension ext = (CanvasExtension) ctx.client().getCanvas();
            VolatileImage buffer = ext.getBackBuffer();

            Path screenshotTarget = Paths.get(System.getProperty("user.home"))
                    .resolve("07kit")
                    .resolve("Screenshots")
                    .resolve(loginNamePath);
            try {
                Files.createDirectories(screenshotTarget);

                String formattedTimestamp = LocalDateTime.now().toString().replace(':', '_');
                Path outputFile = screenshotTarget.resolve(String.format("%s_%s.png", fileName, formattedTimestamp));
                ImageIO.write(buffer.getSnapshot(), "png", outputFile.toFile());

                QuickChatPlugin.sendThroughChatBox(String.format("<col=ff0000>Screenshot saved: %s</col>", outputFile.toAbsolutePath().toString()),
                        "", "", MessageEvent.Type.MESSAGE_SERVER_FILTERED, false);

                if (generateEvent) {
                    Session.get().getEventBus().submit(new ScreenshotEvent(outputFile.toFile(), outputFile.getFileName().toString(), ""));
                }
                return outputFile;
            } catch (IOException e) {
                QuickChatPlugin.sendThroughChatBox(String.format("<col=ff0000>Couldn't save screenshot %s</col>", fileName),
                        "", "",
                        MessageEvent.Type.MESSAGE_SERVER_FILTERED, false);
                logger.error("Failed to store screenshot.", e);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage capture(Rectangle rectangle) {
        CanvasExtension ext = (CanvasExtension) ctx.client().getCanvas();
        BufferedImage buffer = ext.getBackBuffer().getSnapshot();
        if (rectangle.width == 0 || rectangle.height == 0)
            return buffer;
        if (rectangle.width + rectangle.x > buffer.getWidth())
            return buffer;
        if (rectangle.height + rectangle.y > buffer.getHeight())
            return buffer;

        return buffer.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private String getCharacterFolder() {
        String loginNamePath = "";
        if (ctx.isLoggedIn()) {
            String loginName = ctx.client().getUsername();
            if (loginName != null && loginName.trim().length() > 0) {
                loginNamePath = loginName + File.separator;
            }
        }
        return loginNamePath;
    }
}
