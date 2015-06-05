package scraper.main;

import java.util.Timer;

public class PriceScraper {
	
	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new Scrape(), 
		0,
        24*60*60*1000);
	}
}
