package com.kit.api;

import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.Widget;

import java.awt.Point;

/**
 * Date: 27/08/13
 * Time: 14:06
 *
 * @author Matt Collinge
 */
public interface Minimap {

    /**
     * Converts a world coordinate to a minimap coordinate
     *
     * @param tile The tile to convert to minimap coordinate.
     * @return Point representing the position on the minimap. This point may have an x/y of -1.
     */
    Point convert(Tile tile);

    /**
     * Converts a world coordinate to a minimap coordinate.
     *
     * @param x world X coordinate
     * @param y world Y coordinate
     * @return Point representing the position on the minimap. This point may have an x/y of -1.
     */
    Point convert(int x, int y);

    Point convertUnbounded(int x, int y);

    Point convertUnbounded(Tile tile);

    Widget getWidget();
}
