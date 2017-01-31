package com.kit.api.impl.scene;


import com.kit.api.MethodContext;
import com.kit.api.Projectiles;
import com.kit.api.wrappers.Deque;
import com.kit.api.wrappers.Projectile;
import com.kit.game.engine.renderable.IProjectile;
import com.kit.api.MethodContext;
import com.kit.api.Projectiles;
import com.kit.api.wrappers.Deque;
import com.kit.api.wrappers.Projectile;
import com.kit.game.engine.collection.IDeque;
import com.kit.game.engine.renderable.IProjectile;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Deque;
import com.kit.api.wrappers.Projectile;
import com.kit.game.engine.collection.IDeque;
import com.kit.game.engine.renderable.IProjectile;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author : const_
 */
public class ProjectilesImpl implements Projectiles {

    private final MethodContext ctx;


    public ProjectilesImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<Projectile> getAll() {
        List<Projectile> list = newArrayList();
        IDeque deque = ctx.client().getProjectiles();
        if (deque != null) {
            Deque _deque = new Deque(deque);
            while (_deque.hasNext()) {
                    list.add(new Projectile(ctx, (IProjectile) _deque.next()));
            }
        }
        return list;
    }
}
