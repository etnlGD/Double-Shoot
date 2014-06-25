package com.doubleshoot.object;

import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.doubleshoot.body.BodyFactory;
import com.doubleshoot.shape.ShapeFactory;

public abstract class AbstractGOFactory<T extends GameObject>
		extends TaggedObject implements GOFactory<T> {
	private ShapeFactory mShapeFactory;
	private BodyFactory mBodyFactory;
	private BodyType mBodyType = BodyType.DynamicBody;
	
	public void setShapeFactory(ShapeFactory pShapeFactory) {
		mShapeFactory = pShapeFactory;
	}
	
	public void setBodyFactory(BodyFactory pBodyFactory) {
		mBodyFactory = pBodyFactory;
	}
	
	protected void setBodyType(BodyType type) {
		mBodyType = type;
	}
	
	protected double getRotation(Vector2 velocity) {
		return Math.atan2(velocity.y, velocity.x);
	}
	
	@Override
	public T create(GOEnvironment env, Vector2 pos, Vector2 velocity) {
		IAreaShape shape = mShapeFactory.createShape();
		
		float angle = (float) Math.toDegrees(getRotation(velocity));
		Body body = mBodyFactory.createBody(env.getWorld(), mBodyType, pos, angle);
		body.setLinearVelocity(velocity);
		
		T obj = createObject(env, shape, body);
		onObjectCreated(obj);
		return obj;
	}
	
	protected void onObjectCreated(T created) {
		created.addTags(allTags());
	}
	
	protected abstract T createObject(GOEnvironment env, IAreaShape shape, Body body);

}
