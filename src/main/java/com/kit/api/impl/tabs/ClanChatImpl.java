package com.kit.api.impl.tabs;

import com.kit.api.ClanChat;
import com.kit.api.MethodContext;
import com.kit.api.collection.queries.ClanChatMemberQuery;
import com.kit.api.wrappers.ClanChatMember;
import com.kit.game.engine.IClanChatMember;
import com.kit.api.ClanChat;
import com.kit.api.Friends;
import com.kit.api.MethodContext;
import com.kit.api.collection.queries.ClanChatMemberQuery;
import com.kit.api.collection.queries.FriendQuery;
import com.kit.api.wrappers.ClanChatMember;
import com.kit.api.wrappers.Friend;
import com.kit.game.engine.IClanChatMember;
import com.kit.game.engine.IFriend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClanChatImpl implements ClanChat {

    private final MethodContext ctx;

    public ClanChatImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<ClanChatMember> getAllMembers() {
        IClanChatMember[] members = ctx.client().getClanChatMembers();
        if (members == null || members.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.stream(members).map(m -> new ClanChatMember(m, ctx)).collect(Collectors.toList());
    }

    @Override
    public ClanChatMemberQuery find() {
        return new ClanChatMemberQuery(ctx);
    }
}
