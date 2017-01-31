package com.kit.api.impl.tabs;


import com.kit.api.Inventory;
import com.kit.api.Inventory;
import com.kit.api.MethodContext;
import com.kit.api.collection.queries.InventoryQuery;
import com.kit.api.wrappers.Tab;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.Session;

import java.awt.Rectangle;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: Cov
 * Date: 23/09/13
 * Time: 20:49
 */
public class InventoryImpl implements Inventory {
    private final MethodContext ctx;
    private static final int GROUP_ID = 149;
    private static final int CHILD_ID = 0;


    public InventoryImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.util.List<WidgetItem> getAll() {
        java.util.List<WidgetItem> items = newArrayList();
        Widget inventory = getWidget();
        if (inventory != null) {
            int[] ids = inventory.getInventory();
            int[] stacks = inventory.getInventoryStackSizes();
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] > 0 && stacks[i] > 0) {
                    items.add(new WidgetItem(ctx, new Rectangle((inventory.getX() + ((i % 4) * 42)),
                            (inventory.getY() + ((i / 4) * 36)), 31, 31), ids[i] - 1, stacks[i],
                            inventory, i, WidgetItem.Type.INVENTORY));
                }
            }
        }
        return items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        int size = 0;
        for (int id : getWidget().getInventory()) {
            if (id > 0) {
                size++;
            }
        }
        return size;
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
    public int getCount(boolean stacks, int id) {
        int count = 0;
        if (contains(id)) {
            for (WidgetItem item : getAll()) {
                if (item.getId() == id) {
                    count += (stacks ? item.getStackSize() : 1);
                }
            }
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount(boolean stacks, String name) {
        int count = 0;
        for (WidgetItem item : find(name).asList()) {
            count += (stacks ? item.getStackSize() : 1);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFull() {
        return getCount() == 28;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getWidget() {
        return ctx.widgets.find(GROUP_ID, CHILD_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryQuery find() {
        return new InventoryQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryQuery find(int... ids) {
        return find().id(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryQuery find(String... names) {
        return find().named(names);
    }
}
