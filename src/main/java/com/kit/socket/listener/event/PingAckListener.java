package com.kit.socket.listener.event;

import com.kit.socket.event.PingAckEvent;
import com.kit.socket.listener.ObjectListener;
import com.kit.socket.Client;
import com.kit.socket.event.PingAckEvent;
import com.kit.socket.listener.ObjectListener;

import java.sql.Timestamp;

public class PingAckListener extends ObjectListener<PingAckEvent> {

    public PingAckListener(Client client) {
        super(PingAckEvent.EVENT_NAME, PingAckEvent.class, client);
    }

    @Override
    public void onReceive(PingAckEvent evt) {
        logger.debug("Got ping ack sent at " + new Timestamp(evt.getSentTime()));
    }
}