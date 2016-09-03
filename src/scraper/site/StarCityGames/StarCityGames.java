package scraper.site.StarCityGames;

import com.google.gson.Gson;
import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;

public class StarCityGames {

    public static void getCards() throws IOException {
        ScraperUtil.log("Starting SCG");

        //http://sales.starcitygames.com/spoiler/spoiler.php
        //scrape set names and id from here

        //http://www.starcitygames.com/buylist/search?search-type=category&id=5061
        //pull json for each set here

        try {
            //LinkedHashSet<String> sets = getAllSets();

            for(int i=1000;i<2000;i++) {
                processSetId(i);
            }

            for(int i=5000;i<6000;i++) {
                processSetId(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ScraperUtil.log("SCG ending");
    }

    private static void processSetId(int setId) {
        SCGSearchDTO scgSearchDTO = getCardsForSet(setId);
        //ScraperUtil.log(scgSearchDTO.getResults().size() + " cards for set Id: " + setId);
        if(scgSearchDTO.getResults().size() > 0) {
            for (List<SCGCard> scgCards : scgSearchDTO.getResults()) {
                for (SCGCard scgCard : scgCards) {
                    if (scgCard.isAddable()) {
                        Card card = scgCard.toCardWithSet(scgSearchDTO.getSetName());
                        if (card.getName().length() > SharedResources.nameLength)
                            SharedResources.nameLength = card.getName().length();
                        if (card.getSet().length() > SharedResources.setLength)
                            SharedResources.setLength = card.getSet().length();
                        SharedResources.cards.add(card);
                    }
                }
            }
        }
    }

    private static SCGSearchDTO getCardsForSet(int setId) {
        try {
            String USER_AGENT = "Mozilla/5.0";
            String url = "http://www.starcitygames.com/buylist/search?search-type=category&id=" + setId;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            con.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new Gson().fromJson(response.toString(), SCGSearchDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static LinkedHashSet<String> getAllSets() throws Exception {
        LinkedHashSet<String> sets = new LinkedHashSet<>();

        SharedResources.driver.navigate().to("http://sales.starcitygames.com/spoiler/spoiler.php");

        String page = SharedResources.driver.getPageSource();

        int index = 0,
                beginIndex,
                endIndex;

        index = page.indexOf("Set(s):", index + 1) + 1;
        index = page.indexOf("<input", index + 1) + 1;

        while ((index = page.indexOf("childbox magic", index)) != -1) {
            beginIndex = page.indexOf("value", index);
            beginIndex = page.indexOf("=", beginIndex) + 2;
            endIndex = page.indexOf("\"", beginIndex);
            //System.out.println(page.substring(beginIndex, endIndex));
            sets.add(page.substring(beginIndex, endIndex));
            index = endIndex;
        }

        return sets;
    }
}
