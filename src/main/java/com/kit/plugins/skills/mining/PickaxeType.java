package com.kit.plugins.skills.mining;


public enum PickaxeType {


    BRONZE("Bronze pickaxe", 1265),
    IRON("Iron pickaxe", 1267),
    STEEL("Steel pickaxe", 1269),
    MITHRIL("Mithril pickaxe", 1273),
    ADAMANT("Adamant pickaxe", 1271),
    RUNE("Rune pickaxe", 1275),
    BLACK("Black pickaxe", 12297),
    DRAGON("Dragon pickaxe", 11920);
    //TODO support infernal and 3rdage
    //INFERNAL("Infernal pickpickaxe", 6739);

    private String name;
    private int id;

    PickaxeType(String name, int id) {
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
