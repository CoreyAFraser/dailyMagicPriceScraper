package ie.moguntia.webcrawler;
import ie.moguntia.threads.*;
import java.net.*;

public class PSucker implements MessageReceiver {
	public PSucker(Queue q, int maxLevel, int maxThreads)
		throws InstantiationException, IllegalAccessException {
		ThreadController tc = new ThreadController(PSuckerThread.class,
												   maxThreads,
												   maxLevel,
												   q,
												   0,
												   this);
	}

	public void finishedAll() {
		// ignore
	}

	public void receiveMessage(Object o, int threadId) {
		// In our case, the object is already string, but that doesn't matter
		System.out.println("[" + threadId + "] " + o.toString());
	}

	public void finished(int threadId) {
		System.out.println("[" + threadId + "] finished");
	}

	public static void main(String[] args) {
		try {
			int maxLevel = 2;
			int maxThreads = 10;
			int maxDoc = -1;
			if (args.length == 5) {
				maxThreads = Integer.parseInt(args[4]);
			}
			if (args.length >= 4) {
				maxDoc = Integer.parseInt(args[3]);
			}
			if (args.length >= 3) {
				maxLevel = Integer.parseInt(args[2]);
			}
			if (args.length >= 2) {
				URLQueue q = new URLQueue();
				q.setFilenamePrefix(args[1]);
				q.setMaxElements(maxDoc);
				q.push(new URL(args[0]), 0);
				new PSucker(q, maxLevel, maxThreads);
				return;
			}
		} catch (Exception e) {
			System.err.println("An error occured: ");
			e.printStackTrace();
			// System.err.println(e.toString());
		}
		System.err.println("Usage: java PSucker <url> <filenamePrefix> [<maxLevel> [<maxDoc> [<maxThreads>]]]");
		System.err.println("Crawls the web for jpeg pictures and mpeg, avi or wmv movies.");
		System.err.println("-1 for either maxLevel or maxDoc means 'unlimited'.");
	}
}
