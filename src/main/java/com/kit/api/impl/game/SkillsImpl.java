package com.kit.api.impl.game;


import com.kit.api.MethodContext;
import com.kit.api.Skills;
import com.kit.api.wrappers.Skill;

/**
 * User: Cov
 * Date: 23/09/13
 * Time: 20:49
 */
public class SkillsImpl implements Skills {

    private final MethodContext ctx;


    public SkillsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getExperience(Skill skill) {
        int experience = 0;
        if (skill == Skill.OVERALL) {
            for (Skill skill1 : Skill.values()) {
                experience += ctx.client().getSkillExperiences()[skill1.getIndex()];
            }
            return experience;
        }
        if (skill == Skill.COMBAT) {
            for (Skill skill1 : new Skill[]{Skill.MAGIC, Skill.ATTACK, Skill.DEFENCE, Skill.STRENGTH, Skill.HITPOINTS,
                    Skill.PRAYER, Skill.RANGED}) {
                experience += ctx.client().getSkillExperiences()[skill1.getIndex()];
            }
            return experience;
        }
        return ctx.client().getSkillExperiences()[skill.getIndex()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLevel(Skill skill) {
        if (skill == Skill.OVERALL) {
            int levels = 0;
            for (Skill skill1 : Skill.values()) {
                levels += ctx.client().getSkillLevels()[skill1.getIndex()];
            }
            return levels;
        }
        if (skill == Skill.COMBAT) {
            return getCombatLevel(false);
        }
        return ctx.client().getSkillLevels()[skill.getIndex()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBaseLevel(Skill skill) {
        if (skill == Skill.OVERALL) {
            int levels = 0;
            for (Skill skill1 : Skill.values()) {
                levels += ctx.client().getSkillBases()[skill1.getIndex()];
            }
            return levels;
        }
        if (skill == Skill.COMBAT) {
            return getCombatLevel(true);
        }
        return ctx.client().getSkillBases()[skill.getIndex()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getExperienceToLevel(Skill skill, int level) {
        if (level < 0 || level > 99 || skill == Skill.COMBAT || skill == Skill.OVERALL) {
            return -1;
        }
        int experience = getExperience(skill);
        if (experience == -1) {
            return -1;
        }
        return EXPERIENCE_TABLE[level] - experience;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getExperienceAtLevel(int level) {
        return EXPERIENCE_TABLE[level];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPercentToLevel(Skill skill, int level) {
        return (getExperienceToLevel(skill, level) / getExperienceAtLevel(level + 1)) * 100;
    }

    // Table of levels. Index is the level and the value is the experience required
    private static final int[] EXPERIENCE_TABLE = {
            0, 0, 83, 174, 276, 388, 512, 650, 801,
            969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973,
            4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031,
            13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408,
            33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
            83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636,
            184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599,
            407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
            899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
            1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
            3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253,
            7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431
    };

    private int getCombatLevel(boolean baseLevel) {
        double base = 0.25 * ((baseLevel ? getBaseLevel(Skill.DEFENCE) : getLevel(Skill.DEFENCE)) +
                (baseLevel ? getBaseLevel(Skill.HITPOINTS) : getLevel(Skill.HITPOINTS)) +
                (Math.floor((baseLevel ? getBaseLevel(Skill.PRAYER) : getLevel(Skill.PRAYER)) / 2)));
        double melee = 0.325 * ((baseLevel ? getBaseLevel(Skill.ATTACK) : getLevel(Skill.ATTACK)) +
                (baseLevel ? getBaseLevel(Skill.STRENGTH) : getLevel(Skill.STRENGTH)));
        double range = 0.325 * ((baseLevel ? getBaseLevel(Skill.RANGED) : getLevel(Skill.RANGED)) +
                (Math.floor((baseLevel ? getBaseLevel(Skill.RANGED) : getLevel(Skill.RANGED)) / 2)));
        double mage = 0.325 * ((baseLevel ? getBaseLevel(Skill.MAGIC) : getLevel(Skill.MAGIC)) +
                (Math.floor((baseLevel ? getBaseLevel(Skill.MAGIC) : getLevel(Skill.MAGIC)) / 2)));
        double high = 0.0;
        for (double calc : new double[]{melee, range, mage}) {
            if (calc > high) {
                high = calc;
            }
        }
        return (int) Math.floor(base + high);
    }

}
