package com.kit.api.debug.impl;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.GameObject;
import com.kit.core.Session;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

import static com.kit.api.wrappers.GameObjectType.BOUNDARY;

/**
 */
public class BoundaryObjectDebug extends AbstractDebug {

    public BoundaryObjectDebug() {
        super();
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.YELLOW);
        if (ctx().isLoggedIn()) {
            for (GameObject object : ctx().objects.find().distance(10).type(BOUNDARY).asList()) {
                Point pos = object.getBasePoint();
                g.drawString(String.valueOf(object.getId()), pos.x, pos.y);
            }

            List<GameObject> boundariesOnTile = ctx().objects.find().location(ctx().player.getTile()).type(BOUNDARY).asList();
            for (int i = 0; i < boundariesOnTile.size(); i++) {
                GameObject boundary = boundariesOnTile.get(i);
                g.drawString("Boundary on tile: " + boundary.getName(), 10, (150 + (i * 30)));
            }
        }
    }

    @Override
    public String getName() {
        return "Objects/Boundaries";
    }

    @Override
    public String getShortcode() {
        return "boundaries";
    }
}
