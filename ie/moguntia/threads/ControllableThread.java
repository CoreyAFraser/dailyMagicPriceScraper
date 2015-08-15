package ie.moguntia.threads;

/**
 * Abstract class that denotes a thread that can cooperate with a
 * ThreadController and has a Queue, a depth level and a MessageReceiver.
 */

abstract public class ControllableThread extends Thread {
	protected int level;
	protected int id;
	protected Queue queue;
	protected ThreadController tc;
	protected MessageReceiver mr;
	public void setId(int _id) {
		id = _id;
	}
	public void setLevel(int _level) {
		level = _level;
	}
	public void setQueue(Queue _queue) {
		queue = _queue;
	}
	public void setThreadController(ThreadController _tc) {
		tc = _tc;
	}
	public void setMessageReceiver(MessageReceiver _mr) {
		mr = _mr;
	}
	public ControllableThread() {
	}
	public void run() {
		// pop new urls from the queue until queue is empty
		for (Object newTask = queue.pop(level);
			 newTask != null;
			 newTask = queue.pop(level)) {
			// Tell the message receiver what we're doing now
			mr.receiveMessage(newTask, id);
			// Process the newTask
			process(newTask);
			// If there are less threads running than it could, try
			// starting more threads
			if (tc.getMaxThreads() > tc.getRunningThreads()) {
				try {
					tc.startThreads();
				} catch (Exception e) {
					System.err.println("[" + id + "] " + e.toString());
				}
			}
		}
		// Notify the ThreadController that we're done
		tc.finished(id);
	}

	/**
	 * The thread invokes the process method for each object in the queue
	 */
	public abstract void process(Object o);
}
