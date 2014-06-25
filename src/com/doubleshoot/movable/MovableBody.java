package com.doubleshoot.movable;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class MovableBody implements IMovable {
	private Body mBody;
	private static float RATIO = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	private static float INV_RATIO = 1/RATIO;
	
	public MovableBody(Body body) {
		assert(body != null);
		mBody = body;
	}
	
	@Override
	public Vector2 getPosition() {
		return copyMul(mBody.getPosition(), RATIO);
	}

	private static Vector2 copyMul(Vector2 vec, float ratio) {
		return vec.cpy().mul(ratio);
	}

	@Override
	public void setPosition(Vector2 pos) {
		float angle = mBody.getAngle();
		mBody.setTransform(copyMul(pos, INV_RATIO), angle);
	}

	@Override
	public Vector2 getVelocity() {
		return copyMul(mBody.getLinearVelocity(), RATIO);
	}

	@Override
	public void setVolocity(Vector2 vel) {
		mBody.setLinearVelocity(copyMul(vel, INV_RATIO));
	}

}
