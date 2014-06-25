package com.doubleshoot.hud;

import org.andengine.opengl.font.IFont;
import org.andengine.opengl.font.Letter;

public class FontUtils {

	public static int getMaxWidthInDigits(IFont pFont) {
		int maxWidth = 0;
		for (int i = 0; i < 10; i++) {
			Letter l = pFont.getLetter((char) ('0' + i));
			if (maxWidth < l.mWidth) {
				maxWidth = l.mWidth;
			}
		}
		
		return maxWidth;
	}
}
