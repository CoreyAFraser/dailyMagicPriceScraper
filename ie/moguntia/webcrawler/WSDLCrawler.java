package ie.moguntia.webcrawler;
import ie.moguntia.threads.*;
import java.net.*;

public class WSDLCrawler implements MessageReceiver {
	public WSDLCrawler(Queue q, int maxLevel, int maxThreads)
		throws InstantiationException, IllegalAccessException {
		ThreadController tc = new ThreadController(WSDLCrawlerThread.class,
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
			int maxThreads = 5;
			if (args.length == 4) {
				maxThreads = Integer.parseInt(args[3]);
			}
			if (args.length >= 3) {
				maxLevel = Integer.parseInt(args[2]);
			}
			if (args.length >= 2) {
				URLQueue q = new URLQueue();
				q.setFilenamePrefix(args[1]);
				q.push(new URL(args[0]), 0);
				new WSDLCrawler(q, maxLevel, maxThreads);
				return;
			}
		} catch (Exception e) {
			System.err.println("An error occured: ");
			e.printStackTrace();
			// System.err.println(e.toString());
		}
		System.err.println("Usage: java WSDLCrawler <url> <filenamePrefix> [<maxLevel> [<maxThreads>]]");
		System.err.println("Crawls the web for WSDL descriptions.");
	}
}
