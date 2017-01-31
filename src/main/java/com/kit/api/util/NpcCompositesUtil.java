package com.kit.api.util;

import com.kit.api.wrappers.NpcComposite;
import com.kit.api.wrappers.SettingsComposite;
import com.kit.core.Session;
import com.kit.game.cache.io.CacheLoader;
import com.kit.game.cache.io.InputStream;
import com.kit.api.wrappers.NpcComposite;
import com.kit.api.wrappers.SettingsComposite;
import com.kit.core.Session;
import com.kit.game.cache.io.CacheLoader;
import com.kit.game.cache.io.InputStream;
import com.kit.game.cache.io.Stream;

public class NpcCompositesUtil {
    private final CacheLoader npcCacheLoader;
    private final SettingsCompositeUtil settingsCompositeUtil;

    public NpcCompositesUtil(CacheLoader npcCacheLoader, SettingsCompositeUtil settingsCompositeUtil) {
        this.npcCacheLoader = npcCacheLoader;
        this.settingsCompositeUtil = settingsCompositeUtil;
    }

    public NpcComposite get(final int id) {
        if (id < 0) {
            return null;
        }
        byte[] data = npcCacheLoader.getFile(9, id);
        if (data == null) {
            return null;
        }

        NpcComposite composite = new NpcComposite();
        composite.setId(id);
        loadFromStream(new InputStream(data), composite);
        return composite;
    }

    public NpcComposite getChildDefinition(NpcComposite composite) {
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

    private void loadFromStream(final Stream stream, NpcComposite composite) {
        while (true) {
            int opcode = stream.readUnsignedByte();
            if (0 == opcode) {
                return;
            }
            loadFromStream(stream, opcode, composite);
        }
    }

    private void loadFromStream(final Stream stream, final int opcode, NpcComposite composite) {
        int var4;
        int var5;
        if (1 == opcode) {
            var4 = stream.readUnsignedByte();
            composite.setNpcModels(new int[var4]);
            for (var5 = 0; var5 < var4; ++var5) {
                composite.getNpcModels()[var5] = stream.readUnsignedShort();
            }
        } else if (opcode == 2) {
            composite.setName(stream.readString(1172188768));
        } else if (opcode == 12) {
            composite.setBoundDim(stream.readUnsignedByte());
        } else if (13 == opcode) {
            composite.setIdleAnimation(stream.readUnsignedShort());
        } else if (opcode == 14) {
            composite.setWalkAnimation(stream.readUnsignedShort());
        } else if (15 == opcode) {
            stream.readUnsignedShort();
        } else if (16 == opcode) {
            stream.readUnsignedShort();
        } else if (opcode == 17) {
            composite.setWalkAnimation(stream.readUnsignedShort());
            composite.setTurn180Animation(stream.readUnsignedShort());
            composite.setTurn90CWAnimation(stream.readUnsignedShort());
            composite.setTurn90CCWAnimation(stream.readUnsignedShort());
        } else if (opcode >= 30 && opcode < 35) {
            composite.getActions()[opcode - 30] = stream.readString(1452537773);
            if (composite.getActions()[opcode - 30].equalsIgnoreCase("Hidden")) {
                composite.getActions()[opcode - 30] = null;
            }
            return;
        } else if (40 == opcode) {
            var4 = stream.readUnsignedByte();
            composite.setRecolorOriginal(new short[var4]);
            composite.setRecolorTarget(new short[var4]);
            for (var5 = 0; var5 < var4; ++var5) {
                composite.getRecolorOriginal()[var5] = (short) stream.readUnsignedShort();
                composite.getRecolorTarget()[var5] = (short) stream.readUnsignedShort();
            }
        } else if (opcode == 41) {
            var4 = stream.readUnsignedByte();
            composite.setFieldF(new short[var4]);
            composite.setFieldS(new short[var4]);
            for (var5 = 0; var5 < var4; ++var5) {
                composite.getFieldF()[var5] = (short) stream.readUnsignedShort();
                composite.getFieldS()[var5] = (short) stream.readUnsignedShort();
            }
        } else if (60 == opcode) {
            var4 = stream.readUnsignedByte();
            composite.setAdditionalModels(new int[var4]);
            for (var5 = 0; var5 < var4; ++var5) {
                composite.getAdditionalModels()[var5] = (stream.readUnsignedShort());
            }
        } else if (93 == opcode) {
            composite.setDrawMinimapDot(false);
        } else if (95 == opcode) {
            composite.setCombatLevel(stream.readUnsignedShort());
        } else if (97 == opcode) {
            composite.setScaleXZ(stream.readUnsignedShort());
        } else if (opcode == 98) {
            composite.setScaleY(stream.readUnsignedShort());
        } else if (opcode == 99) {
            composite.setVisible(true);
        } else if (opcode == 100) {
            composite.setLightModifier(stream.ag());
        } else if (opcode == 101) {
            composite.setShadowModifier(stream.ag());
        } else if (102 == opcode) {
            composite.setHeadIcon(stream.readUnsignedShort());
        } else if (103 == opcode) {
            composite.setDegreesToTurn(stream.readUnsignedShort());
        } else if (106 == opcode) {
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
        } else if (opcode == 107) {
            composite.setClickable(false);
        } else if (109 == opcode) {
            // composite.setFieldAe(false);
        } else if (112 == opcode) {
            stream.readUnsignedByte();
        }
    }

}
