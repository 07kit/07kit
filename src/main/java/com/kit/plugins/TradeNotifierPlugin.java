package com.kit.plugins;

import com.kit.api.event.MessageEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.control.PluginManager;

public class TradeNotifierPlugin extends Plugin {

    public static final int TYPE = 101;

    public TradeNotifierPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Trade Notifier";
    }

    @EventHandler
    public void onMessage(MessageEvent event) {
        if (event.getRawType() == TYPE && event.getMessage().contains("wishes to trade")) {
            NotificationsUtil.showNotification("Trade", event.getMessage());
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
