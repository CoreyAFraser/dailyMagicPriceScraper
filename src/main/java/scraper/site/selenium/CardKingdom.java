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
        getAllCards("http://www.cardkingdom.com/purchasing/mtg_singles/?filter%5Bsort%5D=&search=mtg_advanced&filter%5Bname%5D=&filter%5Bcategory_id%5D=0&filter%5Bfoil%5D=1&filter%5Bnonfoil%5D=1&filter%5Bprice_op%5D=&filter%5Bprice%5D=");
    }

    private static void getAllCards(String url) throws Exception {
        List<WebElement> cardRows;
        Card card;
        String setText,
                priceText,
                nameText = "";
        boolean pagesRemaining = true;
        String nextPageUrl = "";

        SharedResources.driver.navigate().to(url);
        while (pagesRemaining) {
            try {
                cardRows = SharedResources.driver.findElements(By.className("productItemWrapper"));

                for (WebElement cardRow : cardRows) {
                    card = new Card();
                    card.setSite("CK");

                    nameText = cardRow.findElement(By.cssSelector(".productDetailTitle a")).getText();

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
                    card.setQuantity(Integer.toString(quantities.size()));

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
                    url = nextPageLink.getAttribute("href").replace("catalog", "buylist");
                    if(nextPageUrl.equals(url)) {
                        pagesRemaining = false;
                    } else {
                        nextPageUrl = url;
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
















//    public static void getCards() throws IOException {
//
//        //http://www.cardkingdom.com/purchasing/mtg_singles/?filter%5Bsort%5D=&search=mtg_advanced&filter%5Bname%5D=&filter%5Bcategory_id%5D=0&filter%5Bfoil%5D=1&filter%5Bnonfoil%5D=1&filter%5Bprice_op%5D=&filter%5Bprice%5D=
//
//
//        ScraperUtil.log("Starting CardKingdom");
//        boolean scraping = true;
//        String page;
//        try {
//            page = getPage("http://www.cardkingdom.com/purchasing/mtg_singles?filter[category_id]=0&filter[name]=&filter[rarity]=&filter[foils]=no");
//            ScraperUtil.log("Starting non-foils");
//            while (scraping) {
//                String url = getCardsFromPage(page, "");
//                if (url.isEmpty()) {
//                    scraping = false;
//                } else {
//                    page = getPage(url);
//                }
//            }
//        } catch (Exception e) {
//            ScraperUtil.log(e.getStackTrace());
//            ScraperUtil.log(e.getMessage());
//            ScraperUtil.log(e.getCause());
//        }
//
//        page = getPage("http://www.cardkingdom.com/purchasing/mtg_singles?filter[category_id]=0&filter[name]=&filter[rarity]=&filter[foils]=yes");
//        ScraperUtil.log("Starting foils");
//        scraping = true;
//        while(scraping) {
//            String url = getCardsFromPage(page, "Foil");
//            if(url.isEmpty()) {
//                scraping = false;
//            } else {
//                page = getPage(url);
//            }
//        }
//    }
//
//    private static String getCardsFromPage(String page, String foil) {
//        int index = 0,
//                beginIndex,
//                endIndex;
//        Card card = new Card();
//
//        index = page.indexOf("<a0:tr bgcolor=", index + 1) + 1;
//
//        while ((index = page.indexOf("<a0:tr bgcolor=", index)) != -1) {
//            boolean addAble = true;
//            card = new Card();
//            card.setSite("CK");
//
//            beginIndex = page.indexOf("<a0:td>", index) + 1;
//            beginIndex = page.indexOf("<a0:td>", beginIndex);
//            beginIndex = page.indexOf(">", beginIndex) + 1;
//            endIndex = page.indexOf("</a0:td>", beginIndex);
//            String name = page.substring(beginIndex, endIndex).trim();
//            if (name.contains("color:red")) {
//                int startIndex = name.indexOf(">") + 1;
//                startIndex = name.indexOf(">", startIndex) + 1;
//                int stopIndex = name.indexOf("<", startIndex);
//                name = name.substring(startIndex, stopIndex);
//            }
//            card.setName(name);
//
//            beginIndex = page.indexOf("<a0:td>", endIndex);
//            beginIndex = page.indexOf(">", beginIndex) + 1;
//            endIndex = page.indexOf("</a0:td>", beginIndex);
//            String set = page.substring(beginIndex, endIndex).trim();
//            if (set.contains("color:red")) {
//                int startIndex = set.indexOf(">") + 1;
//                startIndex = set.indexOf(">", startIndex) + 1;
//                int stopIndex = set.indexOf("<", startIndex);
//                set = set.substring(startIndex, stopIndex);
//            }
//            card.setSet(set.replace("foil", "").trim());
//
//            beginIndex = page.indexOf("<a0:td", endIndex);
//            beginIndex = page.indexOf(">", beginIndex) + 1;
//            beginIndex = page.indexOf("<a0:td", beginIndex);
//            beginIndex = page.indexOf(">", beginIndex) + 1;
//            endIndex = page.indexOf("</a0:td>", beginIndex);
//            String price = page.substring(beginIndex, endIndex).trim();
//            if (price.contains("color:red")) {
//                int startIndex = price.indexOf(">") + 1;
//                startIndex = price.indexOf(">", startIndex) + 1;
//                int stopIndex = price.indexOf("<", startIndex);
//                price = price.substring(startIndex, stopIndex);
//            }
//            if (price.contains("Contact Us")) {
//                addAble = false;
//            }
//
//            index = endIndex;
//
//            card.setMintPrice(price);
//
//            beginIndex = page.indexOf("(Limit", endIndex);
//            endIndex = page.indexOf(")", beginIndex);
//            if(beginIndex != -1 && endIndex != -1) {
//            	String quantity = page.substring(beginIndex, endIndex).replace("Limit", "").replace("(", "").replace(")", "").trim();
//
//	            if (quantity.contains("color:red")) {
//	                int startIndex = quantity.indexOf(">") + 1;
//	                startIndex = quantity.indexOf(">", startIndex) + 1;
//	                int stopIndex = quantity.indexOf("<", startIndex);
//	                quantity = quantity.substring(startIndex, stopIndex);
//	            }
//	            card.setQuantity(quantity);
//                index = endIndex;
//            } else {
//            	card.setQuantity("unknown");
//            }
//
//            card.setPldPrice(" ");
//            card.setFoil(foil);
//
//            if (addAble) {
//                if (card.getName().length() > SharedResources.nameLength)
//                    SharedResources.nameLength = card.getName().length();
//                if (card.getSet().length() > SharedResources.setLength)
//                    SharedResources.setLength = card.getSet().length();
//
//                SharedResources.cards.add(card);
//                //ScraperUtil.log("Card: " + card.getName());
//            }
//            //ScraperUtil.log("BeginIndex: " + beginIndex);
//            //ScraperUtil.log("EndIndex: " + endIndex);
//            //ScraperUtil.log("Index: " + index);
//        }
//
//        return getNextUrl(page);
//    }
//
//    private static String getNextUrl(String page) {
//        int nextIndex = page.indexOf("\">next");
//        int beginIndex, endIndex;
//        if (nextIndex != -1) {
//            int previousIndex = page.indexOf("pagination");
//            int linkIndex;
//            int previousLinkIndex = previousIndex;
//            boolean searchForNext = true;
//            while (searchForNext) {
//                linkIndex = page.indexOf("<a0:a href", previousLinkIndex + 1);
//                if (linkIndex > nextIndex) {
//                    searchForNext = false;
//                } else {
//                    previousLinkIndex = linkIndex;
//                }
//            }
//            beginIndex = page.indexOf("=\"", previousLinkIndex) + 2;
//            endIndex = page.indexOf("\"", beginIndex);
//            String url = page.substring(beginIndex, endIndex);
//            url = url.replaceAll("amp;", "");
//            //ScraperUtil.log("Its a URL: " + url);
//            return url;
//        }
//
//        return "";
//    }
//
//    private static String getPage(String urlStr)  {
//        SharedResources.driver.navigate().to(urlStr);
//
//        String pageSource = SharedResources.driver.getPageSource();
//
//        return pageSource;
//    }

}
