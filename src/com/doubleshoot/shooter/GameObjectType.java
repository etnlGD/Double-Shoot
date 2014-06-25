package com.doubleshoot.shooter;

import com.badlogic.gdx.physics.box2d.Filter;

public enum GameObjectType {
	EnemyBullet(1, 8),
	EnemyPlane(2, 8|4),
	HeroBullet(4, 2),
	HeroPlane(8, 1|2|16),
	PowerUP(16, 8);
	
	GameObjectType(int category, int mask) {
		mFilter = new Filter();
		mFilter.categoryBits = (short) category;
		mFilter.maskBits = (short) mask;
	}
	
	public Filter getSharedFilter() {
		return mFilter;
	}
	
	private Filter mFilter;
}