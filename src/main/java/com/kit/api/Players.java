package com.kit.api;

import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.PlayerQuery;
import com.kit.api.wrappers.Player;
import com.kit.api.wrappers.Region;

import java.util.List;

/**
 * Methods for dealing with Players in the game.
 *
 */
public interface Players {

    /**
     * Gets the currently logged in player
     *
     * @return local player
     */
    Player getLocal();

    /**
     * Gets all players within the current region.
     *
     * @return players
     */
    List<Player> getAll();

    /**
     * Gets a new PlayerQuery which can be used for
     * finding and selecting players matching certain
     * conditions.
     *
     * @return query
     */
    PlayerQuery find();

    /**
     * Creates a query using a Player's most basic attribute, being it's name.
     *
     * @return query
     */
    PlayerQuery find(String... names);

    void loadPlayerPositions();

    List<Player> getAll(List<Filter<Player>> filters);
}
