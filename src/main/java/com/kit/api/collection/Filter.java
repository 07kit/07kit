package com.kit.api.collection;

import java.util.List;
import java.util.function.Predicate;

/**
 * A filter interface for generic types
 *
 */
public interface Filter<T> {

    /**
     * Filter function
     *
     * @param acceptable an item that can be filtered
     * @return true if item should be retained, false otherwise.
     */
    boolean accept(T acceptable);

    static <T> Filter<T> collapse(List<Filter<T>> filters) {
        return acceptable -> {
            for (Filter<T> filter : filters) {
                if (!filter.accept(acceptable)) {
                    return false;
                }
            }
            return true;
        };
    }

}
