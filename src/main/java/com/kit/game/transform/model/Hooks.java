package com.kit.game.transform.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hooks {

    @SerializedName("revision")
    public int revision;
    @SerializedName("classDefinitions")
    public List<ClassDefinition> classDefinitions;

}
