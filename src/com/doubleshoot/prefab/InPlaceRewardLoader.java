package com.doubleshoot.prefab;

import static com.doubleshoot.body.SimpleBodyBuilder.newBox;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.doubleshoot.bullet.Barrel;
import com.doubleshoot.bullet.Bullet;
import com.doubleshoot.bullet.BulletEmitter;
import com.doubleshoot.bullet.distribution.BulletDistribution;
import com.doubleshoot.bullet.distribution.ParrallelDistribution;
import com.doubleshoot.bullet.distribution.ScatterDistribution;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.object.GOFactoryLoader;
import com.doubleshoot.object.GORegistry;
import com.doubleshoot.reward.HealReward;
import com.doubleshoot.reward.IRewardPolicy;
import com.doubleshoot.reward.ReplaceBullet;
import com.doubleshoot.reward.Reward;
import com.doubleshoot.reward.RewardFactory;
import com.doubleshoot.shape.ShapeFactory;
import com.doubleshoot.shape.TexturedSpriteFactory;
import com.doubleshoot.shooter.FixtureFactory;
import com.doubleshoot.shooter.GameObjectType;
import com.doubleshoot.texture.IRegionManager;

public class InPlaceRewardLoader implements GOFactoryLoader<Reward> {
	private GORegistry<Bullet> mBulletRegistry;
	private FixtureDef mRewardDef;
	
	public InPlaceRewardLoader(GORegistry<Bullet> bulletRegistry) {
		mBulletRegistry = bulletRegistry;
		mRewardDef = FixtureFactory.createFixture(GameObjectType.PowerUP, 1);
	}
	
	private RewardFactory newReward(IRewardPolicy pPolicy,
			ShapeFactory shapeFactory) {
		RewardFactory rewardFactory = new RewardFactory(pPolicy);
		rewardFactory.setBodyFactory(newBox(30, 30, mRewardDef));
		rewardFactory.setShapeFactory(shapeFactory);
		return rewardFactory;
	}
	
	private ShapeFactory checkShape(VertexBufferObjectManager vbom,
			IRegionManager regions, String name) {
		return new TexturedSpriteFactory(vbom, regions, name);
	}
	
	private GOFactory<Reward>
	loadScatterBullet(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletEmitter emitter = new BulletEmitter();
		BulletDistribution distrib = new ScatterDistribution(-120, -60, 3);
		Barrel barrel = new Barrel(distrib,
				mBulletRegistry.getFilteredFactory("RedRound"));
		barrel.setBarrelPosition(30, 0);
		barrel.setFrozenCycle(0.3f);
		emitter.setBarrel(0, barrel);
		return newReward(new ReplaceBullet(emitter),
				checkShape(vbom, regions, "Reward.Scatter"));
	}
	
	private GOFactory<Reward>
	loadMissle(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletEmitter emitter = new BulletEmitter();
		ParrallelDistribution distrib = new ParrallelDistribution();
		distrib.setDirection(new Vector2(0, -1));
		distrib.setPosition(0, new Vector2(10, 0));
		distrib.setPosition(1, new Vector2(50, 0));

		Barrel barrel = new Barrel(distrib,
				mBulletRegistry.getFilteredFactory("Missile"));
		barrel.setBarrelPosition(0, 0);
		barrel.setFrozenCycle(0.5f);
		emitter.setBarrel(0, barrel);
		
		return newReward(new ReplaceBullet(emitter),
				checkShape(vbom, regions, "Reward.Missile"));
	}
	
	private GOFactory<Reward>
	loadParallel(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletEmitter emitter = new BulletEmitter();
		ParrallelDistribution distrib = new ParrallelDistribution();
		distrib.setDirection(new Vector2(0, -1));
		distrib.setPosition(0, new Vector2(10, 0));
		distrib.setPosition(1, new Vector2(50, 0));
		Barrel barrel = new Barrel(distrib,
				mBulletRegistry.getFilteredFactory("RedRound"));
		barrel.setBarrelPosition(0, 0);
		barrel.setFrozenCycle(0.15f);
		emitter.setBarrel(0, barrel);
		
		return newReward(new ReplaceBullet(emitter),
				checkShape(vbom, regions, "Reward.Parallel"));
	}
	
	private GOFactory<Reward>
	loadLaser(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletEmitter emitter = new BulletEmitter();
		ParrallelDistribution distrib = new ParrallelDistribution();
		distrib.setDirection(new Vector2(0, -1));
		distrib.setPosition(0, new Vector2(30, 0));
		Barrel barrel = new Barrel(distrib,
				mBulletRegistry.getFilteredFactory("Laser"));
		barrel.setBarrelPosition(0, 0);
		barrel.setFrozenCycle(0.3f);
		emitter.setBarrel(0, barrel);
		
		return newReward(new ReplaceBullet(emitter),
				checkShape(vbom, regions, "Reward.Laser"));
	}
	
	private GOFactory<Reward>
	loadHeal(VertexBufferObjectManager vbom, IRegionManager regions) {
		return newReward(new HealReward(0.5f),
				checkShape(vbom, regions, "Reward.Heal"));
	}
	
	@Override
	public void load(VertexBufferObjectManager vbom, IRegionManager regions,
			GOFactoryCallback<Reward> callback) {
		callback.onNewFactory("ScatterReward", loadScatterBullet(vbom, regions));
		callback.onNewFactory("MissileReward", loadMissle(vbom, regions));
		callback.onNewFactory("ParallelReward", loadParallel(vbom, regions));
		callback.onNewFactory("LaserReward", loadLaser(vbom, regions));
		callback.onNewFactory("HealReward", loadHeal(vbom, regions));
	}


}
