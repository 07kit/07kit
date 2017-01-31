package com.kit.api.event;

import com.kit.game.engine.cache.composite.INpcComposite;
import com.kit.game.engine.cache.composite.INpcComposite;
import com.kit.game.engine.cache.composite.INpcComposite;

public class NpcMenuCreatedEvent {

	private INpcComposite composite;

	private int var0;
	private int var1;
	private int var2;

	public NpcMenuCreatedEvent(INpcComposite composite, int var0, int var1, int var2) {
		this.composite = composite;
		this.var0 = var0;
		this.var1 = var1;
		this.var2 = var2;
	}

	public INpcComposite getComposite() {
		return composite;
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
		return "NpcMenuCreatedEvent{" +
				"composite=" + composite +
				", var0=" + var0 +
				", var1=" + var1 +
				", var2=" + var2 +
				'}';
	}
}
