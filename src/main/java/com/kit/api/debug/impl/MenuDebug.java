package com.kit.api.debug.impl;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.core.Session;

import java.awt.*;

/**
 */
public class MenuDebug extends AbstractDebug {
    public MenuDebug() {
        super();
    }

    @Override
    public String getName() {
        return "Menu";
    }

    @Override
    public String getShortcode() {
        return "menu";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics graphics = event.getGraphics();
        graphics.setColor(Color.YELLOW);
        graphics.drawRect(ctx().client().getMenuX(), ctx().client().getMenuY(), 5, 5);
        for (int i = 0; i < ctx().menu.getLines().size(); i++) {
            graphics.drawString("Menu #" + i + ": " + ctx().menu.getLines().get(i), 10, 50 + (30 * i));
        }

    }
}
