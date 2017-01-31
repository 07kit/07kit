package com.kit.plugins.clan.backend;

import com.google.gson.annotations.SerializedName;

public class GetOrJoinClanRequest {

    @SerializedName("clanId")
    public final long clanId;
    @SerializedName("loginName")
    public final String loginName;
    @SerializedName("ingameName")
    public final String ingameName;
    @SerializedName("clanName")
    public final String clanName;
    @SerializedName("status")
    public final ClanRank.Status status;
    @SerializedName("world")
    public final int world;

    public GetOrJoinClanRequest(long clanId, String loginName, String ingameName, String clanName, ClanRank.Status status, int world) {
        this.clanId = clanId;
        this.loginName = loginName;
        this.ingameName = ingameName;
        this.clanName = clanName;
        this.status = status;
        this.world = world;
    }

    @Override
    public String toString() {
        return "GetOrJoinClanRequest{" +
                "clanId=" + clanId +
                ", loginName='" + loginName + '\'' +
                ", ingameName='" + ingameName + '\'' +
                ", clanName='" + clanName + '\'' +
                ", status=" + status +
                ", world=" + world +
                '}';
    }
}
