package com.doubleshoot.body;

import java.util.ArrayList;
import java.util.List;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.doubleshoot.shooter.FixtureFactory;

public class BodyBuilder implements BodyFactory {
	private List<FixtureDef> mFixtureDefs = new ArrayList<FixtureDef>();
	private FixtureDef mDefaultFixtureDef;
	private float mRatio = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	
	private Vector2 transform(Vector2 pixel) {
		return pixel.mul(1/mRatio);
	}
	
	private float transform(float pixel) {
		return pixel/mRatio;
	}
	
	private FixtureDef copyFixtureDef(FixtureDef fix) {
		if (fix == null) {
			if (mDefaultFixtureDef == null)
				mDefaultFixtureDef = new FixtureDef();
			
			fix = mDefaultFixtureDef;
		}
		
		return FixtureFactory.copy(fix);
	}
	
	private void addShape(Shape shape, FixtureDef pFixtureDef) {
		pFixtureDef = copyFixtureDef(pFixtureDef);
		pFixtureDef.shape = shape;
		mFixtureDefs.add(pFixtureDef);
	}
	
	public void setDefaultFixtureDef(FixtureDef pFixtureDef) {
		mDefaultFixtureDef = pFixtureDef;
	}

	public void setPixelToMeterRatio(float ratio) {
		mRatio = ratio;
	}

	public void addBox(Vector2 center, float halfWidth, float halfHeight, float degree, FixtureDef pFixtureDef) {
		transform(center);
		halfWidth = transform(halfWidth);
		halfHeight= transform(halfHeight);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(halfWidth, halfHeight, center, (float) Math.toRadians(degree));
		
		addShape(boxShape, pFixtureDef);
	}

	public void addCircle(Vector2 center, float radius, FixtureDef pFixtureDef) {
		transform(center);
		radius = transform(radius);
		
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius);
		circleShape.setPosition(center);
		
		addShape(circleShape, pFixtureDef);
	}

	public void addPolygon(Vector2[] vertices, FixtureDef pFixtureDef) {
		for (Vector2 vert : vertices)
			transform(vert);
		
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices);
		
		addShape(polygonShape, pFixtureDef);
	}

	public void addLine(Vector2 pointA, Vector2 pointB, FixtureDef pFixtureDef) {
		transform(pointA);
		transform(pointB);
		
		PolygonShape edgeShape = new PolygonShape();
		edgeShape.setAsEdge(pointA, pointB);
		
		addShape(edgeShape, pFixtureDef);
	}
	
	public Body build(PhysicsWorld pWorld, BodyType bType) {
		BodyDef bDef = new BodyDef();
		bDef.type = bType;
		
		Body body = pWorld.createBody(bDef);
		for (FixtureDef fDef : mFixtureDefs)
			body.createFixture(fDef);
		
		return body;
	}
	
	@Override
	public Body createBody(PhysicsWorld pWorld, BodyType bType, Vector2 pos, float degree) {
		Body body = build(pWorld, bType);
		body.setTransform(transform(pos), (float) Math.toRadians(degree));
		return body;
	}
	
	@Override
	protected void finalize() {
		dispose();
	}
	
	public void dispose() {
		for (FixtureDef fDef : mFixtureDefs)
			fDef.shape.dispose();
		
		mFixtureDefs.clear();
	}
	
}