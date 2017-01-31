package com.kit.gui.laf;

import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.DarkDefaultNotification;
import com.kit.Application;

import javax.swing.*;
import java.awt.*;

/**
 */
public class DefaultColourScheme implements ColourScheme {

    private static final INotificationStyle DARK_NOTIFICATION = new DarkDefaultNotification();

    private final Color BRIGHT = new Color(57, 57, 57);
    private final Color DARK = new Color(33, 33, 33);
    private final Color LIGHT = new Color(15, 118, 94);
    private final Color HIGHLIGHT = new Color(70, 70, 70);
    private final Color SELECTED = new Color(60, 60, 60);
    private final Color CLICKED = new Color(60, 60, 60);
    private final Color TEXT = new Color(252, 238, 137);

    @Override
    public Color getDark() {
        return DARK;
    }

    @Override
    public Color getClicked() {
        return CLICKED;
    }

    @Override
    public INotificationStyle getNotificationStyle() {
        return DARK_NOTIFICATION;
    }

    @Override
    public Color getBright() {
        return BRIGHT;
    }

    @Override
    public Color getHighlight() {
        return HIGHLIGHT;
    }

    @Override
    public Color getSelected() {
        return SELECTED;
    }

    @Override
    public Color getLight() {
        return LIGHT;
    }

    @Override
    public Color getText() {
        return TEXT;
    }

    @Override
    public void init() {
        UIManager.put("MenuBar.background", Color.RED);
        UIManager.put("Menu.background", Color.GREEN);
        UIManager.put("MenuItem.background", Color.MAGENTA);
        UIManager.put("MenuItem.selectionBackground", getHighlight());

    }
}
