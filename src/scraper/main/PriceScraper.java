package scraper.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.openqa.selenium.firefox.FirefoxDriver;

import scraper.site.StrikeZone;
import scraper.site.TrollAndToad;
import scraper.site.selenium.ChannelFireball;
import scraper.site.selenium.UntappedGames;
import scraper.util.CardFoilComparator;
import scraper.util.CardNameComparator;
import scraper.util.CardPriceComparator;
import scraper.util.CardSetComparator;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

public class PriceScraper {
	
	public static void main(String[] args) {
		
		while(true) {
		    Calendar c = Calendar.getInstance();
		    c.setTime(new Date());
		    System.out.println(c.get(Calendar.HOUR_OF_DAY));
		    
		    if(23 <= c.get(Calendar.HOUR_OF_DAY) && c.get(Calendar.HOUR_OF_DAY) <= 24) {
		    	System.out.println("Starting");
				try {
					DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();
					String fileName;
					String path;
					String eMail;
					String eol = "<br>";//System.getProperty("line.separator");  
					String message = "New Price List" + eol;
					
					SharedResources.logger = new PrintWriter("logs\\" + dateTimeFormat.format(date)+"_log.txt","UTF-8");
		
					ScraperUtil.log("Started Successfully");
		
					SharedResources.incrBegin = System.currentTimeMillis();
					SharedResources.begin = System.currentTimeMillis();
					SharedResources.cards = new ArrayList<Card>();
					try {
						try {
							ScraperUtil.log("StrikeZone Starting");
							StrikeZone.getCards();
							ScraperUtil.log("StrikeZone Done");
						} catch (java.io.FileNotFoundException e) {
							message = message + "\r\nStrikeZone appears to be down";
							ScraperUtil.log("StrikeZone Error");
							e.printStackTrace(SharedResources.logger);
						}
						ScraperUtil.calculateElapsedTime();
						
						try{
							ScraperUtil.log("Troll and Toad Starting");
							TrollAndToad.getCards();
							ScraperUtil.log("Troll and Toad Done");
						} catch (java.io.FileNotFoundException e) {
							message = message + eol + "Troll and Toad appears to be down";
							ScraperUtil.log("Troll and Toad Error");
							e.printStackTrace(SharedResources.logger);
						}
						ScraperUtil.calculateElapsedTime();
					
		
						ScraperUtil.log("Starting Browser");
						SharedResources.driver = new FirefoxDriver();
						ScraperUtil.log("Browser Open");
						ScraperUtil.calculateElapsedTime();
						
						/*try {
							ScraperUtil.log("TJ Games Starting");
							TJ.getCards();
							ScraperUtil.log("TJ Games Done");
						} catch (java.io.FileNotFoundException e) {
							message = message + eol + "TJ Games appears to be down";
							ScraperUtil.log("TJ Games Error");
							e.printStackTrace(SharedResources.logger);
						}
						ScraperUtil.calculateElapsedTime();*/
						
						try {
							ScraperUtil.log("Untapped Games Starting");
							UntappedGames.getCards();
							ScraperUtil.log("Untapped Games Done");
						} catch (java.io.FileNotFoundException e) {
							message = message + eol + "Untapped Games appears to be down";
							ScraperUtil.log("Untapped Games Error");
							e.printStackTrace(SharedResources.logger);
						}
						ScraperUtil.calculateElapsedTime();
						
						/*try {
							ScraperUtil.log("ABU Starting");
							ABUGames.getCards();
							ScraperUtil.log("ABU Done");
						} catch (java.io.FileNotFoundException e) {
							message = message + eol + "ABU appears to be down";
							ScraperUtil.log("ABU Error");
							e.printStackTrace(SharedResources.logger);
						}
						ScraperUtil.calculateElapsedTime();*/
						
						try {
							ScraperUtil.log("Channel Fireball Starting");
							ChannelFireball.getCards();
							ScraperUtil.log("Channel Fireball Done");
						} catch (java.io.FileNotFoundException e) {
							message = message + eol + "Channel Fireball appears to be down";
							ScraperUtil.log("Channel Fireball Error");
							e.printStackTrace(SharedResources.logger);
						}
						ScraperUtil.calculateElapsedTime();
						//cards.addAll(GamingEtc.getCards());
						
						ScraperUtil.log("Sorting Cards");
		
						SharedResources.driver.close();
					} catch (IOException e) {
						e.printStackTrace(SharedResources.logger);
					} catch (Exception e) {
						e.printStackTrace(SharedResources.logger);
					}
					
					Collections.sort(SharedResources.cards, new CardPriceComparator());
					Collections.sort(SharedResources.cards, new CardFoilComparator());
					Collections.sort(SharedResources.cards, new CardNameComparator());
					Collections.sort(SharedResources.cards, new CardSetComparator());
					
					
					try {
						path = "priceLists\\";
						fileName = "priceList-Corey-"+dateFormat.format(date)+".html";
						PrintWriter writer = new PrintWriter(path+fileName,"UTF-8");
						ScraperUtil.log("Writing Cards to File");
						writer.println("<html>\r\n<head>\r\n<title>\r\nMTG List - Buylist Report\r\n</title>\r\n</head>\r\n<body>\r\n<table>");
						int count = 1;
						for(Card card : SharedResources.cards) {
							writer.println(card.toStringForFile(count));
							count++;
							writer.flush();
						}
						
						writer.println("</table>\r\n</body>\r\n</html>");
						
						writer.close();
						eMail = "CoreyAFraser@gmail.com";
						ScraperUtil.log("Sending List to " + eMail);
						Gmail.send(eMail, fileName, message);
						ScraperUtil.log("Done sending E-mail");
						ScraperUtil.calculateElapsedTime();
					} catch (FileNotFoundException | UnsupportedEncodingException e) {
						e.printStackTrace(SharedResources.logger);
					}
						
					try {
						path = "priceLists\\";
						fileName = "priceList-Pat-"+dateFormat.format(date)+".html";
						PrintWriter writer = new PrintWriter(path+fileName,"UTF-8");
						ScraperUtil.log("Writing Cards to File");
						writer.println("<html>\r\n<head>\r\n<title>\r\nMTG List - Buylist Report\r\n</title>\r\n</head>\r\n<body>\r\n<table>");
						int count = 1;
						for(Card card : SharedResources.cards) {
							writer.println(card.toStringForFile(count));
							count++;
							writer.flush();
						}
						
						writer.println("</table>\r\n</body>\r\n</html>");
						
						writer.close();
						
						eMail = "patbrodericksnc@yahoo.com";
						ScraperUtil.log("Sending List to " + eMail);
						Gmail.send(eMail, fileName, message);
						ScraperUtil.log("Done sending E-mail");
						ScraperUtil.calculateElapsedTime();
					} catch (FileNotFoundException | UnsupportedEncodingException e) {
						e.printStackTrace(SharedResources.logger);
					}
					ScraperUtil.calculateTotalElapsedTime();
					
					SharedResources.logger.close();
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace(SharedResources.logger);
				} catch (Exception e) {
					e.printStackTrace(SharedResources.logger);
				}
				SharedResources.logger.close();
		    } else {
		    	try {
					Thread.sleep(300000);
				} catch (InterruptedException e1) {
					
				}
		    }
		}
	}
}
