package com.kit.api;

import com.google.common.base.Predicate;
import com.kit.api.wrappers.Loot;
import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.LootQuery;
import com.kit.api.wrappers.Loot;
import com.kit.api.collection.queries.LootQuery;
import com.kit.api.wrappers.Loot;

import java.util.List;

/**
 * Author: const_
 * Author: Cov
 */
public interface Loots {

    /**
     * Gets all the ground items within the loaded region
     *
     * @return an array containing all ground items in loaded region
     */
    List<Loot> getAll();

    List<Loot> getAll(Filter<Loot> predicate);

    /**
     * Constructs a query for finding ground items
     *
     * @return query
     */
    LootQuery find();

    LootQuery find(String... names);

    LootQuery find(int... ids);
}
