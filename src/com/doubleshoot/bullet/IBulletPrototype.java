package com.doubleshoot.bullet;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.ITaggedObject;

public interface IBulletPrototype extends ITaggedObject {
	
	public Bullet create(GOEnvironment env, Vector2 pos, Vector2 dir);
	
}