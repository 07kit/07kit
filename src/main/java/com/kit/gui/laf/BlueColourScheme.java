package com.kit.gui.laf;

import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.DarkDefaultNotification;

import javax.swing.*;
import java.awt.*;

/**
 */
public class BlueColourScheme implements ColourScheme {

    private static final INotificationStyle DARK_NOTIFICATION = new DarkDefaultNotification();

    private final Color DARK = new Color(35, 43, 51);
    private final Color LIGHT = new Color(15, 118, 94);
    private final Color BRIGHT = DARK.brighter();
    private final Color HIGHLIGHT = new Color(5, 127, 253);
    private final Color SELECTED = new Color(27, 133, 243);
    private final Color CLICKED = new Color(46, 76, 255);
    private final Color TEXT = new Color(255, 255, 255);

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
