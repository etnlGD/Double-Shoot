package com.doubleshoot.texture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;

public class CachedRegionManager implements IRegionManager {
	private ITextureFactory mTextureFactory;
	private Map<String, ITextureRegion> mRegions = new HashMap<String, ITextureRegion>();
	
	public CachedRegionManager(ITextureFactory texFactory) {
		mTextureFactory = texFactory;
	}
	
	public ITextureRegion addRegion(String id, ITextureRegion region) {
		return mRegions.put(id, region);
	}
	
	public ITextureRegion addRegion(String id, String tex) throws IOException {
		ITexture texture = mTextureFactory.loadTexture(tex);
		return addRegion(id, SimpleTextureRegion.wholeTexture(texture));
	}
	
	public ITextureRegion addRegion(String id, String tex,
			float x, float y, float w, float h) throws IOException {
		ITexture texture = mTextureFactory.loadTexture(tex);
		return addRegion(id, new TextureRegion(texture, x, y, w, h));
	}
	
	@Override
	public ITextureRegion getRegion(String id) {
		return mRegions.get(id);
	}
	
}
