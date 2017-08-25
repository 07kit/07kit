package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.GameObject;
import com.kit.api.wrappers.GameObjectType;
import com.kit.api.wrappers.ObjectComposite;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 */
public class GameObjectQuery extends SceneNodeQuery<GameObject, GameObjectQuery> {

    public GameObjectQuery(MethodContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameObject single() {
        List<GameObject> objects = asList();
        GameObject nearest = null;
        for (GameObject object : objects) {
            if (nearest == null || object.distanceTo(context.player) < nearest.distanceTo(context.player)) {
                nearest = object;
            }
        }
        return nearest;
    }

    /**
     * Ignores all  objects with the given ids
     *
     * @param ids - the names to ignore
     * @return query
     */
    public GameObjectQuery ignore(final int... ids) {
        addCondition(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                for (int id : ids) {
                    if (acceptable.getId() == id) {
                        return false;
                    }
                }
                return true;
            }
        });
        return this;
    }

    /**
     * Ignores all objects with the given names
     *
     * @param names - the names to ignore
     * @return query
     */
    public GameObjectQuery ignore(final String... names) {
        addCondition(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                for (String name : names) {
                    if (acceptable.getName() != null
                            && acceptable.getName().equalsIgnoreCase(name)) {
                        return false;
                    }
                }
                return true;
            }
        });
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameObject> asList() {
        List<GameObject> all = orderSet(context.objects.getFiltered(filters));
        return all;
    }

    /**
     * Adds a filter so that only objects with the IDs matching the
     * specified ones can appear in the result set.
     *
     * @param ids A varargs array of IDs that are accepted.
     * @return query
     */
    public GameObjectQuery id(final int... ids) {
        addCondition(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                for (int id : ids) {
                    if (acceptable.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    /**
     * Adds a filter so that only objects of the specified type
     * can appear in the results set.
     *
     * @param objectTypes varargs array pf GameObjectTypes that are accepted.
     * @return query.
     */
    public GameObjectQuery type(final GameObjectType... objectTypes) {
        addCondition(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                for (GameObjectType objectType : objectTypes) {
                    if (acceptable.getType() == objectType) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }
    /**
     * Adds a filter so that only objects with the names containing the
     * specified ones can appear in the result set.
     *
     * @param names A varargs array of names that are accepted.
     * @return query
     */
    public GameObjectQuery nameContains(final String... names) {
        addCondition(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                for (String name : names) {
                    if (acceptable.getComposite() != null &&
                            acceptable.getComposite().getName() != null &&
                            acceptable.getComposite().getName().toLowerCase().contains(name.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }
    /**
     * Adds a filter so that only objects with the names matching the
     * specified ones can appear in the result set.
     *
     * @param names A varargs array of names that are accepted.
     * @return query
     */
    public GameObjectQuery named(final String... names) {
        addCondition(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                for (String name : names) {
                    if (acceptable.getComposite() != null &&
                            acceptable.getComposite().getName() != null &&
                            acceptable.getComposite().getName().equalsIgnoreCase(name)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    /**
     * Checks if the object has any of the actions specified
     * @param actions actions
     * @return query
     */
    public GameObjectQuery hasAction(final String... actions) {
        addCondition(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                ObjectComposite composite = acceptable.getComposite();
                if (composite != null && composite.getActions() != null) {
                    for(String action : composite.getActions()) {
                        if(action == null) {
                            continue;
                        }
                        for(String actionSpec : actions) {
                            if(action.contains(actionSpec)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
        return this;
    }

    /**
     * Checks if the object has a model or not
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public GameObjectQuery hasModel() {
        addCondition(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                return acceptable.getModel() != null;
            }
        });
        return this;
    }


    /**
     * Checks if the object  is not equal to the supplied one
     *
     * @param object scene node
     * @return not equal
     */
    public GameObjectQuery not(final GameObject object) {
        addCondition(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                return object.unwrap() != null && !acceptable.unwrap().equals(object.unwrap());
            }
        });
        return this;
    }
}
