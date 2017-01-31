package com.kit.api.collection.queries;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.kit.api.collection.Filter;
import com.kit.api.collection.Filter;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

/**
 * An abstract base class for queries
 *
 */
public abstract class Query<T> implements Iterable<T> {
    static final Map<String, Query> prepared = new HashMap<String, Query>();
    List<Comparator<T>> orderings = new ArrayList<Comparator<T>>();
    List<Filter<T>> filters = new ArrayList<Filter<T>>();

    @Override
    public Iterator<T> iterator() {
        return asList().iterator();
    }

    void addOrdering(Comparator<T> ordering) {
        orderings.add(ordering);
    }

    void addCondition(Filter<T> filter) {
        filters.add(filter);
    }

    void removeCondition(Filter<T> filter) {
        filters.remove(filter);
    }

    @SuppressWarnings("unchecked")
    List<T> filterSet(Collection<T> list) {
        for (Filter filter : filters) {
            list = Collections2.filter(list, filterWrap(filter));
        }
        return newArrayList(list);
    }

    List<T> orderSet(List<T> input) {
        List<T> listWrap = newArrayList(input);
        for (Comparator<T> comparator : orderings) {
            Collections.sort(listWrap, comparator);
        }
        return listWrap;
    }

    public <Q extends Query<T>> Q orderWith(Comparator<T> comparator) {
        orderings.add(comparator);
        return (Q) this;
    }

    /**
     * Retrieves a single item from the queried collection.
     *
     * @return single item or null if none found.
     */
    public abstract T single();

    /**
     * Retrieves all matching results from the queried collection.
     *
     * @return list of results, which may have a size of 0 if no matches were found.
     */
    public abstract List<T> asList();

    /**
     * Appends a custom filter to the query list
     *
     * @param filter custom filter
     * @return query
     */
    public <Q extends Query<T>> Q filter(Filter<T> filter) {
        addCondition(filter);
        return (Q) this;
    }

    public List<Filter<T>> getFilters() {
        return filters;
    }

    public boolean exists() {
        return asList().size() > 0;
    }

    private Predicate<T> filterWrap(final Filter<T> filter) {
        return new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return t != null && filter.accept(t);
            }
        };
    }

    public void storeAs(String name) {
        if (!prepared.containsKey(name)) {
            prepared.put(name, this);
        }
    }

    Query retrieveFromStorage(String name) {
        if (prepared.containsKey(name)) {
            return prepared.get(name);
        }
        throw new IllegalStateException("Query '" + name + "' not found in phase, call storeAs(String name) first.");
    }

}
