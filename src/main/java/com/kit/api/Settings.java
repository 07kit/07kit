package com.kit.api;

/**
 * Author: const_
 */
public interface Settings {

    /**
     * Gets all the widget settings.
     *
     * @return int array of all the clients Widget settings
     */
    int[] getWidgetSettings();

    /**
     * Retrieves a widget setting at the supplied index
     *
     * @param index index of the setting
     * @return setting value
     */
    int getWidgetSetting(int index);

    /**
     * Retrieves a client setting at the supplied index
     *
     * @param index index of the setting
     * @return setting value
     */
    int getPlayerSetting(int index);

    /**
     * Gets all the player settings.
     *
     * @return int array of all the clients Player settings
     */
    int[] getPlayerSettings();
}
