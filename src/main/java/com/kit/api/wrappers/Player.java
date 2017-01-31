package com.kit.api.wrappers;

import com.kit.api.MethodContext;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.api.MethodContext;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.api.MethodContext;

/**
 * A wrapper for players within the RuneScape client.
 *
 */
public class Player extends Entity implements Wrapper<IPlayer> {
    public Player(MethodContext ctx, IPlayer wrapped) {
        super(ctx, wrapped);
    }

    /**
     * Get the name of this player.
     *
     * @return name
     */
    @Override
    public String getName() {
        return unwrap().getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPlayer unwrap() {
        return (IPlayer) wrapped.get();
    }


    public int getCombatLevel() {
        return unwrap().getCombatLevel();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            return ((Player) obj).wrapped.equals(wrapped);
        }
        return super.equals(obj);
    }

    /**
     * Checks if a player is idle
     *
     * @return <t>true if they're not moving, interacting or in combat</t> otherwise false
     */
    public boolean isIdle() {
        return !isMoving() && !isInteracting() && !isInCombat() && getAnimationId() == -1;
    }
}
