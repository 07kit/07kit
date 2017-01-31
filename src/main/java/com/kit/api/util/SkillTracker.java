package com.kit.api.util;

import com.kit.api.MethodContext;
import com.kit.api.wrappers.Skill;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Skill;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Skill;

/**
 * @author Hexagon
 */

public class SkillTracker {

    private final MethodContext ctx;
    private final Skill skill;
    private final Timer startTimer;
    private int startLevel;
    private int startExperience;

    private final int[] EXPERIENCE_TABLE = {0, 0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107,
            2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833,
            16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983,
            75127, 83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886,
            273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257,
            992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087, 2673114, 2951373,
            3258594, 3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629, 7944614, 8771558, 9684577,
            10692629, 11805606, 13034431};

    /**
     * Starts the skill tracker.
     *
     * @param ctx   The MethodContext of the script.
     * @param skill The skill to be tracked.
     */

    public SkillTracker(final MethodContext ctx, final Skill skill) {
        this.ctx = ctx;
        this.skill = skill;
        this.startTimer = new Timer(0);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!ctx.isLoggedIn()) {
                        Thread.sleep(100);
                    }
                    SkillTracker.this.startLevel = ctx.skills.getBaseLevel(skill);
                    SkillTracker.this.startExperience = ctx.skills.getExperience(skill);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Gets the skill being tracked.
     *
     * @return The <code>Skill</code> being tracked.
     */

    public Skill getTrackedSkill() {
        return skill;
    }

    /**
     * Gets the total time tracking since the tracker was initialized.
     *
     * @return The total time tracked as a <code>long</code>.
     */

    public long getTimeTracking() {
        return startTimer.elapsed();
    }

    /**
     * Gets the formatted total time tracking since the tracker was initialized.
     *
     * @return The total time tracked as a <code>String</code> formatted in
     * HH:MM:SS.
     */

    public String getFormattedTimeTracking() {
        return Timer.formatTime(getTimeTracking());
    }

    /**
     * Gets the remaining experience to the next level.
     *
     * @return The experience amount to level as an <code>int</code>.
     */

    public int getExperienceToLevel() {
        return ctx.skills.getExperienceToLevel(skill, ctx.skills.getBaseLevel(skill) + 1);
    }

    /**
     * Gets the aproximate time to the next level.
     *
     * @return The aproximate remaining time to the next level as a
     * <code>String</code> formatted in HH:MM:SS.
     */

    public String getTimeToLevel() {
        return getExperiencePerHour() > 0 ? Timer
                .formatTime((long) ((getExperienceToLevel() * 3600000.0) / getExperiencePerHour())) : "Unknown";
    }

    /**
     * Gets the current tracked skill level.
     *
     * @return The current level as an <code>int</code>.
     */

    public int getCurrentLevel() {
        return ctx.skills.getBaseLevel(skill);
    }

    /**
     * Gets the amount of levels gained since the tracker was initialized.
     *
     * @return The amount of levels gained as an <code>int</code>.
     */

    public int getLevelsGained() {
        return getCurrentLevel() - startLevel;
    }

    /**
     * Gets the amount of experience gained since the tracker was initialized.
     *
     * @return The amount of experience gained as an <code>int</code>.
     */

    public int getExperienceGained() {
        return ctx.skills.getExperience(skill) - startExperience;
    }

    /**
     * Gets the aproximate hourly experience gain on the skill.
     *
     * @return The aproximate hourly experience gain as an <code>int</code>.
     */

    public int getExperiencePerHour() {
        return (int) (getExperienceGained() * 3600000.0D / getTimeTracking());
    }

    /**
     * Gets the amount of experience to an specific level.
     *
     * @param level - The level to achieve.
     * @return The amount of experience to the level as an <code>int</code>.
     */

    public int getExperienceToLevel(int level) {
        if (level > 99) {
            return 0;
        }
        int experience = ctx.skills.getExperience(skill);
        return EXPERIENCE_TABLE[level] - experience;
    }

    /**
     * Gets the percentage to the next level.
     *
     * @return The percentage to the next level as an <code>int>/code>.
     */

    public int getPercentToNextLevel() {
        if (getCurrentLevel() == 99) {
            return 100;
        }
        return 100 * (ctx.skills.getExperience(skill) - EXPERIENCE_TABLE[getCurrentLevel()])
                / (EXPERIENCE_TABLE[getCurrentLevel() + 1] - EXPERIENCE_TABLE[getCurrentLevel()]);
    }
}