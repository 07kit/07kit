package com.kit.api.wrappers;

import com.kit.api.MethodContext;
import com.kit.api.util.QuickHuller;
import com.kit.api.wrappers.interaction.Interactable;
import com.kit.game.engine.cache.media.IModel;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * A wrapper for models in the RuneScape client
 */
public class Model extends Interactable implements Wrapper<IModel> {
    private static final int[] SIN_TABLE = new int[16384];
    private static final int[] COS_TABLE = new int[16384];

    private final WeakReference<IModel> wrapped;
    private int[] trianglesX,
            trianglesY,
            trianglesZ,
            verticesX,
            verticesY,
            verticesZ,
            originalX,
            originalZ = new int[0];
    private int localX;
    private int localY;
    private int orientation;

    public Model(IModel wrapped) {
        this.wrapped = new WeakReference<>(wrapped);
    }

    public Model(MethodContext ctx, IModel wrapped) {
        super(ctx);
        this.wrapped = new WeakReference<>(wrapped);
    }

    public Model(MethodContext ctx, IModel wrapped, int localX, int localY, int orientation) {
        super(ctx);
        this.wrapped = new WeakReference<>(wrapped);
        this.localX
                = localX;
        this.localY = localY;
        this.orientation = orientation;
    }

    private void init() {
        IModel wrapped = this.wrapped.get();
        if (wrapped != null) {
            this.trianglesX = wrapped.getTrianglesX();
            this.trianglesY = wrapped.getTrianglesY();
            this.trianglesZ = wrapped.getTrianglesZ();
            this.verticesX = wrapped.getVerticesX().clone();
            this.verticesY = wrapped.getVerticesY().clone();
            this.verticesZ = wrapped.getVerticesZ().clone();
            this.orientation = ((orientation & 0x3FFF) + 1024) % 2048;
            this.originalX = this.verticesX.clone();
            this.originalZ = this.verticesZ.clone();


            setRotation((64 * 128) & 0x3fff);
            if (orientation != 0) {
                setRotation(orientation);
            }
        }
    }

    public Point getCentralPoint() {
        Point returnPoint = new Point(-1, -1);
        List<Point> points = getPoints();
        if (points.size() > 0) {
            int x = 0, y = 0, total = 0;
            for (Point p : points) {
                x += p.x;
                y += p.y;
                total++;
            }
            Point central = new Point(x / total, y / total);
            //Point central = getCentroid();


            double distance = Double.MAX_VALUE;
            for (Point p : points) {
                if (!context.viewport.isInViewport(p)) {
                    continue;
                }
                double dist = Math.sqrt(((central.x - p.x) * (central.x - p.x)) + ((central.y - p.y) * (central.y - p.y)));
                if (dist < distance) {
                    returnPoint = p;
                    distance = dist;
                }
            }
        }
        return returnPoint;
    }

    /**
     * Gets the centroid of the model
     *
     * @return centroid
     */
    public Point getCentroid() {
        int xTotal = 0, yTotal = 0, indices = 0;
        List<Point> points = getPoints();
        if (points.size() == 0) {
            return new Point(-1, -1);
        }
        for (Point p : points) {
            xTotal += p.x;
            yTotal += p.y;
            indices++;
        }

        if (xTotal == 0 || yTotal == 0 || indices == 0) {
            return new Point(-1, -1);
        }

        final Point central = new Point(xTotal / indices, yTotal / indices);
        double dist = Double.MAX_VALUE;
        Point curr = central;
        for (Point p : points) {
            final double d = central.distance(p);
            if (d < dist) {
                curr = p;
                dist = d;
            }
        }
        return curr;
    }

    /**
     * Checks if the model points contains a point
     *
     * @param p - the point to check
     * @return <>true if the model contains the point</> otherwise false
     */
    public boolean contains(Point p) {
        for (Polygon _p : getPolygons()) {
            if (_p.contains(p)) {
                return true;
            }
        }
        Shape hull = quickHull();
        return hull != null && hull.contains(p);
    }

    /**
     * Converts the vertice arrays into polygon objects.
     *
     * @return polygons
     */
    public Polygon[] getPolygons() {
        if (this.verticesX == null || this.verticesY == null || this.verticesZ == null) {
            this.init();
        }

        setRotation((64 * 128) & 0x3fff);
        if (orientation != 0) {
            setRotation(orientation & 0x3fff);
        }

        ArrayList<Polygon> polys = newArrayList();
        for (int i = 0; i < trianglesX.length; i++) {
            if (i >= trianglesY.length && i >= trianglesZ.length) {
                return null;
            }

            Point x = context.viewport.convert(localX - verticesX[trianglesX[i]], localY - verticesZ[trianglesX[i]], -verticesY[trianglesX[i]]);
            Point y = context.viewport.convert(localX - verticesX[trianglesY[i]], localY - verticesZ[trianglesY[i]], -verticesY[trianglesY[i]]);
            Point z = context.viewport.convert(localX - verticesX[trianglesZ[i]], localY - verticesZ[trianglesZ[i]], -verticesY[trianglesZ[i]]);
            if (context.viewport.isInViewport(x) && context.viewport.isInViewport(y) && context.viewport.isInViewport(z)) {
                int xx[] = {
                        x.x, y.x, z.x
                };
                int yy[] = {
                        x.y, y.y, z.y
                };
                polys.add(new Polygon(xx, yy, 3));
            }
        }
        return polys.toArray(new Polygon[polys.size()]);
    }

    private boolean rightTurn(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) > 0;
    }

    /**
     * Gets a list of points from #getPolygons
     *
     * @return list
     */
    public List<Point> getPoints() {
        List<Point> points = newArrayList();
        for (Polygon p : getPolygons()) {
            int x = 0, y = 0;
            for (int n = 0; n < p.npoints; n++) {
                x += p.xpoints[n];
                y += p.ypoints[n];
            }
            x /= p.npoints;
            y /= p.npoints;
            points.add(new Point(x, y));
        }
        return points;
    }

    /**
     * Rotates and scales the model
     *
     * @param orientation orientation
     */
    protected void setRotation(int orientation) {
        int sin = SIN_TABLE[orientation];
        int cos = COS_TABLE[orientation];
        if (this.verticesX == null) {
            return;
        }

        if (originalX == null) {
            if (verticesX == null) {
                return;
            }
            originalX = verticesX;
        }
        if (originalZ == null) {
            if (verticesZ == null) {
                return;
            }
            originalZ = verticesZ;
        }

        for (int i = 0; i < this.verticesX.length; ++i) {
            this.verticesX[i] = this.originalX[i] * cos + this.originalZ[i] * sin >> 16;
            this.verticesZ[i] = this.originalZ[i] * cos - this.originalX[i] * sin >> 16;
        }

    }

    public void draw(Graphics g) {
        for (Polygon p : getPolygons()) {
            g.drawPolygon(p);
        }
    }

    public void fill(Graphics g) {
        for (Polygon p : getPolygons()) {
            g.fillPolygon(p);
        }
    }

    /**
     * Rotates and scales the model
     *
     * @param orientation orientation
     */
    protected void setRotationDownScale(int orientation) {
        int sin = SIN_TABLE[orientation];
        int cos = COS_TABLE[orientation];
        for (int i = 0; i < this.verticesX.length; ++i) {
            this.verticesX[i] = this.originalX[i] * cos + this.originalZ[i] * sin >> 15;
            this.verticesZ[i] = this.originalZ[i] * cos - this.originalX[i] * sin >> 15;
        }

    }

    public Polygon quickHull() {
        return QuickHuller.hull(getPoints());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBasePoint() {
        return context.viewport.convert(localX, localY, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getClickPoint() {
        return getCentralPoint();
    }

    @Override
    public boolean isValid() {
        return unwrap() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IModel unwrap() {
        return wrapped.get();
    }


    public Model update(MethodContext ctx, int localX, int localY, int orientation) {
        this.context = ctx;
        this.localX = localX;
        this.localY = localY;
        this.orientation = ((orientation & 0x3FFF) + 1024) % 2048;
        setRotation(orientation);
        return this;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Model) {
            return ((Model) obj).wrapped.equals(wrapped);
        }
        return super.equals(obj);
    }

    @Override
    public boolean isOnScreen() {
        for (Point p : getPoints()) {
            if (context.viewport.isInViewport(p)) {
                return true;
            }
        }
        return false;
    }

    static {
        for (int i = 0; i < SIN_TABLE.length; i++) {
            SIN_TABLE[i] = (int) (65536.0D * Math.sin((double) i * 0.0030679615D));
            COS_TABLE[i] = (int) (65536.0D * Math.cos((double) i * 0.0030679615D));
        }
    }
}