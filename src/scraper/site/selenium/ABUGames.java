package scraper.site.selenium;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebElement;

import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

public class ABUGames {
	
	
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
		List<RemoteWebElement> cardsList = new ArrayList<RemoteWebElement>();
		String text;
		String setName = "!";
		Boolean pagesRemaining = true;
		Boolean foil = false;
		SharedResources.driver.navigate().to(url);
		int beginIndex,
			endIndex,
			index = 0;
		Card card = new Card();
		
		while(pagesRemaining) {
			try{
				cardsList = (ArrayList<RemoteWebElement>)  ((JavascriptExecutor) SharedResources.driver).executeScript("return document.getElementsByName('inventoryform')");
				text = (String)((JavascriptExecutor) SharedResources.driver).executeScript("return arguments[0].innerHTML",cardsList.get(0));
				beginIndex = text.indexOf("<table");
				if(setName.equals("!")) {
					beginIndex = text.indexOf("<td colspan=\"3\">",beginIndex);
					beginIndex = text.indexOf("<b ",beginIndex);
					beginIndex = text.indexOf(">",beginIndex)+1;
					endIndex = text.indexOf("<",beginIndex);
					setName = text.substring(beginIndex,endIndex);
					if(setName.contains("Foil")) {
						setName = setName.replace("Foil", "").trim();
						foil = true;
					}
					index = endIndex;
				}
				
				while((index = text.indexOf("<td class=\"small\">",index)) != -1) {
					card = new Card();
					card.setSet(setName);
					card.setSite("ABU");
					
					if(foil) {
						card.setFoil("Foil");
					} else {
						card.setFoil("");
					}	
					
					beginIndex = text.indexOf("cardlink",index);
					beginIndex = text.indexOf(">",beginIndex)+1;
					endIndex = text.indexOf("<",beginIndex);
					card.setName(text.substring(beginIndex,endIndex));
					
					beginIndex = text.indexOf("small",endIndex);
					beginIndex = text.indexOf(">",beginIndex)+1;
					endIndex = text.indexOf("<",beginIndex);
					card.setQuantity(text.substring(beginIndex,endIndex).trim());
					
					beginIndex = text.indexOf("small",endIndex);
					beginIndex = text.indexOf(">",beginIndex)+1;
					endIndex = text.indexOf("<",beginIndex);
					card.setMintPrice(text.substring(beginIndex,endIndex).replace("$","").trim());
					
					beginIndex = text.indexOf("small",endIndex);
					beginIndex = text.indexOf(">",beginIndex)+1;
					endIndex = text.indexOf("<",beginIndex);
					card.setPldPrice(text.substring(beginIndex,endIndex).replace("$","").trim());
					
					if(card.getName().length() > SharedResources.nameLength)
			    		SharedResources.nameLength = card.getName().length();
			    	if(card.getSet().length() > SharedResources.setLength)
			    		SharedResources.setLength = card.getSet().length();
					
					index = endIndex;
					SharedResources.cards.add(card);
				}
				
				List<RemoteWebElement> allLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return document.getElementsByClassName('cardlink')");
				for(RemoteWebElement link : allLinks) {
					if(link.getText().equals("Next >")) {
						pagesRemaining = true;
						link.click();
						break;
					} else {
						pagesRemaining = false;
					}
				}
			} catch (java.lang.IndexOutOfBoundsException e) {
				ScraperUtil.log("Something wrong: " + url);
				e.printStackTrace();
			}
		}
	}
	
	
	private static LinkedHashSet<String> getAllSets() throws MalformedURLException, Exception {
		LinkedHashSet<String> sets = new LinkedHashSet<String>();
		
		SharedResources.driver.navigate().to("http://www.abugames.com/buylist.html");

		List<RemoteWebElement> setLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return document.getElementsByTagName('a')");
			
		for(RemoteWebElement set : setLinks) {
			String link = set.getAttribute("href");
			if(link.contains("/buylist.cgi?command=search&edition=")) {
				sets.add(link);
			}				
		}
	     
		return sets;
	}

}