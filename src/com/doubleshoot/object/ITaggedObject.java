package com.doubleshoot.object;

import java.util.Collection;

public interface ITaggedObject {
	
	public void addTags(Collection<String> tags);
	
	public Collection<String> allTags();
	
	public boolean addTag(String tag);
	
	public boolean removeTag(String tag);
	
	public boolean hasTag(String tag);
	
}
