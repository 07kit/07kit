package com.kit.api.util;

import java.util.List;

/**
 */
public final class Paginated<T> {
    private long maxRecords;
    private long records;
    private List<T> items;

    /**
     * Constructor
     */
    public Paginated() {

    }

    /**
     * Constructor
     *
     * @param maxRecords will be the amount of items in the table
     * @param records    number of records that are returned with a query
     * @param items      returning from a query
     */
    public Paginated(long maxRecords, long records, List<T> items) {
        this.maxRecords = maxRecords;
        this.records = records;
        this.items = items;
    }

    public long getMaxRecords() {
        return maxRecords;
    }

    public void setMaxRecords(long maxRecords) {
        this.maxRecords = maxRecords;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}