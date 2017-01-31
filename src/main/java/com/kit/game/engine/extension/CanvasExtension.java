package com.kit.game.engine.extension;

import com.google.common.base.Stopwatch;
import com.kit.api.event.BufferFlipEvent;
import com.kit.api.event.Events;
import com.kit.api.event.PaintEvent;
import com.kit.core.Session;
import com.kit.api.event.Events;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.concurrent.TimeUnit;

/**
 * An extended version of the canvas.
 *
 */
public class CanvasExtension extends Canvas {
    private VolatileImage backBuffer;
    private VolatileImage gameBuffer;
    private Session session;

    public CanvasExtension() {
        createBackbuffers(765, 503);
    }

    public static GraphicsConfiguration getConfig() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        return device.getDefaultConfiguration();
    }
    /**
     * Flips the graphics buffer, after
     * which it returns a clean one
     * for the applet to draw on.
     *
     * @return graphics
     */
    @Override
    public Graphics getGraphics() {
        try {
            GraphicsConfiguration gc = getConfig();
            if(getBackBuffer().validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE ||
                    getGameBuffer().validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                createBackbuffers(Session.get().getClient().getViewportWidth(), Session.get().getClient().getViewportHeight());
            }

            Graphics gameGraphics = gameBuffer.getGraphics();
            // Draw the graphics buffer (bot itself)
            Graphics bufferGraphics = backBuffer.getGraphics();
            bufferGraphics.drawImage(gameBuffer, 0, 0, null);
            handleSession(bufferGraphics);
            Graphics base = super.getGraphics();
            if (base != null) {
                base.drawImage(backBuffer, 0, 0, this);
            }
            return gameGraphics;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getGraphics();
    }

    /**
     * Handles all the drawing and linking for the session.
     *
     * @param bufferGraphics graphics
     */
    private void handleSession(Graphics bufferGraphics) {
        if (getParent() != null) {
            if (session == null) {
                session = Session.get();
            } else {
                Events eventBus = session.getEventBus();
                if (bufferGraphics != null) {
                    eventBus.submitPaintEvent(new PaintEvent(bufferGraphics));
                    eventBus.submitBufferFlipEvent(new BufferFlipEvent(gameBuffer));
                }
            }
        }
    }

    public void createBackbuffers(int width, int height) {
        GraphicsConfiguration config = getConfig();
        if (width <= 0 || height <= 0) {
            width = 1;
            height = 1;
        }
        backBuffer = config.createCompatibleVolatileImage(width, height);
        gameBuffer = config.createCompatibleVolatileImage(width, height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(0, 0, width, height);
        if (backBuffer.getWidth() != width || backBuffer.getHeight() != height) {
            createBackbuffers(width, height);
        }
    }

    public synchronized VolatileImage getBackBuffer() {
        return backBuffer;
    }


    public synchronized VolatileImage getGameBuffer() {
        return gameBuffer;
    }
}
