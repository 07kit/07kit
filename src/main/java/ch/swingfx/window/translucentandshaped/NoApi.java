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
 * Class that represents no support for translucent and shaped windows because there is no api we can wrap.<br />
 * You shouldn't use this class directly. Instead use {@link TranslucentAndShapedWindowApiFactory} to
 * get the correct instance depending on your system JRE. For more details see
 * http://download.oracle.com/javase/tutorial/uiswing/misc/trans_shaped_windows.html#6u10<br />
 * Note: Only Sun/Oracle JREs support sun.awt.AWTUtilities. Other JDK/JREs (example OpenJDK) do not support the private API
 * @author Heinrich Spreiter
 *
 */
public class NoApi implements
		ITranslucentAndShapedWindowApi {

	public boolean isTranslucencySupported(Translucency translucencyKind, GraphicsDevice gd) {
		return false;
	}

	public void setWindowOpacity(Window window, float opacity) {

	}

	public float getWindowOpacity(Window window) {
		return 1f;
	}

	public void setWindowShape(Window window, Shape shape) {

	}

	public Shape getWindowShape(Window window) {
		return null;
	}

	public void setWindowOpaque(Window window, boolean isOpaque) {
	}

	public boolean isWindowOpaque(Window window) {
		return true;
	}

	public boolean isTranslucencyCapable(GraphicsConfiguration gc) {
		return false;
	}

	public ApiType getApiType() {
		return ApiType.NONE;
	}

}
