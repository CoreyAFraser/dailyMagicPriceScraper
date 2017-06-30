package scraper.site.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.util.List;

public class CardKingdom {

    public static void getCards() throws Exception {
        getAllCards("http://www.cardkingdom.com/purchasing/mtg_singles?filter%5Bipp%5D=100&filter%5Bsort%5D=name&filter%5Bsearch%5D=mtg_advanced&filter%5Bname%5D=&filter%5Bcategory_id%5D=0&filter%5Bprice_op%5D=&filter%5Bprice%5D=&filter%5Bnonfoil%5D=1&filter%5Bfoil%5D=1");
    }

    private static void getAllCards(String url) throws Exception {
        List<WebElement> cardRows;
        Card card;
        String setText,
                priceText,
                nameText = "";
        boolean pagesRemaining = true;
        String currentPageUrl = "";

        SharedResources.driver.navigate().to(url);
        while (pagesRemaining) {
            try {
                cardRows = SharedResources.driver.findElements(By.className("productItemWrapper"));

                for (WebElement cardRow : cardRows) {
                    card = new Card();
                    card.setSite("CK");

                    nameText = cardRow.findElement(By.cssSelector(".productDetailTitle")).getText();

                    card.setName(nameText);
                    setText = cardRow.findElement(By.cssSelector(".productDetailSet")).getText();
                    setText = setText.split("\\(")[0];
                    card.setSet(setText);
                    try {
                        cardRow.findElement(By.cssSelector(".productDetailSet .foil"));
                        card.setFoil("Foil");
                    } catch(Exception e) {
                        card.setFoil("");
                    }

                    List<WebElement> quantities = cardRow.findElements(By.cssSelector(".qtyList li"));
                    card.setQuantity(Integer.toString(quantities.size() - 1));

                    try {
                        priceText = cardRow.findElement(By.className("sellDollarAmount")).getText() + "." + cardRow.findElement(By.className("sellCentsAmount")).getText();
                        card.setMintPrice(priceText);
                    } catch (WebDriverException e) {
                        ScraperUtil.log("CardKingdom Elite Card: " + card.getName());
                    }

                    if (card.getName().length() > SharedResources.nameLength)
                        SharedResources.nameLength = card.getName().length();
                    if (card.getSet().length() > SharedResources.setLength)
                        SharedResources.setLength = card.getSet().length();

                    SharedResources.cards.add(card);

                }

                List<WebElement> nextPageLinks = SharedResources.driver.findElements(By.cssSelector(".pagination li:last-child a"));

                if (nextPageLinks != null && !nextPageLinks.isEmpty()) {
                    WebElement nextPageLink = nextPageLinks.get(0);
                    url = nextPageLink.getAttribute("href");
                    if (currentPageUrl.equals(url)) {
                        pagesRemaining = false;
                    } else {
                        currentPageUrl = url;
                        SharedResources.driver.navigate().to(url);
                    }
                } else {
                    pagesRemaining = false;
                }
            } catch (WebDriverException e) {
                System.out.println(nameText);
                ScraperUtil.log(e);
                ScraperUtil.log(e.getStackTrace());
                ScraperUtil.log(url + " doesn't exist");
                pagesRemaining = false;
            } catch (Exception e) {
                ScraperUtil.log(e);
                ScraperUtil.log(e.getStackTrace());
            }
        }
    }
}
