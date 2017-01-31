package com.kit.api.wrappers;

import com.kit.game.engine.renderable.IProjectile;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.interaction.SceneNode;
import com.kit.game.engine.renderable.IProjectile;
import com.kit.api.MethodContext;
import com.kit.game.engine.renderable.IProjectile;

import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * @author : const_
 */
public class Projectile extends SceneNode {

    private final WeakReference<IProjectile> wrapped;

    public Projectile(MethodContext ctx, IProjectile projectile) {
        super(ctx);
        this.wrapped = new WeakReference<>(projectile);
    }

    @Override
    public int getLocalX() {
        return unwrap().getX();
    }

    @Override
    public int getLocalY() {
        return unwrap().getY();
    }

    @Override
    public int getX() {
        return unwrap().getX() + context.client().getBaseX();
    }

    @Override
    public int getY() {
        return unwrap().getY() + context.client().getBaseY();
    }

    @Override
    public int getZ() {
        //TODO
//        return unwrap().getZ();
        return -1;
    }

    public int getVelocity() {
        // TODO
//        return unwrap().getVelocity();
        return -1;
    }

    @Override
    public Model getModel() {
        return unwrap().getModel();
    }

    @Override
    public Point getBasePoint() {
        return context.viewport.convert(getX(), getY(), getZ());
    }

    @Override
    public Point getClickPoint() {
        if (getModel() != null) {
            return getModel().getClickPoint();
        }
        return getBasePoint();
    }

    @Override
    public boolean isValid() {
        return getModel() != null;
    }

    public IProjectile unwrap() {
        return wrapped.get();
    }
}
