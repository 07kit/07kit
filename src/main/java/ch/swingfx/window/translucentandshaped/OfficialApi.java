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

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Shape;
import java.awt.Window;
import java.lang.reflect.Method;

/**
 * Wrapper around the official API for creating translucent and shaped windows. As
 * of JRE 7 there is official support for these types of windows.<br />
 * You shouldn't use this class directly. Instead use {@link TranslucentAndShapedWindowApiFactory} to
 * get the correct instance depending on your system JRE. For more details see
 * http://download.oracle.com/javase/tutorial/uiswing/misc/trans_shaped_windows.html#6u10<br />
 * Note: Only Sun/Oracle JREs support sun.awt.AWTUtilities. Other JDK/JREs (example OpenJDK) do not support the private API
 * @author Heinrich Spreiter
 *
 */
public class OfficialApi implements ITranslucentAndShapedWindowApi {
	private static final int OPAQUE = 255;
	private static final int NOT_OPAQUE = 0;

	/** true if we support this API */
	private static boolean isSupported = false;
	
	/** enum constants from java.awt.GraphicsDevice$WindowTranslucency*/
	private static Object PERPIXEL_TRANSPARENT;
	private static Object TRANSLUCENT;
	private static Object PERPIXEL_TRANSLUCENT;
	
	// methods wrapped around the official APIs
	private static Method isTranslucencySupported;
	private static Method setWindowOpacity;
	private static Method getWindowOpacity;
	private static Method setWindowShape;
	private static Method getWindowShape;
	private static Method isTranslucencyCapable;
	
	static {
		try {
			@SuppressWarnings("rawtypes")
			Class translucencyClass = Class.forName("java.awt.GraphicsDevice$WindowTranslucency");

			// methods and enums of WindowTranslucency
			Method valueOf = translucencyClass.getMethod("valueOf", String.class);
			PERPIXEL_TRANSPARENT = valueOf.invoke(translucencyClass, "PERPIXEL_TRANSPARENT");
			TRANSLUCENT = valueOf.invoke(translucencyClass, "TRANSLUCENT");
			PERPIXEL_TRANSLUCENT = valueOf.invoke(translucencyClass, "PERPIXEL_TRANSLUCENT");
			
			// official api methods
			isTranslucencySupported = GraphicsDevice.class.getMethod("isWindowTranslucencySupported", translucencyClass);
			setWindowOpacity = Window.class.getMethod("setOpacity", float.class);
			getWindowOpacity = Window.class.getMethod("getOpacity");
			setWindowShape = Window.class.getMethod("setShape", Shape.class);
			getWindowShape = Window.class.getMethod("getShape");
			isTranslucencyCapable = GraphicsConfiguration.class.getMethod("isTranslucencyCapable");
			isSupported = true;
		} catch(Exception e) {
			isSupported = false;
		}
	}
	
	public static boolean isSupported() {
		return isSupported;
	}
	
	public boolean isTranslucencySupported(Translucency translucencyKind, GraphicsDevice gd) {
		Object kind = null;
		switch(translucencyKind) {
			case PERPIXEL_TRANSLUCENT:
				kind = PERPIXEL_TRANSLUCENT;
				break;
			case TRANSLUCENT:
				kind = TRANSLUCENT;
				break;
			case PERPIXEL_TRANSPARENT:
				kind = PERPIXEL_TRANSPARENT;
				break;
		}
		
		try {
			return (Boolean) isTranslucencySupported.invoke(gd, kind);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void setWindowOpacity(Window window, float opacity) {
		try {
			setWindowOpacity.invoke(window, opacity);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public float getWindowOpacity(Window window) {
		try {
			return (Float) getWindowOpacity.invoke(window);
		} catch(Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	public void setWindowShape(Window window, Shape shape) {
		try {
			setWindowShape.invoke(window, shape);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Shape getWindowShape(Window window) {
		try {
			return (Shape) getWindowShape.invoke(window);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setWindowOpaque(Window window, boolean isOpaque) {
		// prevent setting if not supported
		if(!isTranslucencyCapable(window.getGraphicsConfiguration()) ||
				!isTranslucencySupported(Translucency.PERPIXEL_TRANSLUCENT, window.getGraphicsConfiguration().getDevice())) {
			return;
		}
		final Color background = window.getBackground();
		final int alpha = isOpaque ? OPAQUE : NOT_OPAQUE;
		window.setBackground(new Color(background.getRed(), background.getGreen(), background.getBlue(), alpha));
	}

	public boolean isWindowOpaque(Window window) {
		return window.getBackground().getAlpha() == OPAQUE;
	}

	public boolean isTranslucencyCapable(GraphicsConfiguration gc) {
		try {
			return (Boolean) isTranslucencyCapable.invoke(gc);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public ApiType getApiType() {
		return ApiType.OFFICIAL;
	}
}
