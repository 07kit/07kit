package com.kit.socket.listener.event;

import com.kit.socket.event.AuthenticateEvent;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.socket.ClientService;
import com.kit.socket.listener.ObjectListener;

public class AuthenticateListener extends ObjectListener<AuthenticateEvent> {

    public AuthenticateListener(ClientService clientService) {
        super(AuthenticateEvent.EVENT_NAME, AuthenticateEvent.class, clientService);
    }

    @Override
    public void onReceive(AuthenticateEvent evt) {
        Session.get().getEventBus().submit(evt);
        if (evt.getStatus() == AuthenticateEvent.Status.SUCCESS) {
            getClientService().setAuthenticated(true);
        } else {
            getClientService().setAuthenticated(false);
            NotificationsUtil.showNotification("Error", "Unable to authenticate with 07Kit Server");
        }
    }
}
