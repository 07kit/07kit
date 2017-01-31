package com.kit.api.impl.game;


import com.kit.api.Camera;
import com.kit.api.Camera;
import com.kit.api.MethodContext;
import com.kit.api.collection.StatePredicate;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Player;
import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.interaction.SceneNode;
import com.kit.api.Camera;
import com.kit.api.MethodContext;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 */
public class CameraImpl implements Camera {

    private final MethodContext context;


    public CameraImpl(MethodContext context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getX() {
        return context.client().getCameraX();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getY() {
        return context.client().getCameraY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getZ() {
        return context.client().getCameraZ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAngle() {
        return (int) (((double) context.client().getCameraYaw() / 2048.0) * 360.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPitch() {
        return context.client().getCameraPitch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAngleTo(Tile tile) {
        Player local = context.players.getLocal();
        int x = local.getX() - tile.getX();
        int y = local.getY() - tile.getY();
        double angle = Math.toDegrees(Math.atan2(x, y));
        if (x == 0 && y > 0)
            angle = 180;
        if (x < 0 && y == 0)
            angle = 90;
        if (x == 0 && y < 0)
            angle = 0;
        if (x < 0 && y == 0)
            angle = 270;
        if (x < 0 && y > 0)
            angle += 270;
        if (x > 0 && y > 0)
            angle += 90;
        if (x < 0 && y < 0)
            angle = Math.abs(angle) - 180;
        if (x > 0 && y < 0)
            angle = Math.abs(angle) + 270;
        if (angle < 0)
            angle = 360 + angle;
        if (angle >= 360)
            angle -= 360;
        return (int) angle;
    }
}
