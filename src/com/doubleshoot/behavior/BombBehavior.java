package com.doubleshoot.behavior;

import org.andengine.entity.sprite.AnimatedSprite;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.doubleshoot.body.BodyBuilder;
import com.doubleshoot.body.SimpleBodyBuilder;
import com.doubleshoot.body.VectorConstant;
import com.doubleshoot.movable.MovableBody;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.object.GameObject;
import com.doubleshoot.shape.ShapeFactory;
import com.doubleshoot.shooter.BarrierObject;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.FixtureFactory;
import com.doubleshoot.shooter.GameObjectType;
import com.doubleshoot.shooter.Harmful;

// Dead
public class BombBehavior implements IBehavior {
	private float mPersistTime;
	private ShapeFactory mBombShapeFactory;
	
	public BombBehavior(float persistTime, ShapeFactory bombShapeFactory) {
		mPersistTime = persistTime;
		mBombShapeFactory = bombShapeFactory;
	}
	
	@Override
	public void onActivated(BaseShooter host, Harmful source, float damage) {
		Filter f = GameObjectType.AllPlane.getSharedFilter();
		BodyBuilder bb = SimpleBodyBuilder.newBox(400, 5, FixtureFactory.sensor(f));
		
		GOFactory<GameObject> gof = BarrierObject.newFactory(bb, mBombShapeFactory);
		MovableBody body = new MovableBody(host.getBody());
		Vector2 pos = body.getPosition();
		final GameObject bomb = gof.create(host.getEnvironment(), pos, VectorConstant.zero);
		((AnimatedSprite) bomb.getShape()).animate(100, false);
		
		// TODO animate
		host.getEnvironment().schedule(new Runnable() {
			
			@Override
			public void run() {
				bomb.destroy();
			}
		}, mPersistTime);
		bb.dispose();
	}

}
