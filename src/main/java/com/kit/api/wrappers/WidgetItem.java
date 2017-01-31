package com.kit.api.wrappers;

import com.kit.api.MethodContext;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.interaction.Interactable;
import com.kit.api.MethodContext;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.interaction.Interactable;
import com.kit.api.MethodContext;
import com.kit.api.util.Utilities;

import java.awt.*;

/**
 * Base class for a widget representing an item
 *
 * @author const_
 * @author tommo
 */
public class WidgetItem extends Interactable {

    private Rectangle area;
    private int id, stackSize, index;
    private Widget widget;
    private ItemComposite composite;
    private Type type;

    /**
     * Creates a widget item with no direct correlation to a widget, just an area
     *
     * @param ctx       The method context
     * @param area      The widget area
     * @param id        The item id
     * @param stackSize The item stack size
     */
    public WidgetItem(MethodContext ctx, Rectangle area, int id, int stackSize) {
        this(ctx, area, id, stackSize, null, -1, Type.NULL);
    }

    /**
     * Creates a widget item with both direct correlation to a widget AND an area
     * <p>
     * Note that in the event of interacting with this widget, the specified AREA will be used,
     * and NOT the associated widget. The rationale behind this is, for example for equipment, there is
     * a single widget containing all of the equipment slot contents (like the inventory), however there is also a visual widget for each
     * of the slots, and therefore through this constructor we link the original contents array but also the widget area
     * for the specified slot so we can identify whether or not a {@link WidgetItem} is associated with an equipment slot.
     *
     * @param ctx       The method context
     * @param area      The widget area
     * @param id        The item id
     * @param stackSize The item stack size
     * @param widget    The widget to associate this {@link WidgetItem} with
     * @param index     The index in the widget
     * @param type      The item type {@link WidgetItem.Type}
     */
    public WidgetItem(MethodContext ctx, Rectangle area, int id, int stackSize, Widget widget, int index, Type type) {
        super(ctx);
        this.area = area;
        this.id = id;
        this.stackSize = stackSize;
        this.widget = widget;
        this.index = index;
        this.type = type;
    }

    /**
     * Creates a widget item with both direct correlation to a widget AND an area
     * <p>
     * Note that in the event of interacting with this widget, the specified AREA will be used,
     * and NOT the associated widget. The rationale behind this is, for example for equipment, there is
     * a single widget containing all of the equipment slot contents (like the inventory), however there is also a visual widget for each
     * of the slots, and therefore through this constructor we link the original contents array but also the widget area
     * for the specified slot so we can identify whether or not a {@link WidgetItem} is associated with an equipment slot.
     *
     * @param ctx       The method context
     * @param id        The item id
     * @param stackSize The item stack size
     * @param widget    The widget to associate this {@link WidgetItem} with
     * @param index     The index in the widget
     * @param type      The item type {@link WidgetItem.Type}
     */
    public WidgetItem(MethodContext ctx, int id, int stackSize, Widget widget, int index, Type type) {
        super(ctx);
        this.id = id;
        this.stackSize = stackSize;
        this.widget = widget;
        this.index = index;
        this.type = type;
    }

    /**
     * Creates a {@link WidgetItem} which directly correlates to a {@link Widget}
     *
     * @param ctx    The method context
     * @param widget The widget to associate this {@link WidgetItem} with
     */
    public WidgetItem(MethodContext ctx, Widget widget) {
        super(ctx);
        this.area = widget.getArea();
        this.id = widget.getItemId();
        this.stackSize = widget.getItemStackSize();
        this.widget = widget;

    }

    public Rectangle getArea() {
        if (area == null) {
            if (widget != null) {
                Rectangle area = widget.getArea();
                if (area != null) {
                    return (this.area = area);
                }
            }
            return null;
        }
        return area;
    }

    /**
     * Returns the widget associated with this item
     *
     * @return The associated {@link Widget}, or NULL if non-existent
     */
    public Widget getWidget() {
        return widget;
    }

    public int getId() {
        return id;
    }

    public int getStackSize() {
        return stackSize;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBasePoint() {
        return new Point((int) area.getX(), (int) area.getY());
    }

    /**
     * Gets the item composite for this items id
     */
    public ItemComposite getComposite() {
        if (composite != null) {
            return composite;
        }
        ItemComposite composite = context.composites.getItemComposite(getId());
        if (composite == null) {
            return null;
        }
        return (this.composite = composite);
    }

    /**
     * Gets the items name
     *
     * @return the items name
     */
    public String getName() {
        return getComposite() != null ? getComposite().getName() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getClickPoint() {
        int x = (int) (getArea().getCenterX() + Utilities.random(-(getArea().width / 4), getArea().width / 4));
        int y = (int) (getArea().getCenterY() + Utilities.random(-(getArea().height / 4), getArea().height / 4));
        return new Point(x, y);
    }
    public void setArea(Rectangle area) {
        this.area = area;
    }

    @Override
    public boolean isValid() {
        Widget widget = null;
        switch (type) {
            case SHOP:
                if ((widget = context.shops.getWidget()) != null &&
                        widget.getInventory() != null && widget.getInventory().length > index) {
                    return context.shops.getWidget().getInventory()[index] - 1 == id;
                }
                break;
            case BANK:
                if ((widget = context.bank.getWidget()) != null &&
                        widget.getChildren().length > index && widget.getChild(index) != null) {
                    return context.bank.getWidget().getChild(index).getItemId() == id;
                }
                break;
            case INVENTORY:

                if ((widget = context.inventory.getWidget()) != null &&
                        widget.getInventory() != null && widget.getInventory().length > index) {
                    return context.inventory.getWidget().getInventory()[index] - 1 == id;
                }
                break;
            case EQUIPMENT:
                return context.equipment.contains(id);
        }
        return false;
    }

    public enum Type {
        SHOP, INVENTORY, BANK, EQUIPMENT, NULL
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WidgetItem item = (WidgetItem) o;

        if (id != item.id) return false;
        if (stackSize != item.stackSize) return false;
        if (index != item.index) return false;
        if (area != null ? !area.equals(item.area) : item.area != null) return false;
        return type == item.type;

    }

    @Override
    public int hashCode() {
        int result = area != null ? area.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + stackSize;
        result = 31 * result + index;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WidgetItem{" +
                "area=" + area +
                ", id=" + id +
                ", stackSize=" + stackSize +
                ", index=" + index +
                ", widget=" + widget +
                ", composite=" + composite +
                ", type=" + type +
                '}';
    }
}
