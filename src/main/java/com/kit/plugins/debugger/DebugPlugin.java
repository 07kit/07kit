package com.kit.plugins.debugger;

import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.core.control.PluginManager;
import com.kit.api.plugin.Plugin;
import com.kit.core.control.PluginManager;

/**
 */
public class DebugPlugin extends Plugin {
    private final DebugSidebarWidget widget = new DebugSidebarWidget();

    public DebugPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Debugger";
    }

    @Override
    public void start() {
        ui.registerSidebarWidget(widget);
    }

    @Override
    public void stop() {
        ui.deregisterSidebarWidget(widget);
    }

    /**
     * Main loop, runs (roughly) every 600ms.
     */
    @Schedule(600)
    public void loop() {
        if (isLoggedIn()) {
            logger.debug(String.valueOf(client().getLoginIndex()));
        } else {
            logger.debug("heya");
        }
    }

}
