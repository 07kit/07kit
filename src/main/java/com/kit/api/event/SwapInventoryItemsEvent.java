package com.kit.api.event;

public class SwapInventoryItemsEvent {

	private int slotTarget;
	private int slotSource;

	public SwapInventoryItemsEvent(int slotTarget, int slotSource) {
		this.slotTarget = slotTarget;
		this.slotSource = slotSource;
	}


}
