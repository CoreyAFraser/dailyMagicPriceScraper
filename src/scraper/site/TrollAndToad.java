package scraper.site;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

public class TrollAndToad {
	
	public static void getCards() throws IOException{
	   
		try {
			String page = getPage();
			int index = 0,
				beginIndex,
				endIndex;
			Card card = new Card();
			String foil;
			
			while((index = page.indexOf("tdrow",index)) != -1) {
				card = new Card();
				card.setSite("TT");
				
				beginIndex = page.indexOf("<strong>",index);
				beginIndex = page.indexOf(">",beginIndex)+1;
				endIndex = page.indexOf("<",beginIndex);
				card.setSet(page.substring(beginIndex,endIndex).replace("Singles", "").replace("Foil","").trim());
				
				beginIndex = page.indexOf("<a",beginIndex);
				beginIndex = page.indexOf(">",beginIndex)+1;
				endIndex = page.indexOf("<",beginIndex);
				card.setName(page.substring(beginIndex,endIndex).trim());
				
				beginIndex = page.indexOf("<label",beginIndex);
				beginIndex = page.indexOf(">",beginIndex)+1;
				endIndex = page.indexOf("<",beginIndex);
				foil = page.substring(beginIndex,endIndex).trim();
				if(foil.contains("Foil")) {
					card.setFoil("Foil");
				} else {
					card.setFoil("");
				}
				
				beginIndex = page.indexOf("<td",beginIndex);
				beginIndex = page.indexOf(">",beginIndex)+1;
				endIndex = page.indexOf("<",beginIndex);
				card.setMintPrice(page.substring(beginIndex,endIndex).replace("$","").trim());
				card.setPldPrice(" ");
				
				beginIndex = page.indexOf("<td",beginIndex);
				beginIndex = page.indexOf(">",beginIndex)+1;
				endIndex = page.indexOf("<",beginIndex);
				card.setQuantity(page.substring(beginIndex,endIndex).trim());
				
				if(card.getName().length() > SharedResources.nameLength)
			    	SharedResources.nameLength = card.getName().length();
			    if(card.getSet().length() > SharedResources.setLength)
			    	SharedResources.setLength = card.getSet().length();
	
			    SharedResources.cards.add(card);
			    
			    index = endIndex;
			}
		} catch (java.net.UnknownHostException e) {
			ScraperUtil.log("Troll and Toad is offline");
			e.printStackTrace(SharedResources.logger);
		}
	
	    
	}
	
	private static String getPage() throws IOException {
		URLConnection urlConnection;
		URL url = new URL("http://www.trollandtoad.com/buying3.php?Department=M");
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
