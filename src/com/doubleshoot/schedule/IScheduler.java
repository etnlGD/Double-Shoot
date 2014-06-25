package com.doubleshoot.schedule;

public interface IScheduler {

	public ITask schedule(Runnable pRunnable, float pDelayedSeconds);

	public ITask schedule(Runnable pRunnable);

	public void cancelAll();
}