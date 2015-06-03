package scraper.util;
import java.util.Comparator;

import scraper.main.Card;

public class CardNameComparator implements Comparator<Card>
{
	public int compare(Card one,Card two)
	{
		try {
			if(one.equals(two)) {
				return 0;
			} else {
				return one.getName().compareTo(two.getName());
			}
		} catch (Exception e) {
			ScraperUtil.log("Name Comparator Error");
			ScraperUtil.log(one.toString());
			ScraperUtil.log(two.toString());
			return 0;
		}
	}

}