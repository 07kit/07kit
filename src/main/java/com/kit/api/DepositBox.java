package com.kit.api;

import com.kit.api.collection.queries.BankQuery;
import com.kit.api.wrappers.WidgetItem;

import java.util.List;

/**
 * @author : const_
 */
public interface DepositBox {

    /**
     * Checks whether the bank is open or not.
     *
     * @return return true if bank interface is up else false.
     */
    boolean isOpen();

    /**
     * Retrieves all the items in the deposit box
     *
     * @return an array containing all items in the deposit box.
     */
    List<WidgetItem> getAll();

    /**
     * Checks if the deposit box contains a specific item
     *
     * @param ids WidgetItem ids to look for
     * @return true if found, otherwise false.
     */
    boolean contains(int... ids);

    /**
     * Checks if the deposit box contains all of the specified items
     *
     * @param ids A var-args list of ids.
     * @return true if all the items were found, false otherwise.
     */
    boolean containsAll(int... ids);

    BankQuery find(int... ids);

    BankQuery find(String... names);

    /**
     * Counts all the items matching the specified ids.
     *
     * @param ids    WidgetItem ids of the items to count
     * @param stacks <t>true to count stacks</t>
     * @return total amount of items ? stacks matching id in deposit box.
     */
    int count(boolean stacks, int... ids);

    /**
     * Constructs a bank query
     *
     * @return query
     */
    BankQuery find();


    /**
     * Checks if the deposit box contains a specific item
     *
     * @param names WidgetItem names to look for
     * @return true if found, otherwise false.
     */
    boolean contains(String... names);

    /**
     * Checks if the deposit box contains all of the specified items
     *
     * @param names A var-args list of names.
     * @return true if all the items were found, false otherwise.
     */
    boolean containsAll(String... names);

    /**
     * Counts all the items matching the specified names.
     *
     * @param names  WidgetItem names of the items to count
     * @param stacks <t>true to count stacks</t>
     * @return total amount of items ? stacks matching id in deposit box.
     */
    int count(boolean stacks, String... names);
}
