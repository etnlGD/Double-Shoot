package com.doubleshoot.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConcreteGORegistry<T extends GameObject> implements GORegistry<T> {
	private Map<String, GOPipeline<T>> mFactoryRegistry;
	private List<String> mKeyArray;
	private List<GOFilter<? super T>> mFilters;
	
	public ConcreteGORegistry() {
		mFactoryRegistry = new HashMap<String, GOPipeline<T>>();
		mKeyArray = new ArrayList<String>();
		mFilters = new LinkedList<GOFilter<? super T>>();
	}
	
	@Override
	public void addFilter(GOFilter<? super T> filter) {
		if (filter != null)
			mFilters.add(filter);
	}
	
	@Override
	public GOFactory<T> registerFactory(String name, GOFactory<T> factory) {
		GOPipeline<T> prev;
		if (factory == null) {	// erase
			if ((prev = mFactoryRegistry.remove(name)) != null)
				mKeyArray.remove(name);
		} else {				// update
			GOPipeline<T> pipeline = new GOPipeline<T>(factory, mFilters);
			if ((prev = mFactoryRegistry.put(name, pipeline)) == null)
				mKeyArray.add(name);
		}
		
		return prev == null ? null : prev.getDelegate();
	}

	@Override
	public GOFactory<T> getFilteredFactory(String name) {
		return mFactoryRegistry.get(name);
	}

	@Override
	public String getFactoryNameAt(int index) {
		return mKeyArray.get(index);
	}

	@Override
	public int size() {
		assert(mKeyArray.size() == mFactoryRegistry.size());
		return mKeyArray.size();
	}
	

}
 