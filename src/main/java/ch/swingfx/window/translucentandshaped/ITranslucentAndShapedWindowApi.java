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

package ch.swingfx.window.translucentandshaped;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Shape;
import java.awt.Window;

/**
 * Support for Translucent and Shaped Windows. It unifies the private (java 6) and official (java 7 and upwards) apis.
 * @author Heinrich Spreiter
 *
 */
public interface ITranslucentAndShapedWindowApi {
	public boolean isTranslucencySupported(Translucency translucencyKind, GraphicsDevice gd);
	public void setWindowOpacity(Window window, float opacity);
	public float getWindowOpacity(Window window);
	public void setWindowShape(Window window, Shape shape);
	public Shape getWindowShape(Window window);
	public void setWindowOpaque(Window window, boolean isOpaque);
	public boolean isWindowOpaque(Window window);
	public boolean isTranslucencyCapable(GraphicsConfiguration gc);
	public ApiType getApiType();
}
