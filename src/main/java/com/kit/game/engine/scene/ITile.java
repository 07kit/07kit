package com.kit.game.engine.scene;

import com.kit.game.engine.scene.tile.*;

public interface ITile {

    IBoundaryObject getBoundaryObject();

    IFloorObject getFloorObject();

    IGroundLayer getGroundLayer();

    IInteractableObject[] getInteractableObjects();

    IWallObject getWallObject();

}
