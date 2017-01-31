package com.kit.api;

import java.awt.Point;

/**
 * Date: 29/08/13
 * Time: 09:29
 *
 * @author Matt Collinge
 */
public interface Mouse {

    /**
     * Gets the current position of the client mouse.
     *
     * @return current mouse position
     */
    Point getPosition();

}
