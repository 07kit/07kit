package com.kit.api.wrappers;

import com.kit.api.MethodContext;
import com.kit.game.engine.IClanChatMember;

import java.lang.ref.WeakReference;

public class ClanChatMember implements Wrapper<IClanChatMember> {

    protected final WeakReference<IClanChatMember> wrapped;
    private final MethodContext ctx;

    public ClanChatMember(IClanChatMember member, MethodContext ctx) {
        this.wrapped = new WeakReference<>(member);
        this.ctx = ctx;
    }

    public String getName() {
        return unwrap() != null ? unwrap().getName() : null;
    }

    @Override
    public IClanChatMember unwrap() {
        return wrapped.get();
    }
}
