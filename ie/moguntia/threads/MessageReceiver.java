package ie.moguntia.threads;

/**
 * Simple interface that allows a thread to tell another class what it is
 * currently doing, i.e. for displaying status information on the screen or
 * anything.
 *
 * This code is in the public domain.
 *
 * @author Andreas Hess <andreas.hess@ucd.ie>, 01/02/2003
 * 
 */

public interface MessageReceiver {

	/**
	 * Receive a message from a thread
	 * ThreadIds may refer to threadIds issued by a ThreadController,
	 * but this is not a requirement. The threadId should just allow the
	 * receiving class to determine which thread has sent the message.
	 */
	public void receiveMessage(Object theMessage, int threadId);

	/**
	 * Receive a 'this thread has ended'-message
	 * This message is sent by a ThreadController when a thread has announced
	 * his 'death' to the controller.
	 */
	public void finished(int threadId);

	/**
	 * Receive a 'all threads have ended'-message
	 * This message is sent by a ThreadController when all threads have
	 * announced their 'death' to the controller and no other threads
	 * are to be started.
	 */
	public void finishedAll();
}
