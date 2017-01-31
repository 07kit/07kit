package com.kit.api.wrappers;

import com.google.gson.annotations.SerializedName;

public class PriceInfo {

	@SerializedName("buyAverage")
	private final int buyAverage;
	@SerializedName("members")
	private final boolean members;
	@SerializedName("itemId")
	private final int itemId;
	@SerializedName("sellAverage")
	private final int sellAverage;
	@SerializedName("average")
	private final int average;
	@SerializedName("name")
	private final String name;

	public PriceInfo(int buyAverage, boolean members, int itemId, int sellAverage, int average, String name) {
		this.buyAverage = buyAverage;
		this.members = members;
		this.itemId = itemId;
		this.sellAverage = sellAverage;
		this.average = average;
		this.name = name;
	}

	public int getBuyAverage() {
		return buyAverage;
	}

	public boolean isMembers() {
		return members;
	}

	public int getItemId() {
		return itemId;
	}

	public int getSellAverage() {
		return sellAverage;
	}

	public int getAverage() {
		return average;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "PriceInfo{" +
				"buyAverage=" + buyAverage +
				", members=" + members +
				", itemId=" + itemId +
				", sellAverage=" + sellAverage +
				", average=" + average +
				", name='" + name + '\'' +
				'}';
	}
}
