package com.doubleshoot.schedule;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import org.andengine.engine.handler.IUpdateHandler;

public class Scheduler implements IUpdateHandler, IScheduler {
	private PriorityQueue<Task> mTaskQueue= new PriorityQueue<Task>();
	private LinkedList<Task> mFreeList = new LinkedList<Task>();
	private Collection<Task> mFreeing = mFreeList;
	private float mTimeline;
	private int mTaskId;
	
	@Override
	public ITask schedule(Runnable pRunnable, float pDelayedSeconds) {
		if (pDelayedSeconds < 0)
			return null;
		
		if (pRunnable == null)
			throw new NullPointerException("Runnable");
		
		Task task = new Task(mTaskId++, pDelayedSeconds + mTimeline, pRunnable);
		mFreeing.add(task);
		return task;
	}
	
	@Override
	public ITask schedule(Runnable pRunnable) {
		return schedule(pRunnable, 0);
	}
	
	private boolean testTask(Task task) {
		if (task.timeout(mTimeline)) {
			task.execute();
			return true;
		}
		
		return false;
	}
	
	private boolean pollTimeoutTasks() {
		Task command, removed;
		while (true) {
			command = mTaskQueue.peek();
			if (command != null && testTask(command)) {
				removed = mTaskQueue.poll();
				assert(removed == command);
			} else {
				break;
			}
		}
		
		return mFreeing.size() > 0;
	}
	
	private void execute_or_pend_impl() {
		Iterator<Task> it = mFreeList.iterator();
		while (it.hasNext()) {
			Task command = it.next();
			if (!testTask(command))
				mTaskQueue.add(command);
			
			it.remove();
		}
	}
	
	private boolean executeOrPend() {
		int prevTaskId = mTaskId;
		execute_or_pend_impl();
		return mTaskId != prevTaskId;
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		mTimeline += pSecondsElapsed;
		
		boolean newPending;
		do {
			// poll from main part, pend new to free part
			newPending = pollTimeoutTasks();
			mFreeing = mTaskQueue;
			
			// poll from free part, pend new to main part
			newPending = executeOrPend();
			mFreeing = mFreeList;
		} while (newPending);
	}

	@Override
	public void reset() {
		cancelAll();
	}
	
	@Override
	public void cancelAll() {
		mTaskId = 0;
		mTimeline = 0;
		mTaskQueue.clear();
		mFreeList.clear();
		mFreeing = mFreeList;
	}
	
	private class Task implements Comparable<Task>, ITask {
		private float time;
		private Runnable callback;
		private int taskId;
		
		public Task(int id, float pTime, Runnable runnable) {
			taskId = id;
			time = pTime;
			callback = runnable;
		}
		
		@Override
		public int compareTo(Task other) {
			if (time == other.time)
				return taskId < other.taskId ? -1 : 1;
			
			return time < other.time ? -1 : 1;
		}

		public boolean timeout(float timeline) {
			return time <= timeline;
		}
		
		public void execute() {
			assert(callback != null);
			
			Runnable runnable = callback;
			callback = null;
			runnable.run();
		}

		@Override
		public void cancel() throws HasExecutedException {
			checkExecetion();
			boolean removed = mTaskQueue.remove(this) || mFreeList.remove(this);
			assert(removed);
		}
		
		private void checkExecetion() throws HasExecutedException {
			if (isExecuted())
				throw new HasExecutedException(this);
		}

		@Override
		public boolean isExecuted() {
			return callback == null;
		}
	}

}
