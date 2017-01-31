package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.WidgetItem;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.WidgetItem;

import java.util.List;

/**
 * @author const_
 */
public class BankQuery extends WidgetItemQuery<WidgetItem, BankQuery> {

    public BankQuery(MethodContext context) {
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
        return filterSet(orderSet(ctx.bank.getAll()));
    }

    /**
     * Adds a filter so that only widget items with the names matching the
     * specified ones can appear in the result set.
     *
     * @param names A varargs array of names that are accepted.
     * @return query
     */
    public BankQuery named(final String... names) {
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

}
