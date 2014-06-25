package com.doubleshoot.object;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.doubleshoot.object.GOFactoryLoader.GOFactoryCallback;
import com.doubleshoot.texture.IRegionManager;

public class RegistryFiller {

	public static <T extends GameObject>
	void fill(GORegistry<T> registry, GOFactoryLoader<T> loader,
			VertexBufferObjectManager vbom, IRegionManager regions)
			throws Exception {
		final GORegistry<T> fRegistry = registry;
		loader.load(vbom, regions, new GOFactoryCallback<T>() {
			@Override
			public void onNewFactory(String name, GOFactory<T> factory) {
				fRegistry.registerFactory(name, factory);
			}
		});
	}
}
