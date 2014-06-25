package com.doubleshoot.shooter;

import java.util.LinkedList;
import java.util.List;

import com.doubleshoot.behavior.IBehavior;
import com.doubleshoot.object.GOFilter;

public class ShooterBehaviorFilter implements GOFilter<BaseShooter> {
	private List<IBehavior> mWoundedBehaviors = new LinkedList<IBehavior>();
	private List<IBehavior> mDeadBehaviors = new LinkedList<IBehavior>();
	
	public void addWoundedBehavior(IBehavior pBehavior) {
		mWoundedBehaviors.add(pBehavior);
	}
	
	public void addDeadBehavior(IBehavior pBehavior) {
		mDeadBehaviors.add(pBehavior);
	}
	
	@Override
	public void filter(BaseShooter obj) {
		for (IBehavior b : mWoundedBehaviors)
			obj.addWoundedBehavior(b);
		
		for (IBehavior b : mDeadBehaviors)
			obj.addDeadBehavior(b);
	}

}
