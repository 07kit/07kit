package com.kit.jfx;

import com.kit.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class JFX {

    private static final HashMap<String, Object> CONTROLLER_MAP = new HashMap<>();

    public static Parent load(String name, String location) {
        try {
            FXMLLoader loader = new FXMLLoader(JFX.class.getClassLoader().getResource(location));
            Parent parent = loader.load();
            Object controller = loader.getController();
            if (!CONTROLLER_MAP.containsKey(name))
                CONTROLLER_MAP.put(name, controller);
            return parent;
        } catch (IOException e) {
            throw new RuntimeException("Unable to load view: " + location, e);
        }
    }

    public static Parent load(String name, String location, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(JFX.class.getClassLoader().getResource(location));
            loader.setController(controller);
            Parent parent = loader.load();
            if (!CONTROLLER_MAP.containsKey(name))
                CONTROLLER_MAP.put(name, controller);
            return parent;
        } catch (IOException e) {
            throw new RuntimeException("Unable to load view: " + location, e);
        }
    }

    public static <T>  T controller(String name) {
        if (!CONTROLLER_MAP.containsKey(name))
            throw new RuntimeException("Unable to get controller for name: " + name);
        return (T) CONTROLLER_MAP.get(name);
    }

}
