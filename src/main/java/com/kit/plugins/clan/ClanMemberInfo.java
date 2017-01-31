package com.kit.plugins.clan;

import com.kit.api.wrappers.Tile;
import com.kit.plugins.clan.backend.ClanRank;
import com.kit.socket.event.ClanSkillEvent;

import java.util.Map;

public class ClanMemberInfo {

    private Map<ClanSkillEvent.Skill, ClanSkillEvent> skills;
    private Tile lastPosition;
    private ClanRank rank;

    public ClanMemberInfo(Map<ClanSkillEvent.Skill, ClanSkillEvent> skills, Tile lastPosition, ClanRank rank) {
        this.skills = skills;
        this.lastPosition = lastPosition;
        this.rank = rank;
    }

    public Map<ClanSkillEvent.Skill, ClanSkillEvent> getSkills() {
        return skills;
    }

    public void setSkills(Map<ClanSkillEvent.Skill, ClanSkillEvent> skills) {
        this.skills = skills;
    }

    public Tile getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Tile lastPosition) {
        this.lastPosition = lastPosition;
    }

    public ClanRank getRank() {
        return rank;
    }

    public void setRank(ClanRank rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "ClanMemberInfo{" +
                "skills=" + skills +
                ", lastPosition=" + lastPosition +
                ", rank=" + rank +
                '}';
    }
}
