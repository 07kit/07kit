package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class ClanSkillEvent {

    public enum Skill {
        ATTACK,
        DEFENCE,
        STRENGTH,
        HITPOINTS,
        RANGED,
        PRAYER,
        MAGIC,
        COOKING,
        WOODCUTTING,
        FLETCHING,
        FISHING,
        FIREMAKING,
        CRAFTING,
        SMITHING,
        MINING,
        HERBLORE,
        AGILITY,
        THIEVING,
        SLAYER,
        FARMING,
        RUNECRAFTING,
        HUNTER,
        CONSTRUCTION,
        OVERALL,
        COMBAT;
    }

    public static final String EVENT_NAME = "clan_skill_evt";

    @SerializedName("userId")
    private long userId;
    @SerializedName("loginNameToken")
    private String loginNameToken;
    @SerializedName("ingameName")
    private String ingameName;
    @SerializedName("baseLevel")
    private int baseLevel;
    @SerializedName("currentLevel")
    private int currentLevel;
    @SerializedName("skill")
    private Skill skill;

    public ClanSkillEvent(long userId, String loginNameToken, String ingameName, int baseLevel, int currentLevel, Skill skill) {
        this.userId = userId;
        this.loginNameToken = loginNameToken;
        this.ingameName = ingameName;
        this.baseLevel = baseLevel;
        this.currentLevel = currentLevel;
        this.skill = skill;
    }

    public String getLoginNameToken() {
        return loginNameToken;
    }

    public long getUserId() {
        return userId;
    }

    public String getIngameName() {
        return ingameName;
    }

    public int getBaseLevel() {
        return baseLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Skill getSkill() {
        return skill;
    }

    @Override
    public String toString() {
        return "ClanSkillEvent{" +
                "userId=" + userId +
                ", loginNameToken='" + loginNameToken + '\'' +
                ", ingameName='" + ingameName + '\'' +
                ", baseLevel=" + baseLevel +
                ", currentLevel=" + currentLevel +
                ", skill=" + skill +
                '}';
    }
}
