package com.kit.gui.view;

import com.kit.Application;
import com.kit.gui.component.LoadingContainer;
import com.kit.gui.controller.MainController;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.painter.BusyPainter;
import com.kit.Application;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.LoadingContainer;
import com.kit.gui.controller.MainController;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * The AppletView is responsible for rendering an applet (or loading message if its bot done yet).
 *
 * * @author const_
 */
public class AppletView extends JPanel {

    private LoadingContainer loadingContainer;
    private Applet applet;

    public AppletView() {
        setPreferredSize(new Dimension(765, 503));
        setLayout(new GridBagLayout());
        setBackground(Application.COLOUR_SCHEME.getDark());
        setBorder(BorderFactory.createLineBorder(Application.COLOUR_SCHEME.getDark(), 2));
        loadingContainer = new LoadingContainer();
        loadingContainer.setBackground(Application.COLOUR_SCHEME.getDark());
        add(loadingContainer);
    }

    public Applet getApplet() {
        return applet;
    }

    public void setApplet(final Applet applet) {
        final Applet self = this.applet;
        if (applet == null) {
            EventQueue.invokeLater(() -> {
                if (self != null) {
                    remove(self);
                }
            });
        } else {
            this.applet = applet;
            EventQueue.invokeLater(() -> {
                setLayout(new BorderLayout());
                applet.setSize(getWidth(), getHeight());
                applet.setVisible(true);
            });
        }
    }

    public void showApplet() {
        remove(loadingContainer);
        ControllerManager.get(MainController.class).toggleSidebar();
        add(applet, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void refresh() {
        loadingContainer.getLoadingLabel().setText("07Kit is currently outdated, check twitter for updates");
        loadingContainer.revalidate();
        loadingContainer.repaint();
    }
}


