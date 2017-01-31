package com.kit.plugins.map.marker;

import com.kit.Application;
import com.kit.api.util.PaintUtils;
import com.kit.core.Session;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.Application;
import com.kit.api.util.PaintUtils;
import com.kit.core.Session;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.Application;
import com.kit.plugins.map.WorldMapPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LocalPlayerMarker extends Marker {

	public static BufferedImage POS_MARKER;

	static {
		try {
			POS_MARKER = ImageIO.read(WorldMapPlugin.class.getResourceAsStream("/map-marker.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LocalPlayerMarker(WorldMapPlugin plugin) {
		super(plugin, POS_MARKER);
	}

	@Override
	public Point getPoint() {
		return WorldMapPlugin.pointForTile(Session.get().player.getTile());
	}

	@Override
	public void render(Graphics2D g, BufferedImage markerIcon, Point p) {
		drawMarker(g, markerIcon, p);

		g.setColor(Application.COLOUR_SCHEME.getText());
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		PaintUtils.drawString(g, "You are here!", p.x + markerIcon.getWidth() / 2, p.y);
	}
}
