package com.kit.api.event;

public class ExperienceChangedEvent {

	private int var0;
	private int var1;

	//this guy dont work yo
	public ExperienceChangedEvent(int var0, int var1) {
		this.var0 = var0;
		this.var1 = var1;
	}

	@Override
	public String toString() {
		return "ExperienceChangedEvent{" +
				"var0=" + var0 +
				", var1=" + var1 +
				'}';
	}
}
