package com.doubleshoot.game;

import static com.doubleshoot.texture.SimpleTextureRegion.titledTexture;
import static com.doubleshoot.texture.SimpleTextureRegion.wholeTexture;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.JumpModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseBackIn;
import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseCircularInOut;

import android.content.Intent;
import android.opengl.GLES20;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.parallax.HVParallaxEntity;
import com.doubleshoot.texture.CachedTextureFactory;
import com.doubleshoot.texture.ITextureFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class GameStartActivity extends BaseGameActivity implements IEntityModifierListener {
	private static final float CAMERA_WIDTH = 800;
	private static final float CAMERA_HEIGHT = 450;

	private final float LETTER_GAP = 13;

	private ITextureFactory mTextureFactory;
	private ITextureRegion mGalaxy;
	private ITextureRegion mFlight;
	private ITextureRegion mGalaxyRain;
	private ITiledTextureRegion mFire;
	private ITextureRegion[] mTitle;
	private ButtonSprite mPlayButton;
	private boolean mPushed;
	private MoveXModifier flightOut;
	
	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		UmengUpdateAgent.update(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mPlayButton != null) {
			Vector2 vec = getCenterAlignedX(mFlight);
			mPlayButton.setPosition(vec.x, vec.y);
		}
		MobclickAgent.onResume(this);
		mPushed = false;
		if (flightOut != null)
			flightOut.reset();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		return new EngineOptions(true,
								ScreenOrientation.LANDSCAPE_FIXED,
								new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
								new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT));
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pCallback) {
		mTextureFactory = new CachedTextureFactory(getAssets(), getTextureManager());
		float x_width[][] = {
				{0,   55},  {66,  39}, {118, 37}, {169, 39}, {221, 9},
				{243, 39}, {296, 54}, {364, 36}, {413, 39}, {465, 39}, {517, 18},
		};
		float y = 0;
		float h = 65;
		int regionSize = x_width.length;
		mTitle = new ITextureRegion[regionSize];
		
		try {
			ITexture texture = mTextureFactory.loadTexture("title.png");
			
			for (int i = 0; i < regionSize; i++) {
				float x = x_width[i][0];
				float w = x_width[i][1];
				
				ITextureRegion region = new TextureRegion(texture, x, y, w, h);
				mTitle[i] = region;
			}
			
			mGalaxy = wholeTexture(mTextureFactory.loadTexture("star.bmp"));
			mGalaxyRain = wholeTexture(mTextureFactory.loadTexture("planet.bmp"));
			mFlight = wholeTexture(mTextureFactory.loadTexture("play.png"));
			mFire = titledTexture(mTextureFactory.loadTexture("fire.bmp"), 2, 2);
		} catch (IOException e) {
			Toast.makeText(this, "Touch the screen to move the particlesystem.", Toast.LENGTH_LONG).show();
			this.finish();
		}
		
		pCallback.onCreateResourcesFinished();
	}

	private void setBackground(Scene pScene) {
		
		ParallaxBackground bg = new AutoParallaxBackground(0, 0, 0, 10);
		VertexBufferObjectManager vbom = getVertexBufferObjectManager();
		
		IAreaShape starSprite = new Sprite(0, 0, mGalaxy, vbom);
		starSprite.setScale(CAMERA_WIDTH/starSprite.getWidth());
		bg.attachParallaxEntity(new HVParallaxEntity(0, 2, starSprite));
		starSprite.setBlendingEnabled(true);
		starSprite.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);

		IAreaShape planetSprite = new Sprite(0, 0, mGalaxyRain, vbom);
		planetSprite.setScale(CAMERA_WIDTH/planetSprite.getWidth());
		bg.attachParallaxEntity(new HVParallaxEntity(0, 10, planetSprite));
		planetSprite.setBlendingEnabled(true);
		planetSprite.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);

		pScene.setBackground(bg);
	}
	
	private IEntity createFire() {
		VertexBufferObjectManager vbom = getVertexBufferObjectManager();
		final IParticleEmitter fireEmitter = new PointParticleEmitter(0, 0);
		final SpriteParticleSystem particleSystem = new SpriteParticleSystem(fireEmitter, 25, 50, 100, mFire, vbom);

		particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(0.8f, 0.9f, 0.5f, 0.6f, 0.3f, 0.3f));
		particleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0, 0.4f));
		particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-80, -100, 0, 0));
		particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 180.0f));
		particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(0.4f, 1.0f));

		particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0f, 0.5f, 0.5f, 1.0f));
		particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0.5f, 1.0f, 1.0f, 0));
		return particleSystem;
	}
	
	private void attachTitle(Scene pScene) {
		VertexBufferObjectManager vbom = getVertexBufferObjectManager();
		
		float x = 0;
		float y = CAMERA_HEIGHT/4.f;
		float timeUnit = 0.5f;
		
		IAreaShape letters[] = new IAreaShape[mTitle.length];
		
		for (int i = 0; i < mTitle.length; i++) {
			ITextureRegion region = mTitle[i];
			IAreaShape letter = new Sprite(x, -region.getHeight(), region, vbom);
			x += region.getWidth();
			x += LETTER_GAP;
			letters[i] = letter;
		}
		
		float totalWidth = x  - LETTER_GAP;
		float offset = (CAMERA_WIDTH - totalWidth) / 2;
		for (int i = 0; i < letters.length; i++) {
			IAreaShape letter = letters[i];
			letter.setX(letter.getX() + offset);
			
			IEntityModifier fallDown = new MoveYModifier(1.f, letter.getY(), y, EaseBounceOut.getInstance());
			IEntityModifier colorChange = new SequenceEntityModifier(
					new DelayModifier(i * timeUnit),
					new JumpModifier(timeUnit, letter.getX(), letter.getX(), y, y, 20, EaseCircularInOut.getInstance()),
					new DelayModifier((mTitle.length-i-1) * timeUnit));
			
			letter.registerEntityModifier(
					new SequenceEntityModifier(fallDown, new DelayModifier(1.0f), new LoopEntityModifier(colorChange)));

			pScene.attachChild(letter);
		}
	}
	
	@Override
	public void onCreateScene(OnCreateSceneCallback pCallback) {
		Scene pScene = new Scene();
		VertexBufferObjectManager vbom = getVertexBufferObjectManager();
		
		float flightPos[] = {
				-mFlight.getWidth() * 2,
				(CAMERA_WIDTH - mFlight.getWidth())/2,
				CAMERA_WIDTH + mFlight.getWidth(),
		};
		
		mPlayButton = new ButtonSprite(
				flightPos[0], CAMERA_HEIGHT/2, mFlight, vbom);
		
		IEntityModifier flightIn = new MoveXModifier(
				1f, flightPos[0], flightPos[1], EaseBackOut.getInstance());
		flightOut = new MoveXModifier(
				1f, flightPos[1], flightPos[2], this, EaseBackIn.getInstance());
		
		mPlayButton.registerEntityModifier(
				new SequenceEntityModifier(
						new DelayModifier(1.0f), flightIn));
		
		mPlayButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				if (mPushed) return;
				
				mPushed = true;
				pButtonSprite.registerEntityModifier(flightOut);
			}
		});
		
		setBackground(pScene);
		attachTitle(pScene);
		IEntity fire = createFire();
		fire.setZIndex(-1);
		mPlayButton.attachChild(fire);
		mPlayButton.sortChildren();
		pScene.attachChild(mPlayButton);
		pScene.registerTouchArea(mPlayButton);
		pCallback.onCreateSceneFinished(pScene);
	}
	
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pCallback) {
		pCallback.onPopulateSceneFinished();
	}

	@Override
	public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

	}

	private Vector2 getCenterAlignedX(ITextureRegion pTextureRegion) {
		return new Vector2((CAMERA_WIDTH - pTextureRegion.getWidth())/2,
				(CAMERA_HEIGHT - pTextureRegion.getHeight())/2);
	}
	
	@Override
	public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
		Intent startGame = new Intent(GameStartActivity.this, GameActivity.class);
		startActivity(startGame);
	}
}
