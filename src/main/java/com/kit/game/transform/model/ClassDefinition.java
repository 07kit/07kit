package com.kit.game.transform.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ClassDefinition {

    @SerializedName("getters")
    public List<GetterDefinition> getters = new ArrayList<>();
    @SerializedName("methods")
    public List<MethodDefinition> methods = new ArrayList<>();
    @SerializedName("identifiedName")
    public String identifiedName;
    @SerializedName("originalName")
    public String originalName;
}
