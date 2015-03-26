package scraper.util;
import java.util.Comparator;

import scraper.main.Card;

public class CardSetComparator implements Comparator<Card>
{
	public int compare(Card one,Card two)
	{
		try {
			if(one.equals(two)) {
				return 0;
			} else {
				return one.getSet().compareTo(two.getSet());
			}
		} catch (Exception e) {
			ScraperUtil.log("Comparator Error");
			ScraperUtil.log(one.toString());
			ScraperUtil.log(two.toString());
			return 0;
		}
	}

}