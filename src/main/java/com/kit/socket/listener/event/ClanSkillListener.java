package com.kit.socket.listener.event;

import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.socket.Client;
import com.kit.socket.event.AuthenticateEvent;
import com.kit.socket.event.ClanSkillEvent;
import com.kit.socket.listener.ObjectListener;

public class ClanSkillListener extends ObjectListener<ClanSkillEvent> {

    public ClanSkillListener(Client client) {
        super(ClanSkillEvent.EVENT_NAME, ClanSkillEvent.class, client);
    }

    @Override
    public void onReceive(ClanSkillEvent evt) {
        if (evt.getLoginNameToken() == null) {
            return;
        }
        Session.get().getEventBus().submit(evt);
    }
}
