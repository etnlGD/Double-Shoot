package com.doubleshoot.coordinate;

import org.andengine.entity.IEntity;

import com.badlogic.gdx.math.Vector2;

public class EntityCoordinate {
	
	public static Vector2 localToScenePosition(IEntity node, Vector2 pos) {
		float[] sharedPos = node.convertLocalToSceneCoordinates(pos.x, pos.y);
		return new Vector2(sharedPos[0], sharedPos[1]);
	}
	
	public static Vector2 localToSceneDirection(IEntity node, Vector2 dir) {
		float[] sharedPos = node.convertLocalToSceneCoordinates(dir.x, dir.y);
		Vector2 target = new Vector2(sharedPos[0], sharedPos[1]);
		
		float[] origin = node.convertLocalToSceneCoordinates(0, 0);
		
		return target.sub(origin[0], origin[1]);
	}
	
}