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

package ch.swingfx.awt;

import java.awt.Insets;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility to help in OS specific graphics environment tasks.
 * @author Heinrich Spreiter
 *
 */
public final class GraphicsEnvironmentUtil {
	private GraphicsEnvironmentUtil() {
		//
	}
	
	/**
	 * Depending on the JRE Toolkit.getScreenInsets() doesn't always work on OS X.<br />
	 * Because the menu bar is always 22 we can provide
	 * the screen insets for the screen with the menu bar in this constant.
	 */
	public static final Insets OSX_MENU_BAR_SCREEN_INSETS = new Insets(22, 0, 0, 0);
	
	/** java.awt.graphicsenv=sun.awt.X11GraphicsEnvironment */
	public static final boolean isX11GraphicsEnvironment;
	/** java.awt.graphicsenv=apple.awt.CGraphicsEnvironment (OS X) */
	public static final boolean isCGraphicsEnvironment;
	/** java.awt.graphicsenv=sun.awt.Win32GraphicsEnvironment */
	public static final boolean isWin32GraphicsEnvironment;
	
	static {
		final String graphicsEnv = System.getProperty("java.awt.graphicsenv");
		isWin32GraphicsEnvironment = "sun.awt.Win32GraphicsEnvironment".equals(graphicsEnv);
		isX11GraphicsEnvironment = "sun.awt.X11GraphicsEnvironment".equals(graphicsEnv);
		isCGraphicsEnvironment = "apple.awt.CGraphicsEnvironment".equals(graphicsEnv);
	}
	
	/**
	 * Get the x11 net workarea from a call to<br />
	 * <code>xprop -root -notype _NET_WORKAREA</code><br />
	 * On some systems (jvms) we can not get the real screen insets
	 * so we read _NET_WORKAREA from the command line.<br />
	 * The output is translated to an Rectangle
	 * Output: _NET_WORKAREA = 0, 0, 1680, 1025
	 * Rectangle: x, y, width, height
	 * @return the x11 net workarea or null if we can't read it
	 */
	public static Rectangle getX11RootNetWorkarea() {
		// workaround for x11 getScreenInsets problem.
		// on some systems (jvms) we can not get the real screen insets.
		// so we read _NET_WORKAREA from the command line
		final ProcessBuilder pc = new ProcessBuilder("xprop", "-root", "-notype", "_NET_WORKAREA");
		try {
			final Process process = pc.start();
			final InputStream inputStream = process.getInputStream();
			final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			final BufferedReader reader = new BufferedReader(inputStreamReader);
			// output: _NET_WORKAREA = 0, 0, 1680, 1025
			// x, y, width, height
			String line = reader.readLine();
			process.waitFor();
			reader.close();
			final String[] windowProperties = line.split("=")[1].trim().split(",");
			final int x = Integer.parseInt(windowProperties[0].trim());
			final int y = Integer.parseInt(windowProperties[1].trim());
			final int width = Integer.parseInt(windowProperties[2].trim());
			final int height = Integer.parseInt(windowProperties[3].trim());
			return new Rectangle(x, y, width, height);
		} catch (Exception e) {
			return null;
		}
	}
	
	
}
