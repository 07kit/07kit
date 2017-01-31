package com.kit.api.wrappers;

import com.kit.api.MethodContext;
import com.kit.api.MethodContext;

import java.awt.*;

/**
 * Wrapper for a tile on the RuneScape map
 *
 */
public class Tile {

    private MethodContext context;
    private int x;
    private int y;
    private int z;
    private Polygon polygon;

    public Tile(MethodContext context, int x, int y, int z) {
        this.context = context;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Tile(int x, int y, int z) {
        this(null, x, y, z);
    }

    public Tile(int x, int y) {
        this(x, y, 0);
    }

    public Tile() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "X: " + x +
                ", Y: " + y +
                ", Z: " + z;
    }

    /**
     * Gets the distance to another tile
     *
     * @param tile The tile to find the distance to
     * @return The distance between this and the tile specified
     */
    public int distanceTo(Tile tile) {
        int px = tile.getX();
        int py = tile.getY();
        px -= getX();
        py -= getY();
        return (int) Math.sqrt(px * px + py * py);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tile)) {
            return super.equals(object);
        }
        Tile tile = (Tile) object;
        return tile.x == x && tile.y == y;
    }

    public Tile derive(int x, int y, int z) {
        return new Tile(context, this.x + x, this.y + y, this.z + z);
    }

    public Polygon getPolygon() {
        final Point pn = context.viewport.convert(this);
        final Point px = context.viewport.convert(derive(1, 0, 0));
        final Point py = context.viewport.convert(derive(0, 1, 0));
        final Point pxy = context.viewport.convert(derive(1, 1, 0));
        if (pn.x != -1 && pn.y != -1 && px.x != -1 && px.y != -1
                && py.x != -1 && py.y != -1 && pxy.x != -1 && pxy.y != -1) {
            return new Polygon(new int[]{py.x, pxy.x, px.x, pn.x}, new int[]{py.y, pxy.y, px.y, pn.y}, 4);
        }
        return new Polygon();
    }

    public void drawBox(final Graphics g, final int height) {
        final Point bottomLeft = context.viewport.convert(x, y, 0, 0, 0);
        final Point bottomRight = context.viewport.convert(x, y, 1.0, 0, 0);
        final Point topLeft = context.viewport.convert(x, y, 0, 1.0, 0);
        final Point topRight = context.viewport.convert(x, y, 1.0, 1.0, 0);

        final Point upperBottomLeft = context.viewport.convert(x, y, 0, 0, height);
        final Point upperBottomRight = context.viewport.convert(x, y, 1.0, 0, height);
        final Point upperTopLeft = context.viewport.convert(x, y, 0, 1.0, height);
        final Point upperTopRight = context.viewport.convert(x, y, 1.0, 1.0, height);

        int[] xPoints = new int[]{
                topLeft.x, topRight.x, bottomRight.x, bottomLeft.x, topLeft.x,
                upperTopLeft.x, upperTopRight.x, topRight.x, upperTopRight.x, upperBottomRight.x,
                bottomRight.x, upperBottomRight.x, upperBottomLeft.x, bottomLeft.x, upperBottomLeft.x,
                upperTopLeft.x, topLeft.x
        };
        int[] yPoints = new int[]{
                topLeft.y, topRight.y, bottomRight.y, bottomLeft.y, topLeft.y,
                upperTopLeft.y, upperTopRight.y, topRight.y, upperTopRight.y, upperBottomRight.y,
                bottomRight.y, upperBottomRight.y, upperBottomLeft.y, bottomLeft.y, upperBottomLeft.y,
                upperTopLeft.y, topLeft.y
        };

        for (int i = 0; i < xPoints.length; i++) {
            if (xPoints[i] == -1 || yPoints[i] == -1) {
                return;
            }
        }

        Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
        g.drawPolygon(polygon);
    }

    public void draw(final Graphics g) {
        g.drawPolygon(getPolygon());
    }

    @Override
    public int hashCode() {
        return x >> 4 + y << 5 + z;
    }

}
