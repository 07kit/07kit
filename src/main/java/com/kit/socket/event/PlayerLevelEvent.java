package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class PlayerLevelEvent {

    public static final String EVENT_NAME = "player_level_evt";

    @SerializedName("currentLevel")
    private final int currentLevel;
    @SerializedName("baseLevel")
    private final int baseLevel;
    @SerializedName("loginName")
    private final String loginName;
    @SerializedName("ingameName")
    private String ingameName;
    @SerializedName("skill")
    private String skill;

    public PlayerLevelEvent(int currentLevel, int baseLevel, String loginName, String ingameName, String skill) {
        this.currentLevel = currentLevel;
        this.baseLevel = baseLevel;
        this.loginName = loginName;
        this.ingameName = ingameName;
        this.skill = skill;
    }

    public String getSkill() {
        return skill;
    }

    public String getIngameName() {
        return ingameName;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getBaseLevel() {
        return baseLevel;
    }

    public String getLoginName() { return loginName; }
}
