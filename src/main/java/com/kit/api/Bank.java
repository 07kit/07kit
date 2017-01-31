package com.kit.api;

import com.kit.api.collection.queries.BankQuery;
import com.kit.api.impl.tabs.BankImpl;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.api.wrappers.WidgetItem;

import java.util.List;

/**
 * @author const_
 */
public interface Bank {

    /**
     * Gets the amount of items in the bank
     * @return amt of items in bank
     */
    int getSize();

    /**
     * Checks whether the bank is open or not.
     *
     * @return return true if bank interface is up else false.
     */
    boolean isOpen();

    /**
     * Retrieves all the items in the Bank
     *
     * @return an array containing all items in the Bank.
     */
    List<WidgetItem> getAll();

    /**
     * Checks if the Bank contains a specific item
     *
     * @param ids WidgetItem ids to look for
     * @return true if found, otherwise false.
     */
    boolean contains(int... ids);

    /**
     * Checks if the Bank contains all of the specified items
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
     * @return total amount of items ? stacks matching id in Bank.
     */
    int count(boolean stacks, int... ids);

    BankQuery find();


    /**
     * Checks if the Bank contains a specific item
     *
     * @param names WidgetItem names to look for
     * @return true if found, otherwise false.
     */
    boolean contains(String... names);

    /**
     * Checks if the Bank contains all of the specified items
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
     * @return total amount of items ? stacks matching id in Bank.
     */
    int count(boolean stacks, String... names);

    /**
     * Gets the bank widget
     * @return widget
     */
    Widget getWidget();

    /**
     * Gets the amount of tabs, 0 if none
     *
     * @return tab count
     */
    int getTabCount();

    /**
     * Gets the current open bank tab
     *
     * @return bank tab
     */
    BankImpl.Tab getCurrent();

    /**
     * Checks if a specified tab is the current tab
     *
     * @param tab the tab to check if is open
     * @return <t>true if the tab is open</t> otherwise false
     */
    boolean isOpen(BankImpl.Tab tab);

    Widget getTitleWidget();

    List<WidgetItem> getItemsForTab(BankImpl.Tab tab);
}
