package com.kit.api.impl;

import com.kit.api.UserInterface;
import com.kit.api.plugin.SidebarTab;
import com.kit.core.Session;

import com.kit.gui.controller.SidebarController;
import com.kit.api.UserInterface;
import com.kit.api.overlay.BoxOverlay;
import com.kit.core.Session;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.SidebarWidget;

import com.kit.gui.controller.SidebarController;
import com.kit.api.overlay.BoxOverlay;
import com.kit.gui.component.SidebarWidget;
import com.kit.jfx.JFX;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class UserInterfaceImpl implements UserInterface {
    private final SidebarController sidebarController;

    public UserInterfaceImpl() {
        sidebarController = ControllerManager.get(SidebarController.class);
    }


    @Override
    public void registerSidebarWidget(SidebarWidget widget) {
        sidebarController.registerSidebarWidget(widget);
    }

    @Override
    public void deregisterSidebarWidget(SidebarWidget widget) {
        sidebarController.deregisterSidebarWidget(widget);
    }

    @Override
    public void registerBoxOverlay(BoxOverlay boxOverlay) {
        Session.get().getOverlayManager().addBoxOverlay(boxOverlay);
    }

    @Override
    public void deregisterBoxOverlay(BoxOverlay boxOverlay) {
        Session.get().getOverlayManager().removeBoxOverlay(boxOverlay);
    }

    @Override
    public boolean isFocused() {
        JFrame window = Session.get().getFrame();
        return window.isFocused() && window.isActive();
    }

    @Override
    public void registerTab(SidebarTab tab) {
        com.kit.jfx.controllers.SidebarController sidebar = JFX.controller("sidebar");
        sidebar.add(tab);
    }

    @Override
    public void deregisterTab(SidebarTab tab) {
        com.kit.jfx.controllers.SidebarController sidebar = JFX.controller("sidebar");
        sidebar.remove(tab);
    }

}
