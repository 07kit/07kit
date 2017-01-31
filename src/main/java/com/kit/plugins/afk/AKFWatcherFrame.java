package com.kit.plugins.afk;

import com.kit.api.MethodContext;
import com.kit.api.MethodContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Date: 09/12/2016
 * Time: 11:08
 *
 * @author Matt Collinge
 */
class AKFWatcherFrame extends JFrame {

    private DrawingPanel panel;

    AKFWatcherFrame(MethodContext ctx, Rectangle size) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        setResizable(false);
        setSize(new Dimension(size.width, size.height));
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getHeight() / 2);
        setLocation(centerX, centerY);
        panel = new DrawingPanel(ctx, size);
        add(panel, BorderLayout.CENTER);
    }

    public void setRectangle(Rectangle rectangle) {
        panel.setRectangle(rectangle);
        setSize(new Dimension(rectangle.width, rectangle.height));
    }

    void redraw() {
        SwingUtilities.invokeLater(() -> repaint());
    }

    private class DrawingPanel extends JPanel {

        private final MethodContext ctx;
        private Rectangle rectangle;

        DrawingPanel(MethodContext ctx, Rectangle rectangle) {
            this.ctx = ctx;
            this.rectangle = rectangle;
            setSize(new Dimension(rectangle.width, rectangle.height));
        }

        public void setRectangle(Rectangle rectangle) {
            this.rectangle = rectangle;
            setSize(new Dimension(rectangle.width, rectangle.height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(ctx.screen.capture(rectangle), 0, 0, null);        }
    }

}
