package com.kit.plugins.cluescrolls;

public enum ClueScrollType {

    COORDINATE("Location"), KEY("Key"), EMOTE("Emote"), SPEECH("Speech"), OBJECT_INTERACTION("Object Interaction");

    private String name;

    ClueScrollType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

