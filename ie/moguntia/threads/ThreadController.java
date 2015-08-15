package ie.moguntia.threads;

/**
 * A simple controller class for a multithreaded environment, where threads
 * may insert and process 'tasks' from/into a queue. Multiple 'depth-levels'
 * are supported. 'Tasks' are not to be confused with OS tasks, but just
 * denote elements in the queue, i.e. a task for the thread to perform.
 *
 * Note that the depth level in this class is just to make sure that where
 * appropriate a thread may only read tasks from level n and write tasks to
 * level n+1, i.e. only two levels at a time are supported. The actual number
 * is in this class only used as a halting criteria, but a thread may use
 * the information.
 *
 * For more information on what the depth-levels are good for see the comments
 * for interface Queue.
 *
 * This code is in the public domain.
 *
 * @author Andreas Hess <andreas.hess@ucd.ie>, 01/02/2003
 * 
 */

public class ThreadController {

	/**
	 * current level (see interface Queue for details on levels)
	 */
	int level;

	/**
	 * maximum depth level allowed
	 * -1 if be unlimited
	 */
	int maxLevel;

	/**
	 * maximum number of parallel threads
	 * -1 if unlimited
	 */
	int maxThreads;

	/**
	 * the task queue
	 */
	Queue tasks;

	/**
	 * An object that is notified about what a thread does
	 * See comments for interface MessageReceiver for details.
	 */
	MessageReceiver receiver;

	/**
	 * The class of the threads created by this ThreadController
	 * This class is expected to be a subtype of ControllableThread.
	 */
	Class threadClass;

	/**
	 * A unique synchronized counter
	 */
	int counter;

	/**
	 * Number of currently running threads
	 * This value is handed to the threads as an id, so note that the thread
	 * id is not unique, but is always in the range 0...maxThreads-1
	 */
	int nThreads;

	/**
	 * Constructor that intializes the instance variables
	 * The queue may already contain some tasks.
	 * If _maxThreads > 0, _maxThreads threads are started immediately.
	 * If _tasks.size(_level) > _maxThreads == -1, then only
	 * _tasks.size(_level) threads are started. Note that this includes
	 * the case where _maxThreads == -1, therefore even if the number of
	 * allowed threads is unlimited, only a finite number of threads are
	 * started.
	 */
	public ThreadController(Class _threadClass,
							int _maxThreads,
							int _maxLevel,
							Queue _tasks,
							int _level,
							MessageReceiver _receiver)
		throws InstantiationException, IllegalAccessException {
		threadClass = _threadClass;
		maxThreads = _maxThreads;
		maxLevel = _maxLevel;
		tasks = _tasks;
		level = _level;
		receiver = _receiver;
		counter = 0;
		nThreads = 0;
		startThreads();
	}

	/**
	 * Get a unique number from a counter
	 */
	public synchronized int getUniqueNumber() {
		return counter++;
	}

	/**
	 * Adjust number of allowed threads and start new threads if possible
	 */
	public synchronized void setMaxThreads(int _maxThreads)
		throws InstantiationException, IllegalAccessException {
		maxThreads = _maxThreads;
		startThreads();
	}

	/**
	 * Get number of maximum allowed threads
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/**
	 * Get number of maximum level
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * Get number of currently running threads
	 */
	public int getRunningThreads() {
		return nThreads;
	}

	/**
	 * Called by a thread to tell the controller that it is about to stop.
	 * The threadId is handed over to the MessageReceiver.
	 * If this was the last running thread it means that one level of the
	 * queue has been completed. In this case, increment the level (if
	 * allowed) and start new threads.
	 */
	public synchronized void finished(int threadId) {
		nThreads--;
		receiver.finished(threadId);
		if (nThreads == 0) {
			level++;
			if (level > maxLevel) {
				receiver.finishedAll();
				return;
			}
			// debug
			// System.err.println("new level " + level);
			// if no tasks in queue we're don
			if (tasks.getQueueSize(level) == 0) {
				receiver.finishedAll();
				return;
			}
			try {
				startThreads();
			} catch (InstantiationException e) {
				// Something has gone wrong on the way, because if it hadn't
				// worked at all we wouldn't be here. Anyway, we can do
				// nothing about it, so we just quit instead of moving to
				// a new level.
			} catch (IllegalAccessException e) {
				// Something has gone wrong on the way, because if it hadn't
				// worked at all we wouldn't be here. Anyway, we can do
				// nothing about it, so we just quit instead of moving to
				// a new level.
			}
		}
	}

	/**
	 * Start the maximum number of allowed threads
	 */
	public synchronized void startThreads()
		throws InstantiationException, IllegalAccessException {
		// Start m threads
		// For more information on where m comes from see comment on
		// the constructor.
		int m = maxThreads - nThreads;
		int ts = tasks.getQueueSize(level);
		if (ts < m || maxThreads == -1) {
			m = ts;
		}
		// debug
		// System.err.println(m + " " + maxThreads + " " + nThreads + " " + ts);
		// Create some threads
		for (int n = 0; n < m; n++) {
			ControllableThread thread =
				(ControllableThread) threadClass.newInstance();
			thread.setThreadController(this);
			thread.setMessageReceiver(receiver);
			thread.setLevel(level);
			thread.setQueue(tasks);
			thread.setId(nThreads++);
			thread.start();
		}
	}
}
