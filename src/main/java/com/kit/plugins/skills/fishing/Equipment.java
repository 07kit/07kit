package com.kit.plugins.skills.fishing;

/**
 */
public enum Equipment {
    FISHING_NET(new int[]{303}),
    BIG_FISHING_NET(new int[]{305}),
    FISHING_ROD(new int[]{307}),
    FLY_FISHING_ROD(new int[]{309}),
    HARPOON(new int[]{311}),
    LOBSTER_POT(new int[]{301});

    int[] ids;

    Equipment(int[] ids) {
        this.ids = ids;
    }
}
