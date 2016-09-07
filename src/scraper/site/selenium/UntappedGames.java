package scraper.site.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebElement;
import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.net.MalformedURLException;
import java.util.LinkedHashSet;
import java.util.List;

public class UntappedGames {
	
	public static void getCards() throws MalformedURLException, Exception{

		LinkedHashSet<String> sets = new LinkedHashSet<String>();
		String url;
		Object[] setArray;

		sets = getAllSets();
		
		setArray = sets.toArray();
		
		for(int i=0;i<setArray.length;i++) {
			url = (String) setArray[i];
			getAllCards(url);
		};
	}
	
	
	private static void getAllCards(String url) throws Exception {
		Object[] pages = getAllPages(url).toArray();
		Object[] cardElements;
		String cardDetails;
		Card card = new Card();
		String	nameFoil,
				priceString;
		int beginIndex,
			endIndex;
		
		
		for(int i=0;i<pages.length;i++) {
			SharedResources.driver.navigate().to((String)pages[i]);
			cardElements = SharedResources.driver.findElements(By.className("product_row")).toArray();
			try{
				for(int j=0;j<cardElements.length;j++) {
					try {
						card = new Card();
						card.setSite("UG");
						
						cardDetails = (String)((JavascriptExecutor) SharedResources.driver).executeScript("return arguments[0].innerHTML",cardElements[j]);
						beginIndex = cardDetails.indexOf("<td")+1;
						beginIndex = cardDetails.indexOf("<td",beginIndex)+1;
						beginIndex = cardDetails.indexOf("/buylist/",beginIndex);
						beginIndex = cardDetails.indexOf(">",beginIndex) + 1;
						endIndex = cardDetails.indexOf("<",beginIndex);
						nameFoil = cardDetails.substring(beginIndex,endIndex);
						if(nameFoil.contains("Foil")) {
							nameFoil = nameFoil.replace(" - Foil", "");
							card.setName(nameFoil);
							card.setFoil("Foil");
						} else {
							card.setName(nameFoil);
							card.setFoil("");
						}
						
						beginIndex = cardDetails.indexOf("category_name");
						beginIndex = cardDetails.indexOf(">",beginIndex)+1;
						endIndex = cardDetails.indexOf("<",beginIndex);
						card.setSet(cardDetails.substring(beginIndex,endIndex).trim());
						
						beginIndex = cardDetails.indexOf("class=\"price\"");
						beginIndex = cardDetails.indexOf(">",beginIndex)+1;
						endIndex = cardDetails.indexOf("<",beginIndex);
						priceString = cardDetails.substring(beginIndex,endIndex).replace("$", "");
						priceString = priceString.replace(",","");
						priceString = priceString.trim();
						card.setMintPrice(priceString);
						card.setPldPrice("0");
						
						beginIndex = cardDetails.indexOf("qty");
						beginIndex = cardDetails.indexOf(">",beginIndex)+1;
						endIndex = cardDetails.indexOf("<",beginIndex);
						card.setQuantity(cardDetails.substring(beginIndex,endIndex).replace("x", "").trim());
						
						if(card.getName().length() > SharedResources.nameLength)
				    		SharedResources.nameLength = card.getName().length();
				    	if(card.getSet().length() > SharedResources.setLength)
				    		SharedResources.setLength = card.getSet().length();
						
						SharedResources.cards.add(card);
						//ScraperUtil.log(card.toString(SharedResources.nameLength,SharedResources.setLength));
					} catch(java.lang.NumberFormatException e) {
						ScraperUtil.log("Blank price for " + card.getName());
                        ScraperUtil.log(e.getStackTrace());
                    }
				}
			} catch(java.lang.ArrayIndexOutOfBoundsException e) {
				ScraperUtil.log(cardElements.length);
                ScraperUtil.log(e.getStackTrace());
            }
			
		}
	}
	
	
	private static LinkedHashSet<String> getAllPages(String url) throws Exception {
		LinkedHashSet<String> pages = new LinkedHashSet<String>();
		
		String link = new String();
		
		pages.add(url);
		try {
			SharedResources.driver.navigate().to(url);
			List<RemoteWebElement> setLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('.pagination a')");
			
			for(RemoteWebElement set : setLinks) {
				link = set.getAttribute("href");
				pages.add(link);
			}
		} catch (org.openqa.selenium.WebDriverException e) {
			ScraperUtil.log("Page Not Found: " + url);
		}
		
		return pages;
	}
	
	public static LinkedHashSet<String> getAllSets() throws MalformedURLException, Exception {
		LinkedHashSet<String> sets = new LinkedHashSet<String>();
		
		SharedResources.driver.navigate().to("http://www.untappedgames.com/buylist");
		
		List<RemoteWebElement> setLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('.depth_3 a')");
		
		for(RemoteWebElement set : setLinks) {
			String link = set.getAttribute("href");
			if(link.contains("magic")) {
				sets.add(link);
			}
		}
		
		return sets;
	}

}
