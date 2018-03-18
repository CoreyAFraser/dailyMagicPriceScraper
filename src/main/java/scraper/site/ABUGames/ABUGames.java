package scraper.site.ABUGames;

import com.google.gson.Gson;
import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ABUGames {

    public static void getCards() throws IOException {
        ScraperUtil.log("Starting ABU Games");

        int index = 0;
        ABUCards abuCards = getCardsStartingAt(index);
        List<ABUCard> cards = abuCards.getResponse().getABUCards();
        while (cards != null && cards.size() != 0) {
            for (ABUCard abuCard : cards) {
                if (abuCard.getCondition().equalsIgnoreCase("SP") || abuCard.getCondition().equalsIgnoreCase("NM-M")) {
                    Card card = new Card(abuCard);
                    SharedResources.addCard(card);
                    ScraperUtil.log(card);
                }
            }
            index += 10000;
            abuCards = getCardsStartingAt(index);
            cards = abuCards.getResponse().getABUCards();
        }

        ScraperUtil.log("ABU Games ending");
    }

    private static ABUCards getCardsStartingAt(int startIndex) {
        try {
            String USER_AGENT = "Mozilla/5.0";
            String url = "https://data.abugames.com/solr/nodes/select?indent=on&q=*:*&fq=%2Bcategory%3A%22Magic%20the" +
                    "%20Gathering%20Singles%22%20%20-buy_price%3A0%20-buy_list_quantity%3A0%20%2Bmagic_edition%3A(%22" +
                    "10th%20Edition%22%20OR%20%224th%20Edition%22%20OR%20%225th%20Edition%22%20OR%20%226th%20Edition%" +
                    "22%20OR%20%227th%20Edition%22%20OR%20%228th%20Edition%22%20OR%20%229th%20Edition%22%20OR%20%22Ae" +
                    "ther%20Revolt%22%20OR%20%22Ajani%20vs.%20Nicol%20Bolas%22%20OR%20%22Alara%20Reborn%22%20OR%20%22" +
                    "Alliances%22%20OR%20%22Alpha%22%20OR%20%22Alternate%204th%20Edition%22%20OR%20%22Amonkhet%22%20O" +
                    "R%20%22Anthologies%22%20OR%20%22Antiquities%22%20OR%20%22Apocalypse%22%20OR%20%22Arabian%20Night" +
                    "s%22%20OR%20%22Archenemy%22%20OR%20%22Archenemy%3A%20Nicol%20Bolas%22%20OR%20%22Avacyn%20Restore" +
                    "d%22%20OR%20%22Battle%20Royale%22%20OR%20%22Battle%20for%20Zendikar%22%20OR%20%22Beatdown%22%20O" +
                    "R%20%22Beta%22%20OR%20%22Betrayers%20of%20Kamigawa%22%20OR%20%22Blessed%20vs.%20Cursed%22%20OR%2" +
                    "0%22Born%20of%20the%20Gods%22%20OR%20%22Champions%20of%20Kamigawa%22%20OR%20%22Chronicles%22%20O" +
                    "R%20%22Coldsnap%22%20OR%20%22Coldsnap%20Theme%20Decks%22%20OR%20%22Collectors%27%20Edition%22%20" +
                    "OR%20%22Collectors%27%20Edition%20-%20International%22%20OR%20%22Commander%22%20OR%20%22Commande" +
                    "r%202013%22%20OR%20%22Commander%202014%22%20OR%20%22Commander%202015%22%20OR%20%22Commander%2020" +
                    "16%22%20OR%20%22Commander%202017%22%20OR%20%22Commander%20Anthology%22%20OR%20%22Commander%27s%2" +
                    "0Arsenal%22%20OR%20%22Conflux%22%20OR%20%22Conspiracy%22%20OR%20%22Conspiracy%20Take%20the%20Cro" +
                    "wn%22%20OR%20%22Dark%20Ascension%22%20OR%20%22Darksteel%22%20OR%20%22Deckmasters%22%20OR%20%22Di" +
                    "ssension%22%20OR%20%22Divine%20vs.%20Demonic%22%20OR%20%22Dragon%27s%20Maze%22%20OR%20%22Dragons" +
                    "%20of%20Tarkir%22%20OR%20%22Duel%20Decks%20Anthology%3A%20Divine%20vs.%20Demonic%22%20OR%20%22Du" +
                    "el%20Decks%20Anthology%3A%20Elves%20vs.%20Goblins%22%20OR%20%22Duel%20Decks%20Anthology%3A%20Gar" +
                    "ruk%20vs.%20Liliana%22%20OR%20%22Duel%20Decks%20Anthology%3A%20Jace%20vs.%20Chandra%22%20OR%20%2" +
                    "2Duels%20of%20the%20Planeswalkers%22%20OR%20%22Eldritch%20Moon%22%20OR%20%22Elspeth%20vs.%20Kior" +
                    "a%22%20OR%20%22Elspeth%20vs.%20Tezzeret%22%20OR%20%22Elves%20vs.%20Goblins%22%20OR%20%22Eternal%" +
                    "20Masters%22%20OR%20%22Eventide%22%20OR%20%22Exodus%22%20OR%20%22Explorers%20of%20Ixalan%22%20OR" +
                    "%20%22Fallen%20Empires%22%20OR%20%22Fate%20Reforged%22%20OR%20%22Fifth%20Dawn%22%20OR%20%22From%" +
                    "20the%20Vault%3A%20Angels%22%20OR%20%22From%20the%20Vault%3A%20Annihilation%22%20OR%20%22From%20" +
                    "the%20Vault%3A%20Dragons%22%20OR%20%22From%20the%20Vault%3A%20Exiled%22%20OR%20%22From%20the%20V" +
                    "ault%3A%20Legends%22%20OR%20%22From%20the%20Vault%3A%20Lore%22%20OR%20%22From%20the%20Vault%3A%2" +
                    "0Realms%22%20OR%20%22From%20the%20Vault%3A%20Relics%22%20OR%20%22From%20the%20Vault%3A%20Transfo" +
                    "rm%22%20OR%20%22From%20the%20Vault%3A%20Twenty%22%20OR%20%22Future%20Sight%22%20OR%20%22Garruk%2" +
                    "0vs.%20Liliana%22%20OR%20%22Gatecrash%22%20OR%20%22Guildpact%22%20OR%20%22Heroes%20vs.%20Monster" +
                    "s%22%20OR%20%22Homelands%22%20OR%20%22Hour%20of%20Devastation%22%20OR%20%22Ice%20Age%22%20OR%20%" +
                    "22Iconic%20Masters%22%20OR%20%22Innistrad%22%20OR%20%22Introductory%204th%20Edition%22%20OR%20%2" +
                    "2Invasion%22%20OR%20%22Ixalan%22%20OR%20%22Izzet%20vs.%20Golgari%22%20OR%20%22Jace%20vs.%20Chand" +
                    "ra%22%20OR%20%22Jace%20vs.%20Vraska%22%20OR%20%22Journey%20into%20Nyx%22%20OR%20%22Judgment%22%2" +
                    "0OR%20%22Kaladesh%22%20OR%20%22Khans%20of%20Tarkir%22%20OR%20%22Knights%20vs.%20Dragons%22%20OR%" +
                    "20%22Legends%22%20OR%20%22Legions%22%20OR%20%22Lorwyn%22%20OR%20%22Magic%202010%20%2F%20M10%22%2" +
                    "0OR%20%22Magic%202011%20%2F%20M11%22%20OR%20%22Magic%202012%20%2F%20M12%22%20OR%20%22Magic%20201" +
                    "3%20%2F%20M13%22%20OR%20%22Magic%202014%20%2F%20M14%22%20OR%20%22Magic%202015%20%2F%20M15%22%20O" +
                    "R%20%22Magic%20Origins%22%20OR%20%22Masterpiece%20Series%3A%20Amonkhet%20Invocations%22%20OR%20%" +
                    "22Masterpiece%20Series%3A%20Kaladesh%20Inventions%22%20OR%20%22Masters%2025%22%20OR%20%22Mercadi" +
                    "an%20Masques%22%20OR%20%22Merfolk%20vs.%20Goblins%22%20OR%20%22Mind%20vs.%20Might%22%20OR%20%22M" +
                    "irage%22%20OR%20%22Mirrodin%22%20OR%20%22Mirrodin%20Besieged%22%20OR%20%22Modern%20Event%20Deck%" +
                    "202014%22%20OR%20%22Modern%20Masters%22%20OR%20%22Modern%20Masters%202015%22%20OR%20%22Modern%20" +
                    "Masters%202017%22%20OR%20%22Morningtide%22%20OR%20%22Nemesis%22%20OR%20%22New%20Phyrexia%22%20OR" +
                    "%20%22Nissa%20vs.%20Ob%20Nixilis%22%20OR%20%22Oath%20of%20the%20Gatewatch%22%20OR%20%22Odyssey%2" +
                    "2%20OR%20%22Onslaught%22%20OR%20%22Phyrexia%20vs.%20The%20Coalition%22%20OR%20%22Planar%20Chaos%" +
                    "22%20OR%20%22Planechase%202009%22%20OR%20%22Planechase%202012%22%20OR%20%22Planechase%20Antholog" +
                    "y%22%20OR%20%22Planeshift%22%20OR%20%22Portal%22%20OR%20%22Portal%20Second%20Age%22%20OR%20%22Po" +
                    "rtal%20Three%20Kingdoms%22%20OR%20%22Premium%20Deck%20Series%3A%20Fire%20and%20Lightning%22%20OR" +
                    "%20%22Premium%20Deck%20Series%3A%20Graveborn%22%20OR%20%22Premium%20Deck%20Series%3A%20Slivers%2" +
                    "2%20OR%20%22Promo%22%20OR%20%22Prophecy%22%20OR%20%22Ravnica%3A%20City%20of%20Guilds%22%20OR%20%" +
                    "22Renaissance%22%20OR%20%22Return%20to%20Ravnica%22%20OR%20%22Revised%22%20OR%20%22Rise%20of%20t" +
                    "he%20Eldrazi%22%20OR%20%22Rivals%20of%20Ixalan%22%20OR%20%22Saviors%20of%20Kamigawa%22%20OR%20%2" +
                    "2Scars%20of%20Mirrodin%22%20OR%20%22Scourge%22%20OR%20%22Shadowmoor%22%20OR%20%22Shadows%20over%" +
                    "20Innistrad%22%20OR%20%22Shards%20of%20Alara%22%20OR%20%22Sorin%20vs.%20Tibalt%22%20OR%20%22Spee" +
                    "d%20vs.%20Cunning%22%20OR%20%22Starter%201999%22%20OR%20%22Starter%202000%22%20OR%20%22Stronghol" +
                    "d%22%20OR%20%22Tempest%22%20OR%20%22The%20Dark%22%20OR%20%22Theros%22%20OR%20%22Time%20Spiral%22" +
                    "%20OR%20%22Time%20Spiral%20-%20Timeshifted%22%20OR%20%22Torment%22%20OR%20%22Unglued%22%20OR%20%" +
                    "22Unhinged%22%20OR%20%22Unlimited%22%20OR%20%22Unstable%22%20OR%20%22Urza%27s%20Destiny%22%20OR%" +
                    "20%22Urza%27s%20Legacy%22%20OR%20%22Urza%27s%20Saga%22%20OR%20%22Vanguard%22%20OR%20%22Venser%20" +
                    "vs.%20Koth%22%20OR%20%22Visions%22%20OR%20%22Weatherlight%22%20OR%20%22Welcome%20Deck%202016%22%" +
                    "20OR%20%22Welcome%20Deck%202017%22%20OR%20%22World%20Championship%22%20OR%20%22Worldwake%22%20OR" +
                    "%20%22Zendikar%22%20OR%20%22Zendikar%20Expeditions%22%20OR%20%22Zendikar%20vs.%20Eldrazi%22)%20%" +
                    "2Bdisplay_title%3A*&sort=magic_edition_sort%20asc,%20display_title%20asc&start=" + startIndex +
                    "&rows=" + (startIndex + 9999) + "&wt=json";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            con.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new Gson().fromJson(response.toString(), ABUCards.class);

        } catch (Exception e) {
            ScraperUtil.log(e.getStackTrace());
        }
        return null;
    }
}
