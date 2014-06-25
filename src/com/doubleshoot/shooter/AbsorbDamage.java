package com.doubleshoot.shooter;

public class AbsorbDamage extends AdditionDamage {
	
	@Override
	public float filter(float base) {
		return 0;
	}

	@Override
	public AdditionDamage add(AdditionDamage other) {
		return this;
	}
}
