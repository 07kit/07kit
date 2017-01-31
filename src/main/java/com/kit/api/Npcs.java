package com.kit.api;

import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.NpcQuery;
import com.kit.api.wrappers.Npc;
import com.kit.api.wrappers.NpcInfo;
import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.NpcQuery;
import com.kit.api.wrappers.Npc;
import com.kit.api.wrappers.NpcInfo;

import java.util.Collection;
import java.util.List;

/**
 */
public interface Npcs {

    /**
     * Gets all loaded NPCs in the region.
     *
     * @return npcs
     */
    List<Npc> getAll();

    /**
     * Creates a new NPC query.
     *
     * @return query
     */
    NpcQuery find();

    /**
     * Creates an NPC query using IDs for matching.
     *
     * @param ids ids
     * @return query
     */
    NpcQuery find(Collection<Integer> ids);

    /**
     * Creates an NPC query using IDs for matching.
     *
     * @param ids ids
     * @return query
     */
    NpcQuery find(int... ids);

    NpcInfo getInfo(Npc npc);

    /**
     * Creates an NPC query using name for matching.
     *
     * @param names names
     * @return query
     */
    NpcQuery find(String... names);

    List<Npc> getFiltered(List<Filter<Npc>> filters);
}
