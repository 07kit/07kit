package com.kit.api.wrappers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author const_
 */
public enum Skill {

    ATTACK("Attack", 0),
    DEFENCE("Defence", 1),
    STRENGTH("Strength", 2),
    HITPOINTS("Hitpoints", 3),
    RANGED("Ranged", 4),
    PRAYER("Prayer", 5),
    MAGIC("Magic", 6),
    COOKING("Cooking", 7),
    WOODCUTTING("Woodcutting", 8),
    FLETCHING("Fletching", 9),
    FISHING("Fishing", 10),
    FIREMAKING("Firemaking", 11),
    CRAFTING("Crafting", 12),
    SMITHING("Smithing", 13),
    MINING("Mining", 14),
    HERBLORE("Herblore", 15),
    AGILITY("Agility", 16),
    THIEVING("Thieving", 17),
    SLAYER("Slayer", 18),
    FARMING("Farming", 19),
    RUNECRAFTING("Runecrafting", 20),
    HUNTER("Hunter", 21),
    CONSTRUCTION("Construction", 22),
    OVERALL("Overall", 23),
    COMBAT("Combat", 24);

    private final String name;
    private final int index;

    Skill(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    private static final Map<Integer, Skill> INDEX_MAP = new HashMap<>();

    static {
        for (Skill s : values()) {
            INDEX_MAP.put(s.getIndex(), s);
        }
    }
    public static Skill forIndex(int idx) {
        return INDEX_MAP.get(idx);
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}
