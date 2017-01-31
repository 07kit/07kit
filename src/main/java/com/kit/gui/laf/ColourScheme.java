package com.kit.gui.laf;

import ch.swingfx.twinkle.style.INotificationStyle;

import java.awt.*;

/**
 */
public interface ColourScheme {

    Color getDark();

    Color getBright();

    Color getHighlight();

    Color getSelected();

    Color getLight();

    Color getText();

    void init();

    Color getClicked();

    INotificationStyle getNotificationStyle();

}
