package com.kit.game.engine.renderable.entity;

import com.kit.api.wrappers.Model;
import com.kit.api.wrappers.Player;
import com.kit.game.engine.cache.composite.IPlayerComposite;
import com.kit.api.wrappers.Model;
import com.kit.game.engine.cache.composite.IPlayerComposite;
import com.kit.api.wrappers.Model;
import com.kit.game.engine.cache.composite.IPlayerComposite;

public interface IPlayer extends IEntity {

    IPlayerComposite getComposite();

    Model getModel();

    String getName();

    int getCombatLevel();

    Player getWrapper();

}
