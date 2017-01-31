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

package ch.swingfx.text;

import java.awt.FontMetrics;
import java.text.BreakIterator;

/**
 * Utility class to work with text
 * @author Heinrich Spreiter
 *
 */
public final class TextUtil {
	private TextUtil() {
		//
	}

	/**
	 * This method calculates the preferred height for
	 * a rectangle that has a width of <code>maxWidth</code>
	 * and should display the String <code>text</code> inside it
	 * with line breaks
	 * @param fm {@link FontMetrics} to use
	 * @param maxWidth the maximum width of the rectangle
	 * @param text the text we want to display
	 * @return the preferred height
	 */
	public static int calculatePreferredHeight(FontMetrics fm, int maxWidth,
			String text) {
				if("".equals(text)) {
					return 0;
				}
				// utility that helps us to break the lines
				final BreakIterator bi = BreakIterator.getLineInstance();
				bi.setText(text);
				
				int lineCount = 0;
				final int lineHeight = fm.getHeight();
				
				// offsets for String.substring(start, end);
				int startOffset = bi.first();
				int endOffset = bi.next();
				// we go over each possible line break that BreakIterator suggests.
				do {
					if(endOffset == text.length()) {
						// we are at the end. this would cause IllegalArgumentException
						// so we just subtract 1
						endOffset--;
					}
					// get the width of the current substring
					// and check if we are over the maximum width
					final String substring = text.substring(startOffset, endOffset);
					final int stringWidth = fm.stringWidth(substring);
					if(stringWidth > maxWidth) {
						// calculate how many lines we have to add.
						// If there is a very long string with no spaces
						// it could be that we have to add more than 1 line.
						int toAdd = (int) (Math.ceil((double) stringWidth / (double) maxWidth) - 1);
						lineCount+= toAdd;
						// we need to advance the startOffset so
						// we can start to search for a new line
						startOffset = bi.preceding(endOffset);
						bi.next();
					}
				} while((endOffset = bi.next()) != BreakIterator.DONE);
				// ensure that the rest of a line also gets a line 
				lineCount++;
				return lineHeight * lineCount;
	}

}
