package com.kit.api.util;

import java.awt.*;

/**
 */
public final class ColorUtils {

    ColorUtils() {

    }

    public static Color setOpacity(Color color, int opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

}
