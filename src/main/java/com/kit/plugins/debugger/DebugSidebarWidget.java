package com.kit.plugins.debugger;

import com.kit.Application;
import com.kit.Application;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.MateProgressBar;
import com.kit.gui.component.MateScrollPane;
import com.kit.gui.component.SidebarWidget;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import com.kit.Application;
import com.kit.gui.component.SidebarWidget;

import javax.swing.*;
import java.awt.*;

/**
 */
public class DebugSidebarWidget extends MateScrollPane implements SidebarWidget {
    private final Image NORMAL_ICON = IconFontSwing.buildImage(FontAwesome.BUG, 20, Color.GRAY);
    private final Image TOGGLED_ICON = IconFontSwing.buildImage(FontAwesome.BUG, 20, Color.WHITE);
    private final DebugCheckBox[] debugs = {
            new DebugCheckBox("Camera", "camera"),
            new DebugCheckBox("Login", "login"),
            new DebugCheckBox("Npcs", "npcs"),
            new DebugCheckBox("Players", "players"),
            new DebugCheckBox("Inventory", "inventory"),
            new DebugCheckBox("Loot", "loot"),
            new DebugCheckBox("Menu", "menu"),
            new DebugCheckBox("Mouse", "mouse"),
            new DebugCheckBox("Position", "position"),
            new DebugCheckBox("Floor", "floor"),
            new DebugCheckBox("Boundaries", "boundaries"),
            new DebugCheckBox("Interactable", "interactable_objects"),
            new DebugCheckBox("Wall objects", "wall_objects"),
            new DebugCheckBox("Widget Explorer", "widgetExplorer"),
            new DebugCheckBox("Settings Explorer", "settingsExplorer"),
            new DebugCheckBox("Equipment", "equipment"),
            new DebugCheckBox("Console", "console"),
            new DebugCheckBox("FPS", "fps")
    };


    public DebugSidebarWidget() {
        JPanel container = new JPanel();
        setViewportView(container);
        setBorder(null);

        container.setBackground(Application.COLOUR_SCHEME.getDark().brighter());
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        for (DebugCheckBox chkbx : debugs) {
            chkbx.setActionCommand("Debug:" + chkbx.internal);
            chkbx.addActionListener(e -> {
                //ControllerManager.get(MainController.class).toggleDebug(e);
            });
            container.add(chkbx);
        }
    }

    @Override
    public Component getContent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Debug";
    }

    @Override
    public Image getIcon(boolean toggled) {
        return toggled ? TOGGLED_ICON : NORMAL_ICON;
    }

    public class DebugCheckBox extends JCheckBox {
        private final String internal;

        DebugCheckBox(String name, String internal) {
            super(name);
            setBackground(Application.COLOUR_SCHEME.getDark().brighter());
            setForeground(Application.COLOUR_SCHEME.getText());
            this.internal = internal;
        }
    }
}
