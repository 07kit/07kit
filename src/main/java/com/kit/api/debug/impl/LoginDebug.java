package com.kit.api.debug.impl;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.core.Session;

import java.awt.*;

/**
 * @author : const_
 */
public class LoginDebug extends AbstractDebug {

    public LoginDebug() {
        super();
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.RED);
        g.drawString(String.format("Login Index: %d", ctx().client().getLoginIndex()), 10, 120);
        g.drawString(String.format("Game State: %d", ctx().client().getGameState()), 10, 130);
    }

    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public String getShortcode() {
        return "login";
    }
}
