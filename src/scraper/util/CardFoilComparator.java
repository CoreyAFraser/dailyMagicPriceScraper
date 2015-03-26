package scraper.util;
import java.util.Comparator;

import scraper.main.Card;

public class CardFoilComparator implements Comparator<Card>
{
	public int compare(Card one,Card two)
	{
		try {
			if(one.equals(two)) {
				return 0;
			} else {
				
				if(one.getFoil().equals(two.getFoil())) {
					return 0;
				}
				
				if(one.getFoil().equals("")) {
					return -1;
				} 
				
				if(two.getFoil().equals("")) {
					return 1;
				}
				
				return 0;
			}
		} catch (Exception e) {
			ScraperUtil.log("Comparator Error");
			ScraperUtil.log(one.toString());
			ScraperUtil.log(two.toString());
			return 0;
		}
	}

}