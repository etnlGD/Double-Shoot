package com.doubleshoot.motion;

import org.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.movable.IMovable;

public class UniformVelocityMotion implements IMotion {
	private Vector2 mVel;
	
	public UniformVelocityMotion() {
	}
	
	public UniformVelocityMotion(Vector2 pVel) {
		setVelocity(pVel);
	}

	public void setVelocity(Vector2 pVel) {
		mVel = pVel.cpy();
	}

	@Override
	public IUpdateHandler createMotionModifier(final IMovable pMovable) {
		return new IUpdateHandler() {

			@Override
			public void reset() {
			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				pMovable.setVolocity(mVel);
			}
		};
	}

}