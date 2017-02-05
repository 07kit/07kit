package com.kit.socket.listener.event;

import com.kit.core.Session;
import com.kit.socket.listener.ObjectListener;
import com.kit.socket.ClientService;
import com.kit.socket.event.ClanLocationEvent;

public class ClanLocationListener extends ObjectListener<ClanLocationEvent> {

    public ClanLocationListener(ClientService clientService) {
        super(ClanLocationEvent.EVENT_NAME, ClanLocationEvent.class, clientService);
    }

    @Override
    public void onReceive(ClanLocationEvent evt) {
        if (evt.getLoginNameToken() == null) {
            return;
        }
        Session.get().getEventBus().submit(evt);
    }
}
