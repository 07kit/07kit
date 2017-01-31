package com.kit.game.engine.renderable.entity;

import com.kit.game.engine.combat.ICombatInfoNode;
import com.kit.game.engine.renderable.IRenderable;
import com.kit.game.engine.combat.ICombatInfoNode;
import com.kit.game.engine.renderable.IRenderable;

public interface IEntity extends IRenderable {

    int getAnimationId();

    ICombatInfoNode getCombatInfoNode();

    int getInteractingIndex();

    int getLocalX();

    int getLocalY();

    String getMessage();

    int getOrientation();

    int getQueueLength();

    int getQueueX();

    int getQueueY();

}
