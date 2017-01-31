package com.kit.plugins.clan.backend;

import com.google.gson.annotations.SerializedName;

public class ClanRank {

    public enum Rank {
        REJECTED, PENDING, BANNED, FRIEND, RECRUIT, CORPORAL, SERGEANT, LIEUTENANT, CAPTAIN, GENERAL, OWNER;

        public String pretty() {
            return name().substring(0, 1) + name().substring(1).toLowerCase();
        }
    }

    public enum Status {
        OFFLINE("Offline"), IN_GAME("In Game"), IN_LOBBY("Lobby");

        private String prettyName;

        Status(String prettyName) {
            this.prettyName = prettyName;
        }

        public String getPrettyName() {
            return prettyName;
        }
    }

    @SerializedName("clanId")
    private long clanId;
    @SerializedName("userId")
    private long userId;
    @SerializedName("loginNameToken")
    private String loginNameToken;
    @SerializedName("ingameName")
    private String ingameName;
    @SerializedName("status")
    private Status status;
    @SerializedName("lastWorld")
    private int lastWorld;
    @SerializedName("rank")
    private Rank rank;

    public ClanRank() {
    }

    public ClanRank(long clanId, long userId, String loginNameToken, String ingameName, Status status, int lastWorld, Rank rank) {
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

    public void setClanId(long clanId) {
        this.clanId = clanId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getIngameName() {
        return ingameName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getLastWorld() {
        return lastWorld;
    }

    public void setLastWorld(int lastWorld) {
        this.lastWorld = lastWorld;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getLoginNameToken() {
        return loginNameToken;
    }

    public void setLoginNameToken(String loginNameToken) {
        this.loginNameToken = loginNameToken;
    }

    public void setIngameName(String ingameName) {
        this.ingameName = ingameName;
    }

    @Override
    public String toString() {
        return "ClanRank{" +
                "clanId=" + clanId +
                ", userId=" + userId +
                ", loginNameToken='" + loginNameToken + '\'' +
                ", ingameName='" + ingameName + '\'' +
                ", status=" + status +
                ", lastWorld=" + lastWorld +
                ", rank=" + rank +
                '}';
    }
}
