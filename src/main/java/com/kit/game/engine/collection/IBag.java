package com.kit.game.engine.collection;

public interface IBag {

    INode[] getCache();

    INode getCurrent();

    int getCurrentIndex();

    INode getHead();

    int getSize();

}
