package com.doubleshoot.bullet;

import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.doubleshoot.object.AbstractGOFactory;
import com.doubleshoot.object.GOEnvironment;

public class BulletPrototype extends AbstractGOFactory<Bullet> {
	private float mDamage = 20;
	private float mSpeed = 10;
	private boolean mPenetrating = true;

	public BulletPrototype() {
		setBodyType(BodyType.DynamicBody);
	}

	@Override
	protected Bullet createObject(GOEnvironment env, IAreaShape shape, Body body) {
		shape.setZIndex(-100);
		body.setLinearVelocity(body.getLinearVelocity().nor().mul(mSpeed));

		return new Bullet(mDamage, mPenetrating, shape, body, env);
	}

	public void setSpeed(float speed) {
		mSpeed = speed;
	}

	public float getSpeed() {
		return mSpeed;
	}

	public void setDamage(float pDamage) {
		mDamage = pDamage;
	}

	public float getDamage() {
		return mDamage;
	}

	public void setPenetrating(boolean b) {
		mPenetrating = b;
	}

	public boolean isPenetrating() {
		return mPenetrating;
	}
}