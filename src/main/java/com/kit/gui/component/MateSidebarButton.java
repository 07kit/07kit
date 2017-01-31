package com.kit.gui.component;

import com.kit.Application;
import com.kit.Application;
import com.kit.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 */
public class MateSidebarButton extends JPanel {
    private final SidebarWidget widget;
    private final ActionListener listener;
    private final MateSidebarButton self;
    private boolean highlighted;
    private boolean toggled;

    public MateSidebarButton(SidebarWidget widget, ActionListener listener) {
        this.self = this;
        this.widget = widget;
        this.listener = listener;

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        setMaximumSize(new Dimension(35, 35));
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Application.COLOUR_SCHEME.getDark());
        g.fillRect(0, 0, getWidth(), getHeight());

        if (!toggled) {
                /* Draw stripe on the left side indicating hover */
            if (!highlighted) {
                g.setColor(Application.COLOUR_SCHEME.getBright());
                g.fillRect(getWidth() - 2, 0, 2, getHeight());
            } else {
                g.setColor(Application.COLOUR_SCHEME.getHighlight());
                g.fillRect(getWidth() - 2, 0, 2, getHeight());
            }
        } else {
                /* Draw stripe on the left side indicating active */
            g.setColor(Application.COLOUR_SCHEME.getSelected());
            g.fillRect(getWidth() - 2, 0, 2, getHeight());
        }

        g.drawImage(widget.getIcon(toggled), 10, 5, null);
    }

    public SidebarWidget getWidget() {
        return widget;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        repaint();
    }

    private final MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseReleased(MouseEvent e) {
            listener.actionPerformed(new ActionEvent(self, 1, "sidebar_widget"));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            highlighted = false;
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            highlighted = true;
            repaint();
        }

    };

}