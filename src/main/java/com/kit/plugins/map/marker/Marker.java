package com.kit.plugins.map.marker;

import com.kit.plugins.map.WorldMapPlugin;
import com.kit.plugins.map.WorldMapPlugin;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Marker {

	private WorldMapPlugin plugin;
	private BufferedImage marker;

	public abstract Point getPoint();

	public Marker(WorldMapPlugin plugin, BufferedImage marker) {
		this.plugin = plugin;
		this.marker = marker;
	}

	public void drawMarker(Graphics2D g, BufferedImage markerIcon, Point point) {
		if (markerIcon != null) {
			g.drawImage(markerIcon, point.x - markerIcon.getWidth() / 2, point.y - markerIcon.getHeight() / 2, null);
		}
	}

	public void moveTo() {
		if (!plugin.isVisible()) {
			plugin.show();
			plugin.refresh();
		}
		Point p = getPoint();
		int widthOffset = plugin.getMapWidth() / 2;
		int heightOffset = plugin.getMapHeight() / 2;

		int x = p.x;
		int y = p.y;

		if (x - widthOffset > 0) {
			x -= widthOffset;
		} else {
			x = 0;
		}
		if (y - heightOffset > 0) {
			y -= heightOffset;
		} else {
			y = 0;
		}

		plugin.setOffset(new Point(x, y));
	}

	public abstract void render(Graphics2D g, BufferedImage markerIcon, Point point);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Marker marker1 = (Marker) o;

		if (!plugin.equals(marker1.plugin)) return false;
		return marker != null ? marker.equals(marker1.marker) : marker1.marker == null;
	}

	@Override
	public int hashCode() {
		int result = plugin.hashCode();
		result = 31 * result + (marker != null ? marker.hashCode() : 0);
		return result;
	}

	public WorldMapPlugin getPlugin() {
		return plugin;
	}

	public BufferedImage getMarker() {
		return marker;
	}
}
