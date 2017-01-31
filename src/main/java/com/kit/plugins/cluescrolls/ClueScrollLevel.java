package com.kit.plugins.cluescrolls;

public enum ClueScrollLevel {

    EASY("Easy"), MEDIUM("Medium"), HARD("Hard"), VERY_HARD("Very Hard");

    private String name;

    ClueScrollLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
