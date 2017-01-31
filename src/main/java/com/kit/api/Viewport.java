package com.kit.api;

import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.Tile;

import java.awt.*;

/**
 * Viewport methods
 *
 */
public interface Viewport {

    /**
     * Converts a set of local coordinates into their screen positions
     *
     * @param tileX        local X coordinate
     * @param tileY        local Y coordinate
     * @param renderHeight Height of the content on the tile.
     * @return A point representing the screen coordinate, of which the x/y may be -1.
     */
    Point convert(int tileX, int tileY, int renderHeight);


    Point convert(int x, int y, int modelHeight, boolean flatten);

    /**
     * Converts a tile to a on screen point
     *
     * @param tileX   the tile x
     * @param tileY   the tile y
     * @param offsetX the x offset
     * @param offsetY the y offset
     * @param height  the tile height
     * @return an on screen point
     */
    Point convert(int tileX, int tileY, double offsetX, double offsetY, int height);

    /**
     * Converts a tile to a on screen point
     *
     * @param tileX   the tile x
     * @param tileY   the tile y
     * @param offsetX the x offset
     * @param offsetY the y offset
     * @param height  the tile height
     * @return an on screen point
     */
    Point convert(int tileX, int tileY, double offsetX, double offsetY, int height, boolean flatten);

    /**
     * Converts a world-wide tile to a regional tile and converts
     * that regional tile into a Point representing the position within the viewport.
     *
     * @param tile tile
     * @return A point representing the screen coordinate, of which the x/y may be -1.
     */
    Point convert(Tile tile);

    Point convert(Tile tile, double offsetX, double offsetY);

    /**
     * Converts a regional tile to a screen position
     *
     * @param tile tile
     * @return A point representing the screen coordinate, of which the x/y may be -1.
     */
    Point convertLocal(Tile tile);

    int getTileHeight(int tileX, int tileY, int tilePlane);

    /**
     * Tells you if a point is in the viewport.
     *
     * @param point The point to check
     * @return <t>true</t> if the point is within the viewport otherwise false.
     */
    boolean isInViewport(Point point);
    /**
     * Tells you if a point is in the viewport.
     *
     * @param rect The rect to check
     * @return <t>true</t> if the point is within the viewport otherwise false.
     */
    boolean isInViewport(Rectangle rect);

}
