package com.doubleshoot.shooter;

public class AdditionDamage {
	private float mPower = 1;
	private float mOffset;
	private float mFixed;
	
	public AdditionDamage() {
	}
	
	public AdditionDamage(float power, float offset, float fixed) {
		this.mPower = power;
		this.mOffset = offset;
		this.mFixed = fixed;
	}
	
	public float filter(float base) {
		return (base+mOffset) * mPower + mFixed;
	}
	
	public AdditionDamage add(AdditionDamage other) {
		return new AdditionDamage(
				this.mPower * other.mPower,
				this.mOffset+ other.mOffset,
				this.mFixed + other.mFixed);
	}
	
	public void abs() {
		mPower = Math.max(0, mPower);
		mOffset= Math.max(0, mOffset);
		mFixed = Math.max(0, mFixed);
	}
}