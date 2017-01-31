package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class ClanLocationEvent {


    public static final String EVENT_NAME = "clan_location_evt";

    @SerializedName("userId")
    private long userId;
    @SerializedName("loginNameToken")
    private String loginNameToken;
    @SerializedName("ingameName")
    private String ingameName;
    @SerializedName("x")
    private int x;
    @SerializedName("y")
    private int y;
    @SerializedName("plane")
    private int plane;

    public ClanLocationEvent(long userId, String loginNameToken, String ingameName, int x, int y, int plane) {
        this.userId = userId;
        this.loginNameToken = loginNameToken;
        this.ingameName = ingameName;
        this.x = x;
        this.y = y;
        this.plane = plane;
    }

    public String getLoginNameToken() {
        return loginNameToken;
    }

    public static String getEventName() {
        return EVENT_NAME;
    }

    public long getUserId() {
        return userId;
    }

    public String getIngameName() {
        return ingameName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlane() {
        return plane;
    }

    @Override
    public String toString() {
        return "ClanLocationEvent{" +
                "userId=" + userId +
                ", loginNameToken='" + loginNameToken + '\'' +
                ", ingameName='" + ingameName + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", plane=" + plane +
                '}';
    }
}
