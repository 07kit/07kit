package com.kit.api.wrappers.hiscores;

import com.kit.api.wrappers.Skill;

import java.util.Map;

public class HiscoreLookup {

	private Map<Skill, HiscoreSkill> skills;

	public HiscoreSkill getSkill(Skill skill) {
		return skills.get(skill);
	}
	public HiscoreLookup(Map<Skill, HiscoreSkill> skills) {
		this.skills = skills;
	}

	public Map<Skill, HiscoreSkill> getSkills() {
		return skills;
	}
}
