package com.doubleshoot.image;

import android.graphics.Bitmap;

public class ImageProcesser {
	
	public static void copyRect(Bitmap src, Bitmap dst,
			int srcX, int srcY, int dstX, int dstY, int width, int height) {
		width = width <= 0 ? src.getWidth() : width;
		height= height<= 0 ? src.getHeight(): height;
		
		int color;
		for (int dy = 0; dy < width; ++dy) {
			for (int dx = 0; dx < height; ++dx) {
				color = src.getPixel(srcX + dx, srcY + dy);
				dst.setPixel(dstX + dx, dstY + dy, color);
			}
		}
	}
	
	public static Bitmap invert(Bitmap origin) {
		int rows = origin.getHeight();
		int cols = origin.getWidth();
		Bitmap inverted = Bitmap.createBitmap(cols, rows, origin.getConfig());
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				inverted.setPixel(x, rows - y - 1, origin.getPixel(x, y));
			}
		}
		return inverted;
	}
	
	public static Bitmap combine(Bitmap left, Bitmap center, Bitmap right) {
		int line1 = left.getWidth()/2;
		int line2 = line1 + center.getWidth();
		int width = line2 + (right.getWidth() - right.getWidth()/2);
		int height = Math.max(center.getHeight(),
						Math.max(left.getHeight(), right.getHeight()));
		Bitmap result = Bitmap.createBitmap(width, height, left.getConfig());

		copyRect(left, result, 0, 0, 0, 0, left.getWidth()/2, -1);
		copyRect(center, result, 0, 0, line2, 0, -1, -1);
		copyRect(right, result, right.getWidth()/2, 0, line2, 0, right.getWidth()/2, -1);
		return result;
	}
}
