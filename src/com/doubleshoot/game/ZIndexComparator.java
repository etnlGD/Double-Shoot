package com.doubleshoot.game;

import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityComparator;

public class ZIndexComparator implements IEntityComparator {

	@Override
	public int compare(final IEntity pEntityA, final IEntity pEntityB) {
		return pEntityA.getZIndex() - pEntityB.getZIndex();
	}
	
	private ZIndexComparator() {
	}
	
	private static ZIndexComparator sInstance;
	public static IEntityComparator instance() {
		if (sInstance == null)
			sInstance = new ZIndexComparator();
		
		return sInstance;
	}
	
}
