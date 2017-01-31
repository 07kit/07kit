package com.kit.plugins.map.marker;

import com.kit.Application;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Tile;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.Application;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Tile;
import com.kit.plugins.map.WorldMapPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DeathMarker extends Marker {

    private Tile location;
    public static BufferedImage DEATH_MARKER;

    static {
        try {
            DEATH_MARKER = ImageIO.read(WorldMapPlugin.class.getResourceAsStream("/skull-crossbones-icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DeathMarker(WorldMapPlugin plugin, Tile location) {
        super(plugin, DEATH_MARKER);
        this.location = location;
    }

    @Override
    public Point getPoint() {
        return WorldMapPlugin.pointForTile(location);
    }

    @Override
    public void render(Graphics2D g, BufferedImage markerIcon, Point p) {
        drawMarker(g, markerIcon, p);

        g.setColor(Application.COLOUR_SCHEME.getText());
        PaintUtils.drawString(g, "You died here!", p.x + markerIcon.getWidth() / 2, p.y);

    }
}
