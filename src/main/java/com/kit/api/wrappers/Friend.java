package com.kit.api.wrappers;

import com.kit.api.MethodContext;
import com.kit.game.engine.IFriend;
import com.kit.api.MethodContext;
import com.kit.game.engine.IFriend;

import java.lang.ref.WeakReference;

public class Friend implements Wrapper<IFriend> {

    protected final WeakReference<IFriend> wrapped;
    private final MethodContext ctx;

    public Friend(IFriend friend, MethodContext ctx) {
        this.wrapped = new WeakReference<>(friend);
        this.ctx = ctx;
    }

    public String getName() {
        return unwrap() != null ? unwrap().getName() : null;
    }

    @Override
    public IFriend unwrap() {
        return wrapped.get();
    }
}
