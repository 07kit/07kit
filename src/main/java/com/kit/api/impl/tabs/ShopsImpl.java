package com.kit.api.impl.tabs;


import com.kit.api.Shops;
import com.kit.api.MethodContext;
import com.kit.api.Shops;
import com.kit.api.collection.Filter;
import com.kit.api.collection.StatePredicate;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.api.MethodContext;
import com.kit.api.Shops;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : const_
 */
public class ShopsImpl implements Shops {

    private static final int WIDGET_GROUP = 300;
    private static final int ITEMS_PANE = 75;
    private static final int SHOP_CLOSE_ID = 92;
    private final MethodContext ctx;


    public ShopsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return ctx.widgets.find(WIDGET_GROUP, SHOP_CLOSE_ID) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WidgetItem> getAll() {
        Widget widget = getWidget();
        if (widget != null) {
            List<WidgetItem> items = new ArrayList<>(widget.getInventory().length);
            for (int i = 0; i < widget.getInventory().length; i++) {
                if (widget.getInventory()[i] - 1 > 0 && widget.getInventoryStackSizes()[i] > 0) {
                    int col = (i % 8);
                    int row = (i / 8);
                    int x = widget.getX() + (col * 47) + 22;
                    int y = widget.getY() + (row * 47) + 18;
                    Rectangle area = new Rectangle(x - (46 / 2), y - (36 / 2), 32, 32);
                    WidgetItem item = new WidgetItem(ctx, area, widget.getInventory()[i] - 1, widget.getInventoryStackSizes()[i],
                            widget, i, WidgetItem.Type.SHOP);
                    items.add(item);
                }
            }
            return items;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WidgetItem> getAll(Filter<WidgetItem> filter) {
        Widget widget = getWidget();
        if (widget != null) {
            List<WidgetItem> items = new ArrayList<>(widget.getInventory().length);
            for (int i = 0; i < widget.getInventory().length; i++) {
                if (widget.getInventory()[i] - 1 > 0 && widget.getInventoryStackSizes()[i] > 0) {
                    int col = (i % 8);
                    int row = (i / 8);
                    int x = widget.getX() + (col * 47) + 22;
                    int y = widget.getY() + (row * 47) + 18;
                    Rectangle area = new Rectangle(x - (46 / 2), y - (36 / 2), 32, 32);
                    WidgetItem item = new WidgetItem(ctx, area, widget.getInventory()[i] - 1, widget.getInventoryStackSizes()[i],
                            widget, i, WidgetItem.Type.SHOP);
                    if (filter.accept(item)) {
                        items.add(item);
                    }
                }
            }
            return items;
        }
        return null;
    }

    public Widget getWidget() {
        return ctx.widgets.find(WIDGET_GROUP, ITEMS_PANE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(int id) {
        if (!isOpen()) {
            return false;
        }
        for (WidgetItem item : getAll()) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetItem getItem(int id) {
        if (!isOpen()) {
            return null;
        }
        for (WidgetItem item : getAll()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetItem getItem(Filter<WidgetItem> filter) {
        if (!isOpen()) {
            return null;
        }
        for (WidgetItem item : getAll()) {
            if (filter.accept(item)) {
                return item;
            }
        }
        return null;
    }
}
