package com.doubleshoot.game;

import static com.doubleshoot.game.GameActivity.CAMERA_HEIGHT;
import static com.doubleshoot.game.GameActivity.CAMERA_WIDTH;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.alien.Alien;
import com.doubleshoot.body.BodyBuilder;
import com.doubleshoot.body.SimpleBodyBuilder;
import com.doubleshoot.bullet.Barrel;
import com.doubleshoot.bullet.Bullet;
import com.doubleshoot.bullet.BulletEmitter;
import com.doubleshoot.bullet.distribution.BulletDistribution;
import com.doubleshoot.bullet.distribution.ScatterDistribution;
import com.doubleshoot.game.listener.CompositeGameListener;
import com.doubleshoot.hero.Hero;
import com.doubleshoot.input.DoubleHeroController;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GOFactory;
import com.doubleshoot.object.GORegistry;
import com.doubleshoot.object.GameObject;
import com.doubleshoot.object.ITaggedObject;
import com.doubleshoot.score.IScorer;
import com.doubleshoot.score.ScorerFinder;
import com.doubleshoot.shooter.BarrierObject;
import com.doubleshoot.shooter.FixtureFactory;
import com.doubleshoot.shooter.GameObjectType;
import com.doubleshoot.shooter.Harmful;
import com.doubleshoot.shooter.TagManager;
import com.doubleshoot.troop.RandomTroopGenerator;
import com.doubleshoot.troop.Randomizer;
import com.doubleshoot.troop.TroopDispatcher;
import com.doubleshoot.troop.TroopGenerator;

public class Game extends CompositeGameListener implements ScorerFinder {
	private static final float CONTROLLER_MARGIN = 60;
	
	private GORegistry<Bullet> mBulletRegistry;
	private GORegistry<Alien> mAlienRegistry;
	private GOFactory<Hero> mHeroFactory;
	private GOFactory<GameObject> mBarrierFactory;
	private GOEnvironment mGOEnv;
	private HeroDeadListener mHeroDeadListener;
	
	private TroopDispatcher mTroopDispatcher;
	private Hero mLeftHero;
	private Hero mRightHero;
	
	private final class ChangeGenerator implements Runnable {
		private TroopGenerator mGenerator;
		
		public ChangeGenerator(Randomizer.Float sleepTime) {
			mGenerator = RandomTroopGenerator.create(sleepTime);
		}
		
		@Override
		public void run() {
			Log.i("Game.ChangeGenerator", "TroopGenerator changed");
			mTroopDispatcher.setTroopGenerator(mGenerator);
		}
	}
	
	public Game(GOFactory<Hero> pHeroFactory, GOEnvironment pEnv,
			GORegistry<Bullet> pBulletRegistry, GORegistry<Alien> pAlienRegistry) {
		mHeroFactory = pHeroFactory;
		mGOEnv = pEnv;
		mAlienRegistry = pAlienRegistry;
		mBulletRegistry = pBulletRegistry;
		
		mHeroDeadListener = new HeroDeadListener(1, this);
		mTroopDispatcher = new TroopDispatcher(mGOEnv, mAlienRegistry);
		
		BodyBuilder builder = SimpleBodyBuilder.newBox(CAMERA_WIDTH, CAMERA_HEIGHT/2,
				FixtureFactory.sensor(GameObjectType.AllEnemyObject.getSharedFilter()));
		
		mBarrierFactory = BarrierObject.newFactory(builder, null);
	}
	
	private void setUpHero(Hero hero, String tag) {
		hero.addDeadBehavior(mHeroDeadListener);
		hero.addTag(tag);
		((AnimatedSprite) hero.getShape()).animate(100);
	}
	
	private void setDefaultEmitter(GOEnvironment env, Hero hero,
			float frozenCycle, int count) {
		BulletEmitter emitter = new BulletEmitter();
		BulletDistribution distrib = new ScatterDistribution(-120, -60, count);
		Barrel barrel = new Barrel(distrib, mBulletRegistry.getFilteredFactory("RedRound"));
		barrel.setBarrelPosition(30, 0);
		barrel.setFrozenCycle(frozenCycle);
		emitter.setBarrel(0, barrel);
		
		hero.setBulletEmitter(emitter);
	}

	private Scene getScene() {
		return mGOEnv.getScene();
	}

	@Override
	public void onGameover() {
		// clear gameobjects..
		super.onGameover();
	}
	
	public void newGame(boolean restart) {
		if (restart) {
			GameObject barrier = mBarrierFactory.create(mGOEnv,
					new Vector2(CAMERA_WIDTH/2, CAMERA_HEIGHT),	// pos
					new Vector2());								// dir
			
			barrier.getBody().setLinearVelocity(0, -960f/32);
		}
		
		mLeftHero = mHeroFactory.create(mGOEnv,
				new Vector2(CAMERA_WIDTH*0.25f, CAMERA_HEIGHT - 43/2), new Vector2());
		setUpHero(mLeftHero, TagManager.sLeftHero);
		setDefaultEmitter(mGOEnv, mLeftHero, .1f, 1);
		
		mRightHero = mHeroFactory.create(mGOEnv,
				new Vector2(CAMERA_WIDTH*0.75f, CAMERA_HEIGHT - 43/2), new Vector2());
		setUpHero(mRightHero, TagManager.sRightHero);
		setDefaultEmitter(mGOEnv, mRightHero, .1f, 1);
		
		float[] seperators = {
				CONTROLLER_MARGIN, CAMERA_WIDTH/2 - CONTROLLER_MARGIN,
				CAMERA_WIDTH/2 + CONTROLLER_MARGIN, CAMERA_WIDTH-CONTROLLER_MARGIN
				};

		getScene().setOnSceneTouchListener(
				new DoubleHeroController(mLeftHero, mRightHero, seperators));
		onGameStart(mLeftHero, mRightHero);
	}
	
	@Override
	public void onGameStart(Hero pLeftHero, Hero pRightHero) {
		mHeroDeadListener.reset();
		mGOEnv.cancelAll();
		
//		mGOEnv.schedule(new ChangeGenerator(Randomizer.uniform(2.f, 0.25f)));
		mGOEnv.schedule(new ChangeGenerator(Randomizer.uniform(1.5f, 0.3f)), 0f);
		mGOEnv.schedule(new ChangeGenerator(Randomizer.uniform(1.f, 0.4f)), 60f);
		
		mGOEnv.schedule(mTroopDispatcher);
		
		super.onGameStart(pLeftHero, pRightHero);
	}

	private boolean mPaused = false;

	@Override
	public void onGamePause() {
		if (!mHeroDeadListener.gameover()) {
			getScene().setIgnoreUpdate(true);
			super.onGamePause();
		}
		mPaused = true;
	}

	@Override
	public void onGameResume() {
		super.onGameResume();
		getScene().setIgnoreUpdate(false);
		mPaused = false;
	}

	@Override
	public IScorer findScorer(Harmful harmful) {
		if (harmful instanceof ITaggedObject) {
			ITaggedObject obj = (ITaggedObject) harmful;
			if (obj.hasTag(TagManager.sLeftHero)) {
				return mLeftHero;
			} else if (obj.hasTag(TagManager.sRightHero)) {
				return mRightHero;
			}
		}
		
		return null;
	}
	
	public boolean isPaused() {
		return mPaused;
	}

	public void getScores(int[] scores) {
		if (mLeftHero != null)
			scores[0] = mLeftHero.getScore();
		
		if (mRightHero != null)
			scores[1] = mRightHero.getScore();
	}
}
