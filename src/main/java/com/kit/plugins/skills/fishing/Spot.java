package com.kit.plugins.skills.fishing;

/**
 */
public enum Spot {

    SHRIMP(new String[]{"Shrimp", "Anchovies"}, new String[]{"Net", "Bait"}, Equipment.FISHING_NET, 1),
    HERRING(new String[]{"Sardine", "Herring"}, new String[]{"Net", "Bait"}, Equipment.FISHING_ROD, 5),
    TROUT(new String[]{"Trout", "Salmon"}, new String[]{"Lure", "Bait"}, Equipment.FLY_FISHING_ROD, 20),
    BASS(new String[]{"Mackerel", "Cod", "Bass"}, new String[]{"Net", "Harpoon"}, Equipment.BIG_FISHING_NET, 16),
    TUNA(new String[]{"Tuna", "Swordfish"}, new String[]{"Cage", "Harpoon"}, Equipment.HARPOON, 35),
    LOBSTER(new String[]{"Lobster"}, new String[]{"Cage", "Harpoon"}, Equipment.LOBSTER_POT, 40),
    MONKFISH(new String[]{"Monkfish"}, new String[]{"Net", "Harpoon"}, Equipment.FISHING_NET, 62),
    SHARK(new String[]{"Shark"}, new String[]{"Net", "Harpoon"}, Equipment.HARPOON, 76),
    DARK_CRAB(new String[]{"Dark crab"}, new String[]{"Cage"}, Equipment.LOBSTER_POT, 85),
    UNKNOWN(new String[0], new String[0], Equipment.HARPOON, 1);

    String[] fish;
    String[] actions;
    Equipment equipment;
    int level;

    Spot(String[] fish, String[] actions, Equipment equipment, int level) {
        this.fish = fish;
        this.actions = actions;
        this.equipment = equipment;
        this.level = level;
    }
}
