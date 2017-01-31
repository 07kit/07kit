package com.kit.api.impl.scene;


import com.kit.api.MethodContext;
import com.kit.api.Objects;
import com.kit.api.collection.Filter;
import com.kit.api.collection.queries.GameObjectQuery;
import com.kit.api.wrappers.GameObject;
import com.kit.game.engine.IClient;
import com.kit.game.engine.scene.IScene;
import com.kit.game.engine.scene.ITile;
import com.kit.game.engine.scene.tile.IInteractableObject;
import com.kit.api.MethodContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

/**
 * A method class implementation for object interaction
 *
 */
public class ObjectsImpl implements Objects {
    private final MethodContext ctx;


    public ObjectsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameObject> getAll() {
        List<GameObject> objects = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getObjectsAt(x, y));
            }
        }
        return objects;
    }

    public List<GameObject> getFiltered(List<Filter<GameObject>> filters) {
        Filter<GameObject> filter = Filter.collapse(filters);
        List<GameObject> result = new ArrayList<>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                result.addAll(getObjectsAt(x, y)
                        .stream()
                        .filter(filter::accept)
                        .collect(Collectors.toList()));
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameObjectQuery find() {
        return new GameObjectQuery(ctx);
    }

    @Override
    public GameObjectQuery find(int... ids) {
        return find().id(ids);
    }

    @Override
    public GameObjectQuery find(String... names) {
        return find().named(names);
    }

    /**
     * Gets all the objects at specified x/y
     *
     * @param x local X coordinate
     * @param y local Y coordinate
     * @return collection of objects at the specified tile
     */
    private Collection<GameObject> getObjectsAt(int x, int y) {
        List<GameObject> objects = newArrayList();
        IScene worldController = ctx.client().getScene();
        ITile[][][] grounds = worldController.getTiles();
        ITile ground = grounds[ctx.client().getPlane()][x][y];

        if (ground != null) {
            try {
                if (ground.getFloorObject() != null) {
                    objects.add(new GameObject(ctx, ground.getFloorObject(), GameObject.GameObjectType.FLOOR));
                }

                if (ground.getWallObject() != null) {
                    objects.add(new GameObject(ctx, ground.getWallObject(), GameObject.GameObjectType.WALL));
                }

                if (ground.getBoundaryObject() != null) {
                    objects.add(new GameObject(ctx, ground.getBoundaryObject(), GameObject.GameObjectType.BOUNDARY));
                }

                if (ground.getInteractableObjects() != null) {
                    for (IInteractableObject object : ground.getInteractableObjects()) {
                        if (object == null) {
                            continue;
                        }
                        GameObject wrapped = new GameObject(ctx, object, GameObject.GameObjectType.INTERACTABLE);
                        objects.add(wrapped);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objects;
    }

}
