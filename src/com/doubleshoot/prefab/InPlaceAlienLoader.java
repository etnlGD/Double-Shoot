package com.doubleshoot.prefab;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.doubleshoot.alien.Alien;
import com.doubleshoot.alien.AlienFactory;
import com.doubleshoot.alien.DeadBulletBehavior;
import com.doubleshoot.alien.RevengeBehavior;
import com.doubleshoot.behavior.BombBehavior;
import com.doubleshoot.body.BodyBuilder;
import com.doubleshoot.body.BodyFactory;
import com.doubleshoot.bullet.Barrel;
import com.doubleshoot.bullet.Bullet;
import com.doubleshoot.bullet.distribution.RandomDirectionDistribution;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.object.GOFactoryLoader;
import com.doubleshoot.object.GOPipeline;
import com.doubleshoot.object.GORegistry;
import com.doubleshoot.shape.ShapeFactory;
import com.doubleshoot.shape.TexturedSpriteFactory;
import com.doubleshoot.shooter.ContinuousShootPolicy;
import com.doubleshoot.shooter.FixtureFactory;
import com.doubleshoot.shooter.GameObjectType;
import com.doubleshoot.shooter.ShooterBehaviorFilter;
import com.doubleshoot.texture.IRegionManager;
import com.doubleshoot.texture.SimpleTextureRegion;

public class InPlaceAlienLoader implements GOFactoryLoader<Alien> {
	private FixtureDef mAlienDef;
	private GORegistry<Bullet> mBulletRegistry;
	public static int RESERVE_COUNT = 16;

	public InPlaceAlienLoader(GORegistry<Bullet> bulletRegistry) {
		mBulletRegistry = bulletRegistry;
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
		
		TexturedSpriteFactory factory = new TexturedSpriteFactory(vbom, region, name);
		factory.reserve(RESERVE_COUNT);
		return factory;
	}
	
	private GOFactory<Alien>
	loadYellow(VertexBufferObjectManager vbom, IRegionManager regions) {
		AlienFactory af = newAlien(100, 50, 4,
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
		AlienFactory af = newAlien(100, 100, 16,
				checkShape(vbom, regions, "Alien.Blue"), newBodyFactory(3, 16));
		Barrel b = new Barrel(
				new RandomDirectionDistribution(75, 105),
				mBulletRegistry.getFilteredFactory("BlueRound"));
		b.setFrozenCycle(1.4f);
		b.setBarrelPosition(16, 16);
		af.setBarrel(0, b);
		af.setShootPolicy(new ContinuousShootPolicy());
		GOPipeline<Alien> pipeline = new GOPipeline<Alien>(af);
		ShooterBehaviorFilter filter = new ShooterBehaviorFilter();
		filter.addDeadBehavior(new DeadBulletBehavior(
				mBulletRegistry.getFilteredFactory("DeadBullet"), 6));
		pipeline.addFilter(filter);
		
		return pipeline;
	}
	
	private GOFactory<Alien>
	loadWhite(VertexBufferObjectManager vbom, IRegionManager regions) {
		AlienFactory af = newAlien(150, 120, 8,
				checkShape(vbom, regions, "Alien.White"), newBodyFactory(3, 16));
		GOPipeline<Alien> pipeline = new GOPipeline<Alien>(af);
		ShooterBehaviorFilter filter = new ShooterBehaviorFilter();
		filter.addWoundedBehavior(new RevengeBehavior(250, 240));
		pipeline.addFilter(filter);
		
		return pipeline;
	}
	
	private GOFactory<Alien>
	loadGreen(VertexBufferObjectManager vbom, IRegionManager regions) {
		AlienFactory af = newAlien(100, 150, 4,
				checkShape(vbom, regions, "Alien.Green"), newBodyFactory(3, 16));
		return af;
	}
	
	private GOFactory<Alien>
	loadHuge(VertexBufferObjectManager vbom, IRegionManager regions) {
		AlienFactory af = newAlien(1000, 800, 128,
				checkShape(vbom, regions, "Alien.Huge"),
				newBodyFactory(46, 10, 8, 36));
		
		af.addTag("Huge");
		
		// TODO change to other bullet
		Barrel left = new Barrel(
				new RandomDirectionDistribution(90, 90),
				mBulletRegistry.getFilteredFactory("BlueRound"));
		left.setBarrelPosition(27, 57);
		left.setFrozenCycle(0.2f);
		af.setBarrel(0, left);
		
		Barrel right = new Barrel(
				new RandomDirectionDistribution(90, 90),
				mBulletRegistry.getFilteredFactory("BlueRound"));
		right.setBarrelPosition(63, 57);
		right.setFrozenCycle(0.2f);
		af.setBarrel(1, right);
		
		ITextureRegion[] subRegions = new ITextureRegion[12];
		for (int i = 0; i < subRegions.length; ++i)
			subRegions[i] = regions.getRegion("Bomb." + i);
		TexturedSpriteFactory shapeFactory = new TexturedSpriteFactory(
				vbom, SimpleTextureRegion.join(subRegions), "bomb");
		shapeFactory.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		
		af.setShootPolicy(new ContinuousShootPolicy());
		GOPipeline<Alien> pipeline = new GOPipeline<Alien>(af);
		ShooterBehaviorFilter filter = new ShooterBehaviorFilter();
		// bomb
		filter.addDeadBehavior(new BombBehavior(1, shapeFactory));
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
		callback.onNewFactory(AlienType.HUGE, loadHuge(vbom, regions));
	}

}