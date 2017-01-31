package com.kit.api.wrappers.interaction;


import com.kit.api.collection.StatePredicate;
import com.kit.api.MethodContext;
import com.kit.api.collection.StatePredicate;
import com.kit.api.util.Utilities;
import com.kit.api.MethodContext;
import com.kit.api.collection.StatePredicate;

import java.awt.Point;

/**
 * A base-class for interactable objects within the game world.
 *
 */
public abstract class Interactable {
    public MethodContext context;

    public Interactable(MethodContext context) {
        this.context = context;
    }

    public Interactable() {
    }

    /**
     * Gets the base point for the tile on which the interactable is positioned.
     *
     * @return base point
     */
    public abstract Point getBasePoint();

    /**
     * Gets the click point for the model of the interactable.
     *
     * @return click point
     */
    public abstract Point getClickPoint();

    /**
     * Checks if the interactable is still valid.
     *
     * @return valid
     */
    public abstract boolean isValid();

    /**
     * Checks if it is on screen
     *
     * @return <t>true if on screen</t> otherwise false
     */
    public boolean isOnScreen() {
        return getClickPoint() != null &&
                context.viewport.isInViewport(getClickPoint()) || getBasePoint() != null && context.viewport.isInViewport(getBasePoint());
    }

    public StatePredicate isInMenu(final String text) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return context.menu.contains(text);
            }
        };
    }

}
