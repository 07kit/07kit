package com.kit.game.engine;

public interface IGrandExchangeOffer {

	int getItemId();

	int getItemPrice();

	int getQuantity();

	byte getState();

	int getTotalSpent();

	int getTransferred();

}
