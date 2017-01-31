package com.kit.gui.component;

import com.kit.Application;
import com.kit.api.service.SocialService;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.gui.ControllerManager;
import com.kit.gui.controller.GalleryController;
import com.kit.gui.controller.MainController;
import com.kit.gui.controller.SettingsController;
import com.kit.gui.util.ComponentMover;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import com.kit.plugins.afk.AFKWatcherPlugin;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

/**
 */
public class MateTitleBar extends JPanel {

    public static final Color CLOSE_COLOR = new Color(220, 90, 90);
    public static final Color MAXIMISE_COLOR = new Color(230, 220, 70);
    public static final Color MINIMISE_COLOR = new Color(40, 175, 40);

    public MateTitleBar(JFrame parent, boolean isLogin) {
        // TODO: Get pattern graphic or something nice.
        // TODO: Get logo
        setPreferredSize(new Dimension(parent.getWidth(), 30));
        setBackground(Application.COLOUR_SCHEME.getSelected());
        setLayout(new BorderLayout());

        if (!isLogin) {
            JPanel rButtons = new JPanel();
            FlowLayout layout = new FlowLayout();
            layout.setVgap(0);
            layout.setHgap(0);
            rButtons.setLayout(layout);
            rButtons.setBorder(null);
            rButtons.getInsets().set(0, 0, 0, 0);
            rButtons.setBackground(Application.COLOUR_SCHEME.getSelected());

            MateTitleButton screenshot = new MateTitleButton(IconFontSwing.buildIcon(FontAwesome.CAMERA, 20, Application.COLOUR_SCHEME.getText()));
            screenshot.addActionListener((evt) -> {
                Session.get().screen.capture("Screenshot", true);
            });
            rButtons.add(screenshot);

            MateTitleButton gallery = new MateTitleButton(IconFontSwing.buildIcon(FontAwesome.PICTURE_O, 20, Application.COLOUR_SCHEME.getText()));
            gallery.addActionListener((evt) -> {
                ControllerManager.get(GalleryController.class).show();
            });
            rButtons.add(gallery);

            MateTitleButton watcher = new MateTitleButton(IconFontSwing.buildIcon(FontAwesome.TELEVISION, 20, Application.COLOUR_SCHEME.getText()));
            watcher.addActionListener((evt) -> {
                AFKWatcherPlugin afkWatcherPlugin = (AFKWatcherPlugin) Session.get().getPluginManager().getPlugins()
                        .stream().filter(p -> p.getClass().equals(AFKWatcherPlugin.class))
                        .findFirst().orElse(null);
                if (afkWatcherPlugin != null) {
                    if (afkWatcherPlugin.isEnabled()) {
                        afkWatcherPlugin.toggleDrawing();
                    } else {
                        NotificationsUtil.showNotification("AFK", "AFK Watcher Plugin is disabled, please enable it");
                    }
                } else {
                    NotificationsUtil.showNotification("AFK", "Error starting AFK Watcher");
                }
            });
            rButtons.add(watcher);

            MateTitleButton openSettings = new MateTitleButton(IconFontSwing.buildIcon(FontAwesome.COG, 20, Application.COLOUR_SCHEME.getText()));
            openSettings.addActionListener((evt) -> {
                ControllerManager.get(SettingsController.class).show();
            });
            rButtons.add(openSettings);

            MateTitleButton toggleSidebar = new MateTitleButton(IconFontSwing.buildIcon(FontAwesome.COLUMNS, 20, Application.COLOUR_SCHEME.getText()));
            toggleSidebar.addActionListener((evt) -> ((MainController) ControllerManager.get(MainController.class)).toggleSidebar());
            rButtons.add(toggleSidebar);

            add(rButtons, BorderLayout.EAST);

            JLabel title = new JLabel("07Kit");
            title.setFont(title.getFont().deriveFont(Font.BOLD));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setForeground(Application.COLOUR_SCHEME.getText());
            title.setOpaque(false);

            add(title, BorderLayout.CENTER);
        } else {
            JLabel title = new JLabel("07Kit - Please login");
            title.setFont(title.getFont().deriveFont(Font.BOLD));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setForeground(Application.COLOUR_SCHEME.getText());
            title.setOpaque(false);

            add(title, BorderLayout.CENTER);
        }

        JPanel lButtons = new JPanel();
        FlowLayout layout2 = new FlowLayout();
        layout2.setVgap(0);
        layout2.setHgap(0);
        lButtons.setLayout(layout2);
        lButtons.setBorder(null);
        lButtons.getInsets().set(0, 0, 0, 0);
        lButtons.setOpaque(false);
        lButtons.setBackground(Application.COLOUR_SCHEME.getSelected());

        MateTitleButton close = new MateTitleButton(IconFontSwing.buildIcon(FontAwesome.TIMES, 20, CLOSE_COLOR));
        close.addActionListener((evt) -> System.exit(1));

        lButtons.add(close);

        if (!isLogin) {
            MateTitleButton maxMinimize = new MateTitleButton(IconFontSwing.buildIcon(FontAwesome.EXPAND, 20, MAXIMISE_COLOR));
            maxMinimize.addActionListener((evt) -> ControllerManager.get(MainController.class).toggleMaximized());

            lButtons.add(maxMinimize);
        }

        MateTitleButton iconify = new MateTitleButton(IconFontSwing.buildIcon(FontAwesome.MINUS, 20, MINIMISE_COLOR));
        iconify.addActionListener((evt) -> ControllerManager.get(MainController.class).getComponent().setState(Frame.ICONIFIED));

        lButtons.add(iconify);

        add(lButtons, BorderLayout.WEST);

        /* Set up movement */
        ComponentMover cm = new ComponentMover(parent, this);
    }

    private class MateTitleButton extends JButton {

        public MateTitleButton(Icon icon) {
            super(icon);
            setFocusPainted(false);
            setOpaque(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            Color background;
            if (getModel().isPressed()) {
                background = Application.COLOUR_SCHEME.getClicked();
            } else if (getModel().isRollover()) {
                background = Application.COLOUR_SCHEME.getDark();
            } else {
                background = Application.COLOUR_SCHEME.getSelected();
            }
            g2.setColor(background);
            g2.fillRect(0, 0, getWidth(), getHeight());
            int x = (getWidth() / 2) - (getIcon().getIconWidth() / 2);
            int y = (getHeight() - getIcon().getIconHeight()) / 2;
            getIcon().paintIcon(this, g, x, y);
        }
        
    }

}
