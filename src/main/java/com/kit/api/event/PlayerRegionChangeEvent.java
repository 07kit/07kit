package com.kit.api.event;

public class PlayerRegionChangeEvent {

    private final int playerId;
    private final Type type;
    private final int oldX;
    private final int oldY;
    private final int oldPlane;
    private final int newX;
    private final int newY;
    private final int newPlane;

    public PlayerRegionChangeEvent(int playerId, Type type, int oldX, int oldY, int oldPlane, int newX, int newY, int newPlane) {
        this.playerId = playerId;
        this.type = type;
        this.oldX = oldX;
        this.oldY = oldY;
        this.oldPlane = oldPlane;
        this.newX = newX;
        this.newY = newY;
        this.newPlane = newPlane;
    }

    public enum Type {
        ADDED_TO_LOCAL,
        PLANE_CHANGE,
        ADJACENT_REGION,
        NONADJACENT_REGION
    }

    public int getPlayerId() {
        return playerId;
    }

    public Type getType() {
        return type;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public int getOldPlane() {
        return oldPlane;
    }

    public int getNewX() {
        return newX;
    }

    public int getNewY() {
        return newY;
    }

    public int getNewPlane() {
        return newPlane;
    }

    @Override
    public String toString() {
        return "PlayerRegionChangeEvent{" +
                "playerId=" + playerId +
                ", type=" + type +
                ", oldX=" + oldX +
                ", oldY=" + oldY +
                ", oldPlane=" + oldPlane +
                ", newX=" + newX +
                ", newY=" + newY +
                ", newPlane=" + newPlane +
                '}';
    }
}