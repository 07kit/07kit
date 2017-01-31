package com.kit.plugins.skills.woodcutting;

/**
 */
public enum HatchetType {


    BRONZE("Bronze axe", 1351),
    IRON("Iron axe", 1349),
    STEEL("Steel axe", 1353),
    MITHRIL("Mithril axe", 1355),
    ADAMANT("Adamant axe", 1357),
    RUNE("Rune axe", 1359),
    BLACK("Black axe", 1361),
    DRAGON("Dragon axe", 6739);

    private String name;
    private int id;

    HatchetType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
