package com.kit.api.event;

import java.awt.Graphics;

/**
 * @author const_
 */
public class PaintEvent {

    private Graphics graphics;

    public PaintEvent(Graphics graphics) {
        this.graphics = graphics;
    }

    public Graphics getGraphics() {
        return graphics;
    }
}
