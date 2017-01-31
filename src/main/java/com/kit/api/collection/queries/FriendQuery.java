package com.kit.api.collection.queries;

import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.wrappers.Friend;
import com.kit.api.wrappers.WidgetItem;

import java.util.List;

public class FriendQuery extends Query<Friend> {
    private final MethodContext ctx;

    public FriendQuery(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Friend single() {
        List<Friend> friends = asList();
        return !friends.isEmpty() ? friends.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Friend> asList() {
        return filterSet(orderSet(ctx.friends.getAll()));
    }

    public FriendQuery named(final String... names) {
        addCondition(acceptable -> {
            for (String name : names) {
                if (acceptable.getName() != null &&
                        acceptable.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        });
        return this;
    }

}
