package com.kit.api.impl;

import com.kit.api.UserInterface;
import com.kit.core.Session;
import com.kit.gui.controller.MainController;
import com.kit.gui.controller.SidebarController;
import com.kit.api.UserInterface;
import com.kit.api.overlay.BoxOverlay;
import com.kit.core.Session;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.SidebarWidget;
import com.kit.gui.controller.MainController;
import com.kit.gui.controller.SidebarController;
import com.kit.api.overlay.BoxOverlay;
import com.kit.gui.component.SidebarWidget;

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
        JFrame window = ControllerManager.get(MainController.class).getComponent();
        return window.isFocused() && window.isActive();
    }

}
