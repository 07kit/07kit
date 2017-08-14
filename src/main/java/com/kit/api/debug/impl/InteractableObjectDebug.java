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

import java.awt.*;
import java.util.Arrays;

import static com.kit.api.wrappers.GameObject.GameObjectType.INTERACTABLE;

/**
 */
public class InteractableObjectDebug extends AbstractDebug {
    public InteractableObjectDebug() {
        super();
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.YELLOW);
        if (ctx().isLoggedIn()) {
            for (GameObject object : ctx().objects.find().distance(10).type(INTERACTABLE).asList()) {
                Point pos = object.getBasePoint();
                Shape hull = object.getModel().quickHull();
                ((Graphics2D) g).draw(hull);
                if (object.getComposite() != null) {
                    g.drawString(String.valueOf(object.getId()) + ":" +
                            Arrays.toString(object.getComposite().getOriginalModelColors())
                            + ":" + object.getComposite().getName(), pos.x, pos.y);
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Objects/Interactable Objects";
    }

    @Override
    public String getShortcode() {
        return "interactable_objects";
    }
}
