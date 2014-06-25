package com.doubleshoot.bullet;

import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.doubleshoot.game.QuickSortScene;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GameObject;
import com.doubleshoot.shooter.Harmful;
import com.doubleshoot.shooter.TagManager;

public class Bullet extends GameObject implements Harmful {
	private float mDamage;
	private boolean mPenetrating;
	private BulletListener mListener;
	private IEntity mShooter;
	
	public Bullet(float damage, boolean penetrating, IAreaShape pBulletShape, Body body, GOEnvironment pEnv) {
		super(body, attachToBulletParent(pEnv, pBulletShape), pEnv);
		
		assert(pBulletShape != null);
		mDamage = damage;
		mPenetrating = penetrating;
	}
	
	private static IAreaShape attachToBulletParent(
			GOEnvironment pEnv, IAreaShape pBulletShape) {
		QuickSortScene scene = (QuickSortScene) pEnv.getScene();
		scene.getNegativeRoot().attachChild(pBulletShape);
		return pBulletShape;
	}

	@Override
	protected int onContactBegin(GameObject other, int param) {
		if (!other.hasTag(TagManager.sBoundTag)) {
			if (mListener != null)
				mListener.onCollision(this, other);
			
			if (!mPenetrating) {
				if (mListener != null)
					mListener.onExplosion(this, other);
				
				destroy();
			}
		}
		
		return 1;
	}

	@Override
	protected int onContactEnd(GameObject other, int param) {
		return 1;
	}
	
	public void setFilter(Filter pFilter) {
		for (Fixture f : getBody().getFixtureList())
			f.setFilterData(pFilter);
	}
	
	@Override
	public float countDamage(GameObject target) {
		return mDamage;
	}
	
	public void setShooter(IEntity pShooter) {
		mShooter = pShooter;
	}
	
	public IEntity getShooter() {
		return mShooter;
	}
}