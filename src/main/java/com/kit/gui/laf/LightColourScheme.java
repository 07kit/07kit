package com.kit.gui.laf;

import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.LightDefaultNotification;

import javax.swing.*;
import java.awt.*;

/**
 */
public class LightColourScheme implements ColourScheme {

    private static final INotificationStyle LIGHT_NOTIFICATION = new LightDefaultNotification();

    private final Color DARK = new Color(190, 190, 190);
    private final Color LIGHT = new Color(15, 118, 94);
    private final Color BRIGHT = new Color(225, 225, 225);
    private final Color HIGHLIGHT = new Color(24, 24, 24);
    private final Color SELECTED = new Color(255, 255, 255);
    private final Color CLICKED = new Color(53, 53, 52);
    private final Color TEXT = new Color(30, 30, 30);

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
        return LIGHT_NOTIFICATION;
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
