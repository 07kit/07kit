package com.kit.api.wrappers;

import com.google.gson.annotations.SerializedName;

public class NpcInfo {

	@SerializedName("id")
	private final int id;
	@SerializedName("name")
	private final String name;
	@SerializedName("members")
	private final boolean members;
	@SerializedName("combatLevel")
	private final int combatLevel;
	@SerializedName("hp")
	private final int hp;


	public NpcInfo(int id, String name, boolean members, int combatLevel, int hp) {
		this.id = id;
		this.name = name;
		this.members = members;
		this.combatLevel = combatLevel;
		this.hp = hp;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isMembers() {
		return members;
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public int getHp() {
		return hp;
	}
}
