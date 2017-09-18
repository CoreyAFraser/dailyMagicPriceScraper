package scraper.site;

import scraper.main.Card;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TrollAndToad {
	
	public static void getCards() throws IOException{
		getPage("https://www.trollandtoad.com/buylist/ajax_scripts/csv-download.php?deptCode=M");
		getPage("https://www.trollandtoad.com/buylist/ajax_scripts/csv-download.php?deptCode=B");
	}

	private static void getPage(String urlString) {
		try{

			URLConnection urlConnection;

			URL url = new URL(urlString);
			urlConnection = url.openConnection();

			BufferedReader dis  = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String tmp;
			Card card = new Card();
            dis.readLine();

			while ((tmp = dis.readLine()) != null) {
				try {
					card = generateCard(tmp);
				} catch(IndexOutOfBoundsException e) {
					ScraperUtil.log("Troll and Toad bugged card " + tmp);
					ScraperUtil.log(e.getStackTrace());
				}

                SharedResources.addCard(card);
            }
			dis.close();

		} catch (java.net.UnknownHostException e) {
			ScraperUtil.log("Troll and Toad is offline");
			ScraperUtil.log(e.getStackTrace());
		} catch (IOException e) {
			ScraperUtil.log(e.getStackTrace());
		}
	}

	private static Card generateCard(String card) {
		Card result = new Card();
        String[] attributes = parseCSVString(card);
        if (attributes.length > 0) {
				result.setSite("TT");
				result.setSet(attributes[1].replace("\"", "").trim());
				result.setName(attributes[2].replace("\"", "").trim());
				result.setMintPrice(attributes[4].replace("\"", "").trim());
				result.setQuantity(attributes[5].replace("\"", "").trim());
				if (attributes[3].contains("Foil")) {
					result.setFoil("Foil");
				} else {
					result.setFoil("");
				}
			}

		return result;
	}

    private static String[] parseCSVString(String card) {
        List<String> attributesList = new ArrayList<>();

        int start = card.indexOf('\"');
        int end = card.indexOf('\"', start + 1);

        while (start != -1 && end != -1) {
            attributesList.add(card.substring(start, end));
            start = card.indexOf('\"', end + 1);
            end = card.indexOf('\"', start + 1);
        }

        String[] attributes = new String[attributesList.size()];
        for (int i = 0; i < attributesList.size(); i++) {
            attributes[i] = attributesList.get(i);
        }
        return attributes;
    }
}
