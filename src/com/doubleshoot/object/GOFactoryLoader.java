package com.doubleshoot.object;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.doubleshoot.texture.IRegionManager;

public interface GOFactoryLoader<T extends GameObject> {
	
	public static interface GOFactoryCallback<T extends GameObject> {
		
		public void onNewFactory(String name, GOFactory<T> factory);
		
	}
	
	public void load(VertexBufferObjectManager vbom, IRegionManager regions,
			GOFactoryCallback<T> callback) throws Exception;
	
}
