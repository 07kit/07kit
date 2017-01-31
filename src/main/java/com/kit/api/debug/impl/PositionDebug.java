package com.kit.api.debug.impl;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.wrappers.Tile;
import com.kit.api.event.EventHandler;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.Tile;

import java.awt.Color;
import java.awt.Graphics;

/**
 */
public class PositionDebug extends AbstractDebug {

    public PositionDebug() {
        super();
    }

    @Override
    public String getName() {
        return "Position";
    }

    @Override
    public String getShortcode() {
        return "position";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        try {
            Graphics g = event.getGraphics();
            g.setColor(Color.RED);
            if (ctx().isLoggedIn()) {
                g.drawString("Tile: " + ctx().player.getTile().toString(), 10, 70);
                g.drawString("Base: " + ctx().client().getBaseX() + ", " + ctx().client().getBaseY(), 10, 100);
            } else {
                g.drawString("Tile: " + new Tile(0, 0).toString(), 10, 70);
                g.drawString("Base: " + ctx().client().getBaseX() + ", " + ctx().client().getBaseY(), 10, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
