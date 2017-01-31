package com.kit.api.wrappers;

import com.kit.api.util.Utilities;
import com.kit.game.engine.media.IWidgetNode;
import com.kit.api.MethodContext;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.structure.Bag;
import com.kit.game.engine.media.IWidget;
import com.kit.game.engine.media.IWidgetNode;
import com.kit.api.MethodContext;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.interaction.Interactable;
import com.kit.api.wrappers.structure.Bag;
import com.kit.game.engine.media.IWidget;

import java.awt.Point;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;


/**
 * A wrapper for RuneScape screen widgets
 *
 */
public class Widget extends Interactable implements Wrapper<IWidget> {

    private final WeakReference<IWidget> wrapped;
    private int group;
    private int index;

    public Widget(MethodContext ctx, IWidget wrapped, int group, int index) {
        super(ctx);
        this.wrapped = new WeakReference<>(wrapped);
        this.group = group;
        this.index = index;
    }

    /**
     * Gets the interface ID.
     *
     * @return id
     */
    public int getId() {
        return index;
    }

    /**
     * Sets the interface ID
     *
     * @param id id
     */
    public void setId(int id) {
        this.index = id;
    }

    /**
     * Gets the interface group
     *
     * @return group
     */
    public int getGroup() {
        return group;
    }

    /**
     * Sets the interface group
     *
     * @param group group
     */
    public void setGroup(int group) {
        this.group = group;
    }

    /**
     * Gets the UID for this widget
     *
     * @return uid
     */
    public int getUid() {
        return unwrap().getUid();
    }

    /**
     * Gets the contained item ID for this widget
     *
     * @return item id
     */
    public int getItemId() {
        return unwrap().getItemId();
    }

    /**
     * Gets the stack size of the contained item
     *
     * @return stack size
     */
    public int getItemStackSize() {
        return unwrap().getItemStackSize();
    }

    /**
     * Gets the index for this widget in the boundsX/boundsY arrays
     *
     * @return bounds index
     */
    public int getBoundsIndex() {
        return unwrap().getBoundsIndex();
    }

    /**
     * Gets the widget type identifier
     *
     * @return type identifier
     */
    public int getType() {
        return unwrap().getType();
    }

    /**
     * Gets the absolute X of this widget on the screen.
     *
     * @return absolute x
     */
    public int getX() {
        if (wrapped.get() == null) {
            return -1;
        }
        Widget parent = getParent();

        int x = 0;
        if (parent != null) {
            x = parent.getX();
        } else {
            int[] posX = context.client().getWidgetBoundsX();
            if (getBoundsIndex() != -1) {
                return (posX[getBoundsIndex()] + (getType() > 0 ? unwrap().getX() : 0));
            }
        }
        return (unwrap().getX() + x);
    }

    /**
     * Gets the absolute Y of this widget on the screen.
     *
     * @return absolute y.
     */
    public int getY() {
        if (wrapped.get() == null) {
            return -1;
        }
        Widget parent = getParent();
        int y = 0;
        if (parent != null) {
            y = parent.getY() - parent.unwrap().getScrollY();
        } else {
            int[] posY = context.client().getWidgetBoundsY();
            if (getBoundsIndex() != -1) {
                return (posY[getBoundsIndex()] + (getType() > 0 ? unwrap().getY() : 0));
            }
        }
        return (unwrap().getY() + y);
    }

    /**
     * Gets the width of this widget
     *
     * @return width
     */
    public int getWidth() {
        if (wrapped.get() == null) {
            return -1;
        }
        Widget parent = getParent();
        int width = 0;
        if (parent != null) {
            width = parent.getWidth();
        } else {
            int[] widths = context.client().getWidgetBoundsWidth();
            if (getBoundsIndex() != -1) {
                return (widths[getBoundsIndex()] + (getType() > 0 ? unwrap().getWidth() : 0));
            }
        }
        return (width + (getType() > 0 ? unwrap().getHeight() : 0));
    }

    /**
     * Gets the height of this widget
     *
     * @return height
     */
    public int getHeight() {
        if (wrapped.get() == null) {
            return -1;
        }
        Widget parent = getParent();
        int height = 0;
        if (parent != null) {
            height = parent.getHeight();
        } else {
            int[] heights = context.client().getWidgetBoundsHeight();
            if (getBoundsIndex() != -1) {
                return (heights[getBoundsIndex()] + (getType() > 0 ? unwrap().getHeight() : 0));
            }
        }
        return (height + (getType() > 0 ? unwrap().getHeight() : 0));
    }

    /**
     * Gets the text set in this widget
     *
     * @return text
     */
    public String getText() {
        return unwrap().getText();
    }

    /**
     * Gets an array with all the child widgets of this widget
     *
     * @return child widgets
     */
    public Widget[] getChildren() {
        IWidget[] children = unwrap().getChildren();
        if (children != null) {
            Widget[] wrappedChildren = new Widget[children.length];
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    Widget widget = new Widget(context, children[i], index, i);
                    wrappedChildren[i] = widget;
                } else {
                    wrappedChildren[i] = null;
                }
            }
            return wrappedChildren;
        }
        return new Widget[0];
    }

    /**
     * Gets a child by their array index
     *
     * @param id array index
     * @return child
     */
    public Widget getChild(int id) {
        return getChildren()[id];
    }

    /**
     * Checks and obtains a valid parent for this widget
     *
     * @return parent or null if there is none.
     */
    public Widget getParent() {
        int parent = getParentId();
        if (parent != -1) {
            return context.widgets.find(parent >> 16, parent & 0xFFFF);
        }
        return null;
    }

    /**
     * Gets the parent ID for this widget
     *
     * @return parent id or -1 if there is none.
     */
    public int getParentId() {
        if (unwrap().getParentId() != -1) {
            return unwrap().getParentId();
        }
        int i = getUid() >>> 16;
        Bag<IWidgetNode> cache = new Bag<>(context.client().getWidgetNodeBag());
        for (IWidgetNode node = cache.getHead(); node != null; node = cache.getNext()) {
            if (i == node.getId()) {
                return (int) node.getUID(); // ayy?
            }
        }
        return -1;
    }

    /**
     * Get an array containing inventory item ids for each
     * inventory slot. (0-27)
     *
     * @return item ids
     */
    public int[] getInventory() {
        return unwrap().getContentIds();
    }

    /**
     * Get an array containing inventory stack sizes for each
     * inventory slot. (0-27)
     *
     * @return stack sizes
     */
    public int[] getInventoryStackSizes() {
        return unwrap().getContentStackSizes();
    }

    /**
     * Gets the ID of the texture used for this widget.
     *
     * @return texture id
     */
    public int getSpriteId() {
        return unwrap().getSpriteId();
    }

    /**
     * Gets an array of all possible actions that can be performed
     * using this widget.
     *
     * @return actions
     */
    public String[] getActions() {
        return unwrap().getActions();
    }

    /**
     * Gets the ID of the model currently displayed in the widget.
     *
     * @return model id
     */
    public int getModelId() {
        return unwrap().getModelId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBasePoint() {
        return new Point(getX(), getY());
    }

    /**
     * Get a rectangle of the area that this widget is set in.
     *
     * @return a rectangle representation of the widget area
     */
    public Rectangle getArea() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getClickPoint() {
        Rectangle area = getArea();
        int x = (int) (area.getX() + Utilities.random(0, area.getWidth()));
        int y = (int) (area.getY() + Utilities.random(0, area.getHeight()));
        return new Point(x, y);
    }

    @Override
    public boolean isValid() {
        return unwrap() != null && context.client().getLoopCycle() < (unwrap().getLoopCycleStatus() + 20);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IWidget unwrap() {
        return wrapped.get();
    }

    @Override
    public String toString() {
        return "Widget[parent=" + getParentId() + ", id=" + getId() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Widget) {
            return ((Widget) obj).unwrap().equals(wrapped);
        }
        return super.equals(obj);
    }

}
