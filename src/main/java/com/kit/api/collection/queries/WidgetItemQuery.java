package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.WidgetItem;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.WidgetItem;

/**
 * Base queries for widget items
 *
 * @param <E> Actual type
 * @param <T> Actual query
 * @author : const_
 */
public abstract class WidgetItemQuery<E extends WidgetItem, T extends WidgetItemQuery> extends Query<E> {

    final MethodContext ctx;

    public WidgetItemQuery(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Adds a filter so that only widget items with the IDs matching the
     * specified ones can appear in the result set.
     *
     * @param ids A varargs array of IDs that are accepted.
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T id(final int... ids) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                for (int id : ids) {
                    if (acceptable.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        });
        return (T) this;
    }

    /**
     * Checks if the item's name contains a specified name
     *
     * @param names the names(parts of) to look for
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T nameContains(final String... names) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                if (acceptable.getName() == null) {
                    return false;
                }

                for (String name : names) {
                    if (acceptable.getName().toLowerCase().contains(name.toLowerCase())) {
                        return true;
                    } else if (name.contains("|") && acceptable.getName().toLowerCase().matches(name.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        });
        return (T) this;
    }

    /**
     * Adds a filter so that only widget items with the stacks sizes >=(greater) the
     * specified ones can appear in the result set.
     *
     * @param greater    <t>true if you want stack sizes greater than or equal to specified</t>
     * @param stackSizes A varargs array of sizes that are accepted.
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T stack(final boolean greater, final int... stackSizes) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                for (int size : stackSizes) {
                    if (acceptable.getStackSize() == size || greater &&
                            acceptable.getStackSize() >= size) {
                        return true;
                    }
                }
                return false;
            }
        });
        return (T) this;
    }

    /**
     * Adds a filter so that only widget items with the stacks sizes equal to the
     * specified ones can appear in the result set.
     *
     * @param stackSizes A varargs array of sizes that are accepted.
     * @return query
     */
    public T stack(final int... stackSizes) {
        return stack(false, stackSizes);
    }

    public T named(final String... names) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                for (String name : names) {
                    if (acceptable.getComposite() != null && acceptable.getComposite().getName() != null &&
                            acceptable.getComposite().getName().equals(name)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return (T) this;
    }

    /**
     * Ignores all items with the given names
     *
     * @param names - the names to ignore
     * @return query
     */
    public T ignore(final String... names) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                for (String name : names) {
                    if (acceptable.getName() != null &&
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
     * Ignores all items with the given names
     *
     * @param ids - the ids to ignore
     * @return query
     */
    public T ignore(final int... ids) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                for (int id : ids) {
                    if (acceptable.getId() == id) {
                        return false;
                    }
                }
                return true;
            }
        });
        return (T) this;
    }

}
