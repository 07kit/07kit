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
 * Enum off all supported API types for translucent and shaped windows<br />
 * For more details see http://download.oracle.com/javase/tutorial/uiswing/misc/trans_shaped_windows.html#6u10<br />
 * Note: Only Sun/Oracle JREs support sun.awt.AWTUtilities. Other JDK/JREs (example OpenJDK) do not support the private API
 * @author Heinrich Spreiter
 *
 */
public enum ApiType {
	/** support for official api */
	OFFICIAL,
	/** support for sun.awt.AWTUtilities api */
	PRIVATE,
	/** no support for any api */
	NONE
}