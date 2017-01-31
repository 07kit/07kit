package com.kit.socket.listener;

import com.google.gson.Gson;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import com.kit.socket.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SimpleListener implements Emitter.Listener {

    public static final Gson GSON = new Gson();

    public final Logger logger = LoggerFactory.getLogger(getClass());

    private final String eventName;
    private final Client client;

    public SimpleListener(String eventName, Client client) {
        this.eventName = eventName;
        this.client = client;
        client.getSocket().on(eventName, this);
    }

    public Client getClient() {
        return client;
    }

    public Socket getSocket() {
        return client.getSocket();
    }
}
