package com.kit.api.wrappers;

public class CombatInfo {
	private final int combatLevel;
	private final int hitpoints;
	private final int maxHit;

	public CombatInfo(int combatLevel, int hitpoints, int maxHit) {
		this.combatLevel = combatLevel;
		this.hitpoints = hitpoints;
		this.maxHit = maxHit;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	@Override
	public String toString() {
		return "CombatInfo{" +
				"combatLevel=" + combatLevel +
				", hitpoints=" + hitpoints +
				", maxHit=" + maxHit +
				'}';
	}
}
