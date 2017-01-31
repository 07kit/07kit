package com.kit.game.transform.model;

import com.google.gson.annotations.SerializedName;

/**
 */
public class GetterDefinition {

    @SerializedName("name")
    public String name;
    @SerializedName("signature")
    public String signature;
    @SerializedName("actualSig")
    public String actualSig;
    @SerializedName("fieldClass")
    public String fieldClass;
    @SerializedName("fieldName")
    public String fieldName;
    @SerializedName("injectSetter")
    public boolean injectSetter;
    @SerializedName("multiplier")
    public long multiplier;
    @SerializedName("member")
    public boolean member;

    public boolean isNonPrimitive() {
        return !(signature.contains("java") && signature.contains("/"))
                && !signature.matches("\\[{0,3}(I|Z|F|J|S|B|D|C)(;?)");
    }
}
