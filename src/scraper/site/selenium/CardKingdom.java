package scraper.site.selenium;

import java.io.IOException;

import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

public class CardKingdom {

    public static void getCards() throws IOException {
        ScraperUtil.log("Starting CardKingdom");
        boolean scraping = true;
        String page;
        try {
            page = getPage("http://www.cardkingdom.com/purchasing/mtg_singles?filter[category_id]=0&filter[name]=&filter[rarity]=&filter[foils]=no");
            ScraperUtil.log("Starting non-foils");
            while (scraping) {
                String url = getCardsFromPage(page, "");
                if (url.isEmpty()) {
                    scraping = false;
                } else {
                    page = getPage(url);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }

        page = getPage("http://www.cardkingdom.com/purchasing/mtg_singles?filter[category_id]=0&filter[name]=&filter[rarity]=&filter[foils]=yes");
        ScraperUtil.log("Starting foils");
        scraping = true;
        while(scraping) {
            String url = getCardsFromPage(page, "Foil");
            if(url.isEmpty()) {
                scraping = false;
            } else {
                page = getPage(url);
            }
        }
    }

    private static String getCardsFromPage(String page, String foil) {
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
            
            index = endIndex;

            card.setMintPrice(price);

            beginIndex = page.indexOf("(Limit", endIndex);
            endIndex = page.indexOf(")", beginIndex);
            if(beginIndex != -1 && endIndex != -1) {
            	String quantity = page.substring(beginIndex, endIndex).replace("Limit", "").replace("(", "").replace(")", "").trim();
            
	            if (quantity.contains("color:red")) {
	                int startIndex = quantity.indexOf(">") + 1;
	                startIndex = quantity.indexOf(">", startIndex) + 1;
	                int stopIndex = quantity.indexOf("<", startIndex);
	                quantity = quantity.substring(startIndex, stopIndex);
	            }
	            card.setQuantity(quantity);
            } else {
            	card.setQuantity("unknown");
            }

            card.setPldPrice(" ");
            card.setFoil(foil);

            if (addAble) {
                if (card.getName().length() > SharedResources.nameLength)
                    SharedResources.nameLength = card.getName().length();
                if (card.getSet().length() > SharedResources.setLength)
                    SharedResources.setLength = card.getSet().length();

                SharedResources.cards.add(card);
                //ScraperUtil.log("Card: " + card.getName());
            }

            if(endIndex != -1) {
            	index = endIndex;
            }
        }

        return getNextUrl(page);
    }

    private static String getNextUrl(String page) {
        int nextIndex = page.indexOf("\">next");
        int beginIndex, endIndex;
        if (nextIndex != -1) {
            int previousIndex = page.indexOf("pagination");
            int linkIndex;
            int previousLinkIndex = previousIndex;
            boolean searchForNext = true;
            while (searchForNext) {
                linkIndex = page.indexOf("<a0:a href", previousLinkIndex + 1);
                if (linkIndex > nextIndex) {
                    searchForNext = false;
                } else {
                    previousLinkIndex = linkIndex;
                }
            }
            beginIndex = page.indexOf("=\"", previousLinkIndex) + 2;
            endIndex = page.indexOf("\"", beginIndex);
            String url = page.substring(beginIndex, endIndex);
            url = url.replaceAll("amp;", "");
            //ScraperUtil.log("Its a URL: " + url);
            return url;
        }

        return "";
    }

    private static String getPage(String urlStr)  {
        SharedResources.driver.navigate().to(urlStr);

        String pageSource = SharedResources.driver.getPageSource();

        return pageSource;
    }

}
