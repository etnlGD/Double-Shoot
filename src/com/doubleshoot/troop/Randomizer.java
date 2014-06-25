package com.doubleshoot.troop;

import java.util.Random;

public class Randomizer {
	private static Random sRandom = new Random();
	
	public static interface Float {
		public float shuffle();
	}
	
	public static Float uniform(final float value) {
		return new Float() {
			
			@Override
			public float shuffle() {
				return value;
			}
		};
	}
	
	public static Float uniform(float center, float bias) {
		final float base = center - bias;
		final float rand = bias * 2;
		return new Float() {
			
			@Override
			public float shuffle() {
				return sRandom.nextFloat() * rand + base;
			}
		};
	}
	
	
	public static interface Int {
		public int shuffle();
	}
	
	public static Int uniform(final int value) {
		return new Int() {
			
			@Override
			public int shuffle() {
				return value;
			}
		};
	}
	
	public static Int uniform(int center, int bias) {
		final int base = center - bias;
		final int rand = bias * 2 + 1;
		return uniformFT(base, rand);
	}
	
	public static Int uniformFT(final int from, final int rand) {
		return new Int() {
			
			@Override
			public int shuffle() {
				return sRandom.nextInt(rand) + from;
			}
		};
	}

	public static Float asfloat(final Int uniform) {
		return new Float() {
			
			@Override
			public float shuffle() {
				return uniform.shuffle();
			}
		};
	}
	
}
