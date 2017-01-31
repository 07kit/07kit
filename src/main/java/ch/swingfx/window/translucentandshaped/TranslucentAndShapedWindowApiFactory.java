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

/**
 * Factory for creating {@link ITranslucentAndShapedWindowApi} objects depending on the JRE<br />
 * Prior to JRE 6 update 10 there is no support for this. As of JRE 6u10 there is support in private APIs.
 * As of JRE 7 there is official support for this.<br />
 * This factory figures out which one to use.<br />
 * Note: Only Sun/Oracle JREs support sun.awt.AWTUtilities. Other JDK/JREs (example OpenJDK) do not support the private API
 * @author Heinrich Spreiter
 */
public class TranslucentAndShapedWindowApiFactory {
	
	private static final ITranslucentAndShapedWindowApi api;
	
	static {
		if(OfficialApi.isSupported()) {
			api = new OfficialApi();
		} else if(PrivateApi.isSupported()) {
			api = new PrivateApi();
		} else {
			api = new NoApi();
		}
	}
	
	/**
	 * get a singleton instance of an {@link ITranslucentAndShapedWindowApi} object.
	 * @return a singleton instance of an {@link ITranslucentAndShapedWindowApi} object
	 */
	public static ITranslucentAndShapedWindowApi getApi() {
		return api;
	}
	
}
