package com.kit.api.wrappers;

import java.io.Serializable;

public class SettingsComposite implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int[] BITFIELD_MAX_VALUE = new int[32];
    private int id;
    private int configID;
    private int mostSignificantBit;
    private int leastSignificantBit;

    static {
        int i = 2;
        for (int index = 0; index < 32; index++) {
            BITFIELD_MAX_VALUE[index] = i - 1;
            i += i;
        }
    }

    public static int[] getBitfieldMaxValue() {
        return BITFIELD_MAX_VALUE;
    }

    public int getId() {
        return id;
    }

    public void setId(int varpID) {
        this.id = varpID;
    }

    public int getConfigID() {
        return configID;
    }

    public void setConfigID(int configID) {
        this.configID = configID;
    }

    public int getMostSignificantBit() {
        return mostSignificantBit;
    }

    public void setMostSignificantBit(int mostSignificantBit) {
        this.mostSignificantBit = mostSignificantBit;
    }

    public int getLeastSignificantBit() {
        return leastSignificantBit;
    }

    public void setLeastSignificantBit(int leastSignificantBit) {
        this.leastSignificantBit = leastSignificantBit;
    }

    @Override
    public String toString() {
        return "SettingsComposite{" +
                "id=" + id +
                ", configID=" + configID +
                ", mostSignificantBit=" + mostSignificantBit +
                ", leastSignificantBit=" + leastSignificantBit +
                '}';
    }
}
