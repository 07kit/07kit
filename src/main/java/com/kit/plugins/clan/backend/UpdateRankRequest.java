package com.kit.plugins.clan.backend;

import com.google.gson.annotations.SerializedName;

public class UpdateRankRequest {

    @SerializedName("clanId")
    public long clanId;
    @SerializedName("senderLoginName")
    public String senderLoginName;
    @SerializedName("loginNameToken")
    public String loginNameToken;
    @SerializedName("userId")
    public long userId;
    @SerializedName("rank")
    public ClanRank.Rank rank;

    public UpdateRankRequest(long clanId, String senderLoginName, long userId, String loginNameToken, ClanRank.Rank rank) {
        this.clanId = clanId;
        this.senderLoginName = senderLoginName;
        this.userId = userId;
        this.loginNameToken = loginNameToken;
        this.rank = rank;
    }
}
