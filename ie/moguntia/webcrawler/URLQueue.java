package ie.moguntia.webcrawler;

import java.net.*;
import java.util.*;

/**
 * Class URLQueue
 *
 * This class implements the ie.moguntia.Queue interface for queues of 'tasks'
 * in a multithreaded environment.
 *
 * This class has a queue and a set of URLs (exactly: Strings that represent
 * URLs). Synchronised methods are provided for inserting in both the set
 * and the queue, retrieving (and removing) an element from the queue only and
 * for clearing the queue.
 *
 * The intended use of this class is inside a web crawler.
 * The crawler can add new urls as it encounters them, and another thread
 * can pop the queue and crawl the remaining urls.
 *
 * A 'depth level' is supported. If you run a multithreaded crawler and want
 * want to know, how far a certain page is away from your starting point,
 * e.g. for limiting the maximum crawling depth, you must take care that
 * the crawlers only read urls from level n and add new urls to be processed
 * to level n+1. This is important. The reason: Consider the following link
 * structure (capital letters denoting pages and -> denoting a link):
 * A->B, B->C, A->C, C->D. The maximum link depth in this example is 2.
 * So, you want page D to stored, because (A->C->D) its distance from A is 2.
 * But what if the crawler first processes the link B->C, before it gathers
 * A->C ? In this case, your crawler might think, D's distance from A is 3.
 * If C->D has not been processed yet, the crawler might still be able to
 * update the distance for C, but what if D has already been discarded,
 * because it is too far away? As you see, this gets very messy, so it is
 * better to prevent that two or more crawlers 'overtake' each other by
 * just synchronizing them.
 *
 * Because we only read from level n and write to n+1, only two queues need
 * to be supported, not a queue for each level. When pushing or popping
 * elements to/from the queue, we can use the depth level modulo 2 to determine
 * which queue to use.
 * 
 * In a multithreaded crawler, a thread should die, if pop(n) returns null.
 * This way, the crawler can be halted by clearing the queue.
 * If no thread is running any more for depth level n, a thread manager may
 * decide to create new threads for depth level n+1.
 *
 * In addition to the methods defined by the interface queue, this class
 * supports a String as additional data that is through this way handed over
 * to the thread. This string is used to specifiy a filename prefix, where
 * the retrieved files are stored.
 *
 * This code is in the public domain.
 *
 * @author Andreas Hess <andreas.hess@ucd.ie>, 01/02/2003
 * 
 */

public class URLQueue implements ie.moguntia.threads.Queue {

	LinkedList evenQueue;
	LinkedList oddQueue;
	Set gatheredLinks;
	Set processedLinks;

	/**
	 * Maximum number of elements allowed in the gatheredLinks set
	 */
	int maxElements;

	/**
	 * Additional data to be passed to the thread: A filename prefix that
	 * specifies where the spidered files should be stored
	 */
	String filenamePrefix;

	public URLQueue() {
		evenQueue = new LinkedList();
		oddQueue = new LinkedList();
		gatheredLinks = new HashSet();
		processedLinks = new HashSet();
		maxElements = -1;
		filenamePrefix = "";
	}

	public URLQueue(int _maxElements, String _filenamePrefix) {
		evenQueue = new LinkedList();
		oddQueue = new LinkedList();
		gatheredLinks = new HashSet();
		processedLinks = new HashSet();
		maxElements = _maxElements;
		filenamePrefix = _filenamePrefix;
	}

	/**
	 * Setter for filename prefix
	 */
	public void setFilenamePrefix(String _filenamePrefix) {
		filenamePrefix = _filenamePrefix;
	}

	/**
	 * Getter for filename prefix
	 */
	public String getFilenamePrefix() {
		return filenamePrefix;
	}

	/**
	 * Set the maximum number of allowed elements
	 */
	public void setMaxElements(int _maxElements) {
		maxElements = _maxElements;
	}

	/**
	 * Return all links gathered so far
	 * This method is not synchronized, so use it with care when in a
	 * multithreaded envireonment.
	 */
	public Set getGatheredElements() {
		return gatheredLinks;
	}

	/**
	 * Return all links processed so far
	 * This method is not synchronized, so use it with care when in a
	 * multithreaded envireonment.
	 */
	public Set getProcessedElements() {
		return processedLinks;
	}

	/**
	 * Return how many elements are in the queue
	 */
	public int getQueueSize(int level) {
		if (level % 2 == 0) {
			return evenQueue.size();
		} else {
			return oddQueue.size();
		}
	}

	/**
	 * Return how many links have been processed yet
	 */
	public int getProcessedSize() {
		return processedLinks.size();
	}

	/**
	 * Return how many links have been gathered yet
	 */
	public int getGatheredSize() {
		return gatheredLinks.size();
	}

	/**
	 * Return and remove the first element from the appropriate queue
	 * Note that the return type of this method is Object for compliance
	 * with interface Queue.
	 */
	public synchronized Object pop(int level) {
		String s;
		// try to get element from the appropriate queue
		// is the queue is empty, return null
		if (level % 2 == 0) {
			if (evenQueue.size() == 0) {
				return null;
			} else {
				s = (String) evenQueue.removeFirst();
			}
		} else {
			if (oddQueue.size() == 0) {
				return null;
			} else {
				s = (String) oddQueue.removeFirst();
			}
		}
		// convert the string to a url and add to the set of processed links
		try {
			URL url = new URL(s);
			processedLinks.add(s);
			return url;
		} catch (MalformedURLException e) {
			// shouldn't happen, as only URLs can be pushed
			return null;
		}
	}

	/**
	 * Add an element at the end of the appropriate queue
	 * Note that the type of argument url is Object for compliance with
	 * interface Queue.
	 */
	public synchronized boolean push(Object url, int level) {
		// don't allow more than maxElements links to be gathered
		if (maxElements != -1 && maxElements <= gatheredLinks.size())
			return false;
		String s = ((URL) url).toString();
		if (gatheredLinks.add(s)) {
			// has not been in set yet, so add to the appropriate queue
			if (level % 2 == 0) {
				evenQueue.addLast(s);
			} else {
				oddQueue.addLast(s);
			}
			return true;
		} else {
			// this link has already been gathered
			return false;
		}
	}

	/**
	 * Clear both queues
	 * The sets of gathered and processed Elements are not affected.
	 */
	public synchronized void clear() {
		evenQueue.clear();
		oddQueue.clear();
	}
}
