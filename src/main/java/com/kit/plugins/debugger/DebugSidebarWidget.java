package com.kit.plugins.debugger;

import com.kit.Application;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.debug.impl.WidgetDebug;
import com.kit.api.plugin.SidebarTab;
import com.kit.core.Session;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.*;
import com.kit.gui.controller.SettingsDebugController;
import com.kit.gui.controller.WidgetDebugController;
import com.kit.jfx.JFX;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.function.Function;

/**
 */
public class DebugSidebarWidget implements SidebarTab {
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
            new DebugCheckBox("Widget Explorer", "widgetExplorer", (o) -> {
                WidgetDebug debug = null;
                for (AbstractDebug abstractDebug : Session.get().getDebugManager().getDebugs()) {
                    if (abstractDebug instanceof WidgetDebug) {
                        abstractDebug.setEnabled(true);
                        debug = (WidgetDebug) abstractDebug;
                        break;
                    }
                }
                ControllerManager.get(WidgetDebugController.class).show(debug);
                return null;
            }),
            new DebugCheckBox("Settings Explorer", "settingsExplorer", (o) -> {
                ControllerManager.get(SettingsDebugController.class).show(Session.get().getClient());
                return null;
            }),
            new DebugCheckBox("Equipment", "equipment"),
            new DebugCheckBox("Console", "console"),
            new DebugCheckBox("FPS", "fps", (o) -> {
                Session.get().getClient().setFpsOn(!Session.get().getClient().getFpsOn());
                return null;
            })
    };

    @Override
    public Parent root() {
        Pane box = new VBox();
        box.getStyleClass().add("wrapper-dark");
        for (DebugCheckBox chkbx : debugs) {
            chkbx.setOnAction(event -> {
                Session session = Session.get();
                if (session != null) {
                    chkbx.handler.apply(null);
                }
            });

            box.getChildren().add(chkbx);
        }
        return box;
    }

    @Override
    public String title() {
        return "Debug";
    }

    @Override
    public Image image() {
        return null;
    }

    @Override
    public FontAwesomeIcon icon() {
        return FontAwesomeIcon.BUG;
    }

    public class DebugCheckBox extends CheckBox {
        private final String internal;
        private final Function<Void, Void> handler;

        DebugCheckBox(String name, String internal) {
            this(name, internal, null);
        }

        DebugCheckBox(String name, String internal, Function<Void, Void> handler) {
            super(name);
//            getStyleClass().add("wrapper-dark");
            getStyleClass().add("label");
            this.internal = internal;
            if (handler == null) {
                handler = o -> {
                    Session.get().getDebugManager().toggle(
                            internal,
                            isSelected());

                    return null;
                };
            }

            this.handler = handler;
        }
    }
}
