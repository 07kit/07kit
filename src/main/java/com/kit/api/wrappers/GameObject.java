package com.kit.api.wrappers;

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

/**
 */
public class GameObject extends SceneNode implements Wrapper<IGameObject> {
    private static final Map<Integer, Model> modelCache = new HashMap<>();
    private final WeakReference<IGameObject> wrapped;
    private final GameObjectType type;
    private ObjectComposite composite;

    public GameObject(MethodContext context, IGameObject wrapped, GameObjectType type) {
        super(context);
        this.wrapped = new WeakReference<>(wrapped);
        this.type = type;
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
        return type;
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
        if (!modelCache.containsKey(unwrap().hashCode())) {
            Model model = null;
            if (renderable != null && renderable instanceof IModel) {
                model = new Model(context, (IModel) renderable);
                model = model.update(context, getLocalX(), getLocalY(), 0);
            }
            if (renderable != null && renderable.getModel() != null) {
                model = renderable.getModel().update(context, getLocalX(), getLocalY(), 0);
            }
            modelCache.put(unwrap().hashCode(), model);
        }
        return modelCache.get(unwrap().hashCode());
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

    /**
     * An enum defining several types of objects.
     */
    public enum GameObjectType {
        FLOOR, WALL, INTERACTABLE, BOUNDARY
    }
}
