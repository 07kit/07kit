package com.kit.api.impl.scene;


import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Deque;
import com.kit.api.wrappers.GroundLayer;
import com.kit.api.wrappers.Loot;
import com.kit.game.engine.scene.tile.IGroundLayer;
import com.kit.api.Loots;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.LootQuery;
import com.kit.api.wrappers.Deque;
import com.kit.api.wrappers.GroundLayer;
import com.kit.api.wrappers.Loot;
import com.kit.game.engine.collection.IDeque;
import com.kit.game.engine.renderable.ILoot;
import com.kit.game.engine.scene.tile.IGroundLayer;
import com.kit.api.MethodContext;
import com.kit.api.collection.queries.LootQuery;
import com.kit.api.wrappers.Deque;
import com.kit.api.wrappers.GroundLayer;
import com.kit.api.wrappers.Loot;
import com.kit.game.engine.collection.IDeque;
import com.kit.game.engine.scene.tile.IGroundLayer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Author: const_
 */
public class LootsImpl implements Loots {

    private final MethodContext ctx;


    public LootsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loot> getAll() {
        List<Loot> items = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                items.addAll(getLootAt(x, y, loot -> true));
            }
        }
        return items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loot> getAll(Filter<Loot> predicate) {
        List<Loot> items = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                items.addAll(getLootAt(x, y, predicate));
            }
        }
        return items;
    }

    private List<Loot> getLootAt(int x, int y, Filter<Loot> predicate) {
        List<Loot> items = newArrayList();
        if (ctx.client().getScene() != null && ctx.client().getScene().getTiles()[ctx.client().getPlane()][x][y] != null) {
            int plane = ctx.client().getPlane();
            IDeque itemsDeque = ctx.client().getLoot()[plane][x][y];
            IGroundLayer layer = ctx.client().getScene().getTiles()[plane][x][y].getGroundLayer();
            if (itemsDeque != null) {
                Deque deque = new Deque(itemsDeque);
                while (deque.hasNext()) {
                    ILoot item = (ILoot) deque.next();
                    GroundLayer gLayer = new GroundLayer(layer);
                    if (item != null) {
                        Loot loot = new Loot(ctx, item, gLayer, x, y, plane);
                        if (predicate.accept(loot)) {
                            items.add(loot);
                        }
                    }
                }
            }
        }
        return items;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LootQuery find() {
        return new LootQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LootQuery find(String... names) {
        return new LootQuery(ctx).named(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LootQuery find(int... ids) {
        return new LootQuery(ctx).id(ids);
    }
}
