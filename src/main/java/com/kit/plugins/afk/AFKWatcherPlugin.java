package com.kit.plugins.afk;

import com.kit.Application;
import com.kit.api.event.LoginEvent;
import com.kit.api.plugin.Plugin;
import com.kit.game.engine.extension.CanvasExtension;
import javafx.scene.input.MouseDragEvent;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Date: 09/12/2016
 * Time: 10:57
 *
 * @author Matt Collinge
 */
public class AFKWatcherPlugin extends Plugin {

    private static final Color OVERLAY_COLOUR = new Color(50, 50, 50, 150);

    final Rectangle rectangle;
    boolean drawingArea = false;

    private int startX, startY, endX, endY = 0;

    private AKFWatcherFrame frame;

    public AFKWatcherPlugin(PluginManager manager) {
        super(manager);
        rectangle = new Rectangle(0, 0, 0, 0);
        frame = new AKFWatcherFrame(AFKWatcherPlugin.this, rectangle);
    }

    @Override
    public String getName() {
        return "AFK Watcher";
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        CanvasExtension ext = (CanvasExtension) Session.get().client().getCanvas();
        ext.createBackbuffers(Session.get().getClient().getViewportWidth(), Session.get().getClient().getViewportHeight());
    }

    @EventHandler
    public void onMousePressed(MouseEvent event) {
        if (!drawingArea)
            return;

        try {
            switch (event.getID()) {
                case MouseEvent.MOUSE_PRESSED:
                    if (SwingUtilities.isRightMouseButton(event)) {
                        drawingArea = false;
                        return;
                    }
                    startX = endX = event.getX();
                    startY = endY = event.getY();
                    setRectangle();
                    break;

                case MouseEvent.MOUSE_RELEASED:
                    endX = event.getX();
                    endY = event.getY();
                    setRectangle();
                    drawingArea = false;
                    SwingUtilities.invokeLater(() -> {
                        frame.setRectangle(rectangle);
                        frame.setVisible(true);
                        startX = startY = endX = endY = 0;
                    });
                    break;
            }
        } finally {
          event.consume();
        }
    }

    @EventHandler
    public void onMouseDragged(MouseEvent event) {
        if (!drawingArea)
            return;

        if (event.getID() != MouseEvent.MOUSE_DRAGGED)
            return;

        endX = event.getX();
        endY = event.getY();
        setRectangle();
        event.consume();
    }

    @EventHandler
    public void paint(PaintEvent event) {
        if (frame.isVisible())
            frame.redraw();

        if (!drawingArea)
            return;

        Graphics2D g2d = (Graphics2D) event.getGraphics();

        int width = client().getCanvas().getWidth();
        int height = client().getCanvas().getHeight();

        g2d.setColor(OVERLAY_COLOUR);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Application.COLOUR_SCHEME.getText());
        g2d.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private void setRectangle() {
        rectangle.x = Math.min(startX, endX);
        rectangle.y = Math.min(startY, endY);
        rectangle.width = Math.max(startX, endX) - rectangle.x;
        rectangle.height = Math.max(startY, endY) - rectangle.y;
    }

    public void toggleDrawing() {
        if (frame.isVisible())
            frame.setVisible(false);
        drawingArea = !drawingArea;
    }
}
