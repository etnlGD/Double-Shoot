package com.doubleshoot.shooter;

import com.badlogic.gdx.physics.box2d.Filter;

public enum GameObjectType {
	EnemyBullet(1, 8|64),
	EnemyPlane(2, 4|8|16|64),
	HeroBullet(4, 2),
	HeroPlane(8, 1|2|16|32),
	AllPlane(16, 2|8|64),
	PowerUP(32, 8),
	AllEnemyObject(64, 1|2|16),
	AllObject(-1, -1);
	
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