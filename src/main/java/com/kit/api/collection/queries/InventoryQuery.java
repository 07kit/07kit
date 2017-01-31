package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.WidgetItem;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.WidgetItem;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;

import java.util.List;

/**
 * Author: const_
 */
public class InventoryQuery extends WidgetItemQuery<WidgetItem, InventoryQuery> {

    public InventoryQuery(MethodContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetItem single() {
        List<WidgetItem> items = asList();
        return !items.isEmpty() ? items.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WidgetItem> asList() {
        return filterSet(orderSet(ctx.inventory.getAll()));
    }

    /**
     * Adds a filter so that only inventory items with the names matching the
     * specified ones can appear in the result set.
     *
     * @param names A varargs array of names that are accepted.
     * @return query
     */
    public InventoryQuery named(final String... names) {
        addCondition(new Filter<WidgetItem>() {
            @Override
            public boolean accept(WidgetItem acceptable) {
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
     * Checks if the item has any of the actions specified
     *
     * @param actions actions
     * @return query
     */
    public InventoryQuery hasAction(final String... actions) {
        addCondition(new Filter<WidgetItem>() {
            @Override
            public boolean accept(WidgetItem acceptable) {
                if (acceptable.getComposite() != null && acceptable.getComposite().getInventoryActions() != null) {
                    for (String action : acceptable.getComposite().getInventoryActions()) {
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

}
