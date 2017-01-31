package com.kit.api.wrappers;

import com.kit.api.MethodContext;
import com.kit.api.service.CacheService;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.api.MethodContext;
import com.kit.api.service.CacheService;
import com.kit.api.util.NpcCompositesUtil;
import com.kit.core.Session;
import com.kit.game.engine.renderable.entity.INpc;
import org.apache.http.client.fluent.Request;
import com.kit.api.MethodContext;
import com.kit.game.engine.renderable.entity.INpc;

/**
 * A wrapper for Npcs within the RuneScape client.
 *
 */
public class Npc extends Entity implements Wrapper<INpc> {

    private NpcComposite composite;

    public Npc(MethodContext ctx, INpc wrapped) {
        super(ctx, wrapped);
    }

    /**
     * Get the ID of this Npc.
     *
     * @return npc id
     */
    public int getId() {
        if (getComposite() != null) {
            return getComposite().getId();
        }
        return unwrap().getComposite().getId();
    }

    /**
     * Get the name of this Npc.
     *
     * @return name
     */
    @Override
    public String getName() {
        return unwrap().getComposite() != null ? unwrap().getComposite().getName() : "null";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public INpc unwrap() {
        return (INpc) wrapped.get();
    }

    /**
     * Gets the npc composite for this items id
     */
    public NpcComposite getComposite() {
        if (composite != null) {
            return composite;
        }
        if (unwrap() != null && unwrap().getComposite() != null) {
            NpcComposite composite = CacheService.get().getNpcComposite(unwrap().getComposite().getId());
            if (composite != null) {
                NpcComposite child = CacheService.get().getChildNpcComposite(composite);
                if (child != null) {
                    return (this.composite = child);
                }
            }
            return (this.composite = composite);
        }
        return null;
    }

    public NpcInfo getNpcInfo() {
        return context.npcs.getInfo(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Npc) {
            return ((Npc) obj).wrapped.equals(wrapped);
        }
        return super.equals(obj);
    }
}
