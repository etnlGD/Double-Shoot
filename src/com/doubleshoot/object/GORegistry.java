package com.doubleshoot.object;

public interface GORegistry<T extends GameObject> {
	
	public void addFilter(GOFilter<? super T> filter);
	
	public GOFactory<T> registerFactory(String name, GOFactory<T> factory);
	
	public GOFactory<T> getFilteredFactory(String name);
	
	public String getFactoryNameAt(int index);
	
	public int size();
	
}
