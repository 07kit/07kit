package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class PingEvent {

    public static final String EVENT_NAME = "ping_evt";

    @SerializedName("sentTime")
    private long sentTime;

    public PingEvent(long sentTime) {
        this.sentTime = sentTime;
    }

    public long getSentTime() {
        return sentTime;
    }
}