package com.doubleshoot.texture;

import java.io.IOException;

import org.andengine.opengl.texture.ITexture;

public interface ITextureFactory {
	
	public ITexture loadTexture(String pathName) throws IOException;
	
}