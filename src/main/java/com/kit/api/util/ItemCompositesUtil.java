package com.kit.api.util;

import com.kit.api.wrappers.ItemComposite;
import com.kit.game.cache.io.CacheLoader;
import com.kit.game.cache.io.InputStream;
import com.kit.game.cache.io.Stream;
import com.kit.util.LimitedHashMap;
import com.kit.api.wrappers.ItemComposite;
import com.kit.game.cache.io.CacheLoader;
import com.kit.game.cache.io.InputStream;
import com.kit.game.cache.io.Stream;
import com.kit.util.LimitedHashMap;

import java.util.Map;

public class ItemCompositesUtil {
    private final Map<Integer, ItemComposite> cache;
    private final CacheLoader itemCacheLoader;

    public ItemCompositesUtil(CacheLoader itemCacheLoader) {
        cache = new LimitedHashMap<>(500);
        this.itemCacheLoader = itemCacheLoader;
    }

    public ItemComposite get(final int id) {
        return get(id, true);
    }

    public ItemComposite get(final int id, boolean useCert) {
        if (id < 0) {
            return null;
        }
        ItemComposite composite = cache.get(id);
        if (composite != null) {
            return composite;
        }

        final byte[] data = itemCacheLoader.getFile(10, id);
        if (data == null) {
            return null;
        }

        composite = new ItemComposite();
        composite.setItemID(id);
        loadFromStream(new InputStream(data), composite);
        if (composite.getCertTemplateId() != -1 && useCert) {
            formNotedItem(get(composite.getCertTemplateId(), false), get(composite.getCertReferenceId(), false), composite);
        }
        cache.put(id, composite);
        return composite;
    }

    private void formNotedItem(final ItemComposite notedPaperModel, final ItemComposite unnotedItem, final ItemComposite composite) {
        if (notedPaperModel == null || unnotedItem == null) {
            return;
        }
        composite.setInventoryModelID(notedPaperModel.getInventoryModelID());
        composite.setRotationLength(notedPaperModel.getRotationLength());
        composite.setRotationX(notedPaperModel.getRotationX());
        composite.setRotationY(notedPaperModel.getRotationY());
        composite.setRotationZ(notedPaperModel.getRotationZ());
        composite.setTranslateX(notedPaperModel.getTranslateX());
        composite.setTranslateY(notedPaperModel.getTranslateY());
        composite.setModelRecolorOriginal(notedPaperModel.getModelRecolorOriginal());
        composite.setModelRecolorTarget(notedPaperModel.getModelRecolorTarget());
        composite.setModelTextureOriginal(notedPaperModel.getModelTextureOriginal());
        composite.setModelTextureTarget(notedPaperModel.getModelTextureTarget());
        composite.setName(unnotedItem.getName());
        composite.setMembersOnly(unnotedItem.isMembersOnly());
        composite.setValue(unnotedItem.getValue());
        composite.setIsStackable(1);
        unnotedItem.setCertTemplateId(composite.getItemID());
    }

    private void loadFromStream(Stream stream, ItemComposite composite) {
        while (true) {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0) {
                return;
            }
            loadFromStream(stream, opcode, composite);
        }
    }

    private void loadFromStream(Stream stream, int opcode, ItemComposite composite) {
        if (opcode == 1) {
            composite.setInventoryModelID(stream.readUnsignedShort());
        } else if (opcode == 2) {
            composite.setName(stream.readString(1297377970));
        } else if (opcode == 4) {
            composite.setRotationLength(stream.readUnsignedShort());
        } else if (opcode == 5) {
            composite.setRotationX(stream.readUnsignedShort());
        } else if (opcode == 6) {
            composite.setRotationY(stream.readUnsignedShort());
        } else if (opcode == 7) {
            composite.setTranslateX(stream.readUnsignedShort());
            if (composite.getTranslateX() > 32767) {
                composite.setTranslateX(composite.getTranslateX() - 0x10000);
            }
        } else if (opcode == 8) {
            composite.setTranslateY(stream.readUnsignedShort());
            if (composite.getTranslateY() > 32767) {
                composite.setTranslateY(composite.getTranslateY() - 0x10000);
            }
        } else if (11 == opcode) {
            composite.setIsStackable(1);
        } else if (12 == opcode) {
            composite.setValue(stream.readUnsignedInt(-1975105290));
        } else if (opcode == 16) {
            composite.setMembersOnly(true);
        } else if (23 == opcode) {
            composite.setEquippedModelMale1(stream.readUnsignedShort());
            composite.setEquippedModelMaleTranslationY(stream.readUnsignedByte());
        } else if (24 == opcode) {
            composite.setEquippedModelMale2(stream.readUnsignedShort());
        } else if (opcode == 25) {
            composite.setEquippedModelFemale1(stream.readUnsignedShort());
            composite.setEquippedModelFemaleTranslationY(stream.readUnsignedByte());
        } else if (opcode == 26) {
            composite.setEquippedModelFemale2(stream.readUnsignedShort());
        } else if (opcode >= 30 && opcode < 35) {
            if (composite.getGroundActions() == null) {
                composite.setGroundActions(new String[5]);
            }
            composite.getGroundActions()[opcode - 30] = stream.readString(1886948725);
            if (composite.getGroundActions()[opcode - 30].equalsIgnoreCase("Hidden")) {
                composite.getGroundActions()[opcode - 30] = null;
            }
        } else if (opcode >= 35 && opcode < 40) {
            if (composite.getInventoryActions() == null) {
                composite.setInventoryActions(new String[5]);
            }
            composite.getInventoryActions()[opcode - 35] = stream.readString(868536486);
        } else if (opcode == 40) {
            int amountOfColors = stream.readUnsignedByte();
            composite.setModelRecolorOriginal(new short[amountOfColors]);
            composite.setModelRecolorTarget(new short[amountOfColors]);
            for (int var5 = 0; var5 < amountOfColors; ++var5) {
                composite.getModelRecolorOriginal()[var5] = (short) stream.readUnsignedShort();
                composite.getModelRecolorTarget()[var5] = (short) stream.readUnsignedShort();
            }
        } else if (opcode == 41) {
            int var4 = stream.readUnsignedByte();
            composite.setModelTextureOriginal(new short[var4]);
            composite.setModelTextureTarget(new short[var4]);
            for (int var5 = 0; var5 < var4; ++var5) {
                composite.getModelTextureOriginal()[var5] = (short) stream.readUnsignedShort();
                composite.getModelTextureTarget()[var5] = (short) stream.readUnsignedShort();
            }
        } else if (opcode == 78) {
            composite.setEquippedModelMale3(stream.readUnsignedShort());
        } else if (79 == opcode) {
            composite.setEquippedModelFemale3(stream.readUnsignedShort());
        } else if (opcode == 90) {
            composite.setEquippedModelMaleDialogue1(stream.readUnsignedShort());
        } else if (91 == opcode) {
            composite.setEquippedModelFemaleDialogue1(stream.readUnsignedShort());
        } else if (opcode == 92) {
            composite.setEquippedModelMaleDialogue2(stream.readUnsignedShort());
        } else if (93 == opcode) {
            composite.setEquippedModelFemaleDialogue2(stream.readUnsignedShort());
        } else if (95 == opcode) {
            composite.setRotationZ(stream.readUnsignedShort());
        } else if (opcode == 97) {
            composite.setCertReferenceId(stream.readUnsignedShort());
        } else if (98 == opcode) {
            composite.setCertTemplateId(stream.readUnsignedShort());
        } else if (opcode >= 100 && opcode < 110) {
            if (composite.getStackVariantID() == null) {
                composite.setStackVariantID(new int[10]);
                composite.setStackVariantSize(new int[10]);
            }
            composite.getStackVariantID()[opcode - 100] = stream.readUnsignedShort();
            composite.getStackVariantSize()[opcode - 100] = stream.readUnsignedShort();
        } else if (opcode == 110) {
            composite.setModelScaleX(stream.readUnsignedShort());
        } else if (111 == opcode) {
            composite.setModelScaleY(stream.readUnsignedShort());
        } else if (112 == opcode) {
            composite.setModelScaleZ(stream.readUnsignedShort());
        } else if (113 == opcode) {
            composite.setLightIntensity(stream.ag());
        } else if (opcode == 114) {
            composite.setLightMag(stream.ag());
        } else if (opcode == 115) {
            composite.setTeam(stream.readUnsignedByte());
        } else if (opcode == 148) {
            composite.setPlaceholderReferenceId(stream.readUnsignedShort());
        } else if (opcode == 149) {
            composite.setPlaceholderTemplateId(stream.readUnsignedShort());
        }
    }
}
