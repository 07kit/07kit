package com.kit.api.wrappers;

import com.kit.game.engine.cache.composite.IObjectComposite;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Wrapper for IObjectComposite
 *
 */
public class ObjectComposite implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name = "null";
    private int brightness = 0;
    private int id;
    private String[] actions = new String[5];
    private int[] objectModelTypes;
    private short[] modifiedModelColors;
    private short[] originalModelColors;
    private short[] fieldO;
    private short[] fieldX;
    private int isUnwalkableInt = 0;
    private boolean blocksProjectiles = true;
    private int hasActions = 0;
    private int adjustToTerrain = 0;
    private boolean nonFlatShading = false;
    private int animationID = -1;
    private int[] objectModelIDs;
    private int contrast = 0;
    private boolean isSolidObject = false;
    private int width = 1;
    private int height = 1;
    private int modelSizeX = 128;
    private int modelSizeH = 128;
    private int modelSizeY = 128;
    private int offsetX = 0;
    private int offsetH = 0;
    private int offsetY = 0;
    private int minimapIcon = -1;
    private int[] childIDs;
    private int varbitID = -1;
    private int configID = -1;
    private int mapSceneID = -1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

    public int[] getObjectModelTypes() {
        return objectModelTypes;
    }

    public void setObjectModelTypes(int[] objectModelTypes) {
        this.objectModelTypes = objectModelTypes;
    }

    public short[] getModifiedModelColors() {
        return modifiedModelColors;
    }

    public void setModifiedModelColors(short[] modifiedModelColors) {
        this.modifiedModelColors = modifiedModelColors;
    }

    public short[] getOriginalModelColors() {
        return originalModelColors;
    }

    public void setOriginalModelColors(short[] originalModelColors) {
        this.originalModelColors = originalModelColors;
    }

    public short[] getFieldO() {
        return fieldO;
    }

    public void setFieldO(short[] fieldO) {
        this.fieldO = fieldO;
    }

    public short[] getFieldX() {
        return fieldX;
    }

    public void setFieldX(short[] fieldX) {
        this.fieldX = fieldX;
    }

    public int getIsUnwalkableInt() {
        return isUnwalkableInt;
    }

    public void setIsUnwalkableInt(int isUnwalkableInt) {
        this.isUnwalkableInt = isUnwalkableInt;
    }

    public boolean isBlocksProjectiles() {
        return blocksProjectiles;
    }

    public void setBlocksProjectiles(boolean blocksProjectiles) {
        this.blocksProjectiles = blocksProjectiles;
    }

    public int getHasActions() {
        return hasActions;
    }

    public void setHasActions(int hasActions) {
        this.hasActions = hasActions;
    }

    public int getAdjustToTerrain() {
        return adjustToTerrain;
    }

    public void setAdjustToTerrain(int adjustToTerrain) {
        this.adjustToTerrain = adjustToTerrain;
    }

    public boolean isNonFlatShading() {
        return nonFlatShading;
    }

    public void setNonFlatShading(boolean nonFlatShading) {
        this.nonFlatShading = nonFlatShading;
    }

    public int getAnimationID() {
        return animationID;
    }

    public void setAnimationID(int animationID) {
        this.animationID = animationID;
    }

    public int[] getObjectModelIDs() {
        return objectModelIDs;
    }

    public void setObjectModelIDs(int[] objectModelIDs) {
        this.objectModelIDs = objectModelIDs;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public boolean isSolidObject() {
        return isSolidObject;
    }

    public void setSolidObject(boolean solidObject) {
        isSolidObject = solidObject;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getModelSizeX() {
        return modelSizeX;
    }

    public void setModelSizeX(int modelSizeX) {
        this.modelSizeX = modelSizeX;
    }

    public int getModelSizeH() {
        return modelSizeH;
    }

    public void setModelSizeH(int modelSizeH) {
        this.modelSizeH = modelSizeH;
    }

    public int getModelSizeY() {
        return modelSizeY;
    }

    public void setModelSizeY(int modelSizeY) {
        this.modelSizeY = modelSizeY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetH() {
        return offsetH;
    }

    public void setOffsetH(int offsetH) {
        this.offsetH = offsetH;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getMinimapIcon() {
        return minimapIcon;
    }

    public void setMinimapIcon(int minimapIcon) {
        this.minimapIcon = minimapIcon;
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

    public int getConfigID() {
        return configID;
    }

    public void setConfigID(int configID) {
        this.configID = configID;
    }

    public int getMapSceneID() {
        return mapSceneID;
    }

    public void setMapSceneID(int mapSceneID) {
        this.mapSceneID = mapSceneID;
    }

    @Override
    public String toString() {
        return "ObjectComposite{" +
                "name='" + name + '\'' +
                ", brightness=" + brightness +
                ", id=" + id +
                ", actions=" + Arrays.toString(actions) +
                ", objectModelTypes=" + Arrays.toString(objectModelTypes) +
                ", modifiedModelColors=" + Arrays.toString(modifiedModelColors) +
                ", originalModelColors=" + Arrays.toString(originalModelColors) +
                ", fieldO=" + Arrays.toString(fieldO) +
                ", fieldX=" + Arrays.toString(fieldX) +
                ", isUnwalkableInt=" + isUnwalkableInt +
                ", blocksProjectiles=" + blocksProjectiles +
                ", hasActions=" + hasActions +
                ", adjustToTerrain=" + adjustToTerrain +
                ", nonFlatShading=" + nonFlatShading +
                ", animationID=" + animationID +
                ", objectModelIDs=" + Arrays.toString(objectModelIDs) +
                ", contrast=" + contrast +
                ", isSolidObject=" + isSolidObject +
                ", width=" + width +
                ", height=" + height +
                ", modelSizeX=" + modelSizeX +
                ", modelSizeH=" + modelSizeH +
                ", modelSizeY=" + modelSizeY +
                ", offsetX=" + offsetX +
                ", offsetH=" + offsetH +
                ", offsetY=" + offsetY +
                ", minimapIcon=" + minimapIcon +
                ", childIDs=" + Arrays.toString(childIDs) +
                ", varbitID=" + varbitID +
                ", configID=" + configID +
                ", mapSceneID=" + mapSceneID +
                '}';
    }
}
