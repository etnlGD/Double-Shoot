package com.doubleshoot.game;

import static com.doubleshoot.game.GameActivity.CAMERA_HEIGHT;
import static com.doubleshoot.game.GameActivity.CAMERA_WIDTH;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.alien.Alien;
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
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.TagManager;
import com.doubleshoot.troop.RandomTroopGenerator;
import com.doubleshoot.troop.TroopDispatcher;
import com.doubleshoot.troop.TroopGenerator;

public class Game extends CompositeGameListener {
	private static final float CONTROLLER_MARGIN = 60;
	
	private GORegistry<Bullet> mBulletRegistry;
	private GORegistry<Alien> mAlienRegistry;
	private GOFactory<Hero> mHeroFactory;
	private GOEnvironment mGOEnv;
	private HeroDeadListener mHeroDeadListener;
	
	private TroopGenerator mTroopGenerator;
	
	public Game(GOFactory<Hero> pHeroFactory, GOEnvironment pEnv,
			GORegistry<Bullet> pBulletRegistry, GORegistry<Alien> pAlienRegistry) {
		mHeroFactory = pHeroFactory;
		mGOEnv = pEnv;
		mAlienRegistry = pAlienRegistry;
		mBulletRegistry = pBulletRegistry;
		
		mTroopGenerator = RandomTroopGenerator.create();
		mHeroDeadListener = new HeroDeadListener(1, this);
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
	
	public void newGame() {
		Hero left = mHeroFactory.create(mGOEnv,
				new Vector2(CAMERA_WIDTH*0.25f, CAMERA_HEIGHT), new Vector2());
		setUpHero(left, TagManager.sLeftHero);
		setDefaultEmitter(mGOEnv, left, .3f, 3);
		
		Hero right = mHeroFactory.create(mGOEnv,
				new Vector2(CAMERA_WIDTH*0.75f, CAMERA_HEIGHT), new Vector2());
		setUpHero(right, TagManager.sRightHero);
		setDefaultEmitter(mGOEnv, right, .1f, 1);
		
		float[] seperators = {
				CONTROLLER_MARGIN, CAMERA_WIDTH/2 - CONTROLLER_MARGIN,
				CAMERA_WIDTH/2 + CONTROLLER_MARGIN, CAMERA_WIDTH-CONTROLLER_MARGIN
				};
		DoubleHeroController controller = new DoubleHeroController(left, right, seperators);
		
		getScene().setOnSceneTouchListener(controller);
		onGameStart(left, right);
	}
	
	@Override
	public void onGameStart(BaseShooter pLeftHero, BaseShooter pRightHero) {
		mHeroDeadListener.reset();
		mGOEnv.cancelAll();
		new TroopDispatcher(mGOEnv, mAlienRegistry, mTroopGenerator).run();
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
	
	public boolean isPaused() {
		return mPaused;
	}
}
