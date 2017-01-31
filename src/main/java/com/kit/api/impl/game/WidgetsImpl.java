package com.kit.api.impl.game;


import com.kit.api.Widgets;
import com.kit.api.MethodContext;
import com.kit.api.Widgets;
import com.kit.api.collection.queries.WidgetQuery;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetGroup;
import com.kit.game.engine.media.IWidget;
import com.kit.api.MethodContext;
import com.kit.api.collection.queries.WidgetQuery;
import com.kit.game.engine.media.IWidget;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * An API implementation for obtaining and interacting with widgets.
 *
 */
public class WidgetsImpl implements Widgets {
    private final MethodContext ctx;


    public WidgetsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetGroup getGroup(int parent) {
        if (ctx.client().getWidgets()[parent] != null) {
            return new WidgetGroup(ctx, ctx.client().getWidgets()[parent], parent);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WidgetGroup> getGroups() {
        IWidget[][] cache = ctx.client().getWidgets();
        List<WidgetGroup> groups = newArrayList();
        if (cache == null) {
            return groups;
        }
        for (int i = 0; i < cache.length; i++) {
            WidgetGroup group = getGroup(i);
            if (group != null) {
                groups.add(group);
            }
        }
        return groups;
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public List<Widget> getAll() {
//        List<Widget> widgets = newArrayList();
//        List<WidgetGroup> groups = getGroups();
//        for (WidgetGroup group : groups) {
//            for (Widget widget : group.getAll()) {
//                if (widget != null) {
//                    widgets.add(widget);
//                    if (widget.getChildren() != null &&
//                            widget.getChildren().length > 0) {
//                        for (Widget child : widget.getChildren()) {
//                            if (child != null) {
//                                widgets.add(child);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return widgets;
//    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public WidgetQuery find() {
//        return new WidgetQuery(ctx);
//    }

    @Override
    public Widget find(int parent, int child) {
        IWidget[][] all = ctx.client().getWidgets();
        if (all == null) {
            return null;
        }
        IWidget[] tree = all[parent];
        if (tree == null) {
            return null;
        }

        IWidget widget = tree[child];
        if (widget != null) {
            return new Widget(ctx, widget, parent, child);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canContinue() {
        IWidget[][] all = ctx.client().getWidgets();
        if (all == null) {
            return false;
        }
        for (IWidget[] group : all) {
            if (group == null) {
                return false;
            }
            for (IWidget widget : group) {
                if (widget == null) {
                    continue;
                }
                if (widget.getText() != null && widget.getText().contains("Click here to continue")) {
                    return true;
                }
            }
        }
        return false;
    }
}
