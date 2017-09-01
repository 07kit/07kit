package com.kit.jfx.controllers;

import com.kit.core.Session;
import com.kit.core.model.Property;
import com.kit.gui.ControllerManager;
import com.kit.gui.controller.GalleryController;
import com.kit.gui.controller.SettingsController;
import com.kit.jfx.Frame;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.awt.*;

public class TitleBarController {

    private int xOffset = 0;
    private int yOffset = 0;

    public void onMinimize(ActionEvent event) {
        Session.get().getFrame().setState(Frame.ICONIFIED);
    }

    public void onMaximize(ActionEvent event) {
        if (Session.get().getFrame().getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            Session.get().getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            Session.get().getFrame().setExtendedState(0);

            Property widthProperty = Property.get(Frame.WIDTH_PROPERTY_KEY);
            if (widthProperty == null) {
                widthProperty = new Property(Frame.WIDTH_PROPERTY_KEY, String.valueOf(Frame.STANDARD_WIDTH));
                widthProperty.save();
            }
            int width = Integer.parseInt(widthProperty.getValue());

            Property heightProperty = Property.get(Frame.HEIGHT_PROPERTY_KEY);
            if (heightProperty == null) {
                heightProperty = new Property(Frame.HEIGHT_PROPERTY_KEY, String.valueOf(Frame.STANDARD_HEIGHT));
                heightProperty.save();
            }
            int height = Integer.parseInt(heightProperty.getValue());

            Dimension dimension = new Dimension(width, height);
            Session.get().getFrame().setMinimumSize(dimension);
            Session.get().getFrame().setMaximumSize(dimension);
            Session.get().getFrame().setPreferredSize(dimension);
            Session.get().getFrame().setSize(dimension);

        }
    }

    public void onClose(ActionEvent event) {
        System.exit(1);
    }

    public void onScreenShot(ActionEvent actionEvent) {
        Session.get().screen.capture("Screenshot", true);
    }

    public void onShowGallery(ActionEvent actionEvent) {
        ControllerManager.get(GalleryController.class).show();
    }

    public void onShowWatcher(ActionEvent actionEvent) {

    }

    public void onShowSettings(ActionEvent actionEvent) {
        ControllerManager.get(SettingsController.class).show();
    }

    public void onToggleSidebar(ActionEvent actionEvent) {
        Session.get().getFrame().toggleSidebar();
    }

    public void setWindowOffset(MouseEvent mouseEvent) {
        xOffset = Math.toIntExact(Session.get().getFrame().getX() - Math.round(mouseEvent.getScreenX()));
        yOffset = Math.toIntExact(Session.get().getFrame().getY() - Math.round(mouseEvent.getScreenY()));

    }

    public void setWindowLocation(MouseEvent mouseEvent) {
        Session.get().getFrame().setLocation(new Point(Math.toIntExact(Math.round(mouseEvent.getScreenX()) + xOffset), Math.toIntExact(Math.round(mouseEvent.getScreenY() + yOffset))));
    }
}
