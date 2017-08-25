package com.kit.api.wrappers;

import com.kit.game.engine.collection.INode;
import com.kit.game.engine.combat.ICombatInfo;
import com.kit.game.engine.combat.ICombatInfoNode;
import com.kit.game.engine.combat.ICombatInfoNodeWrapper;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.api.Constants;
import com.kit.api.MethodContext;
import com.kit.api.util.ReflectionUtils;
import com.kit.api.wrappers.interaction.SceneNode;
import com.kit.game.engine.collection.INode;
import com.kit.game.engine.combat.ICombatInfo;
import com.kit.game.engine.combat.ICombatInfoNode;
import com.kit.game.engine.combat.ICombatInfoNodeWrapper;
import com.kit.game.engine.renderable.entity.IEntity;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.api.Constants;
import com.kit.api.MethodContext;
import com.kit.game.engine.collection.INode;
import com.kit.game.engine.renderable.entity.INpc;

import java.awt.Point;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * An Entity in the RuneScape scene graph.
 * Examples of entities are:
 * - NPCs
 * - Players
 *
 */
public abstract class Entity extends SceneNode {
	protected final WeakReference<IEntity> wrapped;
	private final MethodContext ctx;

	public Entity(MethodContext ctx, IEntity wrapped) {
		super(ctx);
		this.wrapped = new WeakReference<>(wrapped);
		this.ctx = ctx;
	}

	protected MethodContext getContext() {
		return ctx;
	}

	public IEntity unwrap() {
		return wrapped.get();
	}

	/**
	 * Get the ID of the animation entity is currently performing.
	 *
	 * @return animation ID
	 */
	public int getAnimationId() {
		return unwrap().getAnimationId();
	}

	public int getHealthPercent() {
		ICombatInfoNodeWrapper wrapper = getCombatInfoWrapper();
		int ratio = getHealthRatio(wrapper);
		int max = getHealthBarScale(wrapper);
		return ratio > 0 ? (int) (((double) ratio / (double) max) * 100.0d) : -1;
	}


	public ICombatInfoNodeWrapper getCombatInfoWrapper() {
		ICombatInfoNode combatInfoNode = unwrap().getCombatInfoNode();
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

	/**
	 * Get the turn direction (model rotation) of this entity.
	 *
	 * @return turn direction
	 */
	public int getOrientation() {
		return unwrap().getOrientation();
	}

	/**
	 * Get the array index of the entity that is interacting with this entity.
	 *
	 * @return entity index
	 */
	public int getAssociatedEntity() {
		return unwrap().getInteractingIndex();
	}

	/**
	 * Get the Entity that is interacting with this one.
	 *
	 * @return Interacting Entity.
	 */
	public Entity getInteractingEntity() {
		int index = getAssociatedEntity();
		if (index == -1)
			return null;

		if (index < 0x8000) {
			INpc accessor = ctx.client().getNpcs()[index];
			return accessor != null ? accessor.getWrapper() : null;
		} else {
			IPlayer accessor = ctx.client().getPlayers()[index - 0x8000];
			if (accessor == null) {
				return ctx.players.getLocal();
			}
			return accessor.getWrapper();
		}
	}

	/**
	 * Get the amount of steps remaining in the walking queue.
	 *
	 * @return path length
	 */
	public int getQueueSize() {
		return unwrap().getQueueLength();
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
		if (model == null || !model.isValid()) {
			return null;
		}
		return model.update(ctx, getLocalX(), getLocalY(), getOrientation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLocalX() {
		return unwrap().getLocalX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLocalY() {
		return unwrap().getLocalY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getX() {
		return ctx.client().getBaseX() + (getLocalX() >> Constants.LOCAL_XY_SHIFT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getY() {
		return ctx.client().getBaseY() + (getLocalY() >> Constants.LOCAL_XY_SHIFT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getZ() {
		return ctx.client().getPlane();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point getBasePoint() {
		return ctx.viewport.convert(getLocalX(), getLocalY(), (unwrap().getHeight() / 2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point getClickPoint() {
		if (getModel() != null) {
			return getModel().getClickPoint();
		}
		return getBasePoint();
	}

	@Override
	public boolean isValid() {
		return unwrap() != null;
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
		return getQueueSize() != 0;
	}

	/**
	 * Checks if the entity is interacting with other
	 * entities
	 *
	 * @return true if it is
	 */
	public boolean isInteracting() {
		return getInteractingEntity() != null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Entity) {
			return ((Entity) obj).unwrap().equals(unwrap());
		}
		return super.equals(obj);
	}

	/**
	 * Returns the entities name
	 */
	public abstract String getName();
}
