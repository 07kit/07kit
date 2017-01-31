package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Npc;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An NPC finder implemented with a query-like pattern.
 *
 */
public class NpcQuery extends EntityQuery<Npc, NpcQuery> {

    public NpcQuery(MethodContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Npc single() {
        List<Npc> npcs = asList();
        return !npcs.isEmpty() ? npcs.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Npc> asList() {
        List<Npc> all = orderSet(context.npcs.getFiltered(filters));
        return all;
    }

    /**
     * Ignores all npcs with the given ids
     *
     * @param ids - the names to ignore
     * @return query
     */
    public NpcQuery ignore(final int... ids) {
        addCondition(new Filter<Npc>() {
            @Override
            public boolean accept(Npc acceptable) {
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
     * Adds a filter so that only npcs with the IDs matching the
     * specified ones can appear in the result set.
     *
     * @param ids A varargs array of IDs that are accepted.
     * @return query
     */
    public NpcQuery id(final Collection<Integer> ids) {
        addCondition(new Filter<Npc>() {
            @Override
            public boolean accept(Npc acceptable) {
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
     * Adds a filter so that only npcs with the IDs matching the
     * specified ones can appear in the result set.
     *
     * @param ids A varargs array of IDs that are accepted.
     * @return query
     */
    public NpcQuery id(final int... ids) {
        addCondition(new Filter<Npc>() {
            @Override
            public boolean accept(Npc acceptable) {
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
     * Adds a filter so that only npcs that support at least one of
     * the specified actions can appear in the result set.
     *
     * @param actions A varargs array of actions that are accepted.
     * @return query
     */
    public NpcQuery hasAction(final String... actions) {
        addCondition(new Filter<Npc>() {
            @Override
            public boolean accept(Npc acceptable) {
                for (String action : actions) {
                    if(acceptable.getComposite() == null || acceptable.getComposite().getActions() == null) {
                        continue;
                    }
                    for (String actAction : acceptable.getComposite().getActions()) {
                        if (actAction != null && actAction.toLowerCase().contains(action.toLowerCase())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        return this;
    }

    public NpcQuery vertexCount(final int vertexCount) {
        addCondition(new Filter<Npc>() {
            @Override
            public boolean accept(Npc acceptable) {
                return acceptable.getModel() != null &&
                        acceptable.getModel().getPolygons().length == vertexCount;
            }
        });
        return this;
    }

    /**
     * Checks if the NPC is not equal to the supplied one
     *
     * @param object scene node
     * @return not equal
     */
    public NpcQuery not(final Npc object) {
        addCondition(new Filter<Npc>() {
            @Override
            public boolean accept(Npc acceptable) {
                return object.unwrap() != null && !acceptable.unwrap().equals(object.unwrap());
            }
        });
        return this;
    }
}
