package com.kit.game.engine.renderable;

public interface IProjectile extends IRenderable {

    int getHeight();

    int getLoopCycle();

    boolean getMoving();

    int getTargetID();

    int getX();

    int getY();

}
