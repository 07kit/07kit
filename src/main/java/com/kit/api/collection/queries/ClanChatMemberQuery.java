package com.kit.api.collection.queries;

import com.kit.api.wrappers.ClanChatMember;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.ClanChatMember;
import com.kit.api.wrappers.Friend;

import java.util.List;

public class ClanChatMemberQuery extends Query<ClanChatMember> {
    private final MethodContext ctx;

    public ClanChatMemberQuery(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClanChatMember single() {
        List<ClanChatMember> friends = asList();
        return !friends.isEmpty() ? friends.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClanChatMember> asList() {
        return filterSet(orderSet(ctx.clanChat.getAllMembers()));
    }

    public ClanChatMemberQuery named(final String... names) {
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
