package com.kit.socket;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.kit.socket.emitter.PingEmitter;
import com.kit.socket.listener.connection.ConnectListener;
import com.kit.socket.listener.connection.DisconnectListener;
import com.kit.socket.listener.event.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public final class ClientService extends AbstractIdleService {
    private static final String SERVER_URI = "http://api.07kit.com:8091";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ServiceManager emitterServices;

    private Socket socket;
    private boolean authenticated;

    public ClientService() {
        emitterServices = new ServiceManager(
                Arrays.asList(new PingEmitter(this)));
    }

    @Override
    protected void startUp() throws Exception {
        IO.Options options = new IO.Options();
        options.reconnection = true;
        options.timeout = 20000;
        socket = IO.socket(SERVER_URI, options);
        registerListeners();

        socket.connect();

        emitterServices.startAsync();
        emitterServices.addListener(new ServiceManager.Listener() {
            @Override
            public void failure(Service service) {
                final String serviceName = service.getClass().getSimpleName();
                logger.error(String.format("Sub-service failed [%s]", serviceName), service.failureCause());
            }
        });
    }

    @Override
    protected void shutDown() throws Exception {
        emitterServices.stopAsync().awaitStopped();
        socket.disconnect();
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