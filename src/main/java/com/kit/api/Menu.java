package com.kit.api;

import java.awt.*;
import java.util.List;

/**
 * Author: const_
 */
public interface Menu {

    /**
     * Gets the index of a menu item in the list of menu items
     *
     * @param action The item to look for
     * @return The index of the item if it was found, otherwise -1.
     */
    int getIndex(String action);

    /**
     * Gets the menu bounds
     *
     * @return menu bounds
     */
    Rectangle getBounds();

    /**
     * Checks if the menu is open
     *
     * @return true if the menu is open
     */
    boolean isOpen();


    /**
     * Checks if the current menu content includes a specific action.
     *
     * @param action Action text (may be a regular expression, too!)
     * @return true if present
     */
    boolean contains(String action);

    /**
     * Gets the menu item that is in the current "up text" / will be invoked
     * when the user clicks with the current game state and current mouse position.
     *
     * @return hover action
     */
    String getHoverAction();

    /**
     * Retrieves a list of all menu content.
     *
     * @return menu content.
     */
    List<String> getLines();

    /**
     * Retrieves a list of all menu content.
     *
     * @return menu content.
     */
    List<String> getActions();

    /**
     * Retrieves a list of all menu content.
     *
     * @return menu content.
     */
    List<String> getOptions();

}
