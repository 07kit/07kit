package com.kit.api.event;

import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

/**
 */
public class BufferFlipEvent {
    private VolatileImage lastBuffer;

    public BufferFlipEvent(VolatileImage lastBuffer) {
        this.lastBuffer = lastBuffer;
    }

    public VolatileImage getLastBuffer() {
        return lastBuffer;
    }

    public void setLastBuffer(VolatileImage lastBuffer) {
        this.lastBuffer = lastBuffer;
    }
}
