package com.kit.api.impl.game;


import com.kit.api.Composites;
import com.kit.api.MethodContext;
import com.kit.api.service.CacheService;
import com.kit.api.util.ItemCompositesUtil;
import com.kit.api.util.NpcCompositesUtil;
import com.kit.api.util.ObjectCompositesUtil;
import com.kit.api.wrappers.ItemComposite;
import com.kit.api.wrappers.NpcComposite;
import com.kit.api.wrappers.ObjectComposite;
import com.kit.game.cache.io.CacheLoader;
import com.kit.game.cache.io.InputStream;
import com.kit.game.cache.io.Stream;
import com.kit.game.engine.cache.composite.IItemComposite;
import com.kit.game.engine.cache.composite.INpcComposite;
import com.kit.game.engine.cache.composite.IObjectComposite;
import com.kit.game.engine.collection.ICache;
import com.kit.game.engine.collection.INode;
import com.kit.api.Composites;
import com.kit.api.MethodContext;
import com.kit.api.util.ItemCompositesUtil;
import com.kit.api.util.ObjectCompositesUtil;
import com.kit.api.wrappers.NpcComposite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class CompositesImpl implements Composites {
    private final MethodContext context;


    public CompositesImpl(MethodContext context) {
        this.context = context;
    }

    @Override
    public ObjectComposite getObjectComposite(int id) {
        return CacheService.get().getObjectComposite(id);
    }

    @Override
    public ItemComposite getItemComposite(int id) {
        return CacheService.get().getItemComposite(id);
    }

    @Override
    public NpcComposite getNpcComposite(int id) {
        return CacheService.get().getNpcComposite(id);
    }


}
