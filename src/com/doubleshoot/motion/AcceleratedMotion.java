package com.doubleshoot.motion;

import org.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.movable.IMovable;

public class AcceleratedMotion implements IMotion {
	private Vector2 mAcceleration;
	
	public AcceleratedMotion(Vector2 pAcceleration) {
		mAcceleration = pAcceleration;
	}

	@Override
	public IUpdateHandler createMotionModifier(final IMovable pMovable) {
		return new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				// v = v0 + a*t;
				Vector2 vel = pMovable.getVelocity().add(
						mAcceleration.cpy().mul(pSecondsElapsed));
				
				pMovable.setVolocity(vel);
			}

			@Override
			public void reset() {
				
			}
		};
	}
}
