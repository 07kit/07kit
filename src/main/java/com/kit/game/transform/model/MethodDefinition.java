package com.kit.game.transform.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : const_
 */
public class MethodDefinition {

    @SerializedName("name")
    public String name;
    @SerializedName("owner")
    public String owner;
    @SerializedName("actualDesc")
    public String actualDesc;
    @SerializedName("modifiedDesc")
    public String modifiedDesc;
    @SerializedName("paramLoadOpcodes")
    public List<Integer> paramLoadOpcodes;
    @SerializedName("paramDescs")
    public List<String> paramDescs;
    @SerializedName("opcodeAndIndexes")
    public List<String> opcodeAndIndexes;
    @SerializedName("injectInvoker")
    public boolean injectInvoker;
    @SerializedName("insnToInjectBefore")
    public int insnToInjectBefore;
    @SerializedName("opcode")
    public int opcode;
    @SerializedName("opaque")
    public int opaque;
    @SerializedName("actualName")
    public String actualName;
}
