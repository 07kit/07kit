package com.kit.socket.listener.event;

import com.kit.core.Session;
import com.kit.socket.ClientService;
import com.kit.socket.event.ClanSkillEvent;
import com.kit.socket.listener.ObjectListener;

public class ClanSkillListener extends ObjectListener<ClanSkillEvent> {

    public ClanSkillListener(ClientService clientService) {
        super(ClanSkillEvent.EVENT_NAME, ClanSkillEvent.class, clientService);
    }

    @Override
    public void onReceive(ClanSkillEvent evt) {
        if (evt.getLoginNameToken() == null) {
            return;
        }
        Session.get().getEventBus().submit(evt);
    }
}
