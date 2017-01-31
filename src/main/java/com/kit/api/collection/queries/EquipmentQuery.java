package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.impl.tabs.EquipmentImpl;
import com.kit.api.wrappers.WidgetItem;

import java.util.List;

/**
 * Equipment query
 *
 * @author tommo
 */
public class EquipmentQuery extends WidgetItemQuery<WidgetItem, EquipmentQuery> {

    public EquipmentQuery(MethodContext context) {
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
        return filterSet(orderSet(ctx.equipment.getAll()));
    }

    /**
     * Filters a query to contain only items which are in the the specified slots
     *
     * @param slots The equipment slots to allow
     * @return query
     */
    public EquipmentQuery slot(final EquipmentImpl.EquipmentSlot... slots) {
        addCondition(new Filter<WidgetItem>() {
            @Override
            public boolean accept(WidgetItem acceptable) {
                for (EquipmentImpl.EquipmentSlot slot : slots) {
                    if (acceptable.getWidget().getId() == slot.widgetId()) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    /**
     * Adds a filter so that only inventory items with the names matching the
     * specified ones can appear in the result set.
     *
     * @param names A varargs array of names that are accepted.
     * @return query
     */
    public EquipmentQuery named(final String... names) {
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
     * Checks if the equipment has any of the actions specified
     *
     * @param actions actions
     * @return query
     */
    public EquipmentQuery hasAction(final String... actions) {
        addCondition(new Filter<WidgetItem>() {
            @Override
            public boolean accept(WidgetItem acceptable) {
                for (String action : acceptable.getComposite().getInventoryActions()) {
                    for (String actionSpec : actions) {
                        if (action.contains(actionSpec)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        return this;
    }

}
