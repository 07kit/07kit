package com.kit.api.impl.game;


import com.kit.api.MethodContext;
import com.kit.api.Minimap;
import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.Widget;
import com.kit.game.engine.media.IWidget;
import com.kit.api.MethodContext;
import com.kit.api.Minimap;
import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.Widget;
import com.kit.game.engine.media.IWidget;

import java.awt.Point;

/**
 * Date: 27/08/13
 * Time: 14:17
 *
 * @author Matt Collinge
 */
public class MinimapImpl implements Minimap {

    public static int[] SINE_TABLE = new int[2048];
    public static int[] COSINE_TABLE = new int[2048];

    private static final int MINIMAP_INTERFACE_GROUP = 160;
    private static final int MINIMAP_INTERFACE_CHILD = 0;

    private final MethodContext ctx;

    static {
        for (int i = 0; i < SINE_TABLE.length; i++) {
            SINE_TABLE[i] = (int) (65536.0D * Math.sin((double) i * 0.0030679615D));
            COSINE_TABLE[i] = (int) (65536.0D * Math.cos((double) i * 0.0030679615D));
        }
    }

    public MinimapImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(Tile tile) {
        return tile == null ? new Point(-1, -1) : convert(tile.getX(), tile.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(int x, int y) {
        Tile tile = new Tile(x, y);
        if (tile.distanceTo(ctx.player.getTile()) > 17) {
            return new Point(-1, -1);
        }

        x -= ctx.client().getBaseX();
        y -= ctx.client().getBaseY();
        Widget mm = getWidget();
        if (mm != null) {
            final int xx = x * 4 + 2 - ctx.player.getLocalX() / 32;
            final int yy = 2 + y * 4 - ctx.player.getLocalY() / 32;


            int degree = ctx.client().getMinimapAngle() & 0x7FF;
            int dist = (int) (Math.pow(xx, 2) + Math.pow(yy, 2));

            if (dist <= 6400) {
                int sin = SINE_TABLE[degree];
                int cos = COSINE_TABLE[degree];

                int mx = yy * sin + cos * xx >> 16;
                int my = sin * xx - yy * cos >> 16;
                final int screenx = 10 + ((mm.getX() + mm.getWidth() / 2) + mx);
                final int screeny = (mm.getY() + mm.getHeight() / 2) + my;

                if (mm.getArea().contains(screenx, screeny) && (Math.max(my, -my) <= (((mm.getWidth()) / 2.0) * .8))
                        && (Math.max(mx, -mx) <= (((mm
                        .getHeight()) / 2) * .8))) {
                    return new Point(screenx, screeny);
                }
            }
        }
        return new Point(-1, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convertUnbounded(Tile tile) {
        return tile == null ? new Point(-1, -1) : convertUnbounded(tile.getX(), tile.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convertUnbounded(int x, int y) {
        x -= ctx.client().getBaseX();
        y -= ctx.client().getBaseY();
        Widget mm = getWidget();
        if (mm != null) {
            final int xx = x * 4 + 2 - ctx.players.getLocal().getLocalX() / 32;
            final int yy = 2 + y * 4 - ctx.players.getLocal().getLocalY() / 32;

            int degree = ctx.client().getMinimapAngle() & 0x7FF;
            int dist = (int) (Math.pow(xx, 2) + Math.pow(yy, 2));

            int sin = SINE_TABLE[degree];
            int cos = COSINE_TABLE[degree];

            int mx = yy * sin + cos * xx >> 16;

            int my = sin * xx - yy * cos >> 16;
            if (dist < 2500) {

                final int sx = 18 + ((mm.getX() + mm.getHeight() / 2) + mx);
                final int sy = (mm.getY() + mm.getHeight() / 2 - 1) + my;

                return new Point(sx, sy);
            }

            final int screenx = 18 + ((mm.getX() + mm.getWidth() / 2) + mx);
            final int screeny = (mm.getY() + mm.getWidth() / 2 - 1) + my;

            return new Point(screenx, screeny);
        }
        return new Point(-1, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getWidget() {
        if (ctx.client().getWidgets() != null) {
            IWidget minimap = ctx.client().getWidgets()[MINIMAP_INTERFACE_GROUP][MINIMAP_INTERFACE_CHILD];
            if (minimap != null) {
                return new Widget(ctx, minimap, MINIMAP_INTERFACE_GROUP, MINIMAP_INTERFACE_CHILD);
            }
        }
        return null;
    }
}
