package com.kit.api.collection.queries;

import com.kit.api.collection.StatePredicate;
import com.kit.api.collection.WaitingPolicy;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Widget;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.collection.StatePredicate;
import com.kit.api.collection.WaitingPolicy;
import com.kit.api.wrappers.Widget;

import java.awt.*;
import java.util.List;

import static com.google.common.collect.Maps.newHashMap;
import static com.kit.api.util.Utilities.sleepUntil;

/**
 * A widget finder implemented with a query-like pattern.
 *
 */
public class WidgetQuery extends Query<Widget> {
    private final MethodContext ctx;
    private Rectangle lastResult;

    public WidgetQuery(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Widget single() {
        List<Widget> widgets = asList();
        if (!widgets.isEmpty()) {
            Widget widget = widgets.get(0);
            lastResult = widget.getArea();
            return widget;
        }
        return null;
    }

    public Widget waitFor(WaitingPolicy policy, int timeOut) {
        if (lastResult != null) {
            switch (policy) {

                default:
                    break;
            }
        }
        Utilities.sleepUntil(new StatePredicate() {
            @Override
            public boolean apply() {
                return exists();
            }
        }, timeOut);
        return single();
    }

    @Override
    public List<Widget> asList() {
        return null;//return filterSet(orderSet(ctx.widgets));
    }
//
//    /**
//     * Selects widgets matching the specified ID
//     *
//     * @param group     group number
//     * @param component component number within group
//     * @return query
//     */
//    public WidgetQuery id(final int group, final int component) {
//        addCondition(new Filter<Widget>() {
//            @Override
//            public boolean accept(Widget acceptable) {
//                return acceptable.getGroup() == group &&
//                        acceptable.getId() == component;
//            }
//        });
//        return this;
//    }

    /**
     * Selects widgets matching the specified group ID
     *
     * @param group group number
     * @return query
     */
    public WidgetQuery group(final int group) {
        addCondition(new Filter<Widget>() {
            @Override
            public boolean accept(Widget acceptable) {
                return acceptable.getGroup() == group;
            }
        });
        return this;
    }

    /**
     * Selects widgets containing the specified text
     *
     * @param text text to look for
     * @return query
     */
    public WidgetQuery text(final String text) {
        addCondition(new Filter<Widget>() {
            @Override
            public boolean accept(Widget acceptable) {
                return acceptable.getText().toLowerCase().contains(text.toLowerCase());
            }
        });
        return this;
    }

    /**
     * Only selects widgets that are visible
     *
     * @return query
     */
    public WidgetQuery visible() {
        addCondition(new Filter<Widget>() {
            @Override
            public boolean accept(Widget acceptable) {
                return acceptable.isValid();
            }
        });
        return this;
    }

    public WidgetQuery storedAs(String name) {
        Query query = retrieveFromStorage(name);
        if (query instanceof WidgetQuery) {
            return (WidgetQuery) query;
        } else {
            throw new IllegalArgumentException("Query '" + name + "' is not of type " + getClass().getSimpleName());
        }
    }
}
