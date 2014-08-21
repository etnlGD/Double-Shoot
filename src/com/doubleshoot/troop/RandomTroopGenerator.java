package com.doubleshoot.troop;

import com.badlogic.gdx.math.Vector2;
import com.doubleshoot.prefab.AlienType;

public class RandomTroopGenerator {

	public static TroopGenerator create(Randomizer.Float sleepTime) {
//		Randomizer.Float sleepTime = Randomizer.uniform(1.f, .5f);
		CompositeTroopGenerator compositeGenerator = new CompositeTroopGenerator();
		{	// Common Alien
			CompositeTroopGenerator commonGenerator = new CompositeTroopGenerator();
			Randomizer.Int commonAlienCount = Randomizer.uniform(4, 1);
			Randomizer.Float commonAlienDtime = Randomizer.uniform(0.5f, 0.05f);
			Randomizer.Float commonVelSize = Randomizer.uniform(100.f, 20.f);
			WeightedDeterminer<String> alienDetermin = new WeightedDeterminer<String>();
			alienDetermin.addData(1, AlienType.COMMON);
			alienDetermin.addData(0.1f, AlienType.WITH_DEAD);
			
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
			
			// -> ->
			commonGenerator.addGenerator(2, new SingleTroopGenerator(
					sleepTime,
					commonAlienDtime,
					commonAlienCount,
					alienDetermin,
					new UniPosFactory(
							Randomizer.asfloat(Randomizer.uniformFT(0, 2)),
							Randomizer.uniform(0.3f, 0.2f)),
					horzMoveFactory));

			compositeGenerator.addGenerator(1.2f, commonGenerator);
		}
		
		{	// NO_BULLET alien
			CompositeTroopGenerator noBulletGenerator = new CompositeTroopGenerator();
			WeightedDeterminer<String> soldierDetermin = new WeightedDeterminer<String>();
			soldierDetermin.addData(1, AlienType.NO_BULLET);
			soldierDetermin.addData(0.1f, AlienType.WITH_DEAD);
			
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
			
			compositeGenerator.addGenerator(1.5f, noBulletGenerator);
		}
		
		{	// REVENGER alien
			CompositeTroopGenerator revengerGenerator = new CompositeTroopGenerator();
			ITroopDetermin<String> soldierDetermin =
					new UniformDeterminer<String>(AlienType.REVENGER);
			Randomizer.Int revengerAlienCount = Randomizer.uniformFT(2, 3);
			Randomizer.Float commonAlienDtime = Randomizer.uniform(1f, 0.1f);
			Randomizer.Float commonVelSize = Randomizer.uniform(70.f, 20.f);
			
			// V
			revengerGenerator.addGenerator(3, new SingleTroopGenerator(
					sleepTime,
					commonAlienDtime,
					revengerAlienCount,
					soldierDetermin,
					new UniPosFactory(
							Randomizer.asfloat(Randomizer.uniformFT(0, 2)),
							Randomizer.uniform(0.15f, 0.15f)),
					new VMotionFactory(0.5f, commonVelSize)));

			MotionDetermineFactory horzMoveFactory =
					new HorzMotionFactory(commonVelSize);

			// ->
			// ->
			revengerGenerator.addGenerator(1, new SingleTroopGenerator(
					sleepTime,
					Randomizer.uniform(0.f),
					revengerAlienCount,
					soldierDetermin,
					new VLineEquiPosFactory(
							Randomizer.uniform(0.15f, 0.05f),
							Randomizer.uniform(0.085f, 0.02f)),
					horzMoveFactory));

			// -->
			// ->
			revengerGenerator.addGenerator(2, new SingleTroopGenerator(
					sleepTime,
					commonAlienDtime,
					revengerAlienCount,
					soldierDetermin,
					new VLineEquiPosFactory(
							Randomizer.uniform(0.15f, 0.05f),
							Randomizer.uniform(0.07f, 0.01f)),
					horzMoveFactory));
			
			// -> ->
			revengerGenerator.addGenerator(2, new SingleTroopGenerator(
					sleepTime,
					commonAlienDtime,
					revengerAlienCount,
					soldierDetermin,
					new UniPosFactory(
							Randomizer.asfloat(Randomizer.uniformFT(0, 2)),
							Randomizer.uniform(0.3f, 0.2f)),
					horzMoveFactory));

			compositeGenerator.addGenerator(0.8f, revengerGenerator);
		}
		
		{	// WITH_DEAD_BULLET alien
			CompositeTroopGenerator deadGenerator = new CompositeTroopGenerator();
			ITroopDetermin<String> soldierDetermin =
					new UniformDeterminer<String>(AlienType.WITH_DEAD);
			
			// | |
			//   |
			deadGenerator.addGenerator(2, new SingleTroopGenerator(
					sleepTime,
					Randomizer.uniform(0.1f, 0.02f),
					Randomizer.uniform(5, 2),
					soldierDetermin,
					new HLineEquiPosFactory(
							Randomizer.uniform(0.5f, 0.3f),
							Randomizer.uniform(0.065f, 0.005f)),
					new UniVelMotionFactory(
							new Vector2(0, 1), Randomizer.uniform(200f, 25f))));
			
			compositeGenerator.addGenerator(0.2f, deadGenerator);
		}
		
		{
			compositeGenerator.addGenerator(.1f, new SingleTroopGenerator(
					Randomizer.uniform(3f, 1f),
					Randomizer.uniform(0f),
					Randomizer.uniform(1),
					new UniformDeterminer<String>(AlienType.HUGE),
					new UniPosFactory(
							Randomizer.uniform(0.5f, 0.05f),
							Randomizer.uniform(-0.1f)),
					new ZMotionFactory(
							Randomizer.uniform(100f, 10f),
							Randomizer.uniform(0.2f, 0.025f),
							Randomizer.uniform(0.2f, 0.05f),
							Randomizer.uniform(0.8f, 0.05f))));
		}
		
		return compositeGenerator;
	}
	
}
