package com.kit.api.wrappers.hiscores;

public class HiscoreSkill {

	private int rank;
	private int level;
	private int experience;

	public HiscoreSkill(int rank, int level, int experience) {
		this.rank = rank;
		this.level = level;
		this.experience = experience;
	}

	public int getRank() {
		return rank;
	}

	public int getLevel() {
		return level;
	}

	public int getExperience() {
		return experience;
	}
}
