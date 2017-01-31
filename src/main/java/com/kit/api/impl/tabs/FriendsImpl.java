package com.kit.api.impl.tabs;

import com.kit.api.Friends;
import com.kit.api.MethodContext;
import com.kit.api.collection.queries.FriendQuery;
import com.kit.api.wrappers.Friend;
import com.kit.game.engine.IFriend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsImpl implements Friends {

    private final MethodContext ctx;

    public FriendsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<Friend> getAll() {
        IFriend[] friends = ctx.client().getFriends();
        if (friends == null || friends.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.stream(friends).map(f -> new Friend(f, ctx)).collect(Collectors.toList());
    }

    @Override
    public FriendQuery find() {
        return new FriendQuery(ctx);
    }
}
