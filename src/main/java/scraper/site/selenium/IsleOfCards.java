package scraper.site.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebElement;
import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.io.IOException;
import java.util.List;

public class IsleOfCards {

    public static void getCards() throws IOException {
        ScraperUtil.log("Starting IsleOfCards");

        SharedResources.driver.navigate().to("https://www.isleofcards.com/buylist");

        List<RemoteWebElement> setLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('li[data-reactid] a[data-reactid*=\"sets\"]')");
        List<RemoteWebElement> allSetsLink = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('button[data-reactid*=\"sets\"]')");
        List<RemoteWebElement> allSortingLink = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('button[data-reactid*=\"sorting\"]')");

        List<RemoteWebElement> sortLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return jQuery.find('li[data-reactid] a[data-reactid*=\"name-asc\"]')");
        allSortingLink.get(0).click();
        sortLinks.get(0).click();

        for(RemoteWebElement link : setLinks) {
            allSetsLink.get(0).click();
            link.click();

        }

        //https://www.isleofcards.com/card_categories.json
        //Get all sets from this to translate set codes to set Names

        ScraperUtil.log("IsleOfCards ending");
    }



    private static void getCardsFromCurrentPage() throws Exception {

        String page = SharedResources.driver.getPageSource();

        int index = 0,
                beginIndex,
                endIndex;
        Card card = new Card();

        index = page.indexOf("<a0:tr bgcolor=", index + 1) + 1;

        while ((index = page.indexOf("<a0:tr bgcolor=", index)) != -1) {
            boolean addAble = true;
            card = new Card();
            card.setSite("CK");

            beginIndex = page.indexOf("<a0:td>", index) + 1;
            beginIndex = page.indexOf("<a0:td>", beginIndex);
            beginIndex = page.indexOf(">", beginIndex) + 1;
            endIndex = page.indexOf("</a0:td>", beginIndex);
            String name = page.substring(beginIndex, endIndex).trim();
            if (name.contains("color:red")) {
                int startIndex = name.indexOf(">") + 1;
                startIndex = name.indexOf(">", startIndex) + 1;
                int stopIndex = name.indexOf("<", startIndex);
                name = name.substring(startIndex, stopIndex);
            }
            card.setName(name);

            beginIndex = page.indexOf("<a0:td>", endIndex);
            beginIndex = page.indexOf(">", beginIndex) + 1;
            endIndex = page.indexOf("</a0:td>", beginIndex);
            String set = page.substring(beginIndex, endIndex).trim();
            if (set.contains("color:red")) {
                int startIndex = set.indexOf(">") + 1;
                startIndex = set.indexOf(">", startIndex) + 1;
                int stopIndex = set.indexOf("<", startIndex);
                set = set.substring(startIndex, stopIndex);
            }
            card.setSet(set.replace("foil", "").trim());

            beginIndex = page.indexOf("<a0:td", endIndex);
            beginIndex = page.indexOf(">", beginIndex) + 1;
            beginIndex = page.indexOf("<a0:td", beginIndex);
            beginIndex = page.indexOf(">", beginIndex) + 1;
            endIndex = page.indexOf("</a0:td>", beginIndex);
            String price = page.substring(beginIndex, endIndex).trim();
            if (price.contains("color:red")) {
                int startIndex = price.indexOf(">") + 1;
                startIndex = price.indexOf(">", startIndex) + 1;
                int stopIndex = price.indexOf("<", startIndex);
                price = price.substring(startIndex, stopIndex);
            }
            if (price.contains("Contact Us")) {
                addAble = false;
            }

            card.setMintPrice(price);

            beginIndex = page.indexOf("(Limit", endIndex);
            endIndex = page.indexOf(")", beginIndex);
            String quantity = page.substring(beginIndex, endIndex).replace("Limit", "").replace("(", "").replace(")", "").trim();
            if (quantity.contains("color:red")) {
                int startIndex = quantity.indexOf(">") + 1;
                startIndex = quantity.indexOf(">", startIndex) + 1;
                int stopIndex = quantity.indexOf("<", startIndex);
                quantity = quantity.substring(startIndex, stopIndex);
            }
            card.setQuantity(quantity);

            card.setPldPrice(" ");
            //card.setFoil(foil);

            if (addAble) {
                SharedResources.addCard(card);
                //ScraperUtil.log("Card: " + card.getName());
            }

            index = endIndex;
        }
    }
}
