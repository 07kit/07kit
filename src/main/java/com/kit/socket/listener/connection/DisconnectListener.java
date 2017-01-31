package com.kit.socket.listener.connection;

import io.socket.client.Socket;
import com.kit.core.Session;
import com.kit.plugins.clan.ClanPlugin;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.socket.Client;
import com.kit.socket.listener.ObjectListener;
import com.kit.socket.listener.SimpleListener;

public class DisconnectListener extends SimpleListener {

    private ClanPlugin clanPlugin;

    public DisconnectListener(Client client) {
        super(Socket.EVENT_DISCONNECT, client);
    }

    @Override
    public void call(Object... objects) {
        logger.info("Disconnected from 07Kit server");
        if (clanPlugin == null) {
            clanPlugin = (ClanPlugin) Session.get().getPluginManager().getPlugins().stream().filter(p -> p.getClass()
                    .equals(ClanPlugin.class))
                    .findFirst().orElse(null);
        }
        if (clanPlugin != null && clanPlugin.getCurrentClan() != null) {
            clanPlugin.setCurrentClan(null);
        }
    }
}
