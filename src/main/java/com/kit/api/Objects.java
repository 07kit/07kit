package com.kit.api;

import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.GameObjectQuery;
import com.kit.api.wrappers.GameObject;
import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.GameObjectQuery;

import java.util.List;

/**
 * A method class for object interaction
 *
 */
public interface Objects {

    /**
     * a method which gets all the {@link GameObject} in the current region
     *
     * @return an array of {@link GameObject} in the current region
     */
    List<GameObject> getAll();

    /**
     * Constructs a query for finding objects
     *
     * @return query
     */
    GameObjectQuery find();

    /**
     * Constructs a query for finding objects using the IDs
     *
     * @return query
     */
    GameObjectQuery find(int... ids);

    GameObjectQuery find(String... names);

    List<GameObject> getFiltered(List<Filter<GameObject>> filters);
}
