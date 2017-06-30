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

        for (Object aSetArray : setArray) {
            String url = (String) aSetArray;
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
                    ScraperUtil.log(e.getStackTrace());
                }

                List<RemoteWebElement> nextPageLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return document.getElementsByClassName('next_page')");
                if (nextPageLinks != null && !nextPageLinks.isEmpty()) {
                    RemoteWebElement nextPageLink = nextPageLinks.get(0);
                    url = nextPageLink.getAttribute("href");
                    nextPageLink.click();
                } else {
                    pagesRemaining = false;
                }
            } catch (WebDriverException e) {
                ScraperUtil.log(url + " doesn't exist");
                ScraperUtil.log(e.getMessage());
                ScraperUtil.log(e.getStackTrace());
                try {
                    List<RemoteWebElement> nextPageLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return document.getElementsByClassName('next_page')");
                    if (nextPageLinks != null && !nextPageLinks.isEmpty()) {
                        RemoteWebElement nextPageLink = nextPageLinks.get(0);
                        url = nextPageLink.getAttribute("href");
                        ScraperUtil.log(url);
                        nextPageLink.click();
                    } else {
                        pagesRemaining = false;
                    }
                } catch (WebDriverException ex) {
                    ScraperUtil.log(url + " really doesn't exist");
                    ScraperUtil.log(ex.getMessage());
                    ScraperUtil.log(ex.getStackTrace());
                    pagesRemaining = false;
                }
            } catch (Exception e) {
                ScraperUtil.log(e);
                ScraperUtil.log(e.getStackTrace());
            }
        }
    }

    private static LinkedHashSet<String> getAllSets() throws Exception {
        LinkedHashSet<String> sets = new LinkedHashSet<>();
        LinkedHashSet<String> blocks = getAllBlocks();

        for (String block : blocks) {
            SharedResources.driver.navigate().to(block);
            List<RemoteWebElement> setLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return document.getElementsByClassName('name')");
            for (RemoteWebElement set : setLinks) {
                String link = set.getAttribute("href");
                sets.add(link);
            }
        }

        return sets;
    }

    private static LinkedHashSet<String> getAllBlocks() throws Exception {
        LinkedHashSet<String> blocks = new LinkedHashSet<>();

        SharedResources.driver.navigate().to("http://store.channelfireball.com/buylist/magic_singles/8");

        List<RemoteWebElement> blockLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return document.getElementsByClassName('name')");

        for (RemoteWebElement block : blockLinks) {
            String link = block.getAttribute("href");
            blocks.add(link);
        }

        return blocks;
    }
}
