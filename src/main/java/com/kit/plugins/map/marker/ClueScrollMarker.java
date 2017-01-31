package com.kit.plugins.map.marker;

import com.kit.Application;
import com.kit.api.util.PaintUtils;
import com.kit.plugins.cluescrolls.ClueScroll;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.Application;
import com.kit.api.util.PaintUtils;
import com.kit.plugins.cluescrolls.ClueScroll;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.plugins.map.WorldMapPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ClueScrollMarker extends Marker {

	public static BufferedImage POS_MARKER;

	static {
		try {
			POS_MARKER = ImageIO.read(WorldMapPlugin.class.getResourceAsStream("/scroll.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ClueScroll scroll;

	public ClueScrollMarker(WorldMapPlugin plugin, ClueScroll scroll) {
		super(plugin, POS_MARKER);
		this.scroll = scroll;
	}

	@Override
	public Point getPoint() {
		if (scroll.getArea() != null) {
			return WorldMapPlugin.pointForTile(scroll.getArea().getCenter());
		}
		return WorldMapPlugin.pointForTile(scroll.getLocation());
	}

	public ClueScroll getScroll() {
		return scroll;
	}

	@Override
	public void render(Graphics2D g, BufferedImage markerIcon, Point p) {
		drawMarker(g, markerIcon, p);
		if (scroll.getArea() != null) {
			Rectangle bounds = scroll.getArea().getBounds();
			g.draw(bounds);
		}
		g.setColor(Application.COLOUR_SCHEME.getText());
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		PaintUtils.drawString(g, "Clue Scroll!", p.x + markerIcon.getWidth() / 2, p.y);
	}
}
