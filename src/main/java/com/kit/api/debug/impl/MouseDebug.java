package com.kit.api.debug.impl;

import com.kit.api.event.EventHandler;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.debug.AutoEnable;
import com.kit.api.event.PaintEvent;
import com.kit.core.Session;

import java.awt.*;

/**
 */
public class MouseDebug extends AbstractDebug {
    public MouseDebug() {
        super();
    }

    @Override
    public String getName() {
        return "Mouse";
    }

    @Override
    public String getShortcode() {
        return "mouse";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(new Color(1f, 0.1f, 0f, 0.7f));
        Point position = ctx().mouse.getPosition();
        g.drawString("(" + position.x + ", " + position.y + ")", 100, 140);
        g.drawLine(position.x - 5, position.y - 5, position.x + 5, position.y + 5);
        g.drawLine(position.x + 5, position.y - 5, position.x - 5, position.y + 5);
    }
}
