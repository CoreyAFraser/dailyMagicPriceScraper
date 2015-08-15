package ie.moguntia.threads;

import java.util.Set;

/**
 * Interface Queue
 *
 * This interface defines a Queue for a multithreaded environment, where
 * threads can insert (push) Objects (tasks) at the end of the queue and
 * retrieve (pop) them from the beginning.
 *
 * This interface supports a level. This is useful, if the threads have to
 * obey certain restrictions, e.g. read tasks only from level n and write
 * tasks only to level n+1. A thread controller must handle this.
 *
 * Note that this interface does not make any assumption of neither how many
 * levels are supported by the implementing class, nor if levels are supported
 * at all. An implementing class could decide just to ignore the level
 * argument.
 *
 * This code is in the public domain.
 *
 * @author Andreas Hess <andreas.hess@ucd.ie>, 01/02/2003
 * 
 */

public interface Queue {
	public Set getGatheredElements();
	public Set getProcessedElements();
	public int getQueueSize(int level);
	public int getProcessedSize();
	public int getGatheredSize();
	public void setMaxElements(int elements);
	public Object pop(int level);
	public boolean push(Object task, int level);
	public void clear();
}
