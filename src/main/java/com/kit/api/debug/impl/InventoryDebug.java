package com.kit.api.debug.impl;

import com.kit.api.event.EventHandler;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.Session;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.WidgetItem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 */
public class InventoryDebug extends AbstractDebug {

    public InventoryDebug() {
        super();
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.ORANGE);
        if (ctx().isLoggedIn()) {
            for (WidgetItem item : ctx().inventory.getAll()) {
                Rectangle area = item.getArea();
                g.drawString(String.valueOf(item.getId()), area.x, area.y);
            }
        }
    }


    @Override
    public String getName() {
        return "Inventory";
    }

    @Override
    public String getShortcode() {
        return "inventory";
    }
}
