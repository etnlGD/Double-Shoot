package com.doubleshoot.object;

public interface GOFilter<T extends GameObject> {
	
	public void filter(T obj);
	
}
