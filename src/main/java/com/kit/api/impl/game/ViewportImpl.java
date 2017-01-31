package com.kit.api.impl.game;


import com.kit.api.Constants;
import com.kit.api.MethodContext;
import com.kit.api.Viewport;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.api.Constants;
import com.kit.api.MethodContext;
import com.kit.api.Viewport;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Class for viewport operations.
 *
 */
public class ViewportImpl implements Viewport {

    public static int[] SINE_TABLE = new int[2048];
    public static int[] COSINE_TABLE = new int[2048];

    private final MethodContext ctx;

    static {
        for (int i = 0; i < SINE_TABLE.length; i++) {
            SINE_TABLE[i] = (int) (65536.0D * Math.sin((double) i * 0.0030679615D));
            COSINE_TABLE[i] = (int) (65536.0D * Math.cos((double) i * 0.0030679615D));
        }
    }

    public ViewportImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(Tile tile) {
        return convertLocal(new Tile(getLocalX(tile.getX(), 0), getLocalY(tile.getY(), 0)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(Tile tile, double offsetX, double offsetY) {
        return convertLocal(new Tile(getLocalX(tile.getX(), offsetX), getLocalY(tile.getY(), offsetY)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(int x, int y, int modelHeight) {
        return convert(x, y, modelHeight, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(int x, int y, int height, boolean fullHeight) {
        if (x < 128 || y < 128 || x > 13056 || y > 13056) {
            return new Point(-1, -1);
        }

        int z = !fullHeight ? getTileHeight(x, y, ctx.client().getPlane()) - height : height;
        x -= ctx.client().getCameraX();
        z -= ctx.client().getCameraZ();
        y -= ctx.client().getCameraY();

        int sinCurveY = SINE_TABLE[ctx.client().getCameraPitch()];
        int cosCurveY = COSINE_TABLE[ctx.client().getCameraPitch()];
        int sinCurveX = SINE_TABLE[ctx.client().getCameraYaw()];
        int cosCurveX = COSINE_TABLE[ctx.client().getCameraYaw()];
        int calculation = sinCurveX * y + cosCurveX * x >> 16;
        y = y * cosCurveX - x * sinCurveX >> 16;
        x = calculation;
        calculation = cosCurveY * z - sinCurveY * y >> 16;
        y = sinCurveY * z + cosCurveY * y >> 16;
        z = calculation;
        if (y >= 50) {
            int viewWidth = ctx.client().getViewportWidth();
            int viewHeight = ctx.client().getViewportHeight();
            int screenX = x * ctx.client().getScale() / y + (viewWidth / 2);
            int screenY = z * ctx.client().getScale() / y + (viewHeight / 2);
            if (screenX < 0 || screenX > viewWidth || screenY < 0 || screenY > viewHeight) {
                return new Point(-1, -1);
            } else if (Session.get().inResizableMode()) {
                return new Point(screenX + 4, screenY + 4);
            } else {
                return new Point(screenX, screenY);
            }
        }

        return new Point(-1, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(int tileX, int tileY, double offsetX, double offsetY, int height) {
        return convert((int) ((tileX - ctx.client().getBaseX() + offsetX) * 128), (int) ((tileY
                - ctx.client().getBaseY() + offsetY) * 128), height, false);
    }

    @Override
    public Point convert(int tileX, int tileY, double offsetX, double offsetY, int height, boolean fullHeight) {
        return convert((int) ((tileX - ctx.client().getBaseX() + offsetX) * 128), (int) ((tileY
                - ctx.client().getBaseY() + offsetY) * 128), height, fullHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convertLocal(Tile tile) {
        return convert(tile.getX(), tile.getY(), tile.getZ());
    }

    /**
     * Calculates the tile height for a specified tile
     *
     * @param tileX     raw local X of the tile
     * @param tileY     raw local Y of the tile
     * @param tilePlane current client plane
     * @return tile height
     */
    @Override
    public int getTileHeight(int tileX, int tileY, int tilePlane) {
        int x = tileX >> Constants.LOCAL_XY_SHIFT;
        int y = tileY >> Constants.LOCAL_XY_SHIFT;
        if (x < 0 || y < 0 || x > 103 || y > 103) {
            return 0;
        }
        int plane = tilePlane;
        if (plane < 3 && (ctx.client().getTileSettings()[1][x][y] & 0x2) == 2) {
            plane++;
        }
        int x2 = tileX & 0x7f;
        int y2 = tileY & 0x7f;
        int i = (((128 - x2)
                * ctx.client().getTileHeights()[plane][x][y] +
                ctx.client().getTileHeights()[plane][x + 1][y] * x2) >> Constants.LOCAL_XY_SHIFT);
        int j = ((ctx.client().getTileHeights()[plane][x][y + 1]
                * (128 - x2) + x2
                * ctx.client().getTileHeights()[plane][1 + x][y + 1]) >> Constants.LOCAL_XY_SHIFT);
        return (128 - y2) * i + y2 * j >> Constants.LOCAL_XY_SHIFT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInViewport(Point point) {
        return new Rectangle(ctx.client().getViewportWidth(), ctx.client().getViewportHeight()).contains(point);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInViewport(Rectangle rect) {
        return new Rectangle(ctx.client().getViewportWidth(), ctx.client().getViewportHeight()).contains(rect);
    }


    private int getLocalX(int x, double offset) {
        return (int) (x - ctx.client().getBaseX() + offset) * 128;
    }

    private int getLocalY(int y, double offset) {
        return (int) (y - ctx.client().getBaseY() + offset) * 128;
    }
}
