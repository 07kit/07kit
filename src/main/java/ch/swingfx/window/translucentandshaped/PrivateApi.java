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
import java.lang.reflect.Method;

/**
 * Wrapper around com.sun.awt.AWTUtilities API for Translucent and Shaped Window support.<br />
 * As of JRE 6 update 10 there is a private API for creating these types of windows.<br />
 * You shouldn't use this class directly. Instead use {@link TranslucentAndShapedWindowApiFactory} to
 * get the correct instance depending on your system JRE. For more details see
 * http://download.oracle.com/javase/tutorial/uiswing/misc/trans_shaped_windows.html#6u10<br />
 * Note: Only Sun/Oracle JREs support sun.awt.AWTUtilities. Other JDK/JREs (example OpenJDK) do not support the private API
 * @author Heinrich Spreiter
 *
 */
public class PrivateApi implements ITranslucentAndShapedWindowApi {
	/** true if we support this API */
	private static boolean isSupported = false;
	
	/** enum constants from com.sun.awt.AWTUtilities$Translucency*/
	private static Object PERPIXEL_TRANSPARENT;
	private static Object TRANSLUCENT;
	private static Object PERPIXEL_TRANSLUCENT;
	
	// methods wrapped around com.sun.awt.AWTUtilities
	private static Method isTranslucencySupported;
	private static Method setWindowOpacity;
	private static Method getWindowOpacity;
	private static Method setWindowShape;
	private static Method getWindowShape;
	private static Method setWindowOpaque;
	private static Method isWindowOpaque;
	private static Method isTranslucencyCapable;
	
	static {
		try {
			// classes we wrap
			@SuppressWarnings("rawtypes")
			Class awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
			@SuppressWarnings("rawtypes")
			Class translucencyClass = Class.forName("com.sun.awt.AWTUtilities$Translucency");
			
			// methods and enums of Translucency
			Method valueOf = translucencyClass.getMethod("valueOf", String.class);
			PERPIXEL_TRANSPARENT = valueOf.invoke(translucencyClass, "PERPIXEL_TRANSPARENT");
			TRANSLUCENT = valueOf.invoke(translucencyClass, "TRANSLUCENT");
			PERPIXEL_TRANSLUCENT = valueOf.invoke(translucencyClass, "PERPIXEL_TRANSLUCENT");
			
			// methods of AWTUtilities
			isTranslucencySupported = awtUtilitiesClass.getMethod("isTranslucencySupported", translucencyClass);
			setWindowOpacity  = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class);
			getWindowOpacity = awtUtilitiesClass.getMethod("getWindowOpacity", Window.class);
			setWindowShape  = awtUtilitiesClass.getMethod("setWindowShape", Window.class, Shape.class);
			getWindowShape = awtUtilitiesClass.getMethod("getWindowShape", Window.class);
			setWindowOpaque = awtUtilitiesClass.getMethod("setWindowOpaque", Window.class, boolean.class);
			isWindowOpaque  = awtUtilitiesClass.getMethod("isWindowOpaque", Window.class);
			isTranslucencyCapable = awtUtilitiesClass.getMethod("isTranslucencyCapable", GraphicsConfiguration.class);
			isSupported = true;
		} catch (Exception e) {
			isSupported = false;
		}
	}
	
	/**
	 * Test if com.sun.awt.AWTUtilities (jdk 6u10) is available
	 * @return true if com.sun.awt.AWTUtilities (jdk 6u10) is available
	 */
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
			return (Boolean) isTranslucencySupported.invoke(null, kind);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public void setWindowOpacity(Window window, float opacity) {
		try {
			setWindowOpacity.invoke(null, window, opacity);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public float getWindowOpacity(Window window) {
		try {
			return (Float) getWindowOpacity.invoke(null, window);
		} catch(Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	public void setWindowShape(Window window, Shape shape) {
		try {
			setWindowShape.invoke(null, window, shape);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Shape getWindowShape(Window window) {
		try {
			return (Shape) getWindowShape.invoke(null, window);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setWindowOpaque(Window window, boolean isOpaque) {
		try {
			// prevent setting if not supported
			if(!isTranslucencyCapable(window.getGraphicsConfiguration()) ||
					!isTranslucencySupported(Translucency.PERPIXEL_TRANSLUCENT, window.getGraphicsConfiguration().getDevice())) {
				return;
			}
			setWindowOpaque.invoke(null, window, isOpaque);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isWindowOpaque(Window window) {
		try {
			return (Boolean) isWindowOpaque.invoke(null, window);
		} catch(Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	public boolean isTranslucencyCapable(GraphicsConfiguration gc) {
		try {
			return (Boolean) isTranslucencyCapable.invoke(null, gc);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public ApiType getApiType() {
		return ApiType.PRIVATE;
	}

}
