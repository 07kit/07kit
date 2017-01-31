package com.kit.api.event;

import com.kit.api.wrappers.Skill;

import java.io.File;

/**
 */
public class LevelUpEvent {
    private final File screenshot;
    private final Skill skill;
    private final int level;

    public LevelUpEvent(File screenshot, Skill skill, int level) {
        this.screenshot = screenshot;
        this.skill = skill;
        this.level = level;
    }

    public File getScreenshot() {
        return screenshot;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }
}
