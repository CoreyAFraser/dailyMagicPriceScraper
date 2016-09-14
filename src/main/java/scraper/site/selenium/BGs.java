package scraper.site.selenium;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scraper.main.Card;
import scraper.util.shared.SharedResources;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BGs {
	public static ConcurrentSkipListSet<String> toBeCrawled = new ConcurrentSkipListSet<String>();
	public static ConcurrentSkipListSet<String> allUrls = new ConcurrentSkipListSet<String>();
	
	public static ConcurrentHashMap<Integer,ProcessCards> cardsThreadsMap = new ConcurrentHashMap<Integer,ProcessCards>();
	public static ConcurrentHashMap<Integer,ProcessLinks> linksThreadsMap = new ConcurrentHashMap<Integer,ProcessLinks>();
	public static ConcurrentHashMap<Integer,Crawl> crawlThreadsMap = new ConcurrentHashMap<Integer,Crawl>();
	public static String baseUrl = "http://store.battlegroundgames.com";
	public static ExecutorService cardsExecutor;
	public static ExecutorService linksExecutor;
	public static ExecutorService crawlExecutor;
	/*public static int cardsThreads = 0;
	public static int linksThreads = 0;
	public static int crawlThreads = 0;*/
	public static Object cardsLock = new Object();
	public static Object linksLock = new Object();
	public static Object crawlLock = new Object();
	public static int cards = 0;
	public static int maxThreads = 15;

	public static void getCards() throws Exception {
		cardsExecutor = Executors.newFixedThreadPool(maxThreads);
		linksExecutor = Executors.newFixedThreadPool(maxThreads);
		crawlExecutor = Executors.newFixedThreadPool(maxThreads);
		allUrls.add(baseUrl + "/buylist");
		toBeCrawled.add(baseUrl + "/buylist");
		
		int oldCards = 0;
		int oldLinks = 0;
		int oldCrawl = 0;
		int oldTotalCards = 0;
		int oldUrls = 0;
		while(cardsThreadsMap.size() + linksThreadsMap.size() + crawlThreadsMap.size() > 0 || toBeCrawled.size() != 0) {
			if(toBeCrawled.size() != 0 && cardsThreadsMap.size() + linksThreadsMap.size() + crawlThreadsMap.size() == 0) {
				synchronized(crawlLock) {
					Integer index = crawlThreadsMap.size();
					Crawl crawl = new Crawl(index);
					crawlThreadsMap.put(index, crawl);
					crawlExecutor.submit(crawl);
				}
			}
			/*Thread.sleep(100);*/
			if(oldLinks != linksThreadsMap.size() || oldCrawl != crawlThreadsMap.size() || oldUrls != toBeCrawled.size() || oldCards != cardsThreadsMap.size() || oldTotalCards != cards ) {
				oldCards = cardsThreadsMap.size();
				oldLinks = linksThreadsMap.size();
				oldCrawl = crawlThreadsMap.size();
				oldTotalCards = cards;
				oldUrls = toBeCrawled.size();
				System.out.println("Cards Threads: " + cardsThreadsMap.size());
				System.out.println("Links Threads: " + linksThreadsMap.size());
				System.out.println("Crawl Threads: " + crawlThreadsMap.size());
				System.out.println("Urls: " + toBeCrawled.size());
				System.out.println("Visited Urls: " + allUrls.size());
				System.out.println("Total Cards: " + cards);
				System.out.println();
			}//*/
		}
		/*while(cards<5000) {
			if(toBeCrawled.size() != 0 && cardsThreads+linksThreads+crawlThreads == 0) {
				Crawl crawl = new Crawl();
				crawlExecutor.submit(crawl);
			}
			if(oldLinks != linksThreads || oldCrawl != crawlThreads || oldUrls != toBeCrawled.size() || oldCards != cardsThreads || oldTotalCards != cards ) {
				oldCards = cardsThreads;
				oldLinks = linksThreads;
				oldCrawl = crawlThreads;
				oldTotalCards = cards;
				oldUrls = toBeCrawled.size();
				System.out.println("Cards Threads: " + cardsThreads);
				System.out.println("Links Threads: " + linksThreads);
				System.out.println("Crawl Threads: " + crawlThreads);
				System.out.println("Urls: " + toBeCrawled.size());
				System.out.println("Visited Urls: " + allUrls.size());
				System.out.println("Total Cards: " + cards);
				System.out.println();
			}
		}*/
		cardsExecutor.shutdown();
		linksExecutor.shutdown();
		crawlExecutor.shutdown();
		/*run = false;
		System.out.println("=============================================================");
		System.out.println("=============================================================");
		System.out.println("Shutting Down");
		System.out.println("=============================================================");
		System.out.println("=============================================================");*/

		while (!cardsExecutor.isTerminated() && !linksExecutor.isTerminated() && !crawlExecutor.isTerminated()) {
			/*if(oldLinks != linksThreadsMap.size() || oldCrawl != crawlThreadsMap.size() || oldUrls != toBeCrawled.size() || oldCards != cardsThreadsMap.size() || oldTotalCards != cards ) {
				oldCards = cardsThreadsMap.size();
				oldLinks = linksThreadsMap.size();
				oldCrawl = crawlThreadsMap.size();
				oldTotalCards = cards;
				oldUrls = toBeCrawled.size();
				System.out.println("Cards Threads: " + cardsThreadsMap.size());
				System.out.println("Links Threads: " + linksThreadsMap.size());
				System.out.println("Crawl Threads: " + crawlThreadsMap.size());
				System.out.println("Urls: " + toBeCrawled.size());
				System.out.println("Visited Urls: " + allUrls.size());
				System.out.println("Total Cards: " + cards);
				System.out.println();
			}*/
		}
		//ScraperUtil.calculateElapsedTime();
	}
	
	public static class ProcessCards implements Runnable {
		Document html;
		Integer id;
		
		ProcessCards() {}
		
		ProcessCards(Document html) {
			this.html = html;
		}
		
		ProcessCards(Document html, Integer id) {
			this.html = html;
			this.id = id;
		}
		
		@Override
		public void run() {
			String cardName = "",
				   setName = "",
				   mintPrice = "",
				   qty = "",
				   foil = "Foil";
			Card card;
			
			Elements productRows = html.select(".product_row");

			for(Element productRow : productRows) {
				card = new Card();
				card.setSite("BGs");
				
				cardName = productRow.select(".product_name").text();
				if (cardName.contains(" - Foil")) {
					cardName = cardName.replaceAll(" - Foil", "");
					card.setFoil(foil);
				} 
				card.setName(cardName);
				
				if (setName.equals("")) {
					Elements descriptors = productRow.select(".descriptor_value");
					setName = descriptors.get(descriptors.size() - 1).text();
				}
				card.setSet(setName);
				
				Elements prices = productRows.select(".price");
				mintPrice = prices.get(0).text().replace("$", "").trim();
				card.setMintPrice(mintPrice);
				
				Elements quantities = productRows.select(".qty");
				qty = quantities.get(0).text().replace("x", "").trim();
				card.setQuantity(qty);
				
				if (card.getName().length() > SharedResources.nameLength)
					SharedResources.nameLength = card.getName().length();
				if (card.getSet().length() > SharedResources.setLength)
					SharedResources.setLength = card.getSet().length();

				if(!SharedResources.cards.contains(card)) {
					SharedResources.cards.add(card);
					cards++;
				}
			}
				cardsThreadsMap.remove(id);

		}
	}
	
	public static class ProcessLinks implements Runnable {
		Document html;
		Integer id;
		
		ProcessLinks() {}
		
		ProcessLinks(Document html) {
			this.html = html;
		}
		
		ProcessLinks(Document html, Integer id) {
			this.html = html;
			this.id = id;
		}
		
		@Override
		public void run() {
			String href;
			int goodUrl = -1;
			int numSlash = 0;
			Elements links = html.select("a[href]");
			
			for (Element link : links) {
				href = link.attr("abs:href");

				if (!href.startsWith("http")) {
					href = baseUrl + href;
				}
				goodUrl = href.indexOf("-");
				goodUrl = href.indexOf("-", goodUrl + 1);
				numSlash = href.split("/").length;

				if (href.length() > 1
						&& href.contains("/buylist/magic_singles-")
						&& !href.contains("[") && !href.contains("]")
						&& numSlash <= 6 && goodUrl != -1
						&& !allUrls.contains(href)) {
					allUrls.add(href);
					toBeCrawled.add(href);
				}
			}
				linksThreadsMap.remove(id);
		}
	}
	
	public static class Crawl implements Runnable {
		
		ProcessCards processCards;
		ProcessLinks processLinks;
		String html, urlStr;
		Document doc;
		boolean alive = true;
		Integer id;
		
		public Crawl() {}
		
		public Crawl(Integer id) {
			this.id = id;
		}
		
		public Crawl(String urlStr) {
			this.urlStr = urlStr;
		}

		@Override
		public void run() {
			while(toBeCrawled.size() > 0 || (linksThreadsMap.size() > 0 && crawlThreadsMap.size() < 5)) {
				try {
					if(toBeCrawled.size() > 0) {
						urlStr = toBeCrawled.pollFirst();
						
						doc = Jsoup.connect(urlStr).get();
						html = doc.body().text();
						
						
						synchronized(cardsLock) {
							Integer index = cardsThreadsMap.size();
							processCards = new ProcessCards(doc,index);
							cardsThreadsMap.put(index, processCards);
							cardsExecutor.submit(processCards);
						}
						
						synchronized(linksLock) {
							Integer index = linksThreadsMap.size();
							processLinks = new ProcessLinks(doc,index);
							linksThreadsMap.put(index, processLinks);
							cardsExecutor.submit(processLinks);
						}
		
						Thread.sleep(500);
						
						synchronized (crawlLock) {
							if (crawlThreadsMap.size() < maxThreads
									&& toBeCrawled.size() > 5) {
								int index = crawlThreadsMap.size();
								Crawl crawl = new Crawl(index);
								crawlThreadsMap.put(index, crawl);
								crawlExecutor.submit(crawl);
							}
						}
					}
					
				} catch (Exception e) {
				}
			}

				crawlThreadsMap.remove(id);
			
		}
	}
}
