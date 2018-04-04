package scraper.site.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;
import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.util.List;

public class UntappedGames {

    private static final String BUYLIST = "http://www.untappedgames.com/advanced_search?utf8=✓&search[fuzzy_search]=&search[tags_name_eq]=&search[sell_price_gte]=&search[sell_price_lte]=&search[buy_price_gte]=&search[buy_price_lte]=&search[in_stock]=0&buylist_mode=0&buylist_mode=1&search[category_ids_with_descendants][]=&search[category_ids_with_descendants][]=8&search[category_ids_with_descendants][]=12&search[sort]=name&search[direction]=ascend&commit=Search&search[catalog_group_id_eq]=";

    public static void getCards() throws Exception {
        getAllCards();
    }

    private static void getAllCards() throws Exception {
        String url = BUYLIST;
        Object[] cardElements;
        String cardDetails;
        Card card = new Card();
        String nameFoil,
                priceString;
        int beginIndex,
                endIndex;
        boolean next = true;

        while (next) {
            try {
                SharedResources.driver.navigate().to(url);
                cardElements = SharedResources.driver.findElements(By.cssSelector(".products.detailed > .product.enable-msrp")).toArray();
                try {
                    for (int j = 0; j < cardElements.length; j++) {
                        try {
                            card = new Card();
                            card.setSite("UG");

                            cardDetails = (String) ((JavascriptExecutor) SharedResources.driver).executeScript("return arguments[0].innerHTML", cardElements[j]);
                            beginIndex = cardDetails.indexOf("itemprop=\"name\"");
                            beginIndex = cardDetails.indexOf(">", beginIndex) + 1;
                            endIndex = cardDetails.indexOf("</h4", beginIndex);
                            nameFoil = cardDetails.substring(beginIndex, endIndex);
                            if (nameFoil.contains("Foil")) {
                                nameFoil = nameFoil.replace(" - Foil", "");
                                card.setName(nameFoil);
                                card.setFoil("Foil");
                            } else {
                                card.setName(nameFoil);
                                card.setFoil("");
                            }

                            beginIndex = cardDetails.indexOf("category");
                            beginIndex = cardDetails.indexOf(">", beginIndex) + 1;
                            endIndex = cardDetails.indexOf("<", beginIndex);
                            card.setSet(cardDetails.substring(beginIndex, endIndex).trim());

                            beginIndex = cardDetails.indexOf("class=\"regular price\"");
                            beginIndex = cardDetails.indexOf(">", beginIndex) + 1;
                            endIndex = cardDetails.indexOf("<", beginIndex);
                            priceString = cardDetails.substring(beginIndex, endIndex).replace("$", "");
                            priceString = priceString.replace(",", "");
                            priceString = priceString.trim();
                            card.setMintPrice(priceString);
                            card.setPldPrice("0");

                            beginIndex = cardDetails.indexOf("qty");
                            beginIndex = cardDetails.indexOf("max", beginIndex);
                            beginIndex = cardDetails.indexOf("=", beginIndex) + 1;
                            endIndex = cardDetails.indexOf("\"", beginIndex);
                            card.setQuantity(cardDetails.substring(beginIndex, endIndex).replace("x", "").trim());

                            SharedResources.addCard(card);
                        } catch (NumberFormatException e) {
                            ScraperUtil.log("Blank price for " + card.getName());
                            ScraperUtil.log(e.getStackTrace());
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    ScraperUtil.log(cardElements.length);
                    ScraperUtil.log(e.getStackTrace());
                }

                List<RemoteWebElement> nextPageLinks = (List<RemoteWebElement>) ((JavascriptExecutor) SharedResources.driver).executeScript("return document.getElementsByClassName('next_page')");
                if (nextPageLinks != null && !nextPageLinks.isEmpty()) {
                    RemoteWebElement nextPageLink = nextPageLinks.get(0);
                    url = nextPageLink.getAttribute("href");
                    nextPageLink.click();
                } else {
                    next = false;
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
                        next = false;
                    }
                } catch (WebDriverException ex) {
                    ScraperUtil.log(url + " really doesn't exist");
                    ScraperUtil.log(ex.getMessage());
                    ScraperUtil.log(ex.getStackTrace());
                    next = false;
                }
            } catch (Exception e) {
                ScraperUtil.log(e);
                ScraperUtil.log(e.getStackTrace());
            }
        }
    }
}
