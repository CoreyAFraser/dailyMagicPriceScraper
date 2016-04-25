package scraper.site.IsleOfCards;

import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.io.IOException;

public class IsleOfCards {

    public static void getCards() throws IOException {
        ScraperUtil.log("Starting IsleOfCards");

        //https://www.isleofcards.com/card_categories.json
        //Pull all set codes from above json file

        //https://www.isleofcards.com/products/typeahead_editor.json?data[search]=&data[has_buy_capacity]=1&data[set_code]=SOI&data[rarity]=&data[sort]=name-asc
        //make this query for each set code found in card_categories

        String page = getPage("https://www.isleofcards.com/buylist");
        getCardsFromPage(page);

        ScraperUtil.log("IsleOfCards ending");
    }

    private static void getCardsFromPage(String page) {
        int index = 0,
                beginIndex,
                endIndex;
        String foil = "";
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
            card.setFoil(foil);

            if (addAble) {
                if (card.getName().length() > SharedResources.nameLength)
                    SharedResources.nameLength = card.getName().length();
                if (card.getSet().length() > SharedResources.setLength)
                    SharedResources.setLength = card.getSet().length();

                SharedResources.cards.add(card);
            }

            index = endIndex;
        }
    }

    private static String getPage(String urlStr)  {
        SharedResources.driver.navigate().to(urlStr);

        String pageSource = SharedResources.driver.getPageSource();

        return pageSource;
    }

}
