package com.kit.socket.listener.event;

import com.kit.core.Session;
import com.kit.socket.listener.ObjectListener;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.socket.Client;
import com.kit.socket.event.AuthenticateEvent;
import com.kit.socket.event.ClanLocationEvent;
import com.kit.socket.listener.ObjectListener;

public class ClanLocationListener extends ObjectListener<ClanLocationEvent> {

    public ClanLocationListener(Client client) {
        super(ClanLocationEvent.EVENT_NAME, ClanLocationEvent.class, client);
    }

    @Override
    public void onReceive(ClanLocationEvent evt) {
        if (evt.getLoginNameToken() == null) {
            return;
        }
        Session.get().getEventBus().submit(evt);
    }
}
