package com.kit.api.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kit.api.wrappers.ItemComposite;
import com.kit.api.wrappers.SettingsComposite;
import com.kit.game.cache.io.ClientCache;
import org.apache.log4j.Logger;
import com.kit.api.wrappers.NpcComposite;
import com.kit.api.wrappers.ObjectComposite;
import com.kit.core.Session;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

/**
 */
public final class CacheService {
    private static final CacheService instance = new CacheService();
    private final Path kitCacheDir = Paths.get(System.getProperty("user.home"))
            .resolve("07Kit").resolve(".cache");
    private final Path itemCacheDir = kitCacheDir.resolve("items");
    private final Path npcCacheDir = kitCacheDir.resolve("npcs");
    private final Path objectCacheDir = kitCacheDir.resolve("objects");
    private final Path settingsCacheDir = kitCacheDir.resolve("settings");


    private final LoadingCache<Integer, ItemComposite> itemCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.MINUTES).build(cacheLoader(itemCacheDir));
    private final LoadingCache<Integer, ObjectComposite> objectCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.MINUTES).build(cacheLoader(objectCacheDir));
    private final LoadingCache<Integer, NpcComposite> npcCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.MINUTES).build(cacheLoader(npcCacheDir));
    private final LoadingCache<Integer, SettingsComposite> settingsCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.MINUTES).build(cacheLoader(settingsCacheDir));
    private final Logger logger = Logger.getLogger(getClass());

    private CacheService() {

    }

    public void init() {
        try {
            logger.info("Creating data directories...");
            Files.createDirectories(kitCacheDir);
            Files.createDirectories(itemCacheDir);
            Files.createDirectories(npcCacheDir);
            Files.createDirectories(objectCacheDir);
            Files.createDirectories(settingsCacheDir);
            logger.info("Loading client cache...");
            String cacheDir = ClientCache.findCacheDirectory(System.getProperty("user.home"));
            ClientCache clientCache = new ClientCache(cacheDir, "main_file_cache");
            logger.info("Dumping item definitions...");
            for (int id = 0; id < 100000; id++) {
                ItemComposite itemComposite = clientCache.getItemCompositesUtil().get(id);
                if (itemComposite != null
                        && itemComposite.getName() != null
                        && !itemComposite.getName().equalsIgnoreCase("null")) {
                    ObjectOutputStream oos = new ObjectOutputStream(newOutputStream(itemCacheDir.resolve(id + ".dat")));
                    oos.writeObject(itemComposite);
                    oos.flush();
                    oos.close();
                }
            }
            logger.info("Dumping NPC definitions...");
            for (int id = 0; id < 100000; id++) {
                NpcComposite npcComposite = clientCache.getNpcCompositesUtil().get(id);
                if (npcComposite != null
                        && npcComposite.getName() != null
                        && !npcComposite.getName().equalsIgnoreCase("null")) {
                    ObjectOutputStream oos = new ObjectOutputStream(newOutputStream(npcCacheDir.resolve(id + ".dat")));
                    oos.writeObject(npcComposite);
                    oos.flush();
                    oos.close();
                }
            }
            logger.info("Dumping object  definitions...");
            for (int id = 0; id < 100000; id++) {
                ObjectComposite objectComposite = clientCache.getObjectCompositesUtil().get(id);
                if (objectComposite != null
                        && objectComposite.getName() != null
                        && !objectComposite.getName().equalsIgnoreCase("null")) {
                    ObjectOutputStream oos = new ObjectOutputStream(newOutputStream(objectCacheDir.resolve(id + ".dat")));
                    oos.writeObject(objectComposite);
                    oos.flush();
                    oos.close();
                }
            }
            logger.info("Dumping setting definitions...");
            for (int id = 0; id < 100000; id++) {
                SettingsComposite settingsComposite = clientCache.getSettingsCompositeUtil().get(id);
                if (settingsComposite != null) {
                    ObjectOutputStream oos = new ObjectOutputStream(newOutputStream(settingsCacheDir.resolve(id + ".dat")));
                    oos.writeObject(settingsComposite);
                    oos.flush();
                    oos.close();
                }
            }
            logger.info("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> CacheLoader<Integer, T> cacheLoader(final Path dir) {
        return new CacheLoader<Integer, T>() {
            @Override
            public T load(Integer id) throws Exception {
                try (ObjectInputStream ois = new ObjectInputStream(newInputStream(dir.resolve(id + ".dat")))) {
                    return (T) ois.readObject();
                }
            }
        };
    }

    public ObjectComposite getObjectComposite(int id) {
        try {
            return objectCache.get(id);
        } catch (ExecutionException e) {
            logger.debug("Failed to load object composite.", e);
            return null;
        }
    }

    public NpcComposite getNpcComposite(int id) {
        try {
            return npcCache.get(id);
        } catch (ExecutionException e) {
            logger.debug("Failed to load npc composite.", e);
            return null;
        }
    }

    public NpcComposite getChildNpcComposite(NpcComposite composite) {
        try {
            int childID = -1;
            if (composite.getVarbitID() != -1) {
                SettingsComposite settingsComposite = getSettingsComposite(composite.getVarbitID());
                if (settingsComposite == null) {
                    logger.info("No varbit comp " + composite.getVarbitID());
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
            return getNpcComposite(composite.getChildIDs()[childID]);
        } catch (Exception e) {
            logger.debug("Failed to load child composite.", e);
            return null;
        }
    }

    public ItemComposite getItemComposite(int id) {
        try {
            return itemCache.get(id);
        } catch (ExecutionException e) {
            logger.debug("Failed to load item composite.", e);
            return null;
        }
    }

    public SettingsComposite getSettingsComposite(int id) {
        try {
            return settingsCache.get(id);
        } catch (ExecutionException e) {
            logger.debug("Failed to load settings composite.", e);
            return null;
        }
    }

    public static CacheService get() {
        return instance;
    }
}
