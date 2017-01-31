package com.kit.api.event;

import com.kit.game.engine.IGrandExchangeOffer;

public class GrandExchangeOfferUpdatedEvent {

	private IGrandExchangeOffer offer;

	public GrandExchangeOfferUpdatedEvent(IGrandExchangeOffer offer) {
		this.offer = offer;
	}

	public IGrandExchangeOffer getOffer() {
		return offer;
	}

	@Override
	public String toString() {
		return "GrandExchangeOfferUpdatedEvent{" +
				"offer={" +
				"id=" + offer.getItemId() + "," +
				"price=" + offer.getItemPrice() + "," +
				"quantity=" + offer.getQuantity() + "," +
				"state=" + offer.getState() + "," +
				"spent=" + offer.getTotalSpent() + "," +
				"transferred=" + offer.getTransferred() + "," +
				"}}";
	}
}
