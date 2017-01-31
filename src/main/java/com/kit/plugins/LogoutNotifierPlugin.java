package com.kit.plugins;

import com.kit.api.event.ActionEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.control.PluginManager;

public class LogoutNotifierPlugin extends Plugin {

    private long lastAction;
    private boolean shown = false;

    public LogoutNotifierPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Logout Notifier";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @EventHandler
    public void onAction(ActionEvent event) {
        lastAction = System.currentTimeMillis();
        shown = false;
    }

    @Schedule(1500)
    public void checkIdleState() {
        if (!shown && Session.get().isLoggedIn() && lastAction > 0 && (System.currentTimeMillis() - lastAction) > 240000) {
            NotificationsUtil.showNotification("Idle", "You will be logged out in less than a minute.");
            shown = true;
        }
    }

    @Override
    public boolean hasOptions() {
        return false;
    }
}
