package com.doubleshoot.object;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;

// 1 body(+fixture), ? sprite
public abstract class GameObject extends TaggedObject {
	private Body mBody;
	private IAreaShape mShape;
	private PhysicsConnector mConnector;
	private GOEnvironment mEnvironment;
	
	public GameObject(Body body, IAreaShape shape, GOEnvironment pEnv) {
		mBody = body;
		mShape = shape;
		mEnvironment = pEnv;
		
		init();
	}
	
	public GameObject(Body body, GOEnvironment env) {
		this(body, null, env);
	}
	
	private void init() {
		mBody.setUserData(GameObject.this);
		
		if (mShape != null) {
			if (!mShape.hasParent())
				getScene().attachChild(mShape);
			
			mConnector = new PhysicsConnector(mShape, mBody, true, false);
			getWorld().registerPhysicsConnector(mConnector);
			
			mConnector.onUpdate(0);
		}
	}
	
	protected abstract int onContactBegin(GameObject other, int param);
	
	protected abstract int onContactEnd(GameObject other, int param);
	
	public void destroy() {
		if (mShape != null) {
			mShape.detachSelf();
			getWorld().unregisterPhysicsConnector(mConnector);
			
			mShape = null;
			mConnector = null;
		}
		
		if (mBody != null)
			getWorld().destroyBody(mBody);
		
		mBody = null;
	}

	public boolean isDestroyed() {
		return mBody == null;
	}
	
	public IAreaShape getShape() {
		return mShape;
	}
	
	public Body getBody() {
		return mBody;
	}
	
	public Scene getScene() {
		return mEnvironment.getScene();
	}
	
	public PhysicsWorld getWorld() {
		return mEnvironment.getWorld();
	}
	
	public GOEnvironment getEnvironment() {
		return mEnvironment;
	}
	
}
