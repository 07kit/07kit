package com.kit.gui.view;


import com.kit.Application;
import com.kit.core.Session;
import com.kit.core.model.Property;
import com.kit.gui.component.MateTitleBar;
import com.kit.gui.controller.MainController;
import com.kit.gui.controller.SidebarController;
import com.kit.gui.util.ComponentResizer;
import com.kit.gui.ControllerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author const_
 * @author A_C
 */
public class MainView extends JFrame {

    public static final String WIDTH_PROPERTY_KEY = "client_width";
    public static final String HEIGHT_PROPERTY_KEY = "client_height";
    public static final String X_PROPERTY_KEY = "client_x";
    public static final String Y_PROPERTY_KEY = "client_y";
    public static final int STANDARD_WIDTH = 1020;
    public static final int STANDARD_HEIGHT = 540;

    private Dimension size;

    private MateTitleBar titleBar;
    private JPanel displayPanel;
    private boolean hasSidebar = false;

    public MainView(final MainController mainController) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Property widthProp = Property.get(WIDTH_PROPERTY_KEY);
        if (widthProp == null) {
            widthProp = new Property(WIDTH_PROPERTY_KEY, String.valueOf(STANDARD_WIDTH));
            widthProp.save();
        }
        int width = Integer.parseInt(widthProp.getValue());

        Property heightProp = Property.get(HEIGHT_PROPERTY_KEY);
        if (heightProp == null) {
            heightProp = new Property(HEIGHT_PROPERTY_KEY, String.valueOf(STANDARD_HEIGHT));
            heightProp.save();
        }

        int height = Integer.parseInt(heightProp.getValue());

        size = new Dimension(width, height);
        setMinimumSize(size);
        setPreferredSize(size);
        setResizable(true);
        setUndecorated(true);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Property xProp = Property.get(X_PROPERTY_KEY);
        if (xProp == null) {
            xProp = new Property(X_PROPERTY_KEY, String.valueOf((toolkit.getScreenSize().width / 2) - (getWidth() / 2)));
            xProp.save();
        }
        Property yProp = Property.get(Y_PROPERTY_KEY);
        if (yProp == null) {
            yProp = new Property(Y_PROPERTY_KEY, String.valueOf((toolkit.getScreenSize().height / 2) - (getHeight() / 2)));
            yProp.save();
        }
        setLocation(Integer.parseInt(xProp.getValue()), Integer.parseInt(yProp.getValue()));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                String x = String.valueOf(e.getComponent().getLocation().x);
                String y = String.valueOf(e.getComponent().getLocation().y);
                Property xProp = Property.get(X_PROPERTY_KEY);
                Property yProp = Property.get(Y_PROPERTY_KEY);
                if (xProp == null) {
                    xProp = new Property(X_PROPERTY_KEY, x);
                } else {
                    xProp.setValue(x);
                }
                if (yProp == null) {
                    yProp = new Property(X_PROPERTY_KEY, y);
                } else {
                    yProp.setValue(y);
                }
                xProp.save();
                yProp.save();
            }

            public void componentResized(ComponentEvent e) {
                String width = String.valueOf(e.getComponent().getWidth());
                String height = String.valueOf(e.getComponent().getHeight());
                Property widthProp = Property.get(WIDTH_PROPERTY_KEY);
                Property heightProp = Property.get(HEIGHT_PROPERTY_KEY);

                if (widthProp == null) {
                    widthProp = new Property(WIDTH_PROPERTY_KEY, width);
                } else {
                    widthProp.setValue(width);
                }
                if (heightProp == null) {
                    heightProp = new Property(HEIGHT_PROPERTY_KEY, height);
                } else {
                    heightProp.setValue(height);
                }
                widthProp.save();
                heightProp.save();
            }
        });

        setLayout(new BorderLayout());

        ComponentResizer cr = new ComponentResizer();
        cr.setDragInsets(new Insets(4, 4, 4, 4));
        cr.registerComponent(this);

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        titleBar = new MateTitleBar(this, false);
        getContentPane().add(titleBar, BorderLayout.NORTH);

        displayPanel = new JPanel();
        displayPanel.setBackground(Application.COLOUR_SCHEME.getDark());
        displayPanel.setPreferredSize(new Dimension(765, 510));
        displayPanel.setMinimumSize(new Dimension(765, 510));
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        getContentPane().add(displayPanel, BorderLayout.CENTER);
        loadGame();
        pack();
    }

    public JPanel getDisplayPanel() {
        return displayPanel;
    }

    private void loadGame() {
        SwingWorker worker = new SwingWorker() {
            protected Object doInBackground() throws Exception {
                try {
                    Application.SESSION.getAppletLoader().call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Application.APPLET_VIEW.setApplet(Application.SESSION.getAppletLoader().getApplet());
                Application.SESSION.getAppletLoader().start();
                while (!Session.get().getPluginManager().isPluginsStarted()) {
                    Thread.sleep(150);
                }
                Application.APPLET_VIEW.showApplet();
                return null;
            }
        };
        worker.execute();
        displayPanel.add(Application.APPLET_VIEW, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void toggleSidebar() {
        if (hasSidebar) {
            int width = ControllerManager.get(SidebarController.class).getComponent().getWidth();
            displayPanel.remove(ControllerManager.get(SidebarController.class).getComponent());

            Dimension newMin = new Dimension(getSize());
            newMin.setSize(newMin.width - width, newMin.height);

            displayPanel.setMinimumSize(newMin);
            displayPanel.setPreferredSize(newMin);
            setMinimumSize(newMin);
            setPreferredSize(newMin);

            displayPanel.revalidate();
            pack();
        } else {
            Dimension size = new Dimension(getSize());

            int width = ControllerManager.get(SidebarController.class).getComponent().getWidth();
            size.width += width;

            displayPanel.add(ControllerManager.get(SidebarController.class).getComponent(), BorderLayout.WEST);
            displayPanel.setMinimumSize(size);
            displayPanel.setPreferredSize(size);
            setMinimumSize(size);
            setPreferredSize(size);

            displayPanel.revalidate();
            pack();
        }
        hasSidebar = !hasSidebar;
        revalidate();
        repaint();
    }

    public Dimension getDefaultSize() {
        return new Dimension(STANDARD_WIDTH, STANDARD_HEIGHT);
    }
}

