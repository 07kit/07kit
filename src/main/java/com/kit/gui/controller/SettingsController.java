package com.kit.gui.controller;

import com.kit.Application;
import com.kit.gui.Controller;
import com.kit.gui.ControllerManager;
import com.kit.gui.view.SettingsView;

import javax.swing.*;

/**
 */
public class SettingsController extends Controller<SettingsView> {
    private final SettingsView settingsView;

    public SettingsController() {
        ControllerManager.add(getClass(), this);
        settingsView = new SettingsView();
    }

    public void show() {
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setContentPane(settingsView);
        settingsView.load();
        frame.pack();
        frame.setResizable(false);
        frame.setIconImage(Application.ICON_IMAGE);
        frame.setLocationRelativeTo(ControllerManager.get(MainController.class).getComponent());
        frame.setVisible(true);
    }

    @Override
    public SettingsView getComponent() {
        return settingsView;
    }
}
