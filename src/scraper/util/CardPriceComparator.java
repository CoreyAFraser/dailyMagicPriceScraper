package scraper.util;
import java.util.Comparator;

import scraper.main.Card;

public class CardPriceComparator implements Comparator<Card>
{
	public int compare(Card one,Card two)
	{ 
		try {
			if(one.equals(two)) {
				return 0;
			} else {
				
				if(one.getMintPrice().equals(""))
					return 1;
				if(two.getMintPrice().equals(""))
					return -1;
				
				if (Double.parseDouble(one.getMintPrice()) < Double.parseDouble(two.getMintPrice())) {
					return 1;
				} else {
					if (Double.parseDouble(one.getMintPrice()) > Double.parseDouble(two.getMintPrice())) {
						return -1;
					} else {
						return 0;
					}
				}
			} 
		} catch (Exception e) {
			ScraperUtil.log("Price Comparator Error \n" + one.toString() + "\n" + two.toString());
			return 0;
		}
	}

}