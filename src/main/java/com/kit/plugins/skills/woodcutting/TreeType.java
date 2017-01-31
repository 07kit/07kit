package com.kit.plugins.skills.woodcutting;

/**
 */
public enum TreeType {

    TREE(new String[]{"Tree"}, new int[]{}),
    OAK(new String[]{"Oak Tree"}, new int[]{1281, 3037, 8462, 8463, 8464, 8465, 8466, 8467, 10083, 11999}),
    WILLOW(new String[]{"Willow Tree"}, new int[]{139, 142, 2210, 2372, 8481, 8482, 8483, 8484, 8485, 8486, 8487, 8488}),
    MAPLE(new String[]{"Maple Tree"}, new int[]{}),
    YEW(new String[]{"Yew Tree"}, new int[]{1753, 1754, 1309, 8503, 8504, 8505, 8506, 8507, 8508, 8509, 8510, 8511, 8512, 8513}),
    MAGIC_TREE(new String[]{"Magic tree"}, new int[]{});

    private String[] aliases;
    private int[] ids;

    TreeType(String[] aliases, int[] ids) {
        this.aliases = aliases;
        this.ids = ids;
    }

    public String[] getAliases() {
        return aliases;
    }

    public int[] getIds() {
        return ids;
    }
}
