package com.doubleshoot.alien;

import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Filter;
import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.bullet.Barrel;
import com.doubleshoot.bullet.Bullet;
import com.doubleshoot.bullet.BulletEmitter;
import com.doubleshoot.bullet.NewBulletCallback;
import com.doubleshoot.bullet.distribution.CircleDistribution;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.object.ITaggedObject;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class DeadBulletBehavior implements IBehavior {
	private GOFactory<Bullet> mBulletPrototype;
	private int mBulletCount;
	
	public DeadBulletBehavior(GOFactory<Bullet> bulletPrototype, int pCount) {
		mBulletPrototype = bulletPrototype;
		mBulletCount = pCount;
	}

	@Override
	public void onActivated(BaseShooter host, final Harmful source) {
		IAreaShape shape = host.getShape();
		float w = shape.getWidthScaled();
		float h = shape.getHeightScaled();

		Barrel barrel = new Barrel(
				new CircleDistribution(w/2, mBulletCount), mBulletPrototype);
		
		barrel.setFrozenCycle(1);
		barrel.setBarrelPosition(w/2, h/2);
		
		BulletEmitter emitter = new BulletEmitter();
		emitter.setBarrel(0, barrel);
		emitter.emit(host.getEnvironment(), shape, new NewBulletCallback() {
			
			@Override
			public void onNewBullet(Bullet bullet) {
				Filter f = new Filter();
				f.categoryBits = -1;
				f.maskBits = -1;
				bullet.setFilter(f);
				
				if (source instanceof ITaggedObject)
					bullet.addTags(((ITaggedObject) source).allTags());
			}
		});
	}

}
