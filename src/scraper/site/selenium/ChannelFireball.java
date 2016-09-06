package scraper.site.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.util.LinkedHashSet;
import java.util.List;

public class ChannelFireball {

    public static void getCards() throws Exception {
        Object[] setArray = getAllSets().toArray();

        for(int i=0;i<setArray.length;i++) {
            String url = (String) setArray[i];
            getAllCards(url);
		}
	}
	
	private static void getAllCards(String url) throws Exception {
		Object[] cardNames;
		Object[] prices;
		Object[] qtys;
        Card card;
        String nameText,
				set,
				nameFoil,
				qtyText;
        boolean pagesRemaining = true;

        SharedResources.driver.navigate().to(url);
        while (pagesRemaining) {
            try {
                prices = SharedResources.driver.findElements(By.className("grid-item-price")).toArray();
                cardNames = SharedResources.driver.findElements(By.cssSelector(".vbox-prod-title a")).toArray();
                qtys = SharedResources.driver.findElements(By.cssSelector(".vbox-info-container .prod-variants .qty")).toArray();
                try {
                    for (int j = 0; j < cardNames.length; j++) {

                        card = new Card();
                        card.setSite("CFB");
                        WebElement priceElement = (WebElement) prices[j];
                        String priceString = priceElement.getText();
                        card.setMintPrice(priceString.replace("$", "").replace(",", "").trim());
                        qtyText = (String) ((JavascriptExecutor) SharedResources.driver).executeScript("return arguments[0].innerHTML", qtys[j]);
                        card.setQuantity(qtyText.trim());
                        nameText = (String) ((JavascriptExecutor) SharedResources.driver).executeScript("return arguments[0].innerHTML", cardNames[j]);
                        set = nameText.substring(nameText.indexOf("<small>") + 7, nameText.indexOf("</small>"));
                        card.setSet(set);
                        nameFoil = nameText.substring(0, nameText.indexOf("<"));
                        if (nameFoil.contains("Foil")) {
                            nameFoil = nameFoil.replace(" - Foil", "");
                            card.setName(nameFoil);
                            card.setFoil("Foil");
                        } else {
                            card.setName(nameFoil);
                            card.setFoil("");
                        }

                        if (card.getName().length() > SharedResources.nameLength)
                            SharedResources.nameLength = card.getName().length();
                        if (card.getSet().length() > SharedResources.setLength)
                            SharedResources.setLength = card.getSet().length();

                        SharedResources.cards.add(card);

                    }
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    ScraperUtil.log("Page: " + url);
                    ScraperUtil.log("  Prices: " + prices.length);
                    ScraperUtil.log("  Names: " + cardNames.length);
                    ScraperUtil.log("  Quantities: " + qtys.length);
                    e.printStackTrace(SharedResources.logger);
                }

                List<RemoteWebElement> nextPageLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('.next_page')");
                if (nextPageLinks != null && !nextPageLinks.isEmpty()) {
                    RemoteWebElement nextPageLink = nextPageLinks.get(0);
                    url = nextPageLink.getAttribute("href").replace("catalog", "buylist");
                    nextPageLink.click();
                } else {
                    pagesRemaining = false;
                }
            } catch (WebDriverException e) {
                ScraperUtil.log(url + " doesn't exist");
                pagesRemaining = false;
            } catch (Exception e) {
                ScraperUtil.log(e);
                for (StackTraceElement element : e.getStackTrace()) {
                    ScraperUtil.log("     " + element);
                }
            }
        }
	}

    private static LinkedHashSet<String> getAllSets() throws Exception {
        LinkedHashSet<String> sets = new LinkedHashSet<>();

        SharedResources.driver.navigate().to("http://store.channelfireball.com/magic_the_gathering_sets_landing");
		
		List<RemoteWebElement> setLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('.set')");
		
		for(RemoteWebElement set : setLinks) {
            String link = set.getAttribute("href").replace("catalog", "buylist");
            sets.add(link);
		}
		
		return sets;
	}

}
