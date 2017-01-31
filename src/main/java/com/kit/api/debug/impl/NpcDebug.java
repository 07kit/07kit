package com.kit.api.debug.impl;

import com.kit.api.debug.AbstractDebug;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.wrappers.Npc;
import com.kit.core.Session;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;

import java.awt.*;
import java.util.List;

/**
 */
public class NpcDebug extends AbstractDebug {

    public NpcDebug() {
        super();
    }

    @Override
    public String getName() {
        return "NPCs";
    }

    @Override
    public String getShortcode() {
        return "npcs";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics graphics = event.getGraphics();
        if (ctx().isLoggedIn()) {
            List<Npc> all = ctx().npcs.find().distance(10).asList();
            for (Npc npc : all) {
                Point screen = npc.getBasePoint();
                graphics.setColor(Color.MAGENTA);
                int modelId = npc.getComposite() != null &&
                        npc.getComposite().getAdditionalModels()!= null &&
                        npc.getComposite().getAdditionalModels().length > 0
                        ? npc.getComposite().getAdditionalModels()[0] : -1;
                if(npc.getComposite() == null) {
                    System.out.println("No composite for " + npc.getId());
                    continue;
                }
                String info = String.format("%s (%d) | M: %d | A: %d | I: %d | Level: %d | WQL: %d | V: %d",
                        npc.getName(), npc.getId(),
                        modelId,
                        npc.getAnimationId(),
                        npc.getAssociatedEntity(),
                        npc.getComposite().getCombatLevel(),
                        npc.getQueueSize(),
                        npc.getModel() == null ? -1 : npc.getModel().getPolygons().length);
                graphics.drawString(info, screen.x, screen.y);
            }
        }
    }
}
