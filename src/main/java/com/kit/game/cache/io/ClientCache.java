package com.kit.game.cache.io;

import com.kit.api.util.ObjectCompositesUtil;
import com.kit.api.util.ItemCompositesUtil;
import com.kit.api.util.NpcCompositesUtil;
import com.kit.api.util.ObjectCompositesUtil;
import com.kit.api.util.SettingsCompositeUtil;

import java.io.File;
import java.io.RandomAccessFile;

public class ClientCache {
    private final ItemCompositesUtil itemCompositesUtil;
    private final NpcCompositesUtil npcCompositesUtil;
    private final ObjectCompositesUtil objectCompositesUtil;
    private final SettingsCompositeUtil settingsCompositeUtil;


    public ClientCache(final String cachePath, final String cacheName) throws Exception {
        RandomAccessFile mainData = new RandomAccessFile(cachePath + File.separator + cacheName + ".dat2", "rw");
        Decompressor idx255 = new Decompressor(255, mainData, new RandomAccessFile(cachePath + cacheName + ".idx255", "r"));
        int idxsCount = idx255.getArchivesCount();
        CacheLoader[] indexFiles = new CacheLoader[idxsCount];
        for (int id = 0; id < idxsCount; id++) {
            CacheLoader index = new CacheLoader(idx255, new Decompressor(id, mainData, new RandomAccessFile(cachePath + cacheName + ".idx" + id, "r")));
            if (index.getTable() == null) {
                continue;
            }
            indexFiles[id] = index;
        }

        settingsCompositeUtil = new SettingsCompositeUtil(indexFiles[2]);
        itemCompositesUtil = new ItemCompositesUtil(indexFiles[2]);
        npcCompositesUtil = new NpcCompositesUtil(indexFiles[2], settingsCompositeUtil);
        objectCompositesUtil = new ObjectCompositesUtil(indexFiles[2], settingsCompositeUtil);
    }

    public ItemCompositesUtil getItemCompositesUtil() {
        return itemCompositesUtil;
    }

    public NpcCompositesUtil getNpcCompositesUtil() {
        return npcCompositesUtil;
    }

    public ObjectCompositesUtil getObjectCompositesUtil() {
        return objectCompositesUtil;
    }

    public SettingsCompositeUtil getSettingsCompositeUtil() {
        return settingsCompositeUtil;
    }

    public static final String findCacheDirectory(final String rsLoadedHome) {
        final String home = rsLoadedHome + File.separator;
        final String cacheLocation = home + "jagexcache" + File.separator + "oldschool" + File.separator + "LIVE" + File.separator;
        return cacheLocation;
    }

}