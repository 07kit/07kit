/*
 * This library is dual-licensed: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation. For the terms of this
 * license, see licenses/gpl_v3.txt or <http://www.gnu.org/licenses/>.
 *
 * You are free to use this library under the terms of the GNU General
 * Public License, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * Alternatively, you can license this library under a commercial
 * license, as set out in licenses/commercial.txt.
 */

package ch.swingfx.color;

import java.awt.Color;

/**
 * Utility for working with colors
 * @author Heinrich Spreiter
 *
 */
public final class ColorUtil {
	private ColorUtil() {
		//
	}
	
	/**
	 * Creates a new {@link Color} with RGB from <code>color</code> and the alpha value  of <code>alpha</code>
	 * @param color {@link Color} you want to have with <code>alpha</code>
	 * @param alpha alpha value you want to set. Must be between 0f and 1f
	 * @return new {@link Color} with <code>alpha</code>
	 */
	public static Color withAlpha(Color color, float alpha) {
		final int newAlpha = floatAlphaToIntAlpha(alpha);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), newAlpha);
	}

	/**
	 * Converts a float value between 0f and 1f to an int value
	 * between 0 and 255
	 * @param alpha value between 0f and 1f
	 * @return int value between 0 and 255
	 */
	public static int floatAlphaToIntAlpha(float alpha) {
		if(alpha < 0f || alpha > 1f) {
			throw new IllegalArgumentException("alpha must be between 0f and 1f. value: " + alpha);
		}
		return (int) (255 * alpha);
	}
	
}
