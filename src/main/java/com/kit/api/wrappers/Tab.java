package com.kit.api.wrappers;

public enum Tab {

    /**
     * 47 - 53 - Top Bar
     * COMBAT, STATS, QUESTS, INVENTORY, EQUIPMEN, PRAYER, MAGIC
     * 30 - 36 - Bottom Bar
     * CLAN_CHAT, FRIENDS_LISTm IGNORE_LIST, LOGOUT, OPTIONS, EMOTES, MUSIC
     * texture | 861580522 ( OPEN )
     * texture | -1347704871 ( CLOSED )
     */

    COMBAT(45),
    STATS(46),
    QUESTS(47),
    INVENTORY(48),
    EQUIPMENT(49),
    PRAYER(50),
    MAGIC(51),
    CLAN_CHAT(28),
    FRIENDS_LIST(29),
    IGNORE_LIST(30),
    LOGOUT(31),
    OPTIONS(32),
    EMOTES(33),
    MUSIC(34);

    private final int id;

    Tab(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Tab byId(int id) {
        for (Tab t : values()) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }
}
