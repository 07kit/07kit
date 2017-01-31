package com.kit.api.debug.impl;

import com.kit.api.event.EventHandler;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.GameObject;
import com.kit.core.Session;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import static com.kit.api.wrappers.GameObject.GameObjectType.WALL;

/**
 */
public class WallObjectDebug extends AbstractDebug {

    public WallObjectDebug() {
        super();
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.YELLOW);
        if (ctx().isLoggedIn()) {
            for (GameObject object : ctx().objects.find().distance(10).type(WALL).asList()) {
                Point pos = object.getBasePoint();
                g.drawString(String.valueOf(object.getId()), pos.x, pos.y);
            }
        }
    }

    @Override
    public String getName() {
        return "Objects/Wall Objects";
    }

    @Override
    public String getShortcode() {
        return "wall_objects";
    }
}
