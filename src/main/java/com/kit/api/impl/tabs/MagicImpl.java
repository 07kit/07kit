package com.kit.api.impl.tabs;


import com.kit.api.Magic;
import com.kit.api.Magic;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Spell;
import com.kit.api.wrappers.Tab;
import com.kit.api.wrappers.Widget;
import com.kit.api.MethodContext;

/**
 * @author const_
 */
public class MagicImpl implements Magic {

    private static final int GROUP = 192;
    private final MethodContext ctx;


    public MagicImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    private Widget getButton(int id) {
        return ctx.widgets.find(GROUP, id);
    }

}