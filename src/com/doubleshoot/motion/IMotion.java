package com.doubleshoot.motion;

import org.andengine.engine.handler.IUpdateHandler;

import com.doubleshoot.movable.IMovable;

public interface IMotion {
	public IUpdateHandler createMotionModifier(IMovable pMovable);
}
