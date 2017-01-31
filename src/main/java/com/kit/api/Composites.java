package com.kit.api;

import com.kit.api.wrappers.ItemComposite;
import com.kit.api.wrappers.NpcComposite;
import com.kit.api.wrappers.ObjectComposite;
import com.kit.api.wrappers.ItemComposite;
import com.kit.api.wrappers.NpcComposite;
import com.kit.api.wrappers.ObjectComposite;
import com.kit.game.engine.cache.composite.INpcComposite;

/**
 */
public interface Composites {

    ObjectComposite getObjectComposite(int id);

    ItemComposite getItemComposite(int id);

    NpcComposite getNpcComposite(int id);

}
