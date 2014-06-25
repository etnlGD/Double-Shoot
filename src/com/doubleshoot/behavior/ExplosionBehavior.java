package com.doubleshoot.behavior;

import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;

import com.doubleshoot.schedule.IScheduler;
import com.doubleshoot.shape.ShapeFactory;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class ExplosionBehavior implements IBehavior, IAnimationListener {
	private static final long ANIM_FRAME_DURATION = 60;
	private ShapeFactory mExplosionFactory;
	private IScheduler mScheduler;
	
	public ExplosionBehavior(IScheduler pScheduler, ShapeFactory pFactory) {
		mScheduler = pScheduler;
		mExplosionFactory = pFactory;
	}
	
	private IAreaShape makeExplosion(IAreaShape exploded) {
		IAreaShape explosion = mExplosionFactory.createShape();
		explosion.setScaleCenter(0, 0);
		float scale = exploded.getWidthScaled()/explosion.getWidth() * 1.2f;
		float scaledW = explosion.getWidth() * scale;
		float scaledH = explosion.getHeight() * scale;
		
		float centerX = exploded.getX() + exploded.getWidthScaled()/2;
		float centerY = exploded.getY() + exploded.getHeightScaled()/2;
		float x = centerX - scaledW/2;
		float y = centerY - scaledH/2;
		explosion.setScale(scale);
		explosion.setPosition(x, y);
		return explosion;
	}
	
	@Override
	public void onActivated(BaseShooter host, Harmful source, float damage) {
		if (host.hasTag("Huge"))
			return; // TODO ugly fix
		
		IAreaShape exploded = host.getShape();
		IAreaShape explosion = makeExplosion(exploded);
		exploded.getParent().attachChild(explosion);
		
		final AnimatedSprite sprite = (AnimatedSprite) explosion;
		sprite.animate(ANIM_FRAME_DURATION, false, this);
	}

	@Override
	public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
			int pInitialLoopCount) {
	}

	@Override
	public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
			int pOldFrameIndex, int pNewFrameIndex) {
	}

	@Override
	public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
			int pRemainingLoopCount, int pInitialLoopCount) {
	}

	@Override
	public void onAnimationFinished(final AnimatedSprite pAnimatedSprite) {
		mScheduler.schedule(new Runnable() {
			
			@Override
			public void run() {
				pAnimatedSprite.detachSelf();
			}
		});
	}
}