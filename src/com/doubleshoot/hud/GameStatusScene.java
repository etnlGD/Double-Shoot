package com.doubleshoot.hud;

import static com.doubleshoot.game.GameActivity.CAMERA_HEIGHT;
import static com.doubleshoot.game.GameActivity.CAMERA_WIDTH;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.modifier.ease.EaseCubicIn;
import org.andengine.util.modifier.ease.EaseCubicOut;

import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.game.listener.IGameListener;
import com.doubleshoot.hero.Hero;
import com.doubleshoot.hud.Lifebar.LookNFeel;
import com.doubleshoot.modifier.VisibilityEntityModifier;
import com.doubleshoot.shape.ShapeFactory;
import com.doubleshoot.shooter.BaseShooter;
import com.doubleshoot.shooter.Harmful;

public class GameStatusScene extends HUD implements IGameListener {
	private static final int SCORE_MAX_BITS = 5;
	private static final float ICON_VERTICLE_MARGIN = 10;
	
	private IAreaShape mLabel;
	private IAreaShape mPause;
	private IAreaShape mResume;
	private IAreaShape mRestart;
	private IAreaShape mShare;
	
	private IEntityModifier mLabelInModifier;
//	private IEntityModifier mLabelOutModifier;
	private IEntityModifier mShareInModifier;
//	private IEntityModifier mShareOutModifier;
	private IEntityModifier mRestartInModifier;
//	private IEntityModifier mAgainOutModifier;
	
	private Lifebar mLLifebar;
	private Lifebar mRLifebar;
	private GameScore mLeftScore;
	private GameScore mRightScore;
	
	private VisiblilityStack mVisiblilityStack = new VisiblilityStack();
	
	public GameStatusScene(IAreaShape pause, IAreaShape resume,
			IAreaShape restart, IAreaShape share, IAreaShape label,
			ITextCreator pTextCreator, ShapeFactory bloodFactory, ShapeFactory frameFactory) {
		mPause = pause;
		mResume = resume;
		mRestart = restart;
		mShare = share;
		mLabel = label;
		
		// lifebar
		LookNFeel lifebarLook = new LookNFeel();
		lifebarLook.animUnit = 0.06f;
		lifebarLook.bloodFactory = bloodFactory;
		lifebarLook.frameFactory = frameFactory;
		lifebarLook.bloodUnitCount = 15;
		lifebarLook.horzMargin = 3;
		lifebarLook.leftAligned = true;
		lifebarLook.unitGap = 1;
		lifebarLook.vertMargin = 2;
		mLLifebar = new Lifebar(lifebarLook);
		lifebarLook.leftAligned = false;
		mRLifebar = new Lifebar(lifebarLook);
		mLLifebar.attachToScene(0, 0, this);
		mRLifebar.attachToScene(CAMERA_WIDTH - mRLifebar.getSize().x, 0, this);
		
		// score
		float maxDigitWidth = FontUtils.getMaxWidthInDigits(pTextCreator.getFont());
		mLeftScore = new GameScore(maxDigitWidth, SCORE_MAX_BITS, pTextCreator);
		mLeftScore.attachToScene(maxDigitWidth * SCORE_MAX_BITS, mLLifebar.getSize().y, this);
		mRightScore = new GameScore(maxDigitWidth, SCORE_MAX_BITS, pTextCreator);
		mRightScore.attachToScene(CAMERA_WIDTH, mRLifebar.getSize().y, this);
		
		setXCentered(pause);
		pause.setY(0);
		setAtCenter(resume);
		
		// gameover
		setXCentered(label);
		label.setY(CAMERA_HEIGHT/2 - label.getHeight() - ICON_VERTICLE_MARGIN);
		
		restart.setPosition(CAMERA_WIDTH/2 - restart.getWidth()*1.5f,
				CAMERA_HEIGHT/2 + ICON_VERTICLE_MARGIN);
		share.setPosition(CAMERA_WIDTH/2 + pause.getWidth()*0.5f,
				CAMERA_HEIGHT/2 + ICON_VERTICLE_MARGIN);
		
		attachChild(pause);
		attachChild(resume);
		attachChild(restart);
		attachChild(share);
		attachChild(label);
		
		initAnim();
		
		mVisiblilityStack.append(mLabel);
		mVisiblilityStack.append(mRestart);
		mVisiblilityStack.append(mPause);
		mVisiblilityStack.append(mShare);
	}
	
	private void setAtCenter(IAreaShape pShape) {
		setXCentered(pShape);
		setYCentered(pShape);
	}
	
	private void setXCentered(IAreaShape pShape) {
		pShape.setX((CAMERA_WIDTH - pShape.getWidth())/2);
	}
	
	private void setYCentered(IAreaShape pShape) {
		pShape.setY((CAMERA_HEIGHT - pShape.getHeight())/2);
	}
	
	private void initAnim() {
		mLabelInModifier = new ScaleModifier(0.5f, 3.0f, 1.0f);
		
		mRestartInModifier = new SequenceEntityModifier(
				new VisibilityEntityModifier(0.5f, false, true),
				new ScaleModifier(0.5f, 0, 1.0f, EaseBackOut.getInstance()));
		mShareInModifier = mRestartInModifier.deepCopy();
		
		IEntityModifier scaleLoop = new LoopEntityModifier(
				new SequenceEntityModifier(
						new ScaleModifier(0.8f, 0.9f, 1.0f, EaseCubicOut.getInstance()),
						new ScaleModifier(0.8f, 1.0f, 0.9f, EaseCubicIn.getInstance())));
		mResume.registerEntityModifier(scaleLoop);
		mPause.registerEntityModifier(scaleLoop.deepCopy());
	}
	
	@Override
	public void onGameStart(Hero pLeftHero, Hero pRightHero) {
		mPause.setVisible(true);
		mLabel.setVisible(false);
		mRestart.setVisible(false);
		mShare.setVisible(false);
		mResume.setVisible(false);
		
		mLLifebar.setPercent(1);
		mRLifebar.setPercent(1);
		
		pLeftHero.setScoreChangeListener(mLeftScore);
		pLeftHero.addWoundedBehavior(new IBehavior() {
			@Override
			public void onActivated(BaseShooter host, Harmful source, float damage) {
				mLLifebar.setPercent(host.getHealthPercent());
			}
		});
		
		pRightHero.setScoreChangeListener(mRightScore);
		pRightHero.addWoundedBehavior(new IBehavior() {
			
			@Override
			public void onActivated(BaseShooter host, Harmful source, float damage) {
				mRLifebar.setPercent(host.getHealthPercent());
			}
		});
	}

	private void show(IAreaShape pShape, IEntityModifier pModifier) {
		pModifier.reset();
		pShape.setVisible(true);
		pShape.registerEntityModifier(pModifier);
	}
	
	@Override
	public void onGameover() {
		mPause.setVisible(false);
		show(mLabel, mLabelInModifier);
		show(mRestart, mRestartInModifier);
		show(mShare, mShareInModifier);
	}
	
	@Override
	public void onGamePause() {
		mPause.setVisible(false);
		mResume.setVisible(true);
	}

	@Override
	public void onGameResume() {
		mResume.setVisible(false);
		mPause.setVisible(true);
	}
	
	public void beginDraw() {
		mVisiblilityStack.changeVisibility(false);
	}
	
	public void endDraw() {
		mVisiblilityStack.recover();
	}
	
	private class VisiblilityStack {
		private Map<IAreaShape, Boolean> mVisibleState = new HashMap<IAreaShape, Boolean>();
		private List<IAreaShape> mUnstableShapes = new LinkedList<IAreaShape>();
		
		private void saveAndChange(boolean visible) {
			for (IAreaShape shape : mUnstableShapes) {
				mVisibleState.put(shape, shape.isVisible());
				shape.setVisible(visible);
			}
		}
		
		public void changeVisibility(boolean visible) {
			saveAndChange(visible);
		}
		
		public void append(IAreaShape pShape) {
			mUnstableShapes.add(pShape);
		}
		
		public void recover() {
			for (Entry<IAreaShape, Boolean> visibility : mVisibleState.entrySet()) {
				IAreaShape shape = visibility.getKey();
				boolean visible = visibility.getValue();
				shape.setVisible(visible);
			}
			
			mVisibleState.clear();
		}
	}
}
