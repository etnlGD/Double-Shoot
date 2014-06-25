package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.alien.Alien;
import com.doubleshoot.motion.IMotion;
import com.doubleshoot.object.GORegistry;
import com.doubleshoot.object.ICoordnateTransformer;
import com.doubleshoot.troop.Randomizer.Int;

public class SingleTroopGenerator implements TroopGenerator {
	private Randomizer.Float mSleepTime;
	private Randomizer.Float mDeltaTime;
	private Randomizer.Int mAlienCount;
	private ITroopDetermin<String> mSoldierDetermin;
	private PositionDetermineFactory mPosFactory;
	private MotionDetermineFactory mMotionFactory;
	
	public SingleTroopGenerator(
			Randomizer.Float sleepTime, Randomizer.Float deltaTime, Int alienCount,
			ITroopDetermin<String> soldierDetermin,
			PositionDetermineFactory posFactory,
			MotionDetermineFactory motionFactory) {
		this.mSleepTime = sleepTime;
		this.mDeltaTime = deltaTime;
		this.mAlienCount = alienCount;
		this.mSoldierDetermin = soldierDetermin;
		this.mPosFactory = posFactory;
		this.mMotionFactory = motionFactory;
	}
	
	private ITroopDetermin<IMotion> createMotionDetermin(
			ICoordnateTransformer pTransformer, ITroopDetermin<Vector2> p) {
		Vector2 leaderPos = p.next(0, null);
		return  mMotionFactory.create(pTransformer, leaderPos);
	}
	
	@Override
	public ITroop nextTroop(ICoordnateTransformer pTransformer, GORegistry<Alien> pAlienRegistry) {
		int alienCount = mAlienCount.shuffle();
		float dtime = mDeltaTime.shuffle();
		
		ITroopDetermin<Vector2> posDetermin = mPosFactory.create(alienCount);
		ITroopDetermin<IMotion> motionDetermin = createMotionDetermin(pTransformer, posDetermin);
		
		return new SingleTroop(alienCount, pAlienRegistry, mSoldierDetermin,
				posDetermin, motionDetermin, new UniformDeterminer<Float>(dtime));
	}

	@Override
	public float nextSleepTime() {
		return mSleepTime.shuffle();
	}
	
	
}
