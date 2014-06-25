package com.doubleshoot.prefab;

import static com.doubleshoot.body.SimpleBodyBuilder.newBox;
import static com.doubleshoot.body.SimpleBodyBuilder.newCircle;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.audio.SE;
import com.doubleshoot.bullet.Bullet;
import com.doubleshoot.bullet.BulletMotionFilter;
import com.doubleshoot.bullet.BulletPrototype;
import com.doubleshoot.bullet.BulletSound;
import com.doubleshoot.motion.AcceleratedMotion;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.object.GOFactoryLoader;
import com.doubleshoot.object.GOPipeline;
import com.doubleshoot.shape.ShapeFactory;
import com.doubleshoot.shape.TexturedSpriteFactory;
import com.doubleshoot.texture.IRegionManager;

public class InPlaceBulletLoader implements GOFactoryLoader<Bullet> {
	public static int RESERVE_COUNT = 32;
	private SE mSoundSet;
	
	public InPlaceBulletLoader(SE pSoundSet) {
		mSoundSet = pSoundSet;
	}
	
	private ShapeFactory newSpriteFactory(VertexBufferObjectManager vbom,
			IRegionManager regions, String name) {
		return newSpriteFactory(vbom, regions, name, Color.WHITE);
	}
	
	private TexturedSpriteFactory newSpriteFactory(
			VertexBufferObjectManager vbom,
			IRegionManager regions, String name, Color added) {
		TexturedSpriteFactory factory = new TexturedSpriteFactory(vbom, regions, name);
		factory.setAddColor(added);
		factory.reserve(RESERVE_COUNT);
		return factory;
	}
	
	private GOFactory<Bullet> loadRed(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletPrototype prototype = new BulletPrototype();
		prototype.setDamage(30);
		prototype.setPenetrating(false);
		prototype.setSpeed(10);
		prototype.setBodyFactory(newCircle(5, 0.1f));
		prototype.setShapeFactory(
				newSpriteFactory(vbom, regions, "Bullet.Red"));
		prototype.setBulletListener(new BulletSound(mSoundSet.get("laser_shoot")));
		return prototype;
	}
	
	private GOFactory<Bullet> loadBlue(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletPrototype prototype = new BulletPrototype();
		prototype.setDamage(20);
		prototype.setPenetrating(false);
		prototype.setSpeed(5);
		prototype.setBodyFactory(newCircle(4, 0.1f));
		prototype.setShapeFactory(
				newSpriteFactory(vbom, regions, "Bullet.Yellow", new Color(1, 0, 1)));
		return prototype;
	}
	
	private GOFactory<Bullet> loadDead(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletPrototype prototype = new BulletPrototype();
		prototype.setDamage(150);
		prototype.setPenetrating(false);
		prototype.setSpeed(7);
		prototype.setBodyFactory(newCircle(4, 0.1f));
		prototype.setShapeFactory(
				newSpriteFactory(vbom, regions, "Bullet.Yellow", new Color(0.5f, 1, 1)));
		return prototype;
	}
	
	private GOFactory<Bullet> loadLaser(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletPrototype prototype = new BulletPrototype();
		prototype.setDamage(40);
		prototype.setPenetrating(true);
		prototype.setSpeed(7);
		prototype.setBodyFactory(newBox(8, 30,
				PhysicsFactory.createFixtureDef(0.1f, 0, 1, true)));
		prototype.setShapeFactory(
				newSpriteFactory(vbom, regions, "Bullet.Laser"));
		return prototype;
	}
	
	private GOFactory<Bullet> loadHugeYellow(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletPrototype prototype = new BulletPrototype();
		prototype.setDamage(80);
		prototype.setPenetrating(false);
		prototype.setSpeed(8);
		prototype.setBodyFactory(newBox(9, 20, 1f));
		prototype.setShapeFactory(
				newSpriteFactory(vbom, regions, "Bullet.HugeYellow"));
//		prototype.setBulletListener(new BulletSound(mSoundSet.get("missile_shoot")));
		return prototype;
	}

	private GOFactory<Bullet> loadMissile(VertexBufferObjectManager vbom, IRegionManager regions) {
		BulletPrototype prototype = new BulletPrototype();
		prototype.setDamage(80);
		prototype.setPenetrating(false);
		prototype.setSpeed(-1);
		prototype.setBodyFactory(newBox(10, 20, 10f));
		prototype.setShapeFactory(
				newSpriteFactory(vbom, regions, "Bullet.Missile"));
		prototype.setBulletListener(new BulletSound(mSoundSet.get("missile_shoot")));
		GOPipeline<Bullet> pipeline = new GOPipeline<Bullet>(prototype);
		pipeline.addFilter(new BulletMotionFilter(
				new AcceleratedMotion(new Vector2(0, -300))));
		return pipeline;
	}

	@Override
	public void load(VertexBufferObjectManager vbom, IRegionManager regions,
			GOFactoryCallback<Bullet> callback) {
		callback.onNewFactory("RedRound", loadRed(vbom, regions));
		callback.onNewFactory("Laser", loadLaser(vbom, regions));
		callback.onNewFactory("BlueRound", loadBlue(vbom, regions));
		callback.onNewFactory("DeadBullet", loadDead(vbom, regions));
		callback.onNewFactory("Missile", loadMissile(vbom, regions));
		callback.onNewFactory("HugeYellow", loadHugeYellow(vbom, regions));
	}
}
