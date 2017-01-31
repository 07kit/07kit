package com.kit.game.engine;

public interface IBuffer {

	int getOffset();

	byte[] getPayload();

	void setOffset(int offset);

}
