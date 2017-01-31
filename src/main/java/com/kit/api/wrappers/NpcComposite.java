package com.kit.api.wrappers;

import com.kit.game.engine.cache.composite.INpcComposite;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * @author const_
 */
public class NpcComposite implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name = "null";
    private int boundDim = -1;
    private int[] additionalModels;
    private short[] fieldS;
    private int configID = -1;
    private int walkAnimation = -1;
    private int turn180Animation = -1;
    private int turn90CWAnimation = -1;
    private int turn90CCWAnimation = -1;
    private short[] recolorTarget;
    private short[] fieldF;
    private int[] npcModels;
    private String[] actions = new String[5];
    private boolean drawMinimapDot = true;
    private int combatLevel = -1;
    private int scaleXZ = 128;
    private int scaleY = 128;
    private boolean isVisible = false;
    private int lightModifier = 0;
    private int shadowModifier = 0;
    private int headIcon = -1;
    private int degreesToTurn = 32;
    private int[] childIDs;
    private int varbitID = -1;
    private boolean isClickable = true;
    private short[] recolorOriginal;
    private int idleAnimation = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBoundDim() {
        return boundDim;
    }

    public void setBoundDim(int boundDim) {
        this.boundDim = boundDim;
    }

    public int[] getAdditionalModels() {
        return additionalModels;
    }

    public void setAdditionalModels(int[] additionalModels) {
        this.additionalModels = additionalModels;
    }

    public short[] getFieldS() {
        return fieldS;
    }

    public void setFieldS(short[] fieldS) {
        this.fieldS = fieldS;
    }

    public int getConfigID() {
        return configID;
    }

    public void setConfigID(int configID) {
        this.configID = configID;
    }

    public int getWalkAnimation() {
        return walkAnimation;
    }

    public void setWalkAnimation(int walkAnimation) {
        this.walkAnimation = walkAnimation;
    }

    public int getTurn180Animation() {
        return turn180Animation;
    }

    public void setTurn180Animation(int turn180Animation) {
        this.turn180Animation = turn180Animation;
    }

    public int getTurn90CWAnimation() {
        return turn90CWAnimation;
    }

    public void setTurn90CWAnimation(int turn90CWAnimation) {
        this.turn90CWAnimation = turn90CWAnimation;
    }

    public int getTurn90CCWAnimation() {
        return turn90CCWAnimation;
    }

    public void setTurn90CCWAnimation(int turn90CCWAnimation) {
        this.turn90CCWAnimation = turn90CCWAnimation;
    }

    public short[] getRecolorTarget() {
        return recolorTarget;
    }

    public void setRecolorTarget(short[] recolorTarget) {
        this.recolorTarget = recolorTarget;
    }

    public short[] getFieldF() {
        return fieldF;
    }

    public void setFieldF(short[] fieldF) {
        this.fieldF = fieldF;
    }

    public int[] getNpcModels() {
        return npcModels;
    }

    public void setNpcModels(int[] npcModels) {
        this.npcModels = npcModels;
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

    public boolean isDrawMinimapDot() {
        return drawMinimapDot;
    }

    public void setDrawMinimapDot(boolean drawMinimapDot) {
        this.drawMinimapDot = drawMinimapDot;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    public int getScaleXZ() {
        return scaleXZ;
    }

    public void setScaleXZ(int scaleXZ) {
        this.scaleXZ = scaleXZ;
    }

    public int getScaleY() {
        return scaleY;
    }

    public void setScaleY(int scaleY) {
        this.scaleY = scaleY;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getLightModifier() {
        return lightModifier;
    }

    public void setLightModifier(int lightModifier) {
        this.lightModifier = lightModifier;
    }

    public int getShadowModifier() {
        return shadowModifier;
    }

    public void setShadowModifier(int shadowModifier) {
        this.shadowModifier = shadowModifier;
    }

    public int getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(int headIcon) {
        this.headIcon = headIcon;
    }

    public int getDegreesToTurn() {
        return degreesToTurn;
    }

    public void setDegreesToTurn(int degreesToTurn) {
        this.degreesToTurn = degreesToTurn;
    }

    public int[] getChildIDs() {
        return childIDs;
    }

    public void setChildIDs(int[] childIDs) {
        this.childIDs = childIDs;
    }

    public int getVarbitID() {
        return varbitID;
    }

    public void setVarbitID(int varbitID) {
        this.varbitID = varbitID;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public short[] getRecolorOriginal() {
        return recolorOriginal;
    }

    public void setRecolorOriginal(short[] recolorOriginal) {
        this.recolorOriginal = recolorOriginal;
    }

    public int getIdleAnimation() {
        return idleAnimation;
    }

    public void setIdleAnimation(int idleAnimation) {
        this.idleAnimation = idleAnimation;
    }

    @Override
    public String toString() {
        return "NpcComposite{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", boundDim=" + boundDim +
                ", additionalModels=" + Arrays.toString(additionalModels) +
                ", fieldS=" + Arrays.toString(fieldS) +
                ", configID=" + configID +
                ", walkAnimation=" + walkAnimation +
                ", turn180Animation=" + turn180Animation +
                ", turn90CWAnimation=" + turn90CWAnimation +
                ", turn90CCWAnimation=" + turn90CCWAnimation +
                ", recolorTarget=" + Arrays.toString(recolorTarget) +
                ", fieldF=" + Arrays.toString(fieldF) +
                ", npcModels=" + Arrays.toString(npcModels) +
                ", actions=" + Arrays.toString(actions) +
                ", drawMinimapDot=" + drawMinimapDot +
                ", combatLevel=" + combatLevel +
                ", scaleXZ=" + scaleXZ +
                ", scaleY=" + scaleY +
                ", isVisible=" + isVisible +
                ", lightModifier=" + lightModifier +
                ", shadowModifier=" + shadowModifier +
                ", headIcon=" + headIcon +
                ", degreesToTurn=" + degreesToTurn +
                ", childIDs=" + Arrays.toString(childIDs) +
                ", varbitID=" + varbitID +
                ", isClickable=" + isClickable +
                ", recolorOriginal=" + Arrays.toString(recolorOriginal) +
                ", idleAnimation=" + idleAnimation +
                '}';
    }
}
