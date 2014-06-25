package com.doubleshoot.texture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;

import android.content.res.AssetManager;

public class CachedTextureFactory implements ITextureFactory {
	private String mBasePath = "gfx/";
	private TextureOptions mOptions = TextureOptions.BILINEAR;
	private AssetManager mAssetManager;
	private TextureManager mTextureManager;
	private Map<String, ITexture> mCachedTextures = new HashMap<String, ITexture>();
	
	public CachedTextureFactory(AssetManager pAssetManager, TextureManager pTextureManager) {
		this.mAssetManager = pAssetManager;
		this.mTextureManager = pTextureManager;
	}

	@Override
	public ITexture loadTexture(String relativePath) throws IOException {
		String path = mBasePath + relativePath;
		ITexture texture = mCachedTextures.get(path);
		if (texture == null) {
			texture = new AssetBitmapTexture(mTextureManager, mAssetManager, path, mOptions);
			texture.load();
			
			mCachedTextures.put(path, texture);
		}
		
		return texture;
	}
}
