package com.kit.plugins.test;

import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.plugin.SidebarTab;
import com.kit.core.control.PluginManager;
import com.kit.jfx.JFX;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Parent;
import javafx.scene.image.Image;

public class JFXTestPlugin extends Plugin {

    private SidebarTab tab;
    private int count = 0;

    public JFXTestPlugin(PluginManager manager) {
        super(manager);
        tab = new SidebarTab() {
            @Override
            public Parent root() {
                return JFX.load(title(), "jfx/views/plugins/test-plugin.fxml");
            }

            @Override
            public String title() {
                return "JFX Test";
            }

            @Override
            public Image image() {
                return null;
            }

            @Override
            public FontAwesomeIcon icon() {
                return FontAwesomeIcon.BUG;
            }
        };
    }

    @Override
    public String getName() {
        return "JFX Plugin Test";
    }

    @Override
    public void start() {
        ui.registerTab(tab);
    }

    @Override
    public void stop() {
        ui.deregisterTab(tab);
    }

    @Schedule(1000)
    public void foo() {
        TestPluginController controller = JFX.controller(tab.title());
        controller.setCounter(++count);
    }

}
