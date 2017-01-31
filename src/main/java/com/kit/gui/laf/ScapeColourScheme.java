package com.kit.gui.laf;

import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.LightDefaultNotification;

import javax.swing.*;
import java.awt.*;

/**
 */
public class ScapeColourScheme implements ColourScheme {

    private static final INotificationStyle LIGHT_NOTIFICATION = new LightDefaultNotification();

    private final Color DARK = new Color(75, 67, 54, 255);
    private final Color LIGHT = new Color(15, 118, 94);
    private final Color BRIGHT = DARK.brighter();
    private final Color HIGHLIGHT = new Color(137, 122, 99, 255);
    private final Color SELECTED = new Color(165, 145, 113, 255);
    private final Color CLICKED = new Color(53, 53, 52);
    private final Color TEXT = new Color(230, 230, 230);

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
