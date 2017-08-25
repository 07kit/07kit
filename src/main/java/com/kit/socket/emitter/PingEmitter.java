package com.kit.socket.emitter;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.gson.Gson;
import com.kit.api.util.GsonFactory;
import com.kit.socket.ClientService;
import com.kit.socket.event.PingEvent;

import java.util.concurrent.TimeUnit;

/**
 */
public final class PingEmitter extends AbstractScheduledService {
    private final Gson gson = new GsonFactory().newInstance();
    private final ClientService clientService;

    public PingEmitter(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    protected void runOneIteration() throws Exception {
        String pingJson = gson.toJson(new PingEvent(System.currentTimeMillis()));
        clientService.getSocket().emit(PingEvent.EVENT_NAME, pingJson);
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0L, 20L, TimeUnit.SECONDS);
    }
}
