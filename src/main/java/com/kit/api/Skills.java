package com.kit.api;

import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.Skill;

/**
 * @author Cov
 */
public interface Skills {

    /**
     * Retrieves the experience for specified skill
     *
     * @param skill The skill to get for
     * @return experience
     */
    int getExperience(Skill skill);

    /**
     * Retrieves the current level for specified skill
     *
     * @param skill The skill to get for
     * @return level
     */
    int getLevel(Skill skill);

    /**
     * Retrives the base level for the current skill
     *
     * @param skill The skill to get for
     * @return the base level
     */
    int getBaseLevel(Skill skill);

    /**
     * Retrieves the remaining XP required to reach a level, based on the current level
     *
     * @param skill The skill to get for
     * @param level Level to reach
     * @return remaining xp to level
     */
    int getExperienceToLevel(Skill skill, int level);

    int getPercentToLevel(Skill skill, int level);

    int getExperienceAtLevel(int level);
}
