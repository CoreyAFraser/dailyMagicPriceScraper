package scraper.util.shared;

import org.openqa.selenium.WebDriver;
import scraper.main.Card;
import scraper.util.CardFoilComparator;
import scraper.util.ScraperUtil;

import java.io.PrintWriter;
import java.util.ArrayList;

public class SharedResources {
    public static WebDriver driver;
    public static Long incrBegin;
	public static Long incrEnd;
	public static PrintWriter logger;
	public static Long begin;
	public static Long end;
    static int nameLength = 0;
    static int setLength = 0;
    static ArrayList<Card> cards;
    private static CardFoilComparator cardFoilComparator = new CardFoilComparator();

    public static void addCard(Card card) {
        if (card.getName().contains("foreign") ||
                card.getSet().contains("foreign") ||
                card.getMintPrice() == 0.0 ||
                card.getSet().contains("nintendo") ||
                card.getSet().contains("collector") ||
                card.getSet().contains("ce d") ||
                card.getSet().contains("ce i") ||
                card.getSet().contains("renaissance") ||
                card.getSet().contains("intro lands") ||
                card.getSet().contains("japanese") ||
                card.getSet().contains("international") ||
                card.getSet().contains("introductory") ||
                card.getSet().contains("magic complete sets") ||
                card.getSet().contains("magic the gathering complete non sets") ||
                card.getSet().contains("magic boxes packs and decks") ||
                card.getSet().contains("magic the gathering sealed product") ||
                card.getSet().contains("magic the gathering nonenglish sets and") ||
                card.getSet().contains("nonenglish") ||
                card.getSet().contains("sealed product")) {
            return;
        }

        if (card.getName().contains("archenemy")) {
            String[] cardNameParts = card.getName().split("archenemy");
            card.setName(cardNameParts[0].trim());
            if (cardNameParts.length == 2) {
                card.setSet("archenemy " + cardNameParts[1].trim());
            } else {
                card.setSet("archenemy");
            }
        }

        if (card.getName().contains("invocations")) {
            card.setName(card.getSet().replace("hour of devastation", "").replace("amonkhet", "").replace("invocations", "").trim());
            card.setSet("amonkhet invocations");
        }

        if (card.getName().contains("inventions")) {
            card.setName(card.getSet().replace("aether revolt", "").replace("kaladesh", "").replace("inventions", "").trim());
            card.setSet("inventions");
        }

        if (card.getName().contains("invocations")) {
            card.setName(card.getSet().replace("amonkhet invocations", "").trim());
            card.setSet("amonkhet invocations");
        }

        if (card.getName().contains("invocations")) {
            card.setName(card.getSet().replace("amonkhet invocations", "").trim());
            card.setSet("amonkhet invocations");
        }

        if (card.getName().length() > nameLength)
            nameLength = card.getName().length();
        if (card.getSet().length() > setLength)
            setLength = card.getSet().length();

        cards.add(card);
    }

    public static void initializeCards() {
        cards = new ArrayList<>();
        nameLength = 0;
        setLength = 0;
    }

    public static void sortCards() {
        try {
            SharedResources.cards.sort((one, two) -> Double.compare(one.getMintPrice(), two.getMintPrice()));
            ScraperUtil.log("Cards sorted by price");
            SharedResources.cards.sort(cardFoilComparator);
            ScraperUtil.log("Cards sorted by Foil");
            SharedResources.cards.sort((one, two) -> one.getName().compareTo(two.getName()));
            ScraperUtil.log("Cards sorted by name");
            SharedResources.cards.sort((one, two) -> one.getSet().compareTo(two.getSet()));
            ScraperUtil.log("Cards sorted by set");
            ScraperUtil.calculateElapsedTime();
            ScraperUtil.log("Cards added: " + SharedResources.cards.size());
        } catch (Exception e) {
            ScraperUtil.log(e.getStackTrace());
            ScraperUtil.log(e.getCause());
        }
    }

    public static ArrayList<Card> getCards() {
        return cards;
    }

    public static int getNameLength() {
        return nameLength;
    }

    public static int getSetLength() {
        return setLength;
    }
}
