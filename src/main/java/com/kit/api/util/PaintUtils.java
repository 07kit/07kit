package com.kit.api.util;

import java.awt.*;

/**
 */
public final class PaintUtils {

    private PaintUtils() {

    }

    public static void drawString(Graphics2D gfx, String text, int x, int y) {
        Color color = gfx.getColor();
        /// Don't draw a shadow if the text is really dark.
        if (gfx.getColor().getRed() >= 33 || gfx.getColor().getBlue() >= 33 || gfx.getColor().getGreen() >= 33) {
            gfx.setColor(Color.BLACK); // Shadow
            gfx.drawString(text, x + 1, y + 1);
        }
        gfx.setColor(color);
        gfx.drawString(text, x, y);
    }

}
