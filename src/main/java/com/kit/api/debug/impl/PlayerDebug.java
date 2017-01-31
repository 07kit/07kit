package com.kit.api.debug.impl;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.Player;
import com.kit.api.wrappers.Skill;
import com.kit.core.Session;
import com.kit.game.engine.IWorld;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 */
public class PlayerDebug extends AbstractDebug {

    public PlayerDebug() {
        super();
    }

    boolean s = false;

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.BLUE);
        if (ctx().isLoggedIn()) {
            for (Player player : ctx().players.find().distance(10).asList()) {
                Point pos = player.getBasePoint();
                String info = String.format("%s | A: %d | I: %d | H: %d | HP: %d | WQL: %d | Level: %d | Combat %s | Z: %d",
                        player.getName(), player.getAnimationId(),
                        player.getAssociatedEntity(),
                        player.getHealthRatio(player.getCombatInfoWrapper()),
                        player.getHealthPercent(),
                        player.getQueueSize(),
                        player.getCombatLevel(),
                        player.isInCombat() ? "Yes" : "No",
                        player.unwrap().getHeight());
                Point mm = ctx().minimap.convert(player.getTile());
                g.setColor(Color.RED);
                g.drawRect(mm.x, mm.y, 1, 1);
                g.drawString(info, pos.x, pos.y);
            }
        }
    }


    @Override
    public String getName() {
        return "Players";
    }

    @Override
    public String getShortcode() {
        return "players";
    }
}
