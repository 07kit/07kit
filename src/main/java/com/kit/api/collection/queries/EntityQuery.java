package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Entity;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Entity;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Entity;

/**
 */
public abstract class EntityQuery<E extends Entity, T extends EntityQuery> extends SceneNodeQuery<E, T> {

    public EntityQuery(MethodContext context) {
        super(context);
    }

    /**
     * Checks if the entity's movement state matches the state specified
     *
     * @param moving true if resulting entity should be moving.
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T moving(final boolean moving) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return moving == acceptable.isMoving();
            }
        });
        return (T) this;
    }

    /**
     * Checks if the entity's combat state matches the state specified
     *
     * @param fighting true if resulting entity should be fighting.
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T fighting(final boolean fighting) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return fighting == acceptable.isInCombat();
            }
        });
        return (T) this;
    }

    /**
     * Checks if the entity's animation state matches the state specified
     *
     * @param animating true if resulting entity should be animating.
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T animating(final boolean animating) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return animating == (acceptable.getAnimationId() != -1);
            }
        });
        return (T) this;
    }

    /**
     * Checks if the entity's message(speech) contains a specified message
     *
     * @param messages the messages(parts of) to look for
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T messageContains(final String... messages) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                for (String message : messages) {
                    if (acceptable.getMessage() != null &&
                            acceptable.getMessage().contains(message)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return (T) this;
    }

    /**
     * Checks if the entity's name contains a specified name
     *
     * @param names the names(parts of) to look for
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T nameContains(final String... names) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                for (String name : names) {
                    if (acceptable.getName().contains(name)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return (T) this;
    }

    /**
     * Checks if the entity's current animation matches the one specified
     *
     * @param animation an animation ID.
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T animation(final int animation) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return acceptable.getAnimationId() == animation;
            }
        });
        return (T) this;
    }

    /**
     * Checks if the entity has a model or not
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T hasModel() {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return acceptable.getModel() != null;
            }
        });
        return (T) this;
    }

    /**
     * Adds a filter so that only entity's with the names matching the
     * specified ones can appear in the result set.
     *
     * @param names A varargs array of names that are accepted.
     * @return query
     */
    public T named(final String... names) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                for (String name : names) {
                    if (acceptable != null &&
                            acceptable.getName() != null &&
                            acceptable.getName().equalsIgnoreCase(name)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return (T) this;
    }

    /**
     * Ignores all entities with the given names
     *
     * @param names - the names to ignore
     * @return query
     */
    public T ignore(final String... names) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                for (String name : names) {
                    if (acceptable != null && acceptable.getName() != null &&
                            acceptable.getName().equalsIgnoreCase(name)) {
                        return false;
                    }
                }
                return true;
            }
        });
        return (T) this;
    }

    /**
     * Checks if the Entity is interacting with the one specified;
     *
     * @param entity Entity to check.
     * @return query.
     */
    public T interacting(final Entity entity) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                if (acceptable.getInteractingEntity() == null ||
                        entity == null) {
                    return false;
                }
                return acceptable.getInteractingEntity().equals(entity); // TODO - Improve to use index's maybe
            }
        });
        return (T) this;
    }

}
