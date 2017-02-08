package com.kit.socket.listener;

import com.google.gson.Gson;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import com.kit.socket.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SimpleListener implements Emitter.Listener {

    public static final Gson GSON = new Gson();

    public final Logger logger = LoggerFactory.getLogger(getClass());

    private final String eventName;
    private final ClientService clientService;

    public SimpleListener(String eventName, ClientService clientService) {
        this.eventName = eventName;
        this.clientService = clientService;
        clientService.getSocket().on(eventName, this);
    }

    public ClientService getClientService() {
        return clientService;
    }

    public Socket getSocket() {
        return clientService.getSocket();
    }
}
