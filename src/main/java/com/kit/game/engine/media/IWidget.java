package com.kit.game.engine.media;

public interface IWidget {

	String[] getActions();

	int getBoundsIndex();

	IWidget[] getChildren();

	int[] getContentIds();

	int[] getContentStackSizes();

	int getHeight();

	int getItemId();

	int getItemStackSize();

	int getLoopCycleStatus();

	int getModelId();

	int getParentId();

	String getText();

	int getSpriteId();

	int getUid();

	int getWidth();

	int getX();

	int getY();

	int getType();

	void setText(String text);

	void setActions(String[] actions);

}
