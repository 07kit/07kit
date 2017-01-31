package com.kit.api.debug.impl;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.core.Session;

import java.awt.Color;
import java.awt.Graphics;

/**
 */
public class CameraDebug extends AbstractDebug {
    public CameraDebug() {
        super();
    }

    @Override
    public String getName() {
        return "Camera";
    }

    @Override
    public String getShortcode() {
        return "camera";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.CYAN);
        g.drawString(String.format("X: %d, Y: %d, Z: %d, Pitch: %d, Yaw: %d",
                ctx().camera.getX(), ctx().camera.getY(), ctx().camera.getZ(),
                ctx().client().getCameraPitch(),
                ctx().client().getCameraYaw()),
                10, 100);
    }
}
