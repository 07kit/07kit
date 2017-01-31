package com.kit.api;

import com.kit.api.wrappers.Prayer;
import com.kit.api.wrappers.Prayer;

import java.util.List;

/**
 * Access to prayers
 *
 * @author tommo
 */
public interface Prayers {

    /**
     * Returns the player's current prayer points
     *
     * @return The amount of prayer points
     */
    int getPrayerPoints();

    /**
     * Returns a list of all currently activated prayers
     *
     * @return The list of active prayers
     */
    List<Prayer> getActivatedPrayers();

    /**
     * Checks if the player has a specified prayer activated
     *
     * @param prayer The prayer to check
     * @return <b>true</b> if the prayer is activated, <b>false</b> if not
     */
    boolean isActivated(Prayer prayer);

    /**
     * Checks if player has the required prayer level to activate a specified prayer.
     *
     * @param prayer The prayer to check
     * @return <b>true</b> if the player <i>can</i> activate the prayer, <b>false</b> if <i>not</i>
     */
    boolean canActivate(Prayer prayer);

}
