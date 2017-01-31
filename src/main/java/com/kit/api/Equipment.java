package com.kit.api;

import com.kit.api.wrappers.Widget;
import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.EquipmentQuery;
import com.kit.api.impl.tabs.EquipmentImpl;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.api.collection.queries.EquipmentQuery;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;

import java.util.List;

/**
 * Methods to interact with equipment
 *
 * @author tommo
 */
public interface Equipment {

    /**
     * Gets the equipment widget
     * @return widget
     */
    Widget getWidget();
    /**
     * Retrieves all currently equipped items
     *
     * @return A list of all items (can be empty).
     */
    List<WidgetItem> getAll();

    /**
     * Constructs a query
     *
     * @return query
     */
    EquipmentQuery find();

    /**
     * Constructs a query using the most basic
     * attribute of an inventory item, the ID.
     *
     * @param ids A varargs of item ids
     * @return query
     */
    EquipmentQuery find(int... ids);

    /**
     * Constructs a query using an
     * attribute of an inventory item, the name.
     *
     * @param names A varargs of item names
     * @return query
     */
    EquipmentQuery find(String... names);

    /**
     * Constructs a quest limiting to items in specified slots only
     *
     * @param slots The {@link EquipmentImpl.EquipmentSlot}s
     * @return The query
     */
    EquipmentQuery find(EquipmentImpl.EquipmentSlot... slots);

    /**
     * Checks if the equipment contains a range of ids
     *
     * @param ids A varargs array of IDs that are accepted.
     * @return <t>true if the equipment contains an id</t> otherwise false
     */
    boolean contains(int... ids);

    /**
     * Checks if the equipment contains a range of names
     *
     * @param names A varargs array of names that are accepted.
     * @return <t>true if the equipment contains an name</t> otherwise false
     */
    boolean contains(String... names);

    /**
     * Returns the amount of items currently equipped
     *
     * @return the size (int)
     */
    int getCount();

    /**
     * Checks if the player is fully equipped, e.g. if all slots are taken
     *
     * @return <t>true</b> if full, <b>false</b> if not
     */
    boolean isFull();

    /**
     * Checks if the player has no equipment, e.g. if all slots are empty
     *
     * @return <t>true</b> if empty, <b>false</b> if not
     */
    boolean isEmpty();


}
