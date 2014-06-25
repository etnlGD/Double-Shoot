package com.doubleshoot.behavior;

import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.util.color.Color;

import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class WoundedShining implements IBehavior{
	private IEntityModifier mAnim;
	
	
	public WoundedShining() {
		mAnim = new LoopEntityModifier(
				new SequenceEntityModifier(
						new ColorModifier(0.02f, Color.WHITE, Color.BLACK),
						new ColorModifier(0.02f, Color.BLACK, Color.WHITE)), 2);
	}
	
	@Override
	public void onActivated(BaseShooter host, Harmful source, float damage) {
		if (mAnim.isFinished())
			mAnim.reset();
		
		if (notFinished()) return;
		host.getShape().registerEntityModifier(mAnim);
	}

	private boolean notFinished() {
		float elapsed = mAnim.getSecondsElapsed();
		float duration = mAnim.getDuration();
		return elapsed > 0 && elapsed < duration;
	}

}