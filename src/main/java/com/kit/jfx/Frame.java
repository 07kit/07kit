package com.kit.jfx;

import com.kit.Application;
import com.kit.core.Session;
import com.kit.core.model.Property;
import com.kit.gui.ControllerManager;
import com.kit.gui.controller.SidebarController;
import com.kit.gui.util.ComponentResizer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Frame extends JFrame {

    private ComponentAdapter componentAdapter = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            if (Frame.this.getExtendedState() == JFrame.MAXIMIZED_BOTH)
                return;

            String width = String.valueOf(e.getComponent().getWidth());
            String height = String.valueOf(e.getComponent().getHeight());
            Property widthProperty = Property.get(WIDTH_PROPERTY_KEY);
            Property heightProperty = Property.get(HEIGHT_PROPERTY_KEY);

            if (widthProperty == null) widthProperty = new Property(WIDTH_PROPERTY_KEY, width);
            else widthProperty.setValue(width);

            if (heightProperty == null) heightProperty = new Property(HEIGHT_PROPERTY_KEY, height);
            else heightProperty.setValue(height);

            widthProperty.save();
            heightProperty.save();
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            String x = String.valueOf(e.getComponent().getLocation().x);
            String y = String.valueOf(e.getComponent().getLocation().y);
            Property xProperty = Property.get(X_PROPERTY_KEY);
            Property yProperty = Property.get(Y_PROPERTY_KEY);

            if (xProperty == null) xProperty = new Property(X_PROPERTY_KEY, x);
            else xProperty.setValue(x);

            if (yProperty == null) yProperty = new Property(Y_PROPERTY_KEY, y);
            else yProperty.setValue(y);

            xProperty.save();
            yProperty.save();
        }
    };

    public static final String WIDTH_PROPERTY_KEY = "client_width";
    public static final String HEIGHT_PROPERTY_KEY = "client_height";
    public static final String X_PROPERTY_KEY = "client_x";
    public static final String Y_PROPERTY_KEY = "client_y";
    public static final int STANDARD_WIDTH = 1020;
    public static final int STANDARD_HEIGHT = 540;

    private JFXPanel titleBarPanel = new JFXPanel();
    private JFXPanel loadingPanel = new JFXPanel();
    private JFXPanel sidebarPanel = new JFXPanel();
    private JPanel displayPanel = new JPanel();
    private boolean hasSideBar = false;

    public Frame() {
        super("07Kit");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Property widthProperty = Property.get(WIDTH_PROPERTY_KEY);
        if (widthProperty == null) {
            widthProperty = new Property(WIDTH_PROPERTY_KEY, String.valueOf(STANDARD_WIDTH));
            widthProperty.save();
        }
        int width = Integer.parseInt(widthProperty.getValue());

        Property heightProperty = Property.get(HEIGHT_PROPERTY_KEY);
        if (heightProperty == null) {
            heightProperty = new Property(HEIGHT_PROPERTY_KEY, String.valueOf(STANDARD_HEIGHT));
            heightProperty.save();
        }
        int height = Integer.valueOf(heightProperty.getValue());

        Dimension size = new Dimension(width, height);
        setMinimumSize(size);
        setPreferredSize(size);
        setResizable(true);
        setUndecorated(true);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Property xProperty = Property.get(X_PROPERTY_KEY);
        if (xProperty == null) {
            xProperty = new Property(X_PROPERTY_KEY, String.valueOf((toolkit.getScreenSize().width / 2) - (getWidth() / 2)));
            xProperty.save();
        }
        int x = Integer.parseInt(xProperty.getValue());

        Property yProperty = Property.get(Y_PROPERTY_KEY);
        if (yProperty == null) {
            yProperty = new Property(X_PROPERTY_KEY, String.valueOf((toolkit.getScreenSize().height / 2) - (getHeight() / 2)));
            yProperty.save();
        }
        int y = Integer.parseInt(yProperty.getValue());

        setLocation(new Point(x, y));

        addComponentListener(componentAdapter);

        setLayout(new BorderLayout());

        ComponentResizer resizer = new ComponentResizer();
        resizer.setDragInsets(new Insets(4,4, 4, 4));
        resizer.registerComponent(this);

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        getContentPane().add(titleBarPanel, BorderLayout.NORTH);

        displayPanel.setBackground(new Color(248, 249, 250));
        displayPanel.setPreferredSize(new Dimension(765, 510));
        displayPanel.setMinimumSize(new Dimension(765, 510));
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));

        displayPanel.add(loadingPanel, BorderLayout.CENTER);
        //displayPanel.add(sidebarPanel, BorderLayout.WEST);
        getContentPane().add(displayPanel, BorderLayout.CENTER);

        initJfx();
        loadGame();
        pack();
    }

    private void initJfx() {
        Platform.runLater(() -> {
            Font.loadFont(getClass().getClassLoader().getResource("jfx/fonts/UbuntuMono-R.ttf").toExternalForm(), 0.0);
            Parent titleBarRoot = JFX.load("titlebar", "jfx/views/titlebar.fxml");
            Scene titleBarScene = new Scene(titleBarRoot);
            titleBarPanel.setScene(titleBarScene);

            Parent loadingRoot = JFX.load("loading", "jfx/views/loading.fxml");

            Scene loadingRootScene = new Scene(loadingRoot);
            loadingPanel.setScene(loadingRootScene);

            Parent sidebarRoot = JFX.load("sidebar", "jfx/views/sidebar.fxml");
            Scene sidebarRootScene = new Scene(sidebarRoot);
            sidebarPanel.setScene(sidebarRootScene);
        });
    }

    private void loadGame() {
        SwingWorker worker = new SwingWorker() {
            protected Object doInBackground() throws Exception {
                try {
                    Session.get().getAppletLoader().call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Session.get().getAppletView().setApplet(Session.get().getAppletLoader().getApplet());
                Session.get().getAppletLoader().getApplet().setSize(displayPanel.getWidth(), displayPanel.getHeight());
                Session.get().getAppletLoader().getApplet().setVisible(true);
                Session.get().getAppletLoader().start();
                while (!Session.get().getPluginManager().isPluginsStarted()) {
                    Thread.sleep(150);
                }
                //Session.get().getAppletView().showApplet();
                displayPanel.remove(loadingPanel);
                loadingPanel = null;
                toggleSidebar();
                displayPanel.add(Session.get().getAppletLoader().getApplet(), BorderLayout.CENTER);
                displayPanel.revalidate();
                displayPanel.repaint();
                return null;
            }
        };
        worker.execute();
        //displayPanel.add(Session.get().getAppletView(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void toggleSidebar() {
        int width = sidebarPanel.getWidth();
        Dimension dimension = new Dimension(getSize());
        if (hasSideBar) {
            //displayPanel.remove(ControllerManager.get(SidebarController.class).getComponent());
            displayPanel.remove(sidebarPanel);
            dimension.setSize(dimension.width - width, dimension.height);
        } else {
            //displayPanel.add(ControllerManager.get(SidebarController.class).getComponent(), BorderLayout.WEST);
            displayPanel.add(sidebarPanel, BorderLayout.WEST);
            dimension.setSize(dimension.width + width, dimension.height);
        }
        displayPanel.setMinimumSize(dimension);
        displayPanel.setPreferredSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        displayPanel.revalidate();
        pack();
    }

    public void present() {
        setIconImage(Application.ICON_IMAGE);
        setVisible(true);
    }

}
