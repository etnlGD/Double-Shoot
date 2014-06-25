package com.doubleshoot.troop;

public interface ITroopDetermin<T> {
	public T next(int index, T prev);
}
