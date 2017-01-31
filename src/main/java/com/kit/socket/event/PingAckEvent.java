package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class PingAckEvent {

    public static final String EVENT_NAME = "ping_ack_evt";

    @SerializedName("sentTime")
    private long sentTime;

    public long getSentTime() {
        return sentTime;
    }
}