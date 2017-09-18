package scraper.site.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebElement;
import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashSet;
import java.util.List;

public class TJ {


    public static void getCards() throws Exception {
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
		Object[] cards;
		Card card = new Card();
		String nameFoil,
				set,
				nameText,
				qtyText,
				cardText,
				foil;
		
		int beginIndex = 0,
			endIndex;
		
		for(int i=0;i<pages.length;i++) {
			SharedResources.driver.navigate().to((String)pages[i]);   
			cards = SharedResources.driver.findElements(By.className("product_grid")).toArray();

            set = SharedResources.driver.findElements(By.cssSelector(".pagetitle")).get(0).getText();
            try{
				for(int j=0;j<cards.length;j++) {
					
					beginIndex = 0;
					endIndex = 0;
					nameFoil = "";
					nameText = "";
					qtyText = "";
					cardText = "";
					foil = "";
					
					card = new Card();
					
					cardText = (String)((JavascriptExecutor) SharedResources.driver).executeScript("return arguments[0].innerHTML",cards[j]);
					beginIndex = cardText.indexOf("name")+1;
					beginIndex = cardText.indexOf("<a href=",beginIndex)+1;
					beginIndex = cardText.indexOf(">",beginIndex)+1;
					endIndex = cardText.indexOf("<",beginIndex);
					
					nameFoil = cardText.substring(beginIndex,endIndex);
					
					if(nameFoil.contains("Foil")) {
						nameFoil = nameFoil.replace(" - Foil", "");
						nameText = nameFoil;
						foil = "Foil";
					} else {
						nameText = nameFoil;
						foil = "";
					}
					
					beginIndex = cardText.indexOf("price_and_qty",beginIndex)+1;
					if(beginIndex != 0) {
					
						beginIndex = cardText.indexOf("price",beginIndex)+1;
						beginIndex = cardText.indexOf(">",beginIndex)+1;
						endIndex = cardText.indexOf("<",beginIndex);
						card.setMintPrice(cardText.substring(beginIndex,endIndex).replace("$","").trim());
						
						beginIndex = cardText.indexOf("qty",beginIndex);
						beginIndex = cardText.indexOf(">",beginIndex)+1;
						endIndex = cardText.indexOf("<",beginIndex);
						card.setQuantity(cardText.substring(beginIndex,endIndex).replace("x","").trim());
						
						card.setSite("TJ");
						card.setName(nameText.trim());
						card.setFoil(foil);
						card.setSet(set.trim());

                        SharedResources.addCard(card);
                    }
				}
			} catch(java.lang.ArrayIndexOutOfBoundsException e) {
				ScraperUtil.log(cards.length);
                ScraperUtil.log(e.getStackTrace());
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
				pages.add(link);
			}
		} catch (org.openqa.selenium.WebDriverException e) {
			ScraperUtil.log("Page/Data Not Found: " + url);
		}
		
		return pages;
	}


    private static LinkedHashSet<String> getAllSets() throws Exception {
        LinkedHashSet<String> sets = new LinkedHashSet<String>();
		
		String page = getPage();
			
		int index = 0;
		int beginIndex = 0;
		int endIndex = 0;
		
		while((index = page.indexOf("categoryProducts",index)) != -1) {
			beginIndex = page.indexOf("<a",index)+1;
			beginIndex = page.indexOf("\"",beginIndex)+1;
			endIndex = page.indexOf("\"",beginIndex);
			sets.add("http://www.magicsingles.com" + page.substring(beginIndex,endIndex));
			index = endIndex;
		}
	     
		return sets;
	}
	
	private static String getPage() throws IOException {
		
		SharedResources.driver.navigate().to("http://www.magicsingles.com/buylist");
		
		URLConnection urlConnection;
		URL url = new URL("http://www.magicsingles.com/buylist");
		urlConnection = url.openConnection();
		
		String page = "";
			
		BufferedReader dis  = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				
		String tmp = "";  

		while ((tmp = dis.readLine()) != null) {  
			page = page + tmp.trim();
		}  
		dis.close(); 
		return page;
	}

}
