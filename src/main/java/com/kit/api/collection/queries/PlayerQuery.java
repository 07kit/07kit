package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Player;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A player finder implemented with a query-like pattern.
 *
 */
public class PlayerQuery extends EntityQuery<Player, PlayerQuery> {

    public PlayerQuery(MethodContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player single() {
        List<Player> players = asList();
        return !players.isEmpty() ? players.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> asList() {
        List<Player> all = context.players.getAll(filters);
        Collections.sort(all, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                int dist1 = o1.distanceTo(context.players.getLocal());
                int dist2 = o2.distanceTo(context.players.getLocal());
                return dist1 - dist2;
            }
        });
        return all;
    }

    public PlayerQuery levelBetween(int lower, int higher) {
        addCondition(acceptable -> acceptable.getCombatLevel() >= lower && acceptable.getCombatLevel() <= higher);
        return this;
    }
}
