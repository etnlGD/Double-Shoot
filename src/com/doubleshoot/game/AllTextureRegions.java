package com.doubleshoot.game;

import java.io.IOException;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.doubleshoot.texture.CachedRegionManager;
import com.doubleshoot.texture.IRegionManager;
import com.doubleshoot.texture.ITextureFactory;

public class AllTextureRegions {
	
	public static IRegionManager loadAll(ITextureFactory texFactory)
			throws IOException {
		CachedRegionManager regionManager = new CachedRegionManager(texFactory);
		
		ITexture tex = texFactory.loadTexture("1945.png");
		ITextureRegion region1 = new TextureRegion(tex, 7, 413, 59, 43);
		ITextureRegion region2 = new TextureRegion(tex, 73, 413, 59, 43);
		ITextureRegion region3 = new TextureRegion(tex, 139, 413, 59, 43);
		ITiledTextureRegion heroRegions = new TiledTextureRegion(tex, region1, region2, region3);
		
		regionManager.addRegion("Lifebar.Frame", "lifebar_frame.png");
		regionManager.addRegion("Lifebar.Unit", "lifebar_unit.png");
		regionManager.addRegion("Planet", "planet.bmp");
		regionManager.addRegion("Star", "star.bmp");
		regionManager.addRegion("TiledHero", heroRegions);
		regionManager.addRegion("GameOver", "gameover.png");
		regionManager.addRegion("Restart", "restart.png");
		regionManager.addRegion("Share", "share.png");
		regionManager.addRegion("Continue", "resume.png");
		regionManager.addRegion("Pause", "pause.png");
		
		regionManager.addRegion("Alien.Yellow","1945.png", 4, 4, 32, 32);
		
		int idx = 0;
		for (int y = 0; y < 192*3; y += 192)
			for (int x = 0; x < 1600; x += 400) {
				regionManager.addRegion("Bomb."+idx,  "bomb.png", x,  y, 400, 192);
				++idx;
			}

		regionManager.addRegion("Alien.Blue", 	"1945.png", 4, 37, 32, 32);
		regionManager.addRegion("Alien.Green", "1945.png", 4, 70, 32, 32);
		regionManager.addRegion("Alien.White", "1945.png", 4, 103, 32, 32);
		regionManager.addRegion("Alien.Huge", "1945.png", 798, 20, 92, 72);
		
		regionManager.addRegion("Bullet.HugeYellow", "1945.png", 48, 176, 9, 20);
		regionManager.addRegion("Bullet.Red", "1945.png", 278, 113, 13, 13);
		regionManager.addRegion("Bullet.Laser", "laser.png");
		regionManager.addRegion("Bullet.Yellow", "1945.png", 49, 214, 9, 9);
		regionManager.addRegion("Bullet.Missile", "1945.png", 279, 272, 11, 22);
		
		regionManager.addRegion("Reward.Scatter", "reward_scatter.png");
		regionManager.addRegion("Reward.Parallel", "reward_parallel.png");
		regionManager.addRegion("Reward.Laser", "reward_laser.png");
		regionManager.addRegion("Reward.Missile", "reward_missile.png");
		regionManager.addRegion("Reward.Heal", "reward_heal.png");
		return regionManager;
	}
	
}
