package com.kit.api;

import com.kit.api.wrappers.Tab;

/**
 * @author tobiewarburton
 */
public interface Tabs {

    /**
     * checks if the specified tab is open
     *
     * @param tab the tab to check if it's selected
     * @return true if tab is selected else false
     */
    boolean isOpen(Tab tab);

    /**
     * gets the currently selected tab
     *
     * @return the current selected tab
     */
    Tab getCurrent();
}
