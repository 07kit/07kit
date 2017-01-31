package com.kit.api.wrappers.interaction;

import com.kit.api.MethodContext;
import com.kit.api.wrappers.Model;
import com.kit.api.wrappers.Tile;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Model;
import com.kit.api.wrappers.Tile;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Model;

/**
 * A SceneNode is a part of the RuneScape scene graph.
 * Examples of scene nodes are:
 * - NPCs
 * - Players
 * - Objects
 *
 */
public abstract class SceneNode extends Interactable {

    public SceneNode(MethodContext context) {
        super(context);
    }

    /**
     * Gets the local X coordinate.
     * This coordinate is only valid within the
     * currently loaded region.
     *
     * @return local X
     */
    public abstract int getLocalX();

    /**
     * Gets the local Y coordinate.
     * This coordinate is only valid within the
     * currently loaded region.
     *
     * @return local Y
     */
    public abstract int getLocalY();

    /**
     * Gets the global X coordinate.
     *
     * @return global X
     */
    public abstract int getX();

    /**
     * Gets the global Y coordinate.
     *
     * @return global Y
     */
    public abstract int getY();

    /**
     * Gets the global Z (plane) coordinate.
     *
     * @return global Z
     */
    public abstract int getZ();

    /**
     * Gets the global position of this SceneNode
     *
     * @return tile
     */
    public Tile getTile() {
        return new Tile(context, getX(), getY(), getZ());
    }

    /**
     * Gets the distance to another tile
     *
     * @param node SceneNode to find the distance to
     * @return The distance between this and the SceneNode specified
     */
    public int distanceTo(SceneNode node) {
        return getTile().distanceTo(node.getTile());
    }

    public int distanceTo(Tile tile) {
        return tile.distanceTo(getTile());
    }

    /**
     * Gets the model of the node
     *
     * @return the model
     */
    public abstract Model getModel();

    @Override
    public boolean isOnScreen() {
        Model model = getModel();
        if (model == null) {
            return super.isOnScreen();
        }
        return model.isOnScreen();
    }

    public boolean isOnMinimap() {
        return context.minimap.convert(getTile()).x > -1;
    }
}
