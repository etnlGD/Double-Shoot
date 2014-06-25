package com.doubleshoot.schedule;

public interface ITask {
	
	@SuppressWarnings("serial")
	public class HasExecutedException extends Exception {
		private ITask task;

		public HasExecutedException(ITask t) {
			task = t;
		}
		
		public ITask getTask() {
			return task;
		}
	}
	
	public void cancel() throws HasExecutedException;
	
	public boolean isExecuted();
}
