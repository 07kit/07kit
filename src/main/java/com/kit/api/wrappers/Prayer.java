package com.kit.api.wrappers;

/**
 * Represents prayers in the prayer book
 *
 * @author tommo
 */
public enum Prayer {
    THICK_SKIN(5, 83, 1),
    BURST_OF_STRENGTH(7, 84, 4),
    CLARITY_OF_THOUGHT(9, 85, 7),
    SHARP_EYE(11, 862, 8),
    MYSTIC_WILL(13, 863, 9),
    ROCK_SKIN(15, 86, 10),
    SUPERHUMAN_STRENGTH(17, 87, 13),
    IMPROVED_REFLEXES(19, 88, 16),
    RAPID_RESTORE(21, 89, 19),
    RAPID_HEAL(23, 90, 22),
    PROTECT_ITEM(25, 91, 25),
    HAWK_EYE(27, 864, 26),
    MYSTIC_LORE(29, 865, 27),
    STEEL_SKIN(31, 92, 28),
    ULTIMATE_STRENGTH(33, 94, 31),
    INCREDIBLE_REFLEXES(35, 94, 34),
    PROTECT_FROM_MAGIC(37, 95, 37),
    PROTECT_FROM_MISSILES(39, 96, 40),
    PROTECT_FROM_MELEE(41, 97, 43),
    //TODO prayers past protect from melee have guessed settings indexes, so need testing
    EAGLE_EYE(43, 98, 44),
    MYSTIC_MIGHT(45, 99, 45),
    RETRIBUTION(47, 100, 46),
    REDEMPTION(49, 101, 49),
    SMITE(51, 102, 52),
    CHIVALRY(53, 103, 60),
    PIETY(55, 104, 70);

    private int widgetId;
    private int settingsIndex;
    private int requiredLevel;

    Prayer(int widgetId, int settingsIndex, int requiredLevel) {
        this.widgetId = widgetId;
        this.settingsIndex = settingsIndex;
        this.requiredLevel = requiredLevel;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public int getSettingsIndex() {
        return settingsIndex;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    @Override
    public String toString() {
        char c = name().charAt(0);
        return c + name().toLowerCase().substring(1).replace("_", " ");
    }
}