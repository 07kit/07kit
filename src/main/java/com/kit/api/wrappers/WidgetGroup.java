package com.kit.api.wrappers;

import com.kit.api.MethodContext;
import com.kit.game.engine.media.IWidget;
import com.kit.api.MethodContext;
import com.kit.game.engine.media.IWidget;

import java.lang.ref.WeakReference;

/**
 * A wrapper for a collection of widgets also known as a widget group
 *
 */
public class WidgetGroup implements Wrapper<IWidget[]> {
    private final WeakReference<IWidget[]> wrapped;
    private final Widget[] widgets;
    private final int index;

    public WidgetGroup(MethodContext ctx, final IWidget[] widgets, int index) {
        this.index = index;
        if (widgets != null) {
            this.widgets = new Widget[widgets.length];

            for (int child = 0; child < widgets.length; child++) {
                IWidget widget = widgets[child];
                if (widget != null) {
                    this.widgets[child] = new Widget(ctx, widget, index, child);
                }
            }
        } else {
            this.widgets = new Widget[0];
        }
        this.wrapped = new WeakReference<>(widgets);
    }

    /**
     * Get the group ID
     *
     * @return group id
     */
    public int getGroup() {
        return index;
    }

    /**
     * Get all widgets in the group
     *
     * @return children
     */
    public Widget[] getAll() {
        return widgets;
    }

    /**
     * Checks if the group is valid (has children)
     *
     * @return true if valid
     */
    public boolean isValid() {
        return widgets.length > 0 && widgets[0] != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IWidget[] unwrap() {
        return wrapped.get();
    }

    @Override
    public String toString() {
        return "Group [id=" + index + ", children=" + widgets.length + "]";
    }

}
