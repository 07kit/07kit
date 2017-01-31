package com.kit.core.input.impl;


import com.kit.api.MethodContext;
import com.kit.api.Mouse;
import com.kit.game.engine.extension.EventExtension;
import com.kit.api.MethodContext;
import com.kit.api.Mouse;
import com.kit.api.util.Utilities;
import com.kit.core.Session;
import com.kit.game.engine.extension.CanvasExtension;
import com.kit.game.engine.extension.EventExtension;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Date: 29/08/13
 * Time: 09:36
 *
 * @author Matt Collinge
 */
public class MouseImpl implements Mouse {

   private final MethodContext ctx;


    public MouseImpl(MethodContext context) {
        ctx = context;
    }


    /**
     * Gets the clients implementation of the MouseListener.
     *
     * @return Clients MouseListener.
     */
    private EventExtension getMouse() {
        return (EventExtension) ctx.client().getMouse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getPosition() {
        return new Point(getMouse().getMouseX(), getMouse().getMouseY());
    }


}
