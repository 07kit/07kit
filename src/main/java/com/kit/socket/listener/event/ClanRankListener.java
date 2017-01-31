package com.kit.socket.listener.event;

import com.kit.core.Session;
import com.kit.socket.event.ClanRankEvent;
import com.kit.socket.listener.ObjectListener;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.socket.Client;
import com.kit.socket.event.AuthenticateEvent;
import com.kit.socket.event.ClanRankEvent;
import com.kit.socket.listener.ObjectListener;

public class ClanRankListener extends ObjectListener<ClanRankEvent> {

    public ClanRankListener(Client client) {
        super(ClanRankEvent.EVENT_NAME, ClanRankEvent.class, client);
    }

    @Override
    public void onReceive(ClanRankEvent evt) {
        if (evt.getLoginNameToken() == null) {
            return;
        }
        Session.get().getEventBus().submit(evt);
    }
}
