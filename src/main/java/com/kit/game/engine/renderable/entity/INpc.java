package com.kit.game.engine.renderable.entity;

import com.kit.api.wrappers.Npc;
import com.kit.game.engine.cache.composite.INpcComposite;

public interface INpc extends IEntity {

    INpcComposite getComposite();

    Npc getWrapper();

}
