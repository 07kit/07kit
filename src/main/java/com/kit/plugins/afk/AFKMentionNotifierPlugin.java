package com.kit.plugins.afk;

import com.kit.api.event.PlayerMentionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PlayerMentionEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

public class AFKMentionNotifierPlugin extends Plugin {

    public AFKMentionNotifierPlugin(PluginManager manager) {
        super(manager);
    }

    @EventHandler
    public void onMention(PlayerMentionEvent event) {
        if (!Session.get().isLoggedIn() || Session.get().ui.isFocused()) {
            return;
        }

        NotificationsUtil.showNotification("Mention", event.getMessage().getSender() + " mentioned you.");
    }

    @Override
    public String getName() {
        return "AFK Mention Notifier";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
