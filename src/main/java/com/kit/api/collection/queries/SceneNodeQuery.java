package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Area;
import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.interaction.SceneNode;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.interaction.SceneNode;

/**
 * Base queries for SceneNodes
 *
 * @param <E> Actual type
 * @param <T> Actual query
 */
public abstract class SceneNodeQuery<E extends SceneNode, T extends SceneNodeQuery> extends Query<E> {
    protected final MethodContext context;

    public SceneNodeQuery(final MethodContext context) {
        this.context = context;
    }

    /**
     * Checks if the Scene Node is at the specified location.
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T location(final SceneNode tile) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return acceptable.getTile().equals(tile);
            }
        });
        return (T) this;
    }

    /**
     * Checks if the Scene Node is at the specified location.
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T location(final Tile tile) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return acceptable.getTile().equals(tile);
            }
        });
        return (T) this;
    }

    /**
     * Checks if the Scene Node is in the specified area
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T area(final Area area) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return area.contains(acceptable);
            }
        });
        return (T) this;
    }

    /**
     * Checks if the Scene node is on screen
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T visible() {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return acceptable.isOnScreen();
            }
        });
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T onMinimap() {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return acceptable.isOnMinimap();
            }
        });
        return (T) this;
    }

    /**
     * Checks if the SceneNode is within a certain distance from the player.
     *
     * @param distance distance to check
     * @return query
     */
    public T distance(final int distance) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return context.players.getLocal().getTile().distanceTo(acceptable.getTile()) < distance;
            }
        });
        return (T) this;
    }

    public T orderByDistance() {
        return (T) this;
    }

}
