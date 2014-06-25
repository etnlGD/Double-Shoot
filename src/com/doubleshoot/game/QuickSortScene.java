package com.doubleshoot.game;

import java.util.Collections;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityComparator;
import org.andengine.entity.scene.Scene;

// ugly fix for our game.
// Andengine is just a shit!
public class QuickSortScene extends Scene {
	private IEntity mNegtiveChildrenParent;
	
	public QuickSortScene() {
		mNegtiveChildrenParent = new Entity();
		mNegtiveChildrenParent.setZIndex(Integer.MIN_VALUE);
		attachChild(mNegtiveChildrenParent);
	}
	
	public IEntity getNegativeRoot() {
		return mNegtiveChildrenParent;
	}
	
	@Override
	public void sortChildren() {
		sortChildren(ZIndexComparator.instance());
	}
	
	@Override
	public void sortChildren(final IEntityComparator pEntityComparator) {
		if(this.mChildren != null) {
			Collections.sort(mChildren, pEntityComparator);
		}
	}
	
	@Override
	public void sortChildren(boolean pImmediate) {
		if(pImmediate) {
			sortChildren();
		} else {
			this.mChildrenSortPending = true;
		}
	}
	
}
