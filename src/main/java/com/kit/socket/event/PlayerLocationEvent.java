package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class PlayerLocationEvent {

    public static final String EVENT_NAME = "player_location_evt";

    @SerializedName("loginName")
    private final String loginName;
    @SerializedName("ingameName")
    private String ingameName;
    @SerializedName("x")
    private int x;
    @SerializedName("y")
    private int y;
    @SerializedName("plane")
    private int plane;

    public PlayerLocationEvent(String loginName, String ingameName, int x, int y, int plane) {
        this.loginName = loginName;
        this.ingameName = ingameName;
        this.x = x;
        this.y = y;
        this.plane = plane;
    }


    public String getLoginName() { return loginName; }

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
}
