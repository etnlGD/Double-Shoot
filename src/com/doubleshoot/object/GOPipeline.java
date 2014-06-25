package com.doubleshoot.object;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class GOPipeline<T extends GameObject> implements GOFactory<T>{
	private GOFactory<T> mDelegateFactory;
	private List<GOFilter<? super T>> mFilters;
	
	public GOPipeline(GOFactory<T> impl, List<GOFilter<? super T>> shared) {
		mDelegateFactory = impl;
		mFilters = shared;
		if (mFilters == null)
			mFilters = new LinkedList<GOFilter<? super T>>();
	}
	
	public GOPipeline(GOFactory<T> impl) {
		this(impl, null);
	}
	
	public void addFilters(Collection<GOFilter<? super T>> filters) {
		if (filters != null)
			mFilters.addAll(filters);
	}
	
	public void addFilter(GOFilter<? super T> filter) {
		if (filter != null)
			mFilters.add(filter);
	}
	
	@Override
	public T create(GOEnvironment env, Vector2 pos, Vector2 velocity) {
		T created = mDelegateFactory.create(env, pos, velocity);
		for (GOFilter<? super T> filter : mFilters)
			filter.filter(created);
		
		return created;
	}
	
	public GOFactory<T> getDelegate() {
		return mDelegateFactory;
	}

}
