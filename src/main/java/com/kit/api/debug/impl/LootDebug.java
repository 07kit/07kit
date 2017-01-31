package com.kit.api.debug.impl;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.wrappers.Loot;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.Loot;
import com.kit.core.Session;

import java.awt.*;

/**
 * @author : const_
 */
public class LootDebug extends AbstractDebug {

    public LootDebug() {
        super();
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        if (ctx().isLoggedIn()) {
            for (Loot _loot : ctx().loot.find().distance(10).asList()) {
                if (_loot.getModel() != null) {
                    _loot.getModel().draw(g);
                }
                g.drawString(String.valueOf(_loot.getId()), _loot.getBasePoint().x, _loot.getBasePoint().y);
            }
        }
    }

    @Override
    public String getName() {
        return "Loot";
    }

    @Override
    public String getShortcode() {
        return "loot";
    }
}
