package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class LeaveClanEvent {

    public static final String EVENT_NAME = "leave_clan_evt";

    @SerializedName("clanId")
    private final long clanId;

    public LeaveClanEvent(long clanId) {
        this.clanId = clanId;
    }

    public long getClanId() {
        return clanId;
    }
}
