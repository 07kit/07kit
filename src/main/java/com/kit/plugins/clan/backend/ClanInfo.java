package com.kit.plugins.clan.backend;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClanInfo {

    @SerializedName("clanId")
    private long clanId;
    @SerializedName("name")
    private String name;
    @SerializedName("rank")
    private ClanRank rank;
    @SerializedName("ranks")
    private List<ClanRank> ranks;
    @SerializedName("ranksMap")
    private Map<String, ClanRank> ranksMap;

    public ClanInfo(long clanId, String name, List<ClanRank> ranks) {
        this.clanId = clanId;
        this.name = name;
        this.ranks = ranks;
        this.ranksMap = ranks.stream().collect(Collectors.toMap(ClanRank::getLoginNameToken, r -> r));
    }

    public long getClanId() {
        return clanId;
    }

    public void setClanId(long clanId) {
        this.clanId = clanId;
    }

    public void setRanks(List<ClanRank> ranks) {
        this.ranks = ranks;
    }

    public void setRanksMap(Map<String, ClanRank> ranksMap) {
        this.ranksMap = ranksMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClanRank getRank() {
        return rank;
    }

    public void setRank(ClanRank rank) {
        this.rank = rank;
    }

    public Map<String, ClanRank> getRanksMap() {
        if (ranksMap == null) {
            ranksMap = ranks.stream().collect(Collectors.toMap(ClanRank::getLoginNameToken, r -> r));
        }
        return ranksMap;
    }

    public void setRanks(Map<String, ClanRank> ranksMap) {
        this.ranksMap = ranksMap;
    }

    public List<ClanRank> getRanks() {
        return ranks;
    }

    @Override
    public String toString() {
        return "ClanInfo{" +
                "clanId=" + clanId +
                ", name='" + name + '\'' +
                ", rank=" + rank +
                ", ranks=" + ranks +
                ", ranksMap=" + ranksMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClanInfo clanInfo = (ClanInfo) o;

        return clanId == clanInfo.clanId;

    }

    @Override
    public int hashCode() {
        return (int) (clanId ^ (clanId >>> 32));
    }
}
