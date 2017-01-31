package com.kit.api;

import com.kit.api.collection.queries.FriendQuery;
import com.kit.api.collection.queries.FriendQuery;
import com.kit.api.wrappers.Friend;

import java.util.List;

public interface Friends {

    List<Friend> getAll();

    FriendQuery find();
}
