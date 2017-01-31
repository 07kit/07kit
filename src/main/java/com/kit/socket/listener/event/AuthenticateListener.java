package com.kit.socket.listener.event;

import com.kit.socket.event.AuthenticateEvent;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.socket.Client;
import com.kit.socket.event.AuthenticateEvent;
import com.kit.socket.listener.ObjectListener;

public class AuthenticateListener extends ObjectListener<AuthenticateEvent> {

    public AuthenticateListener(Client client) {
        super(AuthenticateEvent.EVENT_NAME, AuthenticateEvent.class, client);
    }

    @Override
    public void onReceive(AuthenticateEvent evt) {
        Session.get().getEventBus().submit(evt);
        if (evt.getStatus() == AuthenticateEvent.Status.SUCCESS) {
            getClient().setAuthenticated(true);
        } else {
            getClient().setAuthenticated(false);
            NotificationsUtil.showNotification("Error", "Unable to authenticate with 07Kit Server");
        }
    }
}
