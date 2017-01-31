package com.kit.gui.controller;

import com.kit.Application;
import com.kit.api.debug.AbstractDebug;
import com.kit.gui.Controller;
import com.kit.plugins.debugger.DebugSidebarWidget;
import com.kit.Application;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.debug.impl.WidgetDebug;
import com.kit.core.Session;
import com.kit.gui.Controller;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.MateCheckBox;
import com.kit.gui.view.MainView;
import org.apache.log4j.Logger;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.debug.impl.WidgetDebug;
import com.kit.core.Session;
import com.kit.gui.Controller;
import com.kit.gui.view.MainView;
import com.kit.plugins.debugger.DebugSidebarWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.kit.gui.ControllerManager.get;


public class MainController extends Controller<MainView> {
    private final Logger logger = Logger.getLogger(MainController.class);
    private MainView view;

    public MainController() {
        ControllerManager.add(MainController.class, this);
    }

    @Override
    public MainView getComponent() {
        if (view == null) {
            view = new MainView(this);
        }
        return view;
    }

    public void show() {
        getComponent().setIconImage(Application.ICON_IMAGE);
        getComponent().setVisible(true);
    }

    public void toggleDebug(ActionEvent evt) {
        if (Session.get() != null) {
            if (evt.getActionCommand().contains("fps")) {
                Session.get().getClient().setFpsOn(!Session.get().getClient().getFpsOn());
            }
            if (evt.getActionCommand().contains("settings")) {
                get(SettingsDebugController.class).show(Session.get().getClient());
                return;
            }
            if (evt.getActionCommand().contains("widget")) {
                WidgetDebug debug = null;
                for (AbstractDebug abstractDebug : Session.get().getDebugManager().getDebugs()) {
                    if (abstractDebug instanceof WidgetDebug) {
                        abstractDebug.setEnabled(true);
                        debug = (WidgetDebug) abstractDebug;
                        break;
                    }
                }
                get(WidgetDebugController.class).show(debug);
                return;
            }
            Session.get().getDebugManager().toggle(
                    (evt.getActionCommand().split(":")[1]),
                    ((DebugSidebarWidget.DebugCheckBox) evt.getSource()).isSelected());
        }
    }

    public void toggleSidebar() {
        view.toggleSidebar();
    }

    public void toggleMaximized() {
        if (view.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            view.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            view.setExtendedState(0);
            view.setMinimumSize(view.getDefaultSize());
            view.setMaximumSize(view.getDefaultSize());
            view.setPreferredSize(view.getDefaultSize());
            view.setSize(view.getDefaultSize());
        }
    }
}
