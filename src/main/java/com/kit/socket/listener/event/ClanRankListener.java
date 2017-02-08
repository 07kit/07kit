package com.kit.socket.listener.event;

import com.kit.core.Session;
import com.kit.socket.event.ClanRankEvent;
import com.kit.socket.listener.ObjectListener;
import com.kit.socket.ClientService;

public class ClanRankListener extends ObjectListener<ClanRankEvent> {

    public ClanRankListener(ClientService clientService) {
        super(ClanRankEvent.EVENT_NAME, ClanRankEvent.class, clientService);
    }

    @Override
    public void onReceive(ClanRankEvent evt) {
        if (evt.getLoginNameToken() == null) {
            return;
        }
        Session.get().getEventBus().submit(evt);
    }
}
