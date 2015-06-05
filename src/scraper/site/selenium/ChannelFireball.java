package scraper.site.selenium;

import java.net.MalformedURLException;
import java.util.LinkedHashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;


public class ChannelFireball {
	
	public static void getCards() throws MalformedURLException, Exception{

		LinkedHashSet<String> sets = new LinkedHashSet<String>();
		String url;
		Object[] setArray;

		sets = getAllSets();
		
		setArray = sets.toArray();
		
		for(int i=0;i<setArray.length;i++) {
			url = (String) setArray[i];
			getAllCards(url);
		}
	}
	
	private static void getAllCards(String url) throws Exception {
		Object[] pages = getAllPages(url).toArray();
		Object[] cardNames;
		Object[] prices;
		Object[] qtys;
		Card card = new Card();
		String nameText,
				set,
				nameFoil,
				qtyText;
		
		for(int i=0;i<pages.length;i++) {
			SharedResources.driver.navigate().to((String)pages[i]);
			prices = SharedResources.driver.findElements(By.className("grid-item-price")).toArray();
			cardNames = SharedResources.driver.findElements(By.cssSelector(".vbox-prod-title a")).toArray();
			qtys = SharedResources.driver.findElements(By.cssSelector(".vbox-info-container .prod-variants .qty")).toArray();
			try{
				for(int j=0;j<cardNames.length;j++) {
					
					card = new Card();
					card.setSite("CFB");
					WebElement priceElement = (WebElement)prices[j];
					String priceString = priceElement.getText();
					card.setMintPrice(priceString.replace("$","").replace(",","").trim());
					qtyText = (String)((JavascriptExecutor) SharedResources.driver).executeScript("return arguments[0].innerHTML",qtys[j]);
					card.setQuantity(qtyText.trim());
					nameText = (String)((JavascriptExecutor) SharedResources.driver).executeScript("return arguments[0].innerHTML",cardNames[j]);
					set = nameText.substring(nameText.indexOf("<small>")+7,nameText.indexOf("</small>"));
					card.setSet(set);
					nameFoil = nameText.substring(0,nameText.indexOf("<"));
					if(nameFoil.contains("Foil")) {
						nameFoil = nameFoil.replace(" - Foil", "");
						card.setName(nameFoil);
						card.setFoil("Foil");
					} else {
						card.setName(nameFoil);
						card.setFoil("");
					}
					
					if(card.getName().length() > SharedResources.nameLength)
			    		SharedResources.nameLength = card.getName().length();
			    	if(card.getSet().length() > SharedResources.setLength)
			    		SharedResources.setLength = card.getSet().length();
					
					SharedResources.cards.add(card);
					
				}
			} catch(java.lang.ArrayIndexOutOfBoundsException e) {
				ScraperUtil.log(prices.length);
				ScraperUtil.log(cardNames.length);
				ScraperUtil.log(qtys.length);
				e.printStackTrace(SharedResources.logger);
			}
		}
	}
	
	
	private static LinkedHashSet<String> getAllPages(String url) throws Exception{
		LinkedHashSet<String> pages = new LinkedHashSet<String>();
		
		String link = new String();
		
		pages.add(url);
		try {
			SharedResources.driver.navigate().to(url);
			List<RemoteWebElement> setLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('.pagination a')");
			
			for(RemoteWebElement set : setLinks) {
				link = set.getAttribute("href");
				link = link.replace("catalog", "buylist");
				pages.add(link);
			}
		} catch (org.openqa.selenium.WebDriverException e) {
			ScraperUtil.log("Page/Data Not Found: " + url);
		}
		
		return pages;
	}
	
	private static LinkedHashSet<String> getAllSets() throws MalformedURLException, Exception {
		LinkedHashSet<String> sets = new LinkedHashSet<String>();
		
		SharedResources.driver.navigate().to("http://store.channelfireball.com/magic_the_gathering_sets_landing");
		
		List<RemoteWebElement> setLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('.set')");
		
		for(RemoteWebElement set : setLinks) {
			String link = set.getAttribute("href");
			link = link.replace("catalog", "buylist");
			sets.add(link);
		}
		
		return sets;
	}

}
