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
import java.util.List;

public class StarCityGames {

    public static void getCards() throws IOException {
        ScraperUtil.log("Starting SCG");
        try {
            for(int i=1000;i<2000;i++) {
                processSetIdForBuyList(i);
            }

            for(int i=5000;i<6000;i++) {
                processSetIdForBuyList(i);
            }
        } catch (Exception e) {
            ScraperUtil.log(e.getStackTrace());
        }

        ScraperUtil.log("SCG ending");
    }

    private static void processSetIdForBuyList(int setId) {
        SCGSearchDTO scgSearchDTO = getCardsForSet(setId);
        assert scgSearchDTO != null;
        if(scgSearchDTO.getResults().size() > 0) {
            for (List<SCGCard> scgCards : scgSearchDTO.getResults()) {
                for (SCGCard scgCard : scgCards) {
                    if (scgCard.isAddable()) {
                        Card card = scgCard.toCardWithSet(scgSearchDTO.getSetName());
                        SharedResources.addCard(card);
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
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new Gson().fromJson(response.toString(), SCGSearchDTO.class);

        } catch (Exception e) {
            ScraperUtil.log(e.getStackTrace());
        }
        return null;
    }
}
