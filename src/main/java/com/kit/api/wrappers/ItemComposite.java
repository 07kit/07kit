package com.kit.api.wrappers;

import com.kit.api.util.PriceLookup;
import com.kit.game.engine.cache.composite.IItemComposite;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Wrapper for item composites.
 *
 */
public class ItemComposite implements Serializable {
    private static final long serialVersionUID = 1L;

    private int itemID;
    private int certTemplateId = -1;
    private int certReferenceId = -1;
    private int team = 0;
    private String name = "null";
    private String[] groundActions = null;
    private String[] inventoryActions = null;
    private int isStackable = 0;
    private int value = 1;
    private boolean isMembersOnly = false;
    private int inventoryModelID;
    private short[] modelRecolorOriginal;
    private short[] modelRecolorTarget;
    private int[] stackVariantSize;
    private short[] modelTextureTarget;
    private int rotationLength = 2000;
    private int rotationX = 0;
    private int rotationY = 0;
    private int rotationZ = 0;
    private int translateX = 0;
    private int translateY = 0;
    private int modelScaleX = 128;
    private int modelScaleY = 128;
    private int modelScaleZ = 128;
    private int equippedModelMale1;
    private int equippedModelMale2;
    private int equippedModelMale3;
    private int equippedModelMaleTranslationY;
    private int equippedModelFemale1;
    private int equippedModelFemale2;
    private int equippedModelFemale3;
    private int equippedModelFemaleTranslationY;
    private int equippedModelMaleDialogue1;
    private int equippedModelMaleDialogue2;
    private int equippedModelFemaleDialogue1;
    private int equippedModelFemaleDialogue2;
    private int lightIntensity;
    private int lightMag;
    private int[] stackVariantID;
    private short[] modelTextureOriginal;
    private int placeholderReferenceId;
    private int placeholderTemplateId;
    private int price = -1;

    public int getPrice() {
        if (price == -1) {
            price = PriceLookup.getPrice(itemID);
        }
        return price;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getCertTemplateId() {
        return certTemplateId;
    }

    public void setCertTemplateId(int certTemplateId) {
        this.certTemplateId = certTemplateId;
    }

    public int getCertReferenceId() {
        return certReferenceId;
    }

    public void setCertReferenceId(int certReferenceId) {
        this.certReferenceId = certReferenceId;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getGroundActions() {
        return groundActions;
    }

    public void setGroundActions(String[] groundActions) {
        this.groundActions = groundActions;
    }

    public String[] getInventoryActions() {
        return inventoryActions;
    }

    public void setInventoryActions(String[] inventoryActions) {
        this.inventoryActions = inventoryActions;
    }

    public int getIsStackable() {
        return isStackable;
    }

    public void setIsStackable(int isStackable) {
        this.isStackable = isStackable;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isMembersOnly() {
        return isMembersOnly;
    }

    public void setMembersOnly(boolean membersOnly) {
        isMembersOnly = membersOnly;
    }

    public int getInventoryModelID() {
        return inventoryModelID;
    }

    public void setInventoryModelID(int inventoryModelID) {
        this.inventoryModelID = inventoryModelID;
    }

    public short[] getModelRecolorOriginal() {
        return modelRecolorOriginal;
    }

    public void setModelRecolorOriginal(short[] modelRecolorOriginal) {
        this.modelRecolorOriginal = modelRecolorOriginal;
    }

    public short[] getModelRecolorTarget() {
        return modelRecolorTarget;
    }

    public void setModelRecolorTarget(short[] modelRecolorTarget) {
        this.modelRecolorTarget = modelRecolorTarget;
    }

    public int[] getStackVariantSize() {
        return stackVariantSize;
    }

    public void setStackVariantSize(int[] stackVariantSize) {
        this.stackVariantSize = stackVariantSize;
    }

    public short[] getModelTextureTarget() {
        return modelTextureTarget;
    }

    public void setModelTextureTarget(short[] modelTextureTarget) {
        this.modelTextureTarget = modelTextureTarget;
    }

    public int getRotationLength() {
        return rotationLength;
    }

    public void setRotationLength(int rotationLength) {
        this.rotationLength = rotationLength;
    }

    public int getRotationX() {
        return rotationX;
    }

    public void setRotationX(int rotationX) {
        this.rotationX = rotationX;
    }

    public int getRotationY() {
        return rotationY;
    }

    public void setRotationY(int rotationY) {
        this.rotationY = rotationY;
    }

    public int getRotationZ() {
        return rotationZ;
    }

    public void setRotationZ(int rotationZ) {
        this.rotationZ = rotationZ;
    }

    public int getTranslateX() {
        return translateX;
    }

    public void setTranslateX(int translateX) {
        this.translateX = translateX;
    }

    public int getTranslateY() {
        return translateY;
    }

    public void setTranslateY(int translateY) {
        this.translateY = translateY;
    }

    public int getModelScaleX() {
        return modelScaleX;
    }

    public void setModelScaleX(int modelScaleX) {
        this.modelScaleX = modelScaleX;
    }

    public int getModelScaleY() {
        return modelScaleY;
    }

    public void setModelScaleY(int modelScaleY) {
        this.modelScaleY = modelScaleY;
    }

    public int getModelScaleZ() {
        return modelScaleZ;
    }

    public void setModelScaleZ(int modelScaleZ) {
        this.modelScaleZ = modelScaleZ;
    }

    public int getEquippedModelMale1() {
        return equippedModelMale1;
    }

    public void setEquippedModelMale1(int equippedModelMale1) {
        this.equippedModelMale1 = equippedModelMale1;
    }

    public int getEquippedModelMale2() {
        return equippedModelMale2;
    }

    public void setEquippedModelMale2(int equippedModelMale2) {
        this.equippedModelMale2 = equippedModelMale2;
    }

    public int getEquippedModelMale3() {
        return equippedModelMale3;
    }

    public void setEquippedModelMale3(int equippedModelMale3) {
        this.equippedModelMale3 = equippedModelMale3;
    }

    public int getEquippedModelMaleTranslationY() {
        return equippedModelMaleTranslationY;
    }

    public void setEquippedModelMaleTranslationY(int equippedModelMaleTranslationY) {
        this.equippedModelMaleTranslationY = equippedModelMaleTranslationY;
    }

    public int getEquippedModelFemale1() {
        return equippedModelFemale1;
    }

    public void setEquippedModelFemale1(int equippedModelFemale1) {
        this.equippedModelFemale1 = equippedModelFemale1;
    }

    public int getEquippedModelFemale2() {
        return equippedModelFemale2;
    }

    public void setEquippedModelFemale2(int equippedModelFemale2) {
        this.equippedModelFemale2 = equippedModelFemale2;
    }

    public int getEquippedModelFemale3() {
        return equippedModelFemale3;
    }

    public void setEquippedModelFemale3(int equippedModelFemale3) {
        this.equippedModelFemale3 = equippedModelFemale3;
    }

    public int getEquippedModelFemaleTranslationY() {
        return equippedModelFemaleTranslationY;
    }

    public void setEquippedModelFemaleTranslationY(int equippedModelFemaleTranslationY) {
        this.equippedModelFemaleTranslationY = equippedModelFemaleTranslationY;
    }

    public int getEquippedModelMaleDialogue1() {
        return equippedModelMaleDialogue1;
    }

    public void setEquippedModelMaleDialogue1(int equippedModelMaleDialogue1) {
        this.equippedModelMaleDialogue1 = equippedModelMaleDialogue1;
    }

    public int getEquippedModelMaleDialogue2() {
        return equippedModelMaleDialogue2;
    }

    public void setEquippedModelMaleDialogue2(int equippedModelMaleDialogue2) {
        this.equippedModelMaleDialogue2 = equippedModelMaleDialogue2;
    }

    public int getEquippedModelFemaleDialogue1() {
        return equippedModelFemaleDialogue1;
    }

    public void setEquippedModelFemaleDialogue1(int equippedModelFemaleDialogue1) {
        this.equippedModelFemaleDialogue1 = equippedModelFemaleDialogue1;
    }

    public int getEquippedModelFemaleDialogue2() {
        return equippedModelFemaleDialogue2;
    }

    public void setEquippedModelFemaleDialogue2(int equippedModelFemaleDialogue2) {
        this.equippedModelFemaleDialogue2 = equippedModelFemaleDialogue2;
    }

    public int getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(int lightIntensity) {
        this.lightIntensity = lightIntensity;
    }

    public int getLightMag() {
        return lightMag;
    }

    public void setLightMag(int lightMag) {
        this.lightMag = lightMag;
    }

    public int[] getStackVariantID() {
        return stackVariantID;
    }

    public void setStackVariantID(int[] stackVariantID) {
        this.stackVariantID = stackVariantID;
    }

    public short[] getModelTextureOriginal() {
        return modelTextureOriginal;
    }

    public void setModelTextureOriginal(short[] modelTextureOriginal) {
        this.modelTextureOriginal = modelTextureOriginal;
    }

    public int getPlaceholderReferenceId() {
        return placeholderReferenceId;
    }

    public void setPlaceholderReferenceId(int placeholderReferenceId) {
        this.placeholderReferenceId = placeholderReferenceId;
    }

    public int getPlaceholderTemplateId() {
        return placeholderTemplateId;
    }

    public void setPlaceholderTemplateId(int placeholderTemplateId) {
        this.placeholderTemplateId = placeholderTemplateId;
    }

    @Override
    public String toString() {
        return "ItemComposite{" +
                "itemID=" + itemID +
                ", certTemplateId=" + certTemplateId +
                ", certReferenceId=" + certReferenceId +
                ", team=" + team +
                ", name='" + name + '\'' +
                ", groundActions=" + Arrays.toString(groundActions) +
                ", inventoryActions=" + Arrays.toString(inventoryActions) +
                ", isStackable=" + isStackable +
                ", value=" + value +
                ", isMembersOnly=" + isMembersOnly +
                ", inventoryModelID=" + inventoryModelID +
                ", modelRecolorOriginal=" + Arrays.toString(modelRecolorOriginal) +
                ", modelRecolorTarget=" + Arrays.toString(modelRecolorTarget) +
                ", stackVariantSize=" + Arrays.toString(stackVariantSize) +
                ", modelTextureTarget=" + Arrays.toString(modelTextureTarget) +
                ", rotationLength=" + rotationLength +
                ", rotationX=" + rotationX +
                ", rotationY=" + rotationY +
                ", rotationZ=" + rotationZ +
                ", translateX=" + translateX +
                ", translateY=" + translateY +
                ", modelScaleX=" + modelScaleX +
                ", modelScaleY=" + modelScaleY +
                ", modelScaleZ=" + modelScaleZ +
                ", equippedModelMale1=" + equippedModelMale1 +
                ", equippedModelMale2=" + equippedModelMale2 +
                ", equippedModelMale3=" + equippedModelMale3 +
                ", equippedModelMaleTranslationY=" + equippedModelMaleTranslationY +
                ", equippedModelFemale1=" + equippedModelFemale1 +
                ", equippedModelFemale2=" + equippedModelFemale2 +
                ", equippedModelFemale3=" + equippedModelFemale3 +
                ", equippedModelFemaleTranslationY=" + equippedModelFemaleTranslationY +
                ", equippedModelMaleDialogue1=" + equippedModelMaleDialogue1 +
                ", equippedModelMaleDialogue2=" + equippedModelMaleDialogue2 +
                ", equippedModelFemaleDialogue1=" + equippedModelFemaleDialogue1 +
                ", equippedModelFemaleDialogue2=" + equippedModelFemaleDialogue2 +
                ", lightIntensity=" + lightIntensity +
                ", lightMag=" + lightMag +
                ", stackVariantID=" + Arrays.toString(stackVariantID) +
                ", modelTextureOriginal=" + Arrays.toString(modelTextureOriginal) +
                ", placeholderReferenceId=" + placeholderReferenceId +
                ", placeholderTemplateId=" + placeholderTemplateId +
                ", price=" + price +
                '}';
    }
}
