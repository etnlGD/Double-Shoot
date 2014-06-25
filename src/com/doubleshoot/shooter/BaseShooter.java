package com.doubleshoot.shooter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.bullet.Bullet;
import com.doubleshoot.bullet.BulletEmitter;
import com.doubleshoot.bullet.NewBulletCallback;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GameObject;

public abstract class BaseShooter extends GameObject
		implements Harmful, NewBulletCallback, IUpdateHandler {
	private static class TaggedAddition {
		String tag;
		AdditionDamage damage;
		
		TaggedAddition(String tag, AdditionDamage addition) {
			this.tag = tag;
			this.damage = addition;
		}
	}
	
	private List<TaggedAddition> mAdditionDamages = new LinkedList<TaggedAddition>();
	private BulletEmitter mBulletEmitter;
	private ShootPolicy mShootPolicy;
	private float mDamage;
	private float mInitialHealth = 1;
	private float mHealth = 1;
	
	private Set<IBehavior> mWoundedBehaviors = new HashSet<IBehavior>();
	private Set<IBehavior> mDeadBehaviors = new HashSet<IBehavior>();
	private Set<IBehavior> mHealBehaviors = new HashSet<IBehavior>();
	
	public BaseShooter(IAreaShape shape, Body body, final GOEnvironment pEnv) {
		super(body, shape, pEnv);
		
		assert(shape != null);
		body.setFixedRotation(true);
		shape.registerUpdateHandler(this);
	}
	
	@Override
	public void reset() {
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		if (mShootPolicy != null && mShootPolicy.shouldShoot(this))
			onShootChance();
	}
	
	public void setShootPolicy(ShootPolicy policy) {
		mShootPolicy = policy;
	}
	
	public void resetInitHealth(float initHealth, boolean resetHealth) {
		mInitialHealth = initHealth;
		
		if (resetHealth) {
			mHealth = initHealth;
			onHeal();
		}
	}
	
	public void addWoundedBehavior(IBehavior pBehavior) {
		mWoundedBehaviors.add(pBehavior);
	}
	
	public void addDeadBehavior(IBehavior pBehavior) {
		mDeadBehaviors.add(pBehavior);
	}
	
	public void addHealBehavior(IBehavior pBehavior) {
		mHealBehaviors.add(pBehavior);
	}
	
	public void resetBulletEmitter() {
		if (mBulletEmitter != null)
			mBulletEmitter.reset();
	}
	
	public void setBulletEmitter(BulletEmitter pEmitter) {
		if (mBulletEmitter != null)
			getScene().unregisterUpdateHandler(mBulletEmitter);
		
		mBulletEmitter = pEmitter;
		
		if (mBulletEmitter != null)
			getShape().registerUpdateHandler(mBulletEmitter);
	}
	
	@Override
	public void onNewBullet(Bullet bullet) {
		// Make bullet attack enemies only
		bullet.setFilter(getBulletFilter());
		bullet.addTags(allTags());
	}
	
	protected abstract Filter getBulletFilter();

	private void onShootChance() {
		if (mBulletEmitter != null)
			mBulletEmitter.emit(getEnvironment(), getShape(), this);
	}

	@Override
	protected int onContactBegin(GameObject other, int param) {
		if (other instanceof Harmful) {
			Harmful harmful = (Harmful) other;
			
			float damage = harmful.countDamage(this);
			if (damage > 0) {
				AdditionDamage addition = getAdditionDamageFrom(other);
				
				if (addition != null)
					damage = addition.filter(damage);
				
				mHealth -= damage;
				mHealth = mHealth > 0 ? mHealth : 0;
				onWounded(harmful);
				if (mHealth <= 0) {
					mHealth = Math.max(mHealth, 0);
					onDead(harmful);
					destroy();
				}
			}
		}
		
		return 1;
	}
	
	public void heal(float percentHealed) {
		mHealth = mInitialHealth * (percentHealed + getHealthPercent());
		mHealth = mHealth > mInitialHealth ? mInitialHealth : mHealth;
		onHeal();
	}
	
	private void onHeal() {
		notify(null, mHealBehaviors);
	}
	
	private void onWounded(Harmful source) {
		notify(source, mWoundedBehaviors);
	}
	
	private void onDead(Harmful source) {
		notify(source, mDeadBehaviors);
	}
	
	private void notify(Harmful source, Set<IBehavior> pBehaviors) {
		for (IBehavior behavior : pBehaviors) {
			behavior.onActivated(this, source);
		}
	}
	
	@Override
	protected int onContactEnd(GameObject other, int param) {
		return 1;
	}
	
	public AdditionDamage getAdditionDamageFrom(GameObject object) {
		AdditionDamage addition = new AdditionDamage();
		for (TaggedAddition taggedAddition : mAdditionDamages) {
			if (object.hasTag(taggedAddition.tag))
				addition = addition.add(taggedAddition.damage);
		}
		addition.abs();
		
		return addition;
	}
	
	/**
	 * @return handle to registered object, @see unregisterAdditionDamage()
	 */
	public Object registerAdditionDamage(String tag, AdditionDamage addition) {
		if (tag == null || addition == null)
			throw new IllegalArgumentException("Got null tag or null addition");
		
		TaggedAddition tagged = new TaggedAddition(tag, addition);
		mAdditionDamages.add(tagged);
		return tagged;
	}
	
	public boolean unregisterAdditinDamage(Object handle) {
		return mAdditionDamages.remove(handle);
	}

	public float getHealthPercent() {
		return mHealth/mInitialHealth;
	}
	
	public void setDamageWhenCollide(float damage) {
		mDamage = damage;
	}
	
	@Override
	public float countDamage(GameObject target) {
		return mDamage;
	}
}
