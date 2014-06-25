package com.doubleshoot.shooter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.doubleshoot.body.BodyBuilder;
import com.doubleshoot.body.SimpleBodyBuilder;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GameObject;

public class Border extends GameObject {

	private Border(GOEnvironment pEnv, Body body) {
		super(body, pEnv);
		this.addTag(TagManager.sBoundTag);
	}

	@Override
	protected int onContactBegin(GameObject other, int param) {
		return 1;
	}

	@Override
	protected int onContactEnd(GameObject other, int param) {
		if (!other.hasTag(TagManager.sCrossBorderRight))
			other.destroy();
		
		return 0;
	}
	
	public static GameObject create(GOEnvironment env, Vector2 origin, float width, float height) {
		Filter f = GameObjectType.AllObject.getSharedFilter();
		FixtureDef fDef = FixtureFactory.sensor(f);
		BodyBuilder bodyBuilder = SimpleBodyBuilder.newBox(width, height, fDef);
		float hw = width/2, hh = height/2;
		
		Vector2 pos = new Vector2(origin.x + hw, origin.y + hh);
		Body body = bodyBuilder.createBody(env.getWorld(), BodyType.StaticBody, pos, 0);
		bodyBuilder.dispose();
		
		return new Border(env, body);
	}

}