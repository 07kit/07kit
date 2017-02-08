package com.kit.socket.listener.connection;

import io.socket.client.Socket;
import com.kit.core.Session;
import com.kit.socket.ClientService;
import com.kit.socket.event.AuthenticateEvent;
import com.kit.socket.listener.SimpleListener;

public class ConnectListener extends SimpleListener {

    public ConnectListener(ClientService clientService) {
        super(Socket.EVENT_CONNECT, clientService);
    }

    @Override
    public void call(Object... objects) {
        logger.info("Connected to 07Kit server, authenticating");

        getSocket().emit(AuthenticateEvent.EVENT_NAME, GSON.toJson(
                new AuthenticateEvent(Session.get().getApiToken(), AuthenticateEvent.Status.PENDING)
        ));
    }
}
