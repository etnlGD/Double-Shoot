package com.doubleshoot.hud;

import org.andengine.entity.scene.Scene;

public interface HudItem {
	public void attachToScene(float pX, float pY, Scene pScene);
	
	public void detach();
}