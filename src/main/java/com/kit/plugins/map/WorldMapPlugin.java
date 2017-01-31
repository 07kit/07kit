package com.kit.plugins.map;

import com.kit.Application;
import com.kit.api.event.*;
import com.kit.api.plugin.Option;
import com.kit.api.wrappers.Region;
import com.kit.api.wrappers.Tile;
import com.kit.plugins.map.marker.Marker;
import com.kit.Application;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.Widget;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.game.transform.impl.PlayerMenuActionsExtender;
import com.kit.plugins.map.marker.LocalPlayerMarker;
import com.kit.plugins.map.marker.Marker;
import com.kit.Application;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldMapPlugin extends Plugin {

    private static final int POS_X = 4;
    private static final int POS_Y = 4;
    private static final double ZOOM_UNIT = 0.3;
    private static final double ZOOM_MIN = 1.0;
    private static final double ZOOM_MAX = 6.4;

    public static BufferedImage MAP_ICON;
    public static Image SHOW_REGIONS_ICON_ON;
    public static Image SHOW_REGIONS_ICON_HOVER;
    public static Image SHOW_REGIONS_ICON_OFF;
    public static BufferedImage MAP;

    private final Set<Marker> markers = new HashSet<>();

    private int lastViewportWidth;
    private int lastViewportHeight;

    private double zoom = ZOOM_MIN;
    private double lastZoom = zoom;

    private boolean iconHovered;
    private boolean showRegionHovered;

    private boolean showRegionPressed;
    @Option(type = Option.Type.HIDDEN, label = "showing region on world map", value = "true")
    private boolean showingRegion;

    private boolean closeHovered;
    private boolean closePressed;
    private boolean drag;
    private boolean visible;

    private BufferedImage mapChunk;

    private Point offset;
    private Point last;

    private RegionTracker regionTracker;

    static {
        try {
            SHOW_REGIONS_ICON_ON = IconFontSwing.buildImage(FontAwesome.USERS, 20, Color.WHITE);
            SHOW_REGIONS_ICON_HOVER = IconFontSwing.buildImage(FontAwesome.USERS, 20, Color.GRAY);
            SHOW_REGIONS_ICON_OFF = IconFontSwing.buildImage(FontAwesome.USERS, 20, Application.COLOUR_SCHEME.getHighlight());
            MAP_ICON = ImageIO.read(WorldMapPlugin.class.getResourceAsStream("/map-icon.png"));
            MAP = ImageIO.read(WorldMapPlugin.class.getResourceAsStream("/worldmap.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WorldMapPlugin(PluginManager manager) {
        super(manager);
        regionTracker = new RegionTracker();
        markers.add(new LocalPlayerMarker(this));
    }

    @Override
    public String getName() {
        return "World Map";
    }

    public void addMarker(Marker marker) {
        markers.add(marker);
    }

    public void removeMarker(Marker marker) {
        if (marker == null) return;
        markers.remove(marker);
    }

    public Set<Marker> getMarkers() {
        return markers;
    }

    public void reset() {
        last = null;
        zoom = ZOOM_MIN;
        lastZoom = zoom;
        Point local = pointForTile(Session.get().player.getTile());
        offset = new Point(local.x - getMapWidth() / 2, local.y - getMapHeight() / 2);
        if (offset.x < 0 || offset.y < 0 || offset.x > MAP.getWidth() || offset.y > MAP.getHeight()) {
            offset = new Point(MAP.getWidth() / 2, MAP.getHeight() / 2); // player likely underground/elsewhere
        }
        refresh();
    }

    public synchronized void refresh() {
        int width = (int) (zoom * getMapWidth());
        int height = (int) (zoom * getMapHeight());
        offset.x = Math.max(offset.x, 0);
        offset.y = Math.max(offset.y, 0);
        int maxX = offset.x + width;
        int maxY = offset.y + height;
        if (maxX > MAP.getWidth()) {
            offset.x -= (maxX - MAP.getWidth());
        }
        if (maxY > MAP.getHeight()) {
            offset.y -= (maxY - MAP.getHeight());
        }
        if (offset.x < 0) {
            offset.x = 0;
        }
        if (offset.y < 0) {
            offset.y = 0;
        }
        if (offset.x + width > MAP.getWidth()) {
            offset.x = 0;
            width = MAP.getWidth();
        }
        if (offset.y + height > MAP.getHeight()) {
            offset.y = 0;
            height = MAP.getHeight();
        }
        mapChunk = MAP.getSubimage(offset.x, offset.y, width, height);
    }

    public Rectangle getMapBounds() {
        int x = POS_X;
        int y = POS_Y;
        int w = client().getViewportWidth();
        int h = client().getViewportHeight();
        if (Session.get().inResizableMode()) {
            w = client().getViewportWidth() - 245;
            h = client().getViewportHeight() - 174;
        }
        return new Rectangle(x, y, w, h);
    }

    public Rectangle getIconBounds() {
        Widget mm = Session.get().minimap.getWidget();
        int x;
        int y;
        if (mm != null) {
            x = mm.getX() + 170;
            y = mm.getY() + 120;
        } else {
            //this is a fallback and should never happen
            x = client().getViewportWidth() + (Session.get().inResizableMode() ? -45 : 160);
            y = Session.get().inResizableMode() ? 136 : 130;
        }
        return new Rectangle(x, y, 43, 43);
    }

    public Rectangle getCloseBounds() {
        Rectangle bounds = getMapBounds();
        return new Rectangle(bounds.x + bounds.width - 18, bounds.y + 6, 12, 12);
    }

    public Rectangle getShowRegionBounds() {
        Rectangle bounds = getMapBounds();
        return new Rectangle(bounds.x + bounds.width - 30 - SHOW_REGIONS_ICON_ON.getWidth(null), bounds.y + 2, SHOW_REGIONS_ICON_ON.getWidth(null),
                SHOW_REGIONS_ICON_ON.getHeight(null));
    }

    @Schedule(400)
    public void onAppletResize() {
        int viewportHeight = client().getViewportHeight();
        int viewportWidth = client().getViewportWidth();
        ;
        if (lastViewportHeight != viewportHeight ||
                lastViewportWidth != viewportWidth) {
            if (visible) {
                refresh();
            }
        }
        lastViewportHeight = viewportHeight;
        lastViewportWidth = viewportWidth;
    }

    @EventHandler
    public void onMapZoom(MouseWheelEvent event) {
        if (event.getID() == MouseEvent.MOUSE_WHEEL) {
            if (getMapBounds().contains(event.getPoint()) && visible) {
                event.consume();
                int scroll = event.getWheelRotation();
                zoom += scroll * ZOOM_UNIT;
                if (scroll > 0) {
                    zoom = Math.min(zoom, ZOOM_MAX);
                } else {
                    zoom = Math.max(zoom, ZOOM_MIN);
                }
                if (zoom != lastZoom) {
                    double dz = zoom - lastZoom;
                    offset.x -= (dz * getMapWidth()) / 2;
                    offset.y -= (dz * getMapHeight()) / 2;
                }
                lastZoom = zoom;
                refresh();
            }
        }
    }

    @EventHandler
    public void onMapDrag(MouseEvent event) {
        if (!visible || !getMapBounds().contains(event.getPoint())) {
            return;
        }
        event.consume();
        if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            drag = true;
            last = event.getPoint();
        } else if (event.getID() == MouseEvent.MOUSE_RELEASED) {
            drag = false;
        } else if (event.getID() == MouseEvent.MOUSE_DRAGGED) {
            if (visible && drag) {
                int lastOffsetX = offset.x;
                int dx = last.x - event.getX();
                offset.x += dx * zoom;
                int lastOffsetY = offset.y;
                int dy = last.y - event.getY();
                offset.y += dy * zoom;
                try {
                    refresh();
                } catch (Exception ignored) {
                    offset.x = lastOffsetX;
                    offset.y = lastOffsetY;
                }
                last = (Point) event.getPoint().clone();
            }
        }
    }

    public void show() {
        this.visible = true;
        reset();
    }

    @EventHandler
    public void onIconMouseEvent(MouseEvent event) {
        if (!Session.get().isLoggedIn()) {
            return;
        }
        Rectangle iconBounds = getIconBounds();
        Rectangle closeBounds = getCloseBounds();
        Rectangle showRegionBounds = getShowRegionBounds();
        if (iconBounds == null || closeBounds == null || showRegionBounds == null) {
            return;
        }
        if (event.getID() == MouseEvent.MOUSE_CLICKED) {
            if (iconBounds.contains(event.getPoint())) {
                event.consume();
                visible = !visible;
                if (visible) {
                    show();
                }
            } else if (closeBounds.contains(event.getPoint())) {
                visible = false;
            } else if (showRegionBounds.contains(event.getPoint())) {
                showingRegion = !showingRegion;
                persistOptions();
            }
        } else if (event.getID() == MouseEvent.MOUSE_MOVED) {
            iconHovered = iconBounds.contains(event.getPoint());
            closeHovered = closeBounds.contains(event.getPoint());
            showRegionHovered = showRegionBounds.contains(event.getPoint());
        } else if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            if (closeBounds.contains(event.getPoint())) {
                closePressed = true;
            } else if (showRegionBounds.contains(event.getPoint())) {
                showRegionPressed = true;
            }
        } else if (event.getID() == MouseEvent.MOUSE_RELEASED) {
            closePressed = false;
            showRegionPressed = false;
        }
    }

    public int getMapWidth() {
        return getMapBounds().width;
    }

    public int getMapHeight() {
        return getMapBounds().height;
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (!Session.get().isLoggedIn()) {
            return;
        }
        Graphics2D g = (Graphics2D) event.getGraphics();
        Rectangle bounds = getMapBounds();
        Rectangle iconBounds = getIconBounds();
        Rectangle closeBounds = getCloseBounds();
        Rectangle showRegionBounds = getShowRegionBounds();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, iconHovered || visible ? 1F : 0.8F));
        g.drawImage(MAP_ICON, iconBounds.x, iconBounds.y, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
        if (visible) {
            g.setClip(bounds);
            g.drawImage(mapChunk, bounds.x, bounds.y, getMapWidth(), getMapHeight(), null);
            g.setColor(Color.BLACK);
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

            g.setColor(Application.COLOUR_SCHEME.getHighlight());
            if (closePressed) {
                g.setColor(Application.COLOUR_SCHEME.getClicked());
            } else if (closeHovered) {
                g.setColor(Application.COLOUR_SCHEME.getBright());
            }

            if (showRegionPressed || showingRegion) {
                g.drawImage(SHOW_REGIONS_ICON_ON, showRegionBounds.x, showRegionBounds.y, null);
            } else if (showRegionHovered) {
                g.drawImage(SHOW_REGIONS_ICON_HOVER, showRegionBounds.x, showRegionBounds.y, null);
            } else {
                g.drawImage(SHOW_REGIONS_ICON_OFF, showRegionBounds.x, showRegionBounds.y, null);
            }

            Stroke oldStroke = g.getStroke();
            g.setStroke(new BasicStroke(4));
            g.draw(new Line2D.Float(closeBounds.x, closeBounds.y, closeBounds.x + closeBounds.width, closeBounds.y + closeBounds.height));
            g.draw(new Line2D.Float(closeBounds.x + closeBounds.width, closeBounds.y, closeBounds.x, closeBounds.y + closeBounds.height));
            g.setStroke(oldStroke);

            AffineTransform transform = new AffineTransform();
            transform.translate(POS_X + bounds.x - bounds.x, POS_Y + bounds.y - bounds.y);
            transform.scale(1 / zoom, 1 / zoom);
            transform.translate(-offset.x, -offset.y);
            g.setTransform(transform);

//            if (showingRegion) {
//                Font old = g.getFont();
//                float size = (float) ((zoom - ZOOM_MIN) * 5.0f);
//                if (size < 16.0f) {
//                    size = 16.0f;
//                }
//                g.setFont(g.getFont().deriveFont(size).deriveFont(Font.BOLD));
//                for (Region region : regionTracker.getRegions()) {
//                    Point start = region.getStart();
//                    Point end = region.getEnd();
//
//
//                    String number = String.valueOf(region.getNumberPlayers());
//
//                    int width = g.getFontMetrics().stringWidth(number);
//                    g.setColor(Color.RED);
//                    g.drawString(number,
//                            start.x +(((end.x - start.x) - (width / 2)) / 2), start.y + (Region.HEIGHT / 2));
//                    g.setColor(Color.BLACK);
//                }
//                g.setFont(old);
//            }

            markers.forEach(marker -> {
                marker.render(g, marker.getMarker(), marker.getPoint());
            });
        }
    }

    @Override
    public void start() {
        Session.get().getEventBus().register(regionTracker);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean hasOptions() {
        return false;
    }

    public static Point pointForTile(int tileX, int tileY) {
        return new Point (tileX, MAP.getHeight() - tileY);
    }

    public static Point pointForTile(Tile tile) {
        if (tile == null) {
            return new Point(-1, -1);
        }
        return pointForTile(tile.getX(), tile.getY());
    }

    public static Tile tileForPoint(Point point) {
        return new Tile(point.x, MAP.getHeight() - point.y);
    }

    public void setOffset(Point offset) {
        int lastOffsetX = this.offset.x;
        int lastOffsetY = this.offset.y;
        this.offset = offset;
        try {
            refresh();
        } catch (Exception ignored) {
            offset.x = lastOffsetX;
            offset.y = lastOffsetY;
        }
    }

    public boolean isVisible() {
        return visible;
    }
}


