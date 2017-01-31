package com.kit.game.engine;

public interface IBitBuffer extends IBuffer {

    int getBits(int offset);

    int getOffsetPos();

    void setOffsetPos(int offset);

}
