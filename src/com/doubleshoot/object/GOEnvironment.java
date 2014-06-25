package com.doubleshoot.object;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.schedule.IScheduler;
import com.doubleshoot.schedule.ITask;
import com.doubleshoot.schedule.Scheduler;

public class GOEnvironment implements IScheduler, ICoordnateTransformer {
	private Engine mEngine;
	private Scene mScene;
	private PhysicsWorld mWorld;
	private Scheduler mScheduler;
	
	public GOEnvironment(Engine engine, Scene pScene, PhysicsWorld pWorld) {
		if (engine == null)
			throw new NullPointerException("Incomplete engine");
		
		if (pScene == null)
			throw new NullPointerException("Incomplete scene");
		
		if (pWorld == null)
			throw new NullPointerException("Incomplete world");
		
		mEngine = engine;
		mScene = pScene;
		mWorld = pWorld;
		mScheduler = new Scheduler();
		mScene.registerUpdateHandler(mScheduler);
	}
	
	public Scene getScene() {
		return mScene;
	}
	
	public PhysicsWorld getWorld() {
		return mWorld;
	}
	
	public VertexBufferObjectManager getVBOManager() {
		return mEngine.getVertexBufferObjectManager();
	}
	
	@Override
	public void toAbsolutePosition(Vector2 relative) {
		relative.x *= mEngine.getCamera().getWidthRaw();
		relative.y *= mEngine.getCamera().getHeightRaw();
	}
	
	@Override
	public ITask schedule(Runnable pRunnable, float pDelayedSeconds) {
		return mScheduler.schedule(pRunnable, pDelayedSeconds);
	}

	@Override
	public ITask schedule(Runnable pRunnable) {
		return mScheduler.schedule(pRunnable);
	}

	@Override
	public void cancelAll() {
		mScheduler.cancelAll();
	}
}