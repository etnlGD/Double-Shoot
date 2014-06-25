package com.doubleshoot.shooter;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.bullet.Barrel;
import com.doubleshoot.bullet.BulletEmitter;
import com.doubleshoot.object.AbstractGOFactory;

public abstract class ShooterFactory<T extends BaseShooter> extends AbstractGOFactory<T> {
	private BulletEmitter mPrototype = new BulletEmitter();
	private ShootPolicy mPolicy;
	private float mInitHealth;
	private float mDamage;
	
	public ShooterFactory(float pDamage, float pInitHealth) {
		mInitHealth = pInitHealth;
		mDamage = pDamage;
	}
	
	public void setShootPolicy(ShootPolicy policy) {
		mPolicy = policy;
	}
	
	public void setBarrel(int channel, Barrel barrel) {
		mPrototype.setBarrel(channel, barrel);
	}

	@Override
	protected double getRotation(Vector2 vel) {
		return 0;
	}
	
	@Override
	protected void onObjectCreated(T created) {
		super.onObjectCreated(created);
		
		created.resetInitHealth(mInitHealth, true);
		created.setDamageWhenCollide(mDamage);
		created.setBulletEmitter(mPrototype.copy());
		created.setShootPolicy(mPolicy);
	}
	
}
