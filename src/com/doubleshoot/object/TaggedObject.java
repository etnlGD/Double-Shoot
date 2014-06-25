package com.doubleshoot.object;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TaggedObject implements ITaggedObject {
	private Set<String> mTags = new HashSet<String>();
	
	@Override
	public void addTags(Collection<String> tags) {
		mTags.addAll(tags);
	}
	
	@Override
	public boolean addTag(String tag) {
		return mTags.add(tag);
	}
	
	@Override
	public boolean removeTag(String tag) {
		return mTags.remove(tag);
	}
	
	@Override
	public boolean hasTag(String tag) {
		return mTags.contains(tag);
	}

	@Override
	public Collection<String> allTags() {
		return mTags;
	}
	
}
