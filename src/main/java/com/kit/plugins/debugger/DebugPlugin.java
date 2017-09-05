package com.kit.plugins.debugger;

import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.core.control.PluginManager;

/**
 */
public class DebugPlugin extends Plugin {
    private final DebugSidebarTab widget = new DebugSidebarTab();

    public DebugPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Debugger";
    }

    @Override
    public void start() {
        ui.registerTab(widget);
    }

    @Override
    public void stop() {
        ui.deregisterTab(widget);
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
