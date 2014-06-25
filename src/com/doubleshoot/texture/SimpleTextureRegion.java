package com.doubleshoot.texture;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class SimpleTextureRegion {

	public static ITextureRegion wholeTexture(ITexture tex) {
		return new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
	}

	public static ITiledTextureRegion titledTexture(ITexture tex, int cols, int rows) {
		return TiledTextureRegion.create(tex, 0, 0, tex.getWidth(), tex.getHeight(), cols, rows);
	}
	
	public static ITiledTextureRegion join(ITextureRegion... subRegions) {
		ITexture tex = subRegions[0].getTexture();
		return new TiledTextureRegion(tex, subRegions);
	}
	
}
