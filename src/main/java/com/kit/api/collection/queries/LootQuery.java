package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Loot;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Loot;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author: const_
 */
public class LootQuery extends SceneNodeQuery<Loot, LootQuery> {

    public LootQuery(MethodContext ctx) {
        super(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loot single() {
        List<Loot> items = asList();
        return !items.isEmpty() ? items.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loot> asList() {
        List<Loot> all = context.loot.getAll(Filter.collapse(filters));
        Collections.sort(all, new Comparator<Loot>() {
            @Override
            public int compare(Loot o1, Loot o2) {
                int dist1 = o1.distanceTo(context.players.getLocal());
                int dist2 = o2.distanceTo(context.players.getLocal());
                return dist1 - dist2;
            }
        });
        return all;
    }

    /**
     * Adds a filter so that only ground items with the IDs matching the
     * specified ones can appear in the result set.
     *
     * @param ids A varargs array of IDs that are accepted.
     * @return query
     */
    public LootQuery id(final int... ids) {
        addCondition(new Filter<Loot>() {
            @Override
            public boolean accept(Loot acceptable) {
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
     * Adds a filter so that only ground items with the stacks sizes >=(greater) the
     * specified ones can appear in the result set.
     *
     * @param greater    <t>true if you want stack sizes greater than specified</t>
     * @param stackSizes A varargs array of sizes that are accepted.
     * @return query
     */
    public LootQuery stack(final boolean greater, final int... stackSizes) {
        addCondition(new Filter<Loot>() {
            @Override
            public boolean accept(Loot acceptable) {
                for (int size : stackSizes) {
                    if (acceptable.getStackSize() == size || greater &&
                            acceptable.getStackSize() >= size) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    /**
     * Adds a filter so that only loot with the names matching the
     * specified ones can appear in the result set.
     *
     * @param names A varargs array of names that are accepted.
     * @return query
     */
    public LootQuery named(final String... names) {
        addCondition(new Filter<Loot>() {
            @Override
            public boolean accept(Loot acceptable) {
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
     * Adds a filter so that only loot with the names matching the
     * specified ones can appear in the result set.
     *
     * @param names A varargs array of names that are accepted.
     * @return query
     */
    public LootQuery nameContains(final String... names) {
        addCondition(new Filter<Loot>() {
            @Override
            public boolean accept(Loot acceptable) {
                for (String name : names) {
                    if (acceptable.getComposite() != null &&
                            acceptable.getComposite().getName() != null &&
                            acceptable.getComposite().getName().toLowerCase().
                                    contains(name.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    /**
     * Ignores all items with the given names
     *
     * @param names - the names to ignore
     * @return query
     */
    public LootQuery ignore(final String... names) {
        addCondition(new Filter<Loot>() {
            @Override
            public boolean accept(Loot acceptable) {
                for (String name : names) {
                    if (acceptable.getName() != null &&
                            acceptable.getName().equalsIgnoreCase(name)) {
                        return false;
                    }
                }
                return true;
            }
        });
        return this;
    }

    /**
     * Ignores all items with the given names
     *
     * @param ids - the ids to ignore
     * @return query
     */
    public LootQuery ignore(final int... ids) {
        addCondition(new Filter<Loot>() {
            @Override
            public boolean accept(Loot acceptable) {
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
     * Checks if the item has any of the actions specified
     *
     * @param actions actions
     * @return query
     */
    public LootQuery hasAction(final String... actions) {
        addCondition(new Filter<Loot>() {
            @Override
            public boolean accept(Loot acceptable) {
                if (acceptable.getComposite() != null && acceptable.getComposite().getGroundActions() != null) {
                    for (String action : acceptable.getComposite().getGroundActions()) {
                        for (String actionSpec : actions) {
                            if (action.contains(actionSpec)) {
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

    public LootQuery at(final int x, final int y) {
        addCondition(new Filter<Loot>() {
            @Override
            public boolean accept(Loot acceptable) {
                return acceptable.getX() == x && acceptable.getY() == y;
            }
        });
        return this;
    }
}
