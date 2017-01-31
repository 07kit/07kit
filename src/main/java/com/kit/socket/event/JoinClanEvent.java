package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class JoinClanEvent {

    public static final String EVENT_NAME = "join_clan_evt";

    @SerializedName("clanId")
    private final long clanId;
    @SerializedName("loginName")
    private final String loginName;

    public JoinClanEvent(long clanId, String loginName) {
        this.clanId = clanId;
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    public long getClanId() {
        return clanId;
    }
}