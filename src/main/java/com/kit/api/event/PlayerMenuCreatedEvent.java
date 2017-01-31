package com.kit.api.event;

import com.kit.game.engine.renderable.entity.IPlayer;

public class PlayerMenuCreatedEvent {

	private IPlayer player;

	private int var0;
	private int var1;
	private int var2;

	public PlayerMenuCreatedEvent(IPlayer player, int var0, int var1, int var2) {
		this.player = player;
		this.var0 = var0;
		this.var1 = var1;
		this.var2 = var2;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public int getVar0() {
		return var0;
	}

	public int getVar1() {
		return var1;
	}

	public int getVar2() {
		return var2;
	}

	@Override
	public String toString() {
		return "PlayerMenuCreatedEvent{" +
				"player=" + player +
				", var0=" + var0 +
				", var1=" + var1 +
				", var2=" + var2 +
				'}';
	}
}
