package com.kit.api.impl.scene;


import com.kit.api.MethodContext;
import com.kit.api.Players;
import com.kit.api.collection.queries.PlayerQuery;
import com.kit.api.event.PlayerRegionChangeEvent;
import com.kit.api.wrappers.Region;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Player;
import com.sun.org.apache.regexp.internal.RE;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @see Players
 */
public class PlayersImpl implements Players {
    private final MethodContext ctx;


    public PlayersImpl(MethodContext ctx) {
            this.ctx = ctx;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Player getLocal() {
        IPlayer local = ctx.client().getLocalPlayer();
        if (local != null) {
            return local.getWrapper();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> getAll() {
        List<Player> players = newArrayList();
        IPlayer[] playerArray = ctx.client().getPlayers();
        for (IPlayer player : playerArray) {
            if (player != null) {
                players.add(player.getWrapper());
            }
        }
        return players;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerQuery find() {
        return new PlayerQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerQuery find(String... names) {
        return find().named(names);
    }

    @Override
    public void loadPlayerPositions() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public List<Player> getAll(List<Filter<Player>> filters) {
        Filter<Player> collapsed = Filter.collapse(filters);
        List<Player> players = newArrayList();
        IPlayer[] playerArray = ctx.client().getPlayers();
        for (IPlayer player : playerArray) {
            if (player != null) {
                Player wrapped = player.getWrapper();
                if (collapsed.accept(wrapped)) {
                    players.add(player.getWrapper());
                }
            }
        }
        return players;
    }
}
