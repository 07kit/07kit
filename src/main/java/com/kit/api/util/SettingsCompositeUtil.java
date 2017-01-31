package com.kit.api.util;

import com.kit.api.wrappers.SettingsComposite;
import com.kit.game.cache.io.CacheLoader;
import com.kit.game.cache.io.InputStream;
import com.kit.game.cache.io.Stream;
import com.kit.api.wrappers.SettingsComposite;
import com.kit.game.cache.io.CacheLoader;
import com.kit.game.cache.io.InputStream;
import com.kit.game.cache.io.Stream;

public final class SettingsCompositeUtil {
    private final CacheLoader cacheLoader;

    public SettingsCompositeUtil(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    public SettingsComposite get(int id) {
        byte[] data = cacheLoader.getFile(14, id);
        if (data == null) {
            return null;
        }
        SettingsComposite composite = new SettingsComposite();
        composite.setId(id);
        loadFromStream(new InputStream(data), composite);
        return composite;
    }

    private void loadFromStream(Stream stream, SettingsComposite composite) {
        while (true) {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0) {
                return;
            }
            loadFromStream(stream, opcode, composite);
        }
    }

    private void loadFromStream(Stream stream, int opcode, SettingsComposite composite) {
        if (opcode == 1) {
            composite.setConfigID(stream.readUnsignedShort());
            composite.setLeastSignificantBit(stream.readUnsignedByte());
            composite.setMostSignificantBit(stream.readUnsignedByte());
        }
    }
}
