package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.prefab.AlienType;

public class RandomTroopGenerator {
//		mWeightType = new WeightedRandom<String>();
//		mWeightType.addData(1.5f, AlienType.NO_BULLET);
//		mWeightType.addData(2, AlienType.COMMON);
//		mWeightType.addData(0.4f, AlienType.WITH_DEAD);
//		mWeightType.addData(0.8f, AlienType.REVENGER);
	public static TroopGenerator create() {
		Randomizer.Float sleepTime = Randomizer.uniform(1.f, 1.f);
		
		CompositeTroopGenerator compositeGenerator = new CompositeTroopGenerator();
		{	// Common Alien
			CompositeTroopGenerator commonGenerator = new CompositeTroopGenerator();
			Randomizer.Int commonAlienCount = Randomizer.uniform(4, 1);
			Randomizer.Float commonAlienDtime = Randomizer.uniform(0.5f, 0.05f);
			Randomizer.Float commonVelSize = Randomizer.uniform(100.f, 20.f);
			ITroopDetermin<String> alienDetermin =
					new UniformDeterminer<String>(AlienType.COMMON);
			
			// V
			commonGenerator.addGenerator(3, new SingleTroopGenerator(
					sleepTime,
					commonAlienDtime,
					commonAlienCount,
					alienDetermin,
					new UniPosFactory(
							Randomizer.asfloat(Randomizer.uniformFT(0, 2)),
							Randomizer.uniform(0.15f, 0.15f)),
					new VMotionFactory(0.5f, commonVelSize)));

			MotionDetermineFactory horzMoveFactory =
					new HorzMotionFactory(commonVelSize);

			// ->
			// ->
			commonGenerator.addGenerator(1, new SingleTroopGenerator(
					sleepTime,
					Randomizer.uniform(0.f),
					commonAlienCount,
					alienDetermin,
					new VLineEquiPosFactory(
							Randomizer.uniform(0.15f, 0.05f),
							Randomizer.uniform(0.085f, 0.02f)),
					horzMoveFactory));

			// -->
			// ->
			commonGenerator.addGenerator(2, new SingleTroopGenerator(
					sleepTime,
					commonAlienDtime,
					commonAlienCount,
					alienDetermin,
					new VLineEquiPosFactory(
							Randomizer.uniform(0.15f, 0.05f),
							Randomizer.uniform(0.07f, 0.01f)),
					horzMoveFactory));
			
			commonGenerator.addGenerator(2, new SingleTroopGenerator(
					sleepTime,
					commonAlienDtime,
					commonAlienCount,
					alienDetermin,
					new UniPosFactory(
							Randomizer.asfloat(Randomizer.uniformFT(0, 2)),
							Randomizer.uniform(0.3f, 0.2f)),
					horzMoveFactory));

			compositeGenerator.addGenerator(1.5f, commonGenerator);
		}
		
		{	// NO_BULLET alien
			CompositeTroopGenerator noBulletGenerator = new CompositeTroopGenerator();
			ITroopDetermin<String> soldierDetermin =
					new UniformDeterminer<String>(AlienType.NO_BULLET);
			MotionDetermineFactory motionFactory =
					new UniVelMotionFactory(
							new Vector2(0, 1), Randomizer.uniform(200f, 25f));
			Randomizer.Float deltaTime = Randomizer.uniform(0.1f, 0.02f);
			
			// |   |
			//   |
			noBulletGenerator.addGenerator(1, new SingleTroopGenerator(
					sleepTime,
					deltaTime,
					Randomizer.uniform(5, 2),
					soldierDetermin,
					new SymPosFacotry(
							Randomizer.uniform(0.5f, 0.3f),
							Randomizer.uniform(0.05f, 0.005f)),
					motionFactory));
			
			// |
			// |
			noBulletGenerator.addGenerator(1, new SingleTroopGenerator(
					sleepTime,
					Randomizer.uniform(0.2f, 0.02f),
					Randomizer.uniform(4, 1),
					soldierDetermin,
					new UniPosFactory(
							Randomizer.uniform(0.5f, 0.4f),
							Randomizer.uniform(0f)),
					motionFactory));

			PositionDetermineFactory equiPosFactory =
					new HLineEquiPosFactory(
						Randomizer.uniform(0.5f, 0.3f),
						Randomizer.uniform(0.065f, 0.005f));
			// | |
			//   |
			noBulletGenerator.addGenerator(2, new SingleTroopGenerator(
					sleepTime,
					deltaTime,
					Randomizer.uniform(5, 2),
					soldierDetermin,
					equiPosFactory,
					motionFactory));
			
			// | | |
			noBulletGenerator.addGenerator(1, new SingleTroopGenerator(
					sleepTime,
					Randomizer.uniform(0f),
					Randomizer.uniform(3, 1),
					soldierDetermin,
					equiPosFactory,
					motionFactory));
			
			compositeGenerator.addGenerator(2, noBulletGenerator);
		}
		
//		compositeGenerator.addGenerator(0.4f, generator);
//		compositeGenerator.addGenerator(0.8f, generator);
		
		return compositeGenerator;
	}
	
}
