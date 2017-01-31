package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class ClanRankEvent {

    public static final String EVENT_NAME = "clan_rank_evt";

    public enum Rank {
        PENDING, BANNED, FRIEND, RECRUIT, CORPORAL, SERGEANT, LIEUTENANT, CAPTAIN, GENERAL, OWNER;
    }

    public enum Status {
        OFFLINE, IN_GAME, IN_LOBBY;
    }

    @SerializedName("clanId")
    private long clanId;
    @SerializedName("userId")
    private long userId;
    @SerializedName("loginName")
    private String loginNameToken;
    @SerializedName("ingameName")
    private String ingameName;
    @SerializedName("status")
    private Status status;
    @SerializedName("lastWorld")
    private int lastWorld;
    @SerializedName("rank")
    private Rank rank;

    public ClanRankEvent(long clanId, long userId, String loginNameToken, String ingameName, Status status, int lastWorld, Rank rank) {
        this.clanId = clanId;
        this.userId = userId;
        this.loginNameToken = loginNameToken;
        this.ingameName = ingameName;
        this.status = status;
        this.lastWorld = lastWorld;
        this.rank = rank;
    }

    public long getClanId() {
        return clanId;
    }

    public long getUserId() {
        return userId;
    }

    public String getIngameName() {
        return ingameName;
    }

    public Status getStatus() {
        return status;
    }

    public int getLastWorld() {
        return lastWorld;
    }

    public Rank getRank() {
        return rank;
    }

    public String getLoginNameToken() {
        return loginNameToken;
    }

    @Override
    public String toString() {
        return "ClanRankEvent{" +
                "userId=" + userId +
                ", loginNameToken='" + loginNameToken + '\'' +
                ", ingameName='" + ingameName + '\'' +
                ", status=" + status +
                ", lastWorld=" + lastWorld +
                ", rank=" + rank +
                '}';
    }
}
