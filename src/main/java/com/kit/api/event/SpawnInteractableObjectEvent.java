package com.kit.api.event;

import com.kit.api.wrappers.Loot;
import com.kit.core.Session;

import java.util.Arrays;
import java.util.List;

public class SpawnInteractableObjectEvent {

	private int x;
	private int y;
	private int plane;
	private int hash;
	int var1;
	int var2;
	int var3;
	int var4;
	int var5;
	int var6;
	boolean var7;

	public SpawnInteractableObjectEvent(int x, int y, int plane, int hash, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
		this.x = x;
		this.y = y;
		this.plane = plane;
		this.hash = hash;
		this.var1 = var1;
		this.var2 = var2;
		this.var3 = var3;
		this.var4 = var4;
		this.var5 = var5;
		this.var6 = var6;
		this.var7 = var7;
	}

	public int getHash() {
		return hash;
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

	@Override
	public String toString() {
		return "SpawnInteractableObjectEvent{" +
				"x=" + x +
				", y=" + y +
				", plane=" + plane +
				", hash=" + hash +
				", var1=" + var1 +
				", var2=" + var2 +
				", var3=" + var3 +
				", var4=" + var4 +
				", var5=" + var5 +
				", var6=" + var6 +
				", var7=" + var7 +
				'}';
	}
}
