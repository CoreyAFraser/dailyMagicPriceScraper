package scraper.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.openqa.selenium.firefox.FirefoxDriver;
import scraper.site.StrikeZone;
import scraper.site.TrollAndToad;
import scraper.site.selenium.*;
import scraper.util.CardFoilComparator;
import scraper.util.CardNameComparator;
import scraper.util.CardPriceComparator;
import scraper.util.CardSetComparator;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

public class Scrape extends TimerTask {

	DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	Date date = new Date();
	String eol = "<br>";// System.getProperty("line.separator");
	String message = "Combined Price List" + eol;
	ArrayList<Card> cards = new ArrayList<Card>();

	public void run() {

		try {
			SharedResources.logger = new PrintWriter("logs/"
					+ dateTimeFormat.format(date) + "_log.txt", "UTF-8");

			ScraperUtil.log("Started Successfully");

			SharedResources.incrBegin = System.currentTimeMillis();
			SharedResources.begin = System.currentTimeMillis();
			SharedResources.cards = new CopyOnWriteArrayList<Card>();
			try {
				try {
					ScraperUtil.log("StrikeZone Starting");
					StrikeZone.getCards(); ScraperUtil.log("StrikeZone Done");
				} catch (java.io.FileNotFoundException e) { 
					message = message + "\r\nStrikeZone appears to be down";
					ScraperUtil.log("StrikeZone Error");
					e.printStackTrace(SharedResources.logger); 
				}
				ScraperUtil.calculateElapsedTime();
		 
				try {
					ScraperUtil.log("Troll and Toad Starting");
					TrollAndToad.getCards();
					ScraperUtil.log("Troll and Toad Done");
				} catch(java.io.FileNotFoundException e) { 
					message = message + eol + "Troll and Toad appears to be down";
					ScraperUtil.log("Troll and Toad Error");
					e.printStackTrace(SharedResources.logger);
				}
				ScraperUtil.calculateElapsedTime();

				ScraperUtil.log("Starting Browser"); 
				SharedResources.driver = new FirefoxDriver();
				ScraperUtil.log("Browser Open");
				ScraperUtil.calculateElapsedTime();

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
					ScraperUtil.log("BGs Starting");
					BGs.getCards();
					ScraperUtil.log("Bgs Done");
				} catch (java.io.FileNotFoundException e) {
					message = message + eol + "BGs appears to be down";
					ScraperUtil.log("BGs Error");
					e.printStackTrace(SharedResources.logger);
				}
				ScraperUtil.calculateElapsedTime();*/

				
				try {
					ScraperUtil.log("ABU Starting");
					ABUGames.getCards();
					ScraperUtil.log("ABU Done");
				} catch (java.io.FileNotFoundException e) {
					message = message + eol +  "ABU appears to be down";
					ScraperUtil.log("ABU Error");
					e.printStackTrace(SharedResources.logger);
				}
				ScraperUtil.calculateElapsedTime();
				  
				  
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
				
				/* cards.addAll(GamingEtc.getCards()); /* try {
				 * ScraperUtil.log("TJ Games Starting"); TJ.getCards();
				 * ScraperUtil.log("TJ Games Done"); } catch
				 * (java.io.FileNotFoundException e) { message = message + eol +
				 * "TJ Games appears to be down";
				 * ScraperUtil.log("TJ Games Error");
				 * e.printStackTrace(SharedResources.logger); }
				 * ScraperUtil.calculateElapsedTime();
				 */

				try {
					ScraperUtil.log("CardKingdom Starting");
					CardKingdom.getCards();
					ScraperUtil.log("CardKingdom Done");
				} catch (java.io.FileNotFoundException e) {
					message = message + eol + "CardKingdom appears to be down";
					ScraperUtil.log("CardKingdom Error");
					e.printStackTrace(SharedResources.logger);
				}

				ScraperUtil.log("Sorting Cards");


			} catch (IOException e) {
				e.printStackTrace(SharedResources.logger);
			} catch (Exception e) {
				ScraperUtil.log(e.getStackTrace());
				e.printStackTrace(SharedResources.logger);
			}
			SharedResources.driver.quit();

			Object cardsArray[] = SharedResources.cards.toArray();

			for (Object card : cardsArray) {
				cards.add((Card) card);
			}
			Collections.sort(cards, new CardPriceComparator());
			Collections.sort(cards, new CardFoilComparator());
			Collections.sort(cards, new CardNameComparator());
			Collections.sort(cards, new CardSetComparator());

			sendEmail("Corey", "CoreyAFraser@gmail.com");
			//sendEmail("Pat","patbrodericksnc@yahoo.com");
			ScraperUtil.calculateTotalElapsedTime();

			SharedResources.logger.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace(SharedResources.logger);
		} catch (Exception e) {
			e.printStackTrace(SharedResources.logger);
		}
		SharedResources.logger.close();
	}

	public void zipFile(String path, String file) {
		byte[] buffer = new byte[1024];
		try {
			FileOutputStream fos = new FileOutputStream(path + file + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			ZipEntry ze = new ZipEntry(file + ".html");
			zos.putNextEntry(ze);
			FileInputStream in = new FileInputStream(path + file + ".html");

			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			in.close();
			zos.closeEntry();

			zos.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void sendEmail(String name, String eMail) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path;
		String fileName;

		try {
			path = "priceLists" + File.separator;
			fileName = name + "-pricelist-" + dateFormat.format(date);
			PrintWriter writer = new PrintWriter(path + fileName + ".html",
					"UTF-8");
			ScraperUtil.log("Writing Cards to File");
			writer.println("<html>\r\n<head>\r\n<title>\r\nMTG List - Buylist Report\r\n</title>\r\n</head>\r\n<body>\r\n<table>");
			int count = 1;
			for (Card card : cards) {
				writer.println(card.toStringForFile(count));
				count++;
				writer.flush();
			}

			writer.println("</table>\r\n</body>\r\n</html>");

			writer.close();
			ScraperUtil.log("Zipping File " + fileName + ".html to " + fileName
					+ ".zip");
			zipFile(path, fileName);
			ScraperUtil.log("Sending List to " + eMail + "  " + fileName
					+ ".zip");
			Gmail.send(eMail, path, fileName + ".zip", message);
			ScraperUtil.log("Done sending E-mail");
			ScraperUtil.calculateElapsedTime();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace(SharedResources.logger);
		}
	}

}