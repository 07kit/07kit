package com.kit.api.util;

import com.kit.game.cache.io.CacheLoader;
import com.kit.api.wrappers.ObjectComposite;
import com.kit.api.wrappers.SettingsComposite;
import com.kit.core.Session;
import com.kit.game.cache.io.CacheLoader;
import com.kit.game.cache.io.InputStream;
import com.kit.game.cache.io.Stream;

public class ObjectCompositesUtil {
    private final CacheLoader objectCacheLoader;
    private final SettingsCompositeUtil settingsCompositeUtil;

    public ObjectCompositesUtil(CacheLoader objectCacheLoader, SettingsCompositeUtil settingsCompositeUtil) {
        this.objectCacheLoader = objectCacheLoader;
        this.settingsCompositeUtil = settingsCompositeUtil;
    }

    public ObjectComposite get(final int id) {
        if (id < 0) {
            return null;
        }

        byte[] data = objectCacheLoader.getFile(6, id);
        if (data == null) {
            return null;
        }
        ObjectComposite composite = new ObjectComposite();
        composite.setId(id);
        loadFromStream(new InputStream(data), composite);
        return composite;
    }

    public ObjectComposite getChildDefinition(ObjectComposite composite) {
        int childID = -1;
        if (composite.getVarbitID() != -1) {
            SettingsComposite settingsComposite = settingsCompositeUtil.get(composite.getVarbitID());
            if (settingsComposite == null) {
                return null;
            }
            int configID = settingsComposite.getConfigID();
            int leastSignificantBit = settingsComposite.getLeastSignificantBit();
            int mostSignificantBit = settingsComposite.getMostSignificantBit();
            int bit = SettingsComposite.BITFIELD_MAX_VALUE[mostSignificantBit - leastSignificantBit];
            childID = Session.get().settings.getWidgetSetting(configID) >> leastSignificantBit & bit;
        } else if (composite.getConfigID() != -1) {
            childID = Session.get().settings.getWidgetSetting(composite.getConfigID());
        }
        if (childID < 0 || childID >= composite.getChildIDs().length || composite.getChildIDs()[childID] == -1) {
            return null;
        }
        return get(composite.getChildIDs()[childID]);
    }

    private void loadFromStream(final Stream stream, ObjectComposite composite) {
        while (true) {
            int opcode = stream.readUnsignedByte();
            if (0 == opcode) {
                return;
            }
            loadFromStream(stream, opcode, composite);
        }
    }

    private void loadFromStream(final Stream stream, final int opcode, ObjectComposite composite) {
        int var4;
        int var5;
        if (opcode == 1) {
            var4 = stream.readUnsignedByte();
            if (var4 > 0) {
                if (composite.getObjectModelIDs() != null) {
                    stream.offset += var4 * 3;
                } else {
                    composite.setObjectModelTypes(new int[var4]);
                    composite.setObjectModelIDs(new int[var4]);

                    for (var5 = 0; var5 < var4; ++var5) {
                        composite.getObjectModelIDs()[var5] = stream.readUnsignedShort();
                        composite.getObjectModelTypes()[var5] = stream.readUnsignedByte();
                    }
                }
            }
        } else if (2 == opcode) {
            composite.setName(stream.readString(1204853826));
        } else if (5 == opcode) {
            var4 = stream.readUnsignedByte();
            if (var4 > 0) {
                if (composite.getObjectModelIDs() == null) {
                    stream.offset += var4 * 2;
                } else {
                    composite.setObjectModelTypes(null);
                    composite.setObjectModelIDs(new int[var4]);
                    for (var5 = 0; var5 < var4; ++var5) {
                        composite.getObjectModelIDs()[var5] = stream.readUnsignedShort();
                    }
                }
            }
        } else if (opcode == 14) {
            composite.setWidth(stream.readUnsignedByte());
        } else if (15 == opcode) {
            composite.setHeight(stream.readUnsignedByte());
        } else if (17 == opcode) {
            composite.setIsUnwalkableInt(0);
            composite.setBlocksProjectiles(false);
        } else if (opcode == 18) {
            composite.setBlocksProjectiles(false);
        } else if (opcode == 19) {
            composite.setHasActions(stream.readUnsignedByte());
        } else if (21 == opcode) {
            composite.setAdjustToTerrain(0);
        } else if (opcode == 22) {
            composite.setNonFlatShading(true);
        } else if (23 == opcode) {
            // composite.setFieldN(true);
        } else if (opcode == 24) {
            composite.setAnimationID(stream.readUnsignedShort());
            if (composite.getAnimationID() * -988457711 == '\uffff') {
                composite.setAnimationID(2024784911);
            }
        } else if (27 == opcode) {
            composite.setIsUnwalkableInt(-1);
        } else if (28 == opcode) {
            // composite.setFieldV(stream.readUnsignedByte((byte)) 29);
            stream.readUnsignedByte();
        } else if (opcode == 29) {
            composite.setBrightness(stream.ag());
        } else if (opcode == 39) {
            composite.setContrast(stream.ag());
        } else if (opcode >= 30 && opcode < 35) {
            composite.getActions()[opcode - 30] = stream.readString(1000472337);
            if (composite.getActions()[opcode - 30].equalsIgnoreCase("Hidden")) {
                composite.getActions()[opcode - 30] = null;
            }
        } else if (opcode == 40) {
            var4 = stream.readUnsignedByte();
            composite.setModifiedModelColors(new short[var4]);
            composite.setOriginalModelColors(new short[var4]);
            for (var5 = 0; var5 < var4; ++var5) {
                composite.getModifiedModelColors()[var5] = (short) stream.readUnsignedShort();
                composite.getOriginalModelColors()[var5] = (short) stream.readUnsignedShort();
            }
        } else if (41 == opcode) {
            var4 = stream.readUnsignedByte();
            composite.setFieldO(new short[var4]);
            composite.setFieldX(new short[var4]);
            for (var5 = 0; var5 < var4; ++var5) {
                composite.getFieldO()[var5] = (short) stream.readUnsignedShort();
                composite.getFieldX()[var5] = (short) stream.readUnsignedShort();
            }
        } else if (opcode == 60) {
            composite.setMinimapIcon(stream.readUnsignedShort());
        } else if (62 == opcode) {
            // composite.setFieldAw(true);
        } else if (64 == opcode) {
            // composite.setFieldAv(false);
        } else if (opcode == 65) {
            composite.setModelSizeX(stream.readUnsignedShort());
        } else if (opcode == 66) {
            composite.setModelSizeH(stream.readUnsignedShort());
        } else if (67 == opcode) {
            composite.setModelSizeY(stream.readUnsignedShort());
        } else if (opcode == 68) {
            composite.setMapSceneID(stream.readUnsignedShort());
        } else if (69 == opcode) {
            stream.readUnsignedByte();
        } else if (70 == opcode) {
            composite.setOffsetX(stream.aj(-1632272430));
        } else if (opcode == 71) {
            composite.setOffsetH(stream.aj(-1829119248));
        } else if (72 == opcode) {
            composite.setOffsetY(stream.aj(-1938849617));
        } else if (73 == opcode) {
            // composite.setFieldAa(true);
        } else if (opcode == 74) {
            composite.setSolidObject(true);
        } else if (75 == opcode) {
            // composite.setFieldAz(stream.readUnsignedByte((byte)) 105);
            stream.readUnsignedByte();
        } else if (77 == opcode) {
            composite.setVarbitID(stream.readUnsignedShort());
            if (composite.getVarbitID() == 65535) {
                composite.setVarbitID(-1);
            }
            composite.setConfigID(stream.readUnsignedShort());
            if (composite.getConfigID() == 65535) {
                composite.setConfigID(-1);
            }
            var4 = stream.readUnsignedByte();
            composite.setChildIDs(new int[1 + var4]);
            for (var5 = 0; var5 <= var4; ++var5) {
                composite.getChildIDs()[var5] = stream.readUnsignedShort();
                if (composite.getChildIDs()[var5] == 65535) {
                    composite.getChildIDs()[var5] = -1;
                }
            }
        } else if (78 == opcode) {
            // composite.setFieldAy(stream.readUnsignedShort((byte)) 2);
            // composite.setFieldAs(stream.readUnsignedByte((byte)) 18);
            stream.readUnsignedShort();
            stream.readUnsignedByte();
        } else if (opcode == 79) {
            // composite.fieldAg =
            stream.readUnsignedShort();
            // composite.fieldAc =
            stream.readUnsignedShort();
            // composite.fieldAs =
            stream.readUnsignedByte();
            var4 = stream.readUnsignedByte();
            // composite.setFieldAj(new int[var4]);

            for (var5 = 0; var5 < var4; ++var5) {
                // composite.getFieldAj()[var5] =
                stream.readUnsignedShort();
            }
        } else if (81 == opcode) {
            composite.setAdjustToTerrain(stream.readUnsignedByte());
        }

    }
}
