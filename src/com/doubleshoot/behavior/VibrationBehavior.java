package com.doubleshoot.behavior;

import org.andengine.engine.Engine;

import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class VibrationBehavior implements IBehavior {
	private long mMilliseconds;
	private Engine mEngine;
	
	public VibrationBehavior(Engine pEngine, long pMilliseconds) {
		mEngine = pEngine;
		mMilliseconds = pMilliseconds;
	}

	@Override
	public void onActivated(BaseShooter host, Harmful source, float damage) {
		if (damage > 0)
			mEngine.vibrate(mMilliseconds);
	}

}
