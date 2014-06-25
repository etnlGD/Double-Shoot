package com.doubleshoot.bullet;

import com.doubleshoot.motion.IMotion;
import com.doubleshoot.object.GOFilter;

public class BulletMotionFilter implements GOFilter<Bullet> {
	private IMotion mMotion;
	
	public BulletMotionFilter(IMotion pMotion) {
		mMotion = pMotion;
	}
	
	@Override
	public void filter(Bullet bullet) {
		bullet.setMotion(mMotion);
	}

}
