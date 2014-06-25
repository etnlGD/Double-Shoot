package com.doubleshoot.prefab;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.doubleshoot.alien.Alien;
import com.doubleshoot.alien.AlienFactory;
import com.doubleshoot.alien.DeadBulletBehavior;
import com.doubleshoot.alien.RevengeBehavior;
import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.body.BodyBuilder;
import com.doubleshoot.body.BodyFactory;
import com.doubleshoot.bullet.Barrel;
import com.doubleshoot.bullet.Bullet;
import com.doubleshoot.bullet.distribution.RandomDirectionDistribution;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.object.GOFactoryLoader;
import com.doubleshoot.object.GOPipeline;
import com.doubleshoot.object.GORegistry;
import com.doubleshoot.reward.RandomBulletReward;
import com.doubleshoot.reward.Reward;
import com.doubleshoot.shape.ShapeFactory;
import com.doubleshoot.shape.TexturedSpriteFactory;
import com.doubleshoot.shooter.ContinuousShootPolicy;
import com.doubleshoot.shooter.FixtureFactory;
import com.doubleshoot.shooter.GameObjectType;
import com.doubleshoot.shooter.ShooterBehaviorFilter;
import com.doubleshoot.texture.IRegionManager;

public class InPlaceAlienLoader implements GOFactoryLoader<Alien> {
	private FixtureDef mAlienDef;
	private GORegistry<Bullet> mBulletRegistry;
	private GORegistry<Reward> mRewardRegistry;

	public InPlaceAlienLoader(GORegistry<Bullet> bulletRegistry,
			GORegistry<Reward> rewardRegistry) {
		mBulletRegistry = bulletRegistry;
		mRewardRegistry = rewardRegistry;
		mAlienDef = FixtureFactory.createFixture(GameObjectType.EnemyPlane, 1);
	}
	
	private BodyFactory newBodyFactory(int hw, int hh) {
		return newBodyFactory(hw, hh, hh, hw);
	}
	
	private BodyFactory newBodyFactory(float hw1, float hh1, float hw2, float hh2) {
		BodyBuilder alienBodyBuilder = new BodyBuilder();
		alienBodyBuilder.addBox(new Vector2(), hw1, hh1, 0, mAlienDef);
		alienBodyBuilder.addBox(new Vector2(), hw2, hh2, 0, mAlienDef);
		return alienBodyBuilder;
	}
	
	private AlienFactory newAlien(float damage, float health, int scores,
			ShapeFactory shapeDef, BodyFactory bodyFactory) {
		AlienFactory af = new AlienFactory(damage, health, scores);
		af.setShapeFactory(shapeDef);
		af.setBodyFactory(bodyFactory);
		return af;
	}
	
	private ShapeFactory checkShape(VertexBufferObjectManager vbom,
			IRegionManager regions, String name) {
		ITextureRegion region = regions.getRegion(name);
		if (region == null)
			throw new RuntimeException("No region found");
		
		TexturedSpriteFactory factory = new TexturedSpriteFactory(vbom, region);
		factory.textureName = name;	// TODO remove
		return factory;
	}
	
	private GOFactory<Alien>
	loadYellow(VertexBufferObjectManager vbom, IRegionManager regions) {
		AlienFactory af = newAlien(200, 50, 5,
				checkShape(vbom, regions, "Alien.Yellow"), newBodyFactory(3, 16));
		Barrel b = new Barrel(
				new RandomDirectionDistribution(90, 90),
				mBulletRegistry.getFilteredFactory("BlueRound"));
		b.setFrozenCycle(1.4f);
		b.setBarrelPosition(16, 16);
		af.setBarrel(0, b);
		af.setShootPolicy(new ContinuousShootPolicy());
		
		return af;
	}
	
	private GOFactory<Alien>
	loadBlue(VertexBufferObjectManager vbom, IRegionManager regions) {
		AlienFactory af = newAlien(100, 100, 20,
				checkShape(vbom, regions, "Alien.Blue"), newBodyFactory(3, 16));
		Barrel b = new Barrel(
				new RandomDirectionDistribution(75, 105),
				mBulletRegistry.getFilteredFactory("BlueRound"));
		b.setFrozenCycle(1.4f);
		b.setBarrelPosition(16, 16);
		af.setBarrel(0, b);
		af.setShootPolicy(new ContinuousShootPolicy());
		af.addTag("DeadBullet");
		GOPipeline<Alien> pipeline = new GOPipeline<Alien>(af);
		ShooterBehaviorFilter filter = new ShooterBehaviorFilter();
		filter.addDeadBehavior(new DeadBulletBehavior(
				mBulletRegistry.getFilteredFactory("DeadBullet"), 6));
		pipeline.addFilter(filter);
		
		return pipeline;
	}
	
	private IBehavior newBulletReward(int voteTimes) {
		RandomBulletReward rewards = new RandomBulletReward(voteTimes);
		for (int i = 0; i < mRewardRegistry.size(); i++) {
			rewards.addRewardType(
					mRewardRegistry.getFilteredFactory(
							mRewardRegistry.getFactoryNameAt(i)));
		}
		
		return rewards; 
	}
	
	private GOFactory<Alien>
	loadWhite(VertexBufferObjectManager vbom, IRegionManager regions) {
		AlienFactory af = newAlien(100, 100, 10,
				checkShape(vbom, regions, "Alien.White"), newBodyFactory(3, 16));
		GOPipeline<Alien> pipeline = new GOPipeline<Alien>(af);
		ShooterBehaviorFilter filter = new ShooterBehaviorFilter();
		filter.addDeadBehavior(new RevengeBehavior(250, 240));
		filter.addDeadBehavior(newBulletReward(4));
		pipeline.addFilter(filter);
		return pipeline;
	}
	
	private GOFactory<Alien>
	loadGreen(VertexBufferObjectManager vbom, IRegionManager regions) {
		AlienFactory af = newAlien(200, 150, 2,
				checkShape(vbom, regions, "Alien.Green"), newBodyFactory(3, 16));
		GOPipeline<Alien> pipeline = new GOPipeline<Alien>(af);
		ShooterBehaviorFilter filter = new ShooterBehaviorFilter();
		filter.addDeadBehavior(new RevengeBehavior(250, 240));
		filter.addDeadBehavior(newBulletReward(1));
		pipeline.addFilter(filter);
		return pipeline;
	}
	
	@Override
	public void load(VertexBufferObjectManager vbom, IRegionManager regions,
			GOFactoryCallback<Alien> callback) {
		callback.onNewFactory(AlienType.COMMON, loadYellow(vbom, regions));
		callback.onNewFactory(AlienType.WITH_DEAD, loadBlue(vbom, regions));
		callback.onNewFactory(AlienType.NO_BULLET, loadGreen(vbom, regions));
		callback.onNewFactory(AlienType.REVENGER, loadWhite(vbom, regions));
	}

//	private Path newZPath(float x0, float y0) {
//		float hGap = 150;
//		float xMin = 20;
//		float xMax = 780;
//
//		float x1 = MathUtils.randomSign() > 0 ? xMin : xMax;
//		float x2 = x1 == xMin ? xMax : xMin;
//		float y = Math.abs(x0 - x1)/(xMax - xMin) * hGap;
//
//		Path path = new Path(6);
//		for (int i = 0; i < path.getSize(); i+=2) {
//			path.to(x1, y);
//			path.to(x2, y + hGap);
//			y += 2 * hGap;
//		}
//
//		return path;
//	}
}
