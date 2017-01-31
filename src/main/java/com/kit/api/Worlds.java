package com.kit.api;


import com.kit.api.wrappers.World;
import com.kit.api.wrappers.World;
import com.kit.api.wrappers.World;

import java.util.List;

/**
 * API for world selection etc.
 *
 * * @author tobiewarburton
 */
public interface Worlds {

    /**
     * Retrives the World with the least players
     *
     * @return the most empty world.
     */
    World getEmptiest();

    /**
     * Retrives the world with the most players
     *
     * @return the busiest world.
     */
    World getBusiest();

    /**
     * Randomly selects a world from all of them.
     *
     * @return a random world
     */
    World getAny();

    /**
     * Switches to the specified world
     *
     * @param world the world to switch to.
     */
    void switchTo(World world);

    int getPing(World world);

    int getCurrent();

    /**
     * Retrives a list of all the worlds.
     *
     * @return a list of all the worlds.
     */
    List<World> getAll();

}