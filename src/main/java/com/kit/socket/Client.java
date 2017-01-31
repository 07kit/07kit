package com.kit.socket;

import com.google.gson.Gson;
import com.kit.socket.event.PingEvent;
import com.kit.socket.listener.connection.ConnectListener;
import com.kit.socket.listener.connection.DisconnectListener;
import com.kit.socket.listener.event.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import com.kit.core.Session;
import com.kit.socket.event.AuthenticateEvent;
import com.kit.socket.event.PingEvent;
import com.kit.socket.listener.connection.ConnectListener;
import com.kit.socket.listener.connection.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class Client {

    public static final Gson GSON = new Gson();

    private final Logger logger = LoggerFactory.getLogger(getClass());
    //TODO ssl?
    public static final String SERVER_URI = "http://api.07kit.com:8091";

    private Socket socket;
    //TODO use this??
    private boolean authenticated = false;

    public Client() throws URISyntaxException {
        IO.Options options = new IO.Options();
        options.reconnection = true;
        options.timeout = 20000;

        socket = IO.socket(SERVER_URI, options);

        registerListeners();

        socket.connect();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    getSocket().emit(PingEvent.EVENT_NAME, GSON.toJson(new PingEvent(System.currentTimeMillis())));
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        logger.error("Error sending ping", e);
                    }
                }
            }
        }).start();
    }

    private void registerListeners() {
        new ConnectListener(this);
        new DisconnectListener(this);
        new AuthenticateListener(this);
        new PingAckListener(this);
        new ClanSkillListener(this);
        new ClanLocationListener(this);
        new ClanRankListener(this);
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}