package com.kit.plugins;

import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.control.PluginManager;

/**
 * Plugin that generates a notification when the player becomes idle.
 *
 */
public class IdleNotifierPlugin extends Plugin {
    private boolean previouslyIdle;
    private long lastStateChange;

    public IdleNotifierPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Idle Notifier";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Schedule(1500)
    public void checkIdleState() {
        if (!Session.get().isLoggedIn() || Session.get().ui.isFocused()) {
            return;
        }

        boolean idle = Session.get().player.isIdle();
        if (previouslyIdle != idle) {
            previouslyIdle = idle;
            lastStateChange = System.currentTimeMillis();
            if (idle) {
                NotificationsUtil.showNotification("Idle", "You're now idle.");
            }
        }
    }

    @Override
    public boolean hasOptions() {
        return false;
    }
}
