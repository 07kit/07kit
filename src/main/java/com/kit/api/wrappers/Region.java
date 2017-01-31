package com.kit.api.wrappers;

import com.kit.plugins.map.WorldMapPlugin;

import java.awt.*;

public class Region {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    private int regionX;
    private int regionY;
    private int numberPlayers;
    private Point start;
    private Point end;

    public Region(int regionX, int regionY) {
        this.regionX = regionX;
        this.regionY = regionY;
        this.numberPlayers = 0;
        this.start = new Point(regionX * Region.WIDTH, WorldMapPlugin.MAP.getHeight() -
                ((regionY + 1) * Region.HEIGHT));
        this.end = new Point((regionX * Region.WIDTH) + Region.WIDTH, WorldMapPlugin.MAP.getHeight() -
                ((regionY + 1) * Region.HEIGHT + Region.HEIGHT));
    }

    public int getRegionX() {
        return regionX;
    }

    public void setRegionX(int regionX) {
        this.regionX = regionX;
    }

    public int getRegionY() {
        return regionY;
    }

    public void setRegionY(int regionY) {
        this.regionY = regionY;
    }

    public int getNumberPlayers() {
        return numberPlayers;
    }

    public void setNumberPlayers(int numberPlayers) {
        this.numberPlayers = numberPlayers;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public boolean isInRegion(int x, int y) {
        return regionX == x && regionY == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region = (Region) o;

        if (regionX != region.regionX) return false;
        return regionY == region.regionY;
    }

    @Override
    public int hashCode() {
        int result = regionX;
        result = 31 * result + regionY;
        return result;
    }

    public void increment() {
        this.numberPlayers += 1;
    }

    public void decrement() {
        if (this.numberPlayers == 0) {
            return;
        }
        this.numberPlayers -= 1;
    }

    @Override
    public String toString() {
        return "Region{" +
                "regionX=" + regionX +
                ", regionY=" + regionY +
                ", numberPlayers=" + numberPlayers +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
