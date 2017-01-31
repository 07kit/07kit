package com.kit.api.impl.tabs;


import com.kit.api.Equipment;
import com.kit.api.MethodContext;
import com.kit.api.collection.queries.EquipmentQuery;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetGroup;
import com.kit.api.wrappers.structure.Bag;
import com.kit.game.engine.IItemCache;
import com.kit.game.engine.collection.INode;
import com.kit.api.Equipment;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.EquipmentQuery;
import com.kit.api.collection.queries.InventoryQuery;
import com.kit.api.wrappers.Tab;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetGroup;
import com.kit.api.wrappers.WidgetItem;
import com.kit.api.wrappers.structure.Bag;
import com.kit.game.engine.IItemCache;
import com.kit.game.engine.collection.INode;
import com.kit.api.Equipment;
import com.kit.api.MethodContext;
import com.kit.api.collection.queries.EquipmentQuery;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.api.wrappers.structure.Bag;
import com.kit.game.engine.IItemCache;
import com.kit.game.engine.collection.INode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Equipment interaction implementation
 *
 * @author tommo
 */
public class EquipmentImpl implements Equipment {

    private static final int GROUP_ID = 387;
    private static final int CHILD_ID = 1;

    private static int ITEM_STORAGE_ID = 94;

    private final MethodContext ctx;


    public EquipmentImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<WidgetItem> getAll() {
        List<WidgetItem> items = new ArrayList<>();
        INode node = new Bag(ctx.client().getItemCache()).getNode(ITEM_STORAGE_ID);
        if (node == null || !(node instanceof IItemCache)) {
            return items;
        }
        IItemCache itemCache = (IItemCache) node;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.widgetId() >= itemCache.getItemIds().length ||
                    slot.widgetId() >= itemCache.getItemStackSizes().length) {
                continue;
            }
            WidgetItem slotItem = new WidgetItem(ctx, null,
                    itemCache.getItemIds()[slot.widgetId()],
                    itemCache.getItemStackSizes()[slot.widgetId()], null, slot.widgetId(), WidgetItem.Type.EQUIPMENT);
            items.add(slotItem);
        }
        return items;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getWidget() {
        return ctx.widgets.find(GROUP_ID, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(int... ids) {
        for (WidgetItem item : getAll()) {
            for (int id : ids) {
                if (item.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(String... names) {
        return find(names).exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return getAll().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFull() {
        return getCount() == EquipmentSlot.values().length;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquipmentQuery find() {
        return new EquipmentQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquipmentQuery find(int... ids) {
        return find().id(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquipmentQuery find(String... names) {
        return find().named(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquipmentQuery find(EquipmentSlot... slots) {
        return find().slot(slots);
    }

    /**
     * Represents a slot in the player's equipment interface
     *
     * @author tommo
     */
    public enum EquipmentSlot {
        /**
         * The head slot.
         */
        HEAD(0),
        /**
         * The cape slot.
         */
        CAPE(1),
        /**
         * The neck slot.
         */
        NECK(2),
        /**
         * The quiver slot.
         */
        QUIVER(13),
        /**
         * The weapon slot.
         */
        WEAPON(3),
        /**
         * The chest slot.
         */
        CHEST(4),
        /**
         * The shield slot.
         */
        SHIELD(5),
        /**
         * The legs slot.
         */
        LEGS(7),
        /**
         * The hands slot.
         */
        HANDS(9),
        /**
         * The feet slot.
         */
        FEET(10),
        /**
         * The ring slot.
         */
        RING(12);

        /**
         * The widget id for this slot.
         */
        private int widgetId;

        /**
         * Creates an equipment slot
         *
         * @param widgetId the widget id
         */
        EquipmentSlot(int widgetId) {
            this.widgetId = widgetId;
        }

        /**
         * Returns the child widget id in the equipment interface.
         *
         * @return The index in the equipment {@link WidgetGroup} of the {@link Widget} which visually represents this slot
         */
        public int widgetId() {
            return widgetId;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        public String toString() {
            char c = name().charAt(0);
            return c + name().toLowerCase().substring(1);
        }
    }
}
