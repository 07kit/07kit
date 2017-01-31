package com.kit.api;

import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.interaction.SceneNode;

/**
 * @author const_
 */
public interface Camera {

    /**
     * Gets the camera X
     *
     * @return camera x
     */
    int getX();

    /**
     * Gets the camera y
     *
     * @return camera y
     */
    int getY();

    /**
     * Gets the camera z
     *
     * @return camera z
     */
    int getZ();


    /**
     * Retrieves the current camera angle.
     *
     * @return camera angle
     */
    int getAngle();

    /**
     * Retrieves the current camera pitch
     *
     * @return camera pitch
     */
    int getPitch();


    /**
     * Calculates the angle to tile
     *
     * @param tile the tile in which you want to get the angle to
     * @return the angle to tile
     */
    int getAngleTo(Tile tile);



}
