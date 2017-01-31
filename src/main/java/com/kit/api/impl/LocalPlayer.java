package com.kit.api.impl;


import com.kit.api.wrappers.*;
import com.kit.game.engine.collection.INode;
import com.kit.game.engine.combat.ICombatInfo;
import com.kit.game.engine.combat.ICombatInfoNode;
import com.kit.game.engine.combat.ICombatInfoNodeWrapper;
import com.kit.api.Constants;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.interaction.SceneNode;
import com.kit.game.engine.collection.INode;
import com.kit.game.engine.combat.ICombatInfo;
import com.kit.game.engine.combat.ICombatInfoNode;
import com.kit.game.engine.combat.ICombatInfoNodeWrapper;
import com.kit.api.Constants;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.interaction.SceneNode;
import com.kit.game.engine.collection.INode;

import java.awt.Point;
import java.util.List;

/**
 */
public class LocalPlayer extends SceneNode {
    private final MethodContext ctx;


    public LocalPlayer(MethodContext ctx) {
        super(ctx);
        this.ctx = ctx;
    }

    /**
     * Checks if the player is valid.
     *
     * @return valid
     */
    public boolean isValid() {
        return ctx.players.getLocal() != null;
    }

    /**
     * Get the name of this player.
     *
     * @return name
     */
    public String getName() {
        if (unwrap() == null) {
            return null;
        }
        return unwrap().getName();
    }

    /**
     * Get the equipment this player is wearing.
     *
     * @return equipment
     */
    public List<WidgetItem> getAppearance() {
        return ctx.equipment.getAll();
    }

    public Player unwrap() {
        return ctx.players.getLocal();
    }

    public int getCombatLevel() {
        return unwrap().unwrap().getCombatLevel();
    }
    /**
     * Checks if a player is idle
     *
     * @return <t>true if they're not moving, interacting or in combat</t> otherwise false
     */
    public boolean isIdle() {
        return !isMoving() && !isInteracting() && !isInCombat() && getAnimation() == -1;
    }

    /**
     * Get the ID of the animation entity is currently performing.
     *
     * @return animation ID
     */
    public int getAnimation() {
        return unwrap().getAnimationId();
    }

    /**
     * Get the current health for this entity.
     * Note: this is only available if the entity
     * is engaged in combat.
     *
     * @return current health
     */
    public int getCurrentHealth() {
        return ctx.skills.getLevel(Skill.HITPOINTS);
    }

    /**
     * Get the maximum possible health for this entity.
     *
     * @return max health
     */
    public int getMaxHealth() {
        return ctx.skills.getBaseLevel(Skill.HITPOINTS);
    }

    /**
     * Get the turn direction (model rotation) of this entity.
     *
     * @return turn direction
     */
    public int getTurnDirection() {
        return unwrap().getOrientation();
    }


    /**
     * Get the Entity that is interacting with this one.
     *
     * @return Interacting Entity.
     */
    public Entity getInteractingEntity() {
        return unwrap().getInteractingEntity();
    }

    /**
     * Get the amount of steps remaining in the walking queue.
     *
     * @return path length
     */
    public int getPathLength() {
        return unwrap().getQueueSize();
    }

    /**
     * Get the text of the last message spoken by this Entity.
     *
     * @return last message
     */
    public String getMessage() {
        return unwrap().getMessage();
    }

    /**
     * Get the model of this Entity
     *
     * @return model
     */
    public Model getModel() {
        Model model = unwrap().getModel();
        if (model == null) {
            return null;
        }
        return model.update(ctx, getLocalX(), getLocalY(), getTurnDirection());
    }


    public int getLocalX() {
        return unwrap().getLocalX();
    }

    public int getLocalY() {
        return unwrap().getLocalY();
    }


    /**
     * @{inheritDoc}
     */
    public int getX() {
        return ctx.client().getBaseX() + (getLocalX() >> Constants.LOCAL_XY_SHIFT);
    }

    /**
     * @{inheritDoc}
     */
    public int getY() {
        return ctx.client().getBaseY() + (getLocalY() >> Constants.LOCAL_XY_SHIFT);
    }

    /**
     * {@inheritDoc}
     */
    public int getZ() {
        return ctx.client().getPlane();
    }

    public Point getBasePoint() {
        return ctx.viewport.convert(getLocalX(), getLocalY(), -(unwrap().unwrap().getHeight() / 2));
    }

    public Point getClickPoint() {
        return ctx.viewport.convert(getLocalX(), getLocalY(), -(unwrap().unwrap().getHeight() / 2));
    }

    public int[] getEquipment() {
        return unwrap().unwrap().getComposite() != null ? unwrap().unwrap().getComposite().getAppearance() : null;
    }

    /**
     * Checks if the entity is engaged in combat
     *
     * @return true if the current entity is in combat
     */
    public boolean isInCombat() {
        ICombatInfoNodeWrapper wrapper = getCombatInfoWrapper();
        return getHealthRatio(wrapper) != -1 || (isInteracting() && getInteractingEntity().getHealthRatio(wrapper) != -1);
    }


    /**
     * Checks if the entity is currently moving
     *
     * @return true if the entity is moving
     */
    public boolean isMoving() {
        return unwrap().getQueueSize() != 0;
    }

    /**
     * Checks if the entity is interacting with other
     * entities
     *
     * @return true if it is
     */
    public boolean isInteracting() {
        return unwrap().getAssociatedEntity() != -1;
    }

    public int getHealthPercent() {
        ICombatInfoNodeWrapper wrapper = getCombatInfoWrapper();
        int ratio = getHealthRatio(wrapper);
        int max = getHealthBarScale(wrapper);
        return ratio > 0 ? (int) (((double) ratio / (double) max) * 100.0d) : -1;
    }


    public ICombatInfoNodeWrapper getCombatInfoWrapper() {
        ICombatInfoNode combatInfoNode = unwrap().unwrap().getCombatInfoNode();
        if (combatInfoNode != null) {
            INode node = combatInfoNode.getPrevious();
            INode next = node.getNext();
            if (next instanceof ICombatInfoNodeWrapper) {
                return (ICombatInfoNodeWrapper) next;
            }
        }
        return null;
    }

    public int getHealthRatio(ICombatInfoNodeWrapper wrapper) {
        if (wrapper != null) {
            ICombatInfoNode combatInfoList2 = wrapper.getCombatInfoNode();
            INode node2 = combatInfoList2.getPrevious();
            INode next2 = node2.getNext();
            if (next2 instanceof ICombatInfo) {
                ICombatInfo combatInfo = (ICombatInfo) next2;
                return combatInfo.getHealthRatio();
            }
        }
        return -1;
    }


    public int getHealthBarScale(ICombatInfoNodeWrapper wrapper) {
        if (wrapper != null) {
            return wrapper.getHealthBarType().getScale();
        }
        return -1;
    }

}
