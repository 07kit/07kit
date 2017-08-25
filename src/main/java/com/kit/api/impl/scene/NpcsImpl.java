package com.kit.api.impl.scene;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kit.api.MethodContext;
import com.kit.api.Npcs;
import com.kit.api.wrappers.NpcInfo;
import com.kit.core.Session;
import com.kit.game.engine.renderable.entity.INpc;
import org.apache.http.client.fluent.Executor;
import com.kit.api.MethodContext;
import com.kit.api.Npcs;
import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.NpcQuery;
import com.kit.api.wrappers.GameObject;
import com.kit.api.wrappers.Npc;
import com.kit.api.wrappers.NpcInfo;
import com.kit.core.Session;
import com.kit.game.engine.renderable.entity.INpc;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import com.kit.api.MethodContext;
import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.NpcQuery;
import com.kit.api.wrappers.NpcInfo;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.util.HttpUtil;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Lists.newArrayList;

/**
 */
public class NpcsImpl implements Npcs {

    public static final Gson GSON = new Gson();

    public static final String API_URL = "https://api.07kit.com/npc/lookup/";

    public static final String AUTH_HEADER_KEY = "Authorization";

    private static NpcInfoKey currentGet;

    private class NpcInfoKey {
        private int combatLevel;
        private String name;

        public NpcInfoKey(int combatLevel, String name) {
            this.combatLevel = combatLevel;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NpcInfoKey that = (NpcInfoKey) o;

            if (combatLevel != that.combatLevel) return false;
            return name != null ? name.equals(that.name) : that.name == null;

        }

        @Override
        public int hashCode() {
            int result = combatLevel;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

    private static final LoadingCache<NpcInfoKey, NpcInfo> NPC_INFO_CACHE = CacheBuilder.newBuilder()
            .maximumSize(250)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<NpcInfoKey, NpcInfo>() {

                        @Override
                        public NpcInfo load(NpcInfoKey key) throws Exception {
                            currentGet = key;
                            Request request = Request.Get(API_URL + "name/" + key.name.replaceAll(" ", "%20") + "/" + key.combatLevel)
                                    .addHeader(AUTH_HEADER_KEY, "Bearer " + Session.get().getApiToken());
                            HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();
                            if (response != null) {
                                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                    byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                                    currentGet = null;
                                    return GSON.fromJson(new String(bytes), NpcInfo.class);
                                }
                            }
                            currentGet = null;
                            return null;
                        }
                    }
            );

    private final Logger logger = Logger.getLogger(getClass());

    private final MethodContext ctx;
    private final ExecutorService infoGetterService = Executors.newSingleThreadExecutor();

    public NpcsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Npc> getAll() {
        List<Npc> npcs = newArrayList();
        INpc[] npcArray = ctx.client().getNpcs();
        for (INpc npc : npcArray) {
            if (npc == null) {
                continue;
            }
            npcs.add(npc.getWrapper());
        }
        return npcs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NpcQuery find() {
        return new NpcQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NpcQuery find(Collection<Integer> ids) {
        return find().id(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NpcQuery find(int... ids) {
        return find().id(ids);
    }

    @Override
    public NpcInfo getInfo(Npc npc) {
        try {
            if (npc.getComposite() == null) {
                return null;
            }
            NpcInfoKey key = new NpcInfoKey(npc.getComposite().getCombatLevel(), npc.getName());

            NpcInfo info = NPC_INFO_CACHE.getIfPresent(key);

            if (info == null && (currentGet == null || !currentGet.equals(key))) {
                infoGetterService.submit((Runnable) () -> NPC_INFO_CACHE.refresh(key));
                return null;
            }
            return info;
        } catch (Exception e) {
            logger.error("Error looking up npc [" + npc.getName() + "]", e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NpcQuery find(String... names) {
        return find().named(names);
    }

    @Override
    public List<Npc> getFiltered(List<Filter<Npc>> filters) {
        Filter<Npc> filter = Filter.collapse(filters);
        List<Npc> npcs = newArrayList();
        INpc[] npcArray = ctx.client().getNpcs();
        for (INpc npc : npcArray) {
            if (npc == null) {
                continue;
            }
            Npc wrapped = npc.getWrapper();
            if (filter.accept(wrapped)) {
                npcs.add(wrapped);
            }
        }
        return npcs;
    }
}
