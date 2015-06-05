package scraper.site.selenium;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebElement;

import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

public class BGs {
	
	
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
		String cardName = "";
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
				cardsList = (ArrayList<RemoteWebElement>)  ((JavascriptExecutor) SharedResources.driver).executeScript("return document.querySelectorAll('.product_row')");
				
				for(RemoteWebElement cardElement : cardsList) {
					card = new Card();
					card.setSite("BGS");
					
					text = (String)((JavascriptExecutor) SharedResources.driver).executeScript("return arguments[0].innerHTML",cardElement);
					beginIndex = text.indexOf("product_name");
					beginIndex = text.indexOf(">",beginIndex)+1;
					endIndex = text.indexOf("<",beginIndex);
					cardName = text.substring(beginIndex,endIndex);
					if(cardName.contains(" - Foil")) {
						cardName = cardName.replaceAll(" - Foil", "");
						card.setFoil("Foil");
					}
					card.setName(cardName);
					
					if(setName.equals("!")) {
						beginIndex = text.indexOf("Set Name");
						beginIndex = text.indexOf("descriptor_value",beginIndex);
						beginIndex = text.indexOf(">",beginIndex)+1;
						endIndex = text.indexOf("<",beginIndex);
						setName = text.substring(beginIndex,endIndex);
					}
					card.setSet(setName);
					
					beginIndex = text.indexOf("price");
					beginIndex = text.indexOf(">",beginIndex)+1;
					endIndex = text.indexOf("<",beginIndex);
					card.setMintPrice(text.substring(beginIndex,endIndex).replace("$","").trim());
					
					beginIndex = text.indexOf("qty");
					beginIndex = text.indexOf(">",beginIndex)+1;
					endIndex = text.indexOf("<",beginIndex);
					card.setQuantity(text.substring(beginIndex,endIndex).replace("x","").trim());
					
					if(card.getName().length() > SharedResources.nameLength)
			    		SharedResources.nameLength = card.getName().length();
			    	if(card.getSet().length() > SharedResources.setLength)
			    		SharedResources.setLength = card.getSet().length();
			    	
			    	SharedResources.cards.add(card);
				}
				
			} catch (java.lang.IndexOutOfBoundsException e) {
				ScraperUtil.log("Something wrong: " + url);
				e.printStackTrace();
			}

			List<RemoteWebElement> allLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return document.querySelectorAll('.pagination a')");
			pagesRemaining = false;
			for(RemoteWebElement link : allLinks) {
				if(link.getText().contains("Next")) {
					pagesRemaining = true;
					link.click();
					break;
				} else {
					pagesRemaining = false;
				}
			}
		}
	}
	
	
	private static LinkedHashSet<String> getAllSets() throws MalformedURLException, Exception {
		LinkedHashSet<String> sets = new LinkedHashSet<String>();
		
		/*SharedResources.driver.navigate().to("http://store.battlegroundgames.com/buylist");

		List<RemoteWebElement> setLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return document.querySelectorAll('.depth_3 a')");
			
		for(RemoteWebElement set : setLinks) {
			String link = set.getAttribute("href");
				sets.add(link);				
		}*/
		
		URLConnection urlConnection;

		URL url = new URL("http://store.battlegroundgames.com/buylist");
		urlConnection = url.openConnection();

		BufferedReader dis = new BufferedReader(new InputStreamReader(
				urlConnection.getInputStream()));

		String tmp = "";
		Card card = new Card();
		int beginIndex = 0;
		int endIndex = 0;
		String href = "";
		while ((tmp = dis.readLine()) != null) {
			if ((beginIndex = tmp.indexOf("<a href=")) != -1) {
				beginIndex = tmp.indexOf("\"",beginIndex)+1;
				endIndex = tmp.indexOf("\"",beginIndex);
				href = tmp.substring(beginIndex,endIndex).trim();
				if(href.length() > 1 && href.contains("/buylist/magic_singles-")) {
					sets.add(href);
					System.out.println(href);
				}
			}
		}
		dis.close();
		System.out.println(sets.size());
		return sets;
	}

}
