package com.doubleshoot.shooter;

import org.andengine.entity.shape.IAreaShape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.doubleshoot.body.BodyFactory;
import com.doubleshoot.object.AbstractGOFactory;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.object.GameObject;
import com.doubleshoot.shape.ShapeFactory;

/**
 * This kind of object will kill all contact object
 * 
 * @author etnlGD
 *
 */
public class BarrierObject extends GameObject implements Harmful {

	public BarrierObject(Body body, IAreaShape shape, GOEnvironment pEnv) {
		super(body, shape, pEnv);
	}

	public BarrierObject(Body body, GOEnvironment env) {
		super(body, env);
	}
	
	@Override
	protected int onContactBegin(GameObject other, int param) {
		return 1;
	}

	@Override
	protected int onContactEnd(GameObject other, int param) {
		return 1;
	}

	@Override
	public float countDamage(GameObject target) {
		return Float.MAX_VALUE;
	}
	
	public static GOFactory<GameObject> newFactory(BodyFactory bf, ShapeFactory sf) {
		AbstractGOFactory<GameObject> gof = new AbstractGOFactory<GameObject>() {
			{
				setBodyType(BodyType.KinematicBody);
			}
			
			@Override
			protected GameObject createObject(GOEnvironment env,
					IAreaShape shape, Body body) {
				body.setBullet(true);
				return new BarrierObject(body, shape, env);
			}
		};
		gof.setBodyFactory(bf);
		gof.setShapeFactory(sf);
		return gof;
	}
}
