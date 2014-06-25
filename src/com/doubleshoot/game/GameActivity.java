package com.doubleshoot.game;

import static com.doubleshoot.texture.SimpleTextureRegion.titledTexture;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.doubleshoot.alien.Alien;
import com.doubleshoot.alien.DeadScoreBehavior;
import com.doubleshoot.alien.WoundScoreBehavior;
import com.doubleshoot.audio.SE;
import com.doubleshoot.behavior.ExplosionSound;
import com.doubleshoot.behavior.VibrationBehavior;
import com.doubleshoot.body.BodyFactory;
import com.doubleshoot.body.SimpleBodyBuilder;
import com.doubleshoot.bullet.Bullet;
import com.doubleshoot.game.ScreenShot.IScreenSavedListener;
import com.doubleshoot.hero.Hero;
import com.doubleshoot.hero.HeroFactory;
import com.doubleshoot.hud.DefaultFontCreator;
import com.doubleshoot.hud.GameStatusScene;
import com.doubleshoot.hud.ITextCreator;
import com.doubleshoot.object.ConcreteGORegistry;
import com.doubleshoot.object.ContactResolver;
import com.doubleshoot.object.GOEnvironment;
import com.doubleshoot.object.GOFactoryLoader;
import com.doubleshoot.object.GOFilter;
import com.doubleshoot.object.GOPipeline;
import com.doubleshoot.object.GORegistry;
import com.doubleshoot.object.RegistryFiller;
import com.doubleshoot.parallax.HVParallaxEntity;
import com.doubleshoot.prefab.InPlaceAlienLoader;
import com.doubleshoot.prefab.InPlaceBulletLoader;
import com.doubleshoot.prefab.InPlaceRewardLoader;
import com.doubleshoot.reward.Reward;
import com.doubleshoot.shape.ShapeFactory;
import com.doubleshoot.shape.TexturedSpriteFactory;
import com.doubleshoot.share.WXShare;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Border;
import com.doubleshoot.shooter.FixtureFactory;
import com.doubleshoot.shooter.GameObjectType;
import com.doubleshoot.shooter.ShooterBehaviorFilter;
import com.doubleshoot.shooter.ShooterVisualFilter;
import com.doubleshoot.texture.CachedTextureFactory;
import com.doubleshoot.texture.IRegionManager;
import com.doubleshoot.texture.ITextureFactory;
import com.umeng.analytics.MobclickAgent;

public class GameActivity extends BaseGameActivity
				implements OnClickListener, IScreenSavedListener {
	public static final float SCREEN_RATIO = 16.f/9;
	public static final float CAMERA_WIDTH = 800;
	public static final float CAMERA_HEIGHT = CAMERA_WIDTH/SCREEN_RATIO;

	private static final int SHARE = 1;
	private static final int RESTART = 2;
	private static final int RESUME = 3;
	private static final int PAUSE = 4;
	private static final int EXPLOSION_RESERVE_COUNT = 32;
	
	private IRegionManager mRegions;
	private ITextureFactory mTextureFactory;
	private IFont mFont;
	
	private Game mGame;
	private GameStatusScene mHud;
	private ScreenShot mLeftShot;
	private ScreenShot mRightShot;
	private WXShare mShare;
	private SE mSoundSet;
	private ScreenCapture mScreenCapture;
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		if (mGame != null)
			mGame.onGamePause();
		
		super.onPause();
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		EngineOptions options = new EngineOptions(true,
					ScreenOrientation.LANDSCAPE_FIXED,
					new RatioResolutionPolicy(SCREEN_RATIO),
					new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 10, 10, 10));
		
		options.getAudioOptions().setNeedsMusic(true);
		options.getAudioOptions().setNeedsSound(true);
		options.getTouchOptions().setNeedsMultiTouch(true);
		options.getRenderOptions().setMultiSampling(true);
		return options;
	}
	
	@Override
	public Engine onCreateEngine(final EngineOptions pEngineOptions) {
		Engine engine = new LimitedFPSEngine(pEngineOptions, 60);
		engine.registerUpdateHandler(new FPSLogger(1.5f));
		engine.enableVibrator(this);
		
//		engine.registerUpdateHandler(new TimerHandler(3, new ITimerCallback() {
//
//			@Override
//			public void onTimePassed(TimerHandler pTimerHandler) {
//				Debug.startMethodTracing();
//			}
//		}));
		return engine;
	}
	
	@Override
	protected void onStop() {
//		Debug.stopMethodTracing();
		super.onStop();
	}
	
	@Override
	public void onCreateResources(OnCreateResourcesCallback callback)
			throws IOException {
		mShare = new WXShare(this);

		mTextureFactory = new CachedTextureFactory(getAssets(), getTextureManager());

		TextureManager texMgr = getTextureManager();
		AssetManager assets = getAssets();
		FontManager fontMgr = getFontManager();
		
		Typeface typeface = Typeface.create(
				Typeface.createFromAsset(assets, "font/speed.ttf"),
				Typeface.NORMAL);
		mFont = FontFactory.create(fontMgr, texMgr, 256, 256, typeface , 16, Color.WHITE_ARGB_PACKED_INT);
		mFont.load();

		mRegions = AllTextureRegions.loadAll(mTextureFactory);
		mSoundSet = new SE(this, "mfx/");
		mSoundSet.put("explosion.wav", 1.0f);
		mSoundSet.put("laser_hit.wav", 0.05f);
		mSoundSet.put("laser_shoot.wav", 0.05f);
		mSoundSet.put("shoot.wav", 0.1f);
		mSoundSet.put("hit.wav", 0.1f);
		mSoundSet.put("missile_shoot.wav", 0.5f);
		callback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback callback) {
		callback.onCreateSceneFinished(new QuickSortScene());
	}
	
	private ShapeFactory newTextureFactory(String textureName) {
		return new TexturedSpriteFactory(
				getVertexBufferObjectManager(), mRegions, textureName);
	}
	
	@Override
	public void onPopulateScene(final Scene pScene, OnPopulateSceneCallback populateCallback)
		throws Exception {
		pScene.setBackground(createBackground());

		VertexBufferObjectManager vbom = getVertexBufferObjectManager();

		// HUD
		IAreaShape label = new Sprite(0, 0, mRegions.getRegion("GameOver"), vbom);
		ButtonSprite resume = new ButtonSprite(0, 0, mRegions.getRegion("Continue"), vbom, this);
		resume.setTag(RESUME);
		ButtonSprite pause = new ButtonSprite(0, 0, mRegions.getRegion("Pause"), vbom, this);
		pause.setTag(PAUSE);
		ButtonSprite share = new ButtonSprite(0, 0, mRegions.getRegion("Share"), vbom, this);
		share.setTag(SHARE);
		ButtonSprite restart = new ButtonSprite(0, 0, mRegions.getRegion("Restart"), vbom, this);
		restart.setTag(RESTART);
		
		ITextCreator textCreator = new DefaultFontCreator(
				getVertexBufferObjectManager(), mFont);
		
		mHud = new GameStatusScene(pause, resume, restart, share, label, textCreator,
				newTextureFactory("Lifebar.Unit"),
				newTextureFactory("Lifebar.Frame"));
		mHud.registerTouchArea(resume);
		mHud.registerTouchArea(pause);
		mHud.registerTouchArea(share);
		mHud.registerTouchArea(restart);
		getEngine().getCamera().setHUD(mHud);
		
		ContactResolver contactResolver = new ContactResolver();
		PhysicsWorld world = new PhysicsWorld(new Vector2(), true);
		world.setContactListener(contactResolver);
		pScene.registerUpdateHandler(world);
		pScene.registerUpdateHandler(contactResolver);
		final GOEnvironment pEnv = new GOEnvironment(mEngine, pScene, world);

		// Create border
		Border.create(pEnv, new Vector2(), CAMERA_WIDTH, CAMERA_HEIGHT);
		
		ITexture explosion = mTextureFactory.loadTexture("explosion.jpg");
		ITextureRegion region = titledTexture(explosion, 6, 3);
		TexturedSpriteFactory shapeFactory =
				new TexturedSpriteFactory(vbom, region, "explosion");
		shapeFactory.reserve(EXPLOSION_RESERVE_COUNT);
		shapeFactory.setScale(1.5f);
		shapeFactory.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		GOFilter<BaseShooter> shooterFilter =
				new ShooterVisualFilter<BaseShooter>(pEnv, shapeFactory);
		
		// Create Hero
		FixtureDef heroDef = FixtureFactory.createFixture(GameObjectType.HeroPlane, 1);
		BodyFactory heroBodyFactory = SimpleBodyBuilder.newBox(45, 32, heroDef);
		TexturedSpriteFactory heroShapeFactory =
				new TexturedSpriteFactory(vbom, mRegions, "TiledHero");
		heroShapeFactory.setScale(0.8f);
		HeroFactory heroFactory = new HeroFactory(Float.MAX_VALUE, 300);
		heroFactory.setShapeFactory(heroShapeFactory);
		heroFactory.setBodyFactory(heroBodyFactory);
		GOPipeline<Hero> heroPipeline = new GOPipeline<Hero>(heroFactory);
		heroPipeline.addFilter(shooterFilter);
		
//<<<<<<< HEAD
		ShooterBehaviorFilter soundFilter = new ShooterBehaviorFilter();
		soundFilter.addDeadBehavior(new ExplosionSound(mSoundSet.get("explosion")));
		heroPipeline.addFilter(soundFilter);
		
		ShooterBehaviorFilter heroBehaviors = new ShooterBehaviorFilter();
		heroBehaviors.addWoundedBehavior(new VibrationBehavior(mEngine, 50));
		heroBehaviors.addDeadBehavior(new VibrationBehavior(mEngine, 500));
		mScreenCapture = new ScreenCapture(mEngine, getAssets(), this);
		heroBehaviors.addDeadBehavior(mScreenCapture);
		heroPipeline.addFilter(heroBehaviors);
		
		GORegistry<Bullet> bulletRegistry = new ConcreteGORegistry<Bullet>();
		GOFactoryLoader<Bullet> bulletLoader = new InPlaceBulletLoader(mSoundSet);
		RegistryFiller.fill(bulletRegistry, bulletLoader, vbom, mRegions);
		
		GORegistry<Reward> rewardRegistry = new ConcreteGORegistry<Reward>();
		GOFactoryLoader<Reward> rewardLoader = new InPlaceRewardLoader(bulletRegistry);
		RegistryFiller.fill(rewardRegistry, rewardLoader, vbom, mRegions);
		
		GORegistry<Alien> alienRegistry = new ConcreteGORegistry<Alien>();
		alienRegistry.addFilter(shooterFilter);
		GOFactoryLoader<Alien> alienLoader = new InPlaceAlienLoader(bulletRegistry, rewardRegistry);
		
		RegistryFiller.fill(alienRegistry, alienLoader, vbom, mRegions);
		
		mGame = new Game(heroPipeline, pEnv, bulletRegistry, alienRegistry);
		mGame.appendListener(mHud);
		mGame.newGame(false);

		ShooterBehaviorFilter scoreFilter = new ShooterBehaviorFilter();
		scoreFilter.addDeadBehavior(new DeadScoreBehavior(mGame));
		scoreFilter.addWoundedBehavior(new WoundScoreBehavior(mGame));
		alienRegistry.addFilter(scoreFilter);
		alienRegistry.addFilter(soundFilter);
		
		populateCallback.onPopulateSceneFinished();
	}

	private IBackground createBackground() {
		ParallaxBackground bg = new AutoParallaxBackground(0, 0, 0, 50);
		
		IAreaShape starSprite = new Sprite(0, 0, mRegions.getRegion("Star"), getVertexBufferObjectManager());
		starSprite.setScale(CAMERA_WIDTH/starSprite.getWidth());
		bg.attachParallaxEntity(new HVParallaxEntity(0, 1, starSprite));
		starSprite.setBlendingEnabled(true);
		starSprite.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);

		IAreaShape planetSprite = new Sprite(0, 0, mRegions.getRegion("Planet"), getVertexBufferObjectManager());
		planetSprite.setScale(CAMERA_WIDTH/planetSprite.getWidth());
		bg.attachParallaxEntity(new HVParallaxEntity(0, 5, planetSprite));
		planetSprite.setBlendingEnabled(true);
		planetSprite.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
		
		return bg;
	}
	
	@Override
	public void onBackPressed() {
		if (mGame.isPaused())
			super.onBackPressed();
		else {
			mGame.onGamePause();
			this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(GameActivity.this,
							getResources().getString(R.string.press_again),
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		switch (pButtonSprite.getTag()) {
		case PAUSE:
			mGame.onGamePause();
			break;
		case RESUME:
			mGame.onGameResume();
			break;
		case RESTART:
			mGame.newGame(true);
			break;
		case SHARE:
			mScreenCapture.save();
			break;
		default:
			Log.d(GameActivity.class.getSimpleName(),
					"Unexpected button, tag: " + pButtonSprite.getTag());
		}
	}

	@Override
	public void onSaved(Bitmap pBitmap) {
		try {
			int[] scores = { 0, 0 };
			mGame.getScores(scores);
			mShare.share(pBitmap, scores[0], scores[1]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
