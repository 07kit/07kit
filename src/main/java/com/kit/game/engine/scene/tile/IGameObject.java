package com.kit.game.engine.scene.tile;

import com.kit.api.wrappers.GameObject;
import com.kit.api.wrappers.GameObjectType;
import com.kit.game.engine.renderable.IRenderable;
import com.kit.game.engine.renderable.IRenderable;

/**
 */
public interface IGameObject {

    int getHash();

    int getX();

    int getY();

    int getOrientation();

    IRenderable getRenderable();

    GameObject getWrapper();

    GameObjectType getType();

}
