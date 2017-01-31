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
 * This enum is used in combination with {@link ITranslucentAndShapedWindowApi}.isTranslucencySupported()<br />
 * @author Heinrich Spreiter
 *
 */
public enum Translucency {
	/** shaping */
	PERPIXEL_TRANSPARENT,
	/** simple translucency (everything or nothing) */
	TRANSLUCENT,
	/** per-pixel translucency (example: Only window but not the button).<br/>
	 * check with isTranslucencyCapable(gc)  */
	PERPIXEL_TRANSLUCENT
}
