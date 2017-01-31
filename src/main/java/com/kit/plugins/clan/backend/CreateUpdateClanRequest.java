package com.kit.plugins.clan.backend;

import com.google.gson.annotations.SerializedName;

public final class CreateUpdateClanRequest {

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

    public CreateUpdateClanRequest(String loginName, String ingameName, String clanName, ClanRank.Status status, int world) {
        this.loginName = loginName;
        this.ingameName = ingameName;
        this.clanName = clanName;
        this.status = status;
        this.world = world;
    }

    @Override
    public String toString() {
        return "CreateUpdateClanRequest{" +
                "loginName='" + loginName + '\'' +
                ", ingameName='" + ingameName + '\'' +
                ", clanName='" + clanName + '\'' +
                ", status=" + status +
                ", world=" + world +
                '}';
    }
}
