package com.doubleshoot.shooter;

import com.doubleshoot.behavior.ExplosionBehavior;
import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.behavior.WoundedShining;
import com.doubleshoot.object.GOFilter;
import com.doubleshoot.schedule.IScheduler;
import com.doubleshoot.shape.ShapeFactory;

public class ShooterVisualFilter<T extends BaseShooter> implements GOFilter<T> {
	private IBehavior mExplosionBehavior;
	
	public ShooterVisualFilter(
			IScheduler pScheduler, ShapeFactory explosionShapeFactory) {
		mExplosionBehavior = new ExplosionBehavior(pScheduler, explosionShapeFactory);
	}
	
	@Override
	public void filter(T pShooter) {
		pShooter.addWoundedBehavior(new WoundedShining());
		pShooter.addDeadBehavior(mExplosionBehavior);
	}

}
