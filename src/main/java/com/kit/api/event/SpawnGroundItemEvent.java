package com.kit.api.event;

import com.kit.api.wrappers.Loot;
import com.kit.core.Session;

import java.util.Arrays;
import java.util.List;

public class SpawnGroundItemEvent {

	private int x;
	private int y;
	private int plane;
	private int var1;
	private int var2;
	private List<Loot> loot;

	public SpawnGroundItemEvent(int x, int y) {
		this.x = Session.get().getClient().getBaseX() + x;
		this.y = Session.get().getClient().getBaseY() + y;
		this.plane = Session.get().getClient().getPlane();
		this.var1 = var1;
		this.var2 = var2;//some sort of id
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getPlane() {
		return plane;
	}

	public List<Loot> getLoot() {
		return loot;
	}

	public void setLoot(List<Loot> loot) {
		this.loot = loot;
	}

	@Override
	public String toString() {
		return "SpawnGroundItemEvent{" +
				"x=" + x +
				", y=" + y +
				", plane=" + plane +
				", var1=" + var1 +
				", var2=" + var2 +
				", loot=" + loot +
				'}';
	}
}
