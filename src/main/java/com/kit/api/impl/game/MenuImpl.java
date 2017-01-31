package com.kit.api.impl.game;


import com.kit.api.*;
import com.kit.api.collection.StatePredicate;
import com.kit.api.Menu;
import com.kit.api.MethodContext;
import com.kit.api.collection.StatePredicate;
import com.kit.api.util.Utilities;
import com.kit.api.collection.StatePredicate;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Author: const_
 */
public class MenuImpl implements com.kit.api.Menu {
    private final MethodContext ctx;


    public MenuImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex(String action) {
        action = action.toLowerCase();

        for (String entry : getActions()) {
            Matcher matcher = Pattern.compile(action.toLowerCase()).matcher(entry);
            if (matcher.find()) {
                return getActions().indexOf(entry);
            }
        }

        for (String entry : getLines()) {
            Matcher matcher = Pattern.compile(action.toLowerCase()).matcher(entry);
            if (matcher.find()) {
                return getLines().indexOf(entry);
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(ctx.client().getMenuX(), ctx.client().getMenuY(),
                ctx.client().getMenuWidth(), ctx.client().getMenuHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return ctx.client().getMenuOpen();
    }

   /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(String action) {
        return getIndex(action) != -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHoverAction() {
        return getActions().get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getLines() {
        String[] actions = ctx.client().getMenuOptions();
        String[] targets = ctx.client().getMenuActions();
        List<String> menuContent = newArrayList();
        for (int i = ctx.client().getMenuCount() - 1; i >= 0; i--) {
            if (actions[i] != null && targets[i] != null) {
                menuContent.add(format(actions[i] + " " + targets[i]));
            }
        }
        return menuContent;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getOptions() {
        String[] actions = ctx.client().getMenuOptions();
        List<String> menuContent = newArrayList();
        for (int i = ctx.client().getMenuCount() - 1; i >= 0; i--) {
            if (actions[i] != null) {
                menuContent.add(format(actions[i]));
            }
        }
        return menuContent;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getActions() {
        String[] actions = ctx.client().getMenuActions();
        List<String> menuContent = newArrayList();
        for (int i = ctx.client().getMenuCount() - 1; i >= 0; i--) {
            if (actions[i] != null) {
                menuContent.add(format(actions[i]));
            }
        }
        return menuContent;
    }

    /**
     * A simple helper method which standardizes the naming of menu items
     * Also prevents nullpointers
     *
     * @param input the input string
     * @return the formatted string
     */
    private String format(String input) {
        return input != null ? Pattern.compile("<.+?>").matcher(input).replaceAll("") : "null";
    }

    private final StatePredicate menuOpen = new StatePredicate() {
        @Override
        public boolean apply() {
            return isOpen();
        }
    };

    private final StatePredicate menuClosed = new StatePredicate() {
        @Override
        public boolean apply() {
            return !isOpen();
        }
    };

    private StatePredicate containsPred(final String args) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return contains(args);
            }
        };
    }
}
