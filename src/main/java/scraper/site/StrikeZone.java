package scraper.site;

import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class StrikeZone {
	
	public static void getCards() throws IOException{
		try{
			
		    URLConnection urlConnection;
		    
			URL url = new URL("http://shop.strikezoneonline.com/List/MagicBuyList.txt");
			urlConnection = url.openConnection();
			
			BufferedReader dis  = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String tmp;
			Card card;
	
			while ((tmp = dis.readLine()) != null) {  
			    card = generateCard(tmp);
			    if(card.getName().length() > SharedResources.nameLength)
			    	SharedResources.nameLength = card.getName().length();
			    if(card.getSet().length() > SharedResources.setLength)
			    	SharedResources.setLength = card.getSet().length();
			    if(card.getMintPrice() != 0.0)
			    	SharedResources.cards.add(card);
			}  
			dis.close(); 
			
		} catch (java.net.UnknownHostException e) {
			ScraperUtil.log("Strike Zone is offline");
			ScraperUtil.log(e.getStackTrace());
		}

	}
	
	private static Card generateCard(String card) {
		Card result = new Card();
		String[] attributes = card.split("\\t");
		if(attributes.length > 0) {
			result.setSite("SZ");
			result.setSet(attributes[0].trim());
			result.setName(attributes[1].trim());
			if(attributes.length == 4) {
				result.setFoil("");
				result.setQuantity(attributes[2].trim());
				result.setMintPrice(attributes[3].trim());
			} else {
				result.setFoil(attributes[2].trim());
				result.setQuantity(attributes[3].trim());
				result.setMintPrice(attributes[4].replace(",","").trim());
			}
		}
		return result;
	}

}
