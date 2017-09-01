package com.kit.api.wrappers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.MapMaker;
import com.kit.Application;
import com.kit.api.Constants;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.interaction.SceneNode;
import com.kit.core.Session;
import com.kit.game.engine.cache.media.IModel;
import com.kit.game.engine.renderable.IRenderable;
import com.kit.game.engine.scene.tile.*;
import com.kit.api.Constants;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.interaction.SceneNode;
import com.kit.game.engine.cache.media.IModel;
import com.kit.game.engine.renderable.IRenderable;
import com.kit.game.engine.scene.tile.IGameObject;
import com.kit.api.Constants;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.interaction.SceneNode;
import com.kit.game.engine.cache.media.IModel;
import com.kit.game.engine.scene.tile.IGameObject;

import java.awt.Point;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 */
public class GameObject extends SceneNode implements Wrapper<IGameObject> {
    private static final Cache<Integer, Model> modelCache = CacheBuilder.newBuilder()
            .maximumSize(2000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private final WeakReference<IGameObject> wrapped;
    private ObjectComposite composite;

    public GameObject(IGameObject wrapped) {
        super(Session.get());
        this.wrapped = new WeakReference<>(wrapped);
    }

    /**
     * Gets the ID of the object.
     *
     * @return id
     */
    public int getId() {
        return unwrap() != null ? unwrap().getHash() >> 14 & 32767 : -1;
    }

    /**
     * Gets the type of object
     *
     * @return type
     */
    public GameObjectType getType() {
        return unwrap().getType();
    }

    /**
     * {@inheritDoc}
     */
    public final int getLocalX() {
        return unwrap().getX();
    }

    /**
     * {@inheritDoc}
     */
    public final int getLocalY() {
        return unwrap().getY();
    }

    /**
     * {@inheritDoc}
     */
    public final int getX() {
        return (getLocalX() >> Constants.LOCAL_XY_SHIFT) + context.client().getBaseX();
    }

    /**
     * {@inheritDoc}
     */
    public final int getY() {
        return (getLocalY() >> Constants.LOCAL_XY_SHIFT) + context.client().getBaseY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getZ() {
        return context.client().getPlane();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model getModel() {
        IRenderable renderable = unwrap().getRenderable();
        if (renderable != null) {
            Model model = modelCache.getIfPresent(unwrap().hashCode());
            if (model == null) {
                if (renderable instanceof IModel) {
                    model = new Model(context, (IModel) renderable);
                    model = model.update(context, getLocalX(), getLocalY(), 0);
                }
                if (renderable.getModel() != null) {
                    model = renderable.getModel().update(context, getLocalX(), getLocalY(), 0);
                }
                modelCache.put(unwrap().hashCode(), model);
            }
            return model;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBasePoint() {
        return context.viewport.convert(getTile());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getClickPoint() {
        if (getModel() != null) {
            return getModel().getClickPoint();
        }
        return getBasePoint();
    }

    @Override
    public boolean isValid() {
        return unwrap() != null && context.objects.find().location(
                getTile()).id(getId()).exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGameObject unwrap() {
        return wrapped.get();
    }

    /**
     * Gets the objects name
     *
     * @return the objects name
     */
    public String getName() {
        return getComposite() != null ? getComposite().getName() : null;
    }

    /**
     * Gets the object composite for this objects id
     */
    public ObjectComposite getComposite() {
        if (composite != null) {
            return composite;
        }
        ObjectComposite composite = context.composites.getObjectComposite(getId());
        if (composite == null) {
            return null;
        }
        return (this.composite = composite);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameObject) {
            return ((GameObject) obj).unwrap().equals(wrapped);
        }
        return super.equals(obj);
    }
}
