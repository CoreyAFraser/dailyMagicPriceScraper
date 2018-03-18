package scraper.site.ABUGames;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("docs")
    public List<ABUCard> ABUCards = null;

    public List<ABUCard> getABUCards() {
        return ABUCards;
    }

    public void setABUCards(List<ABUCard> ABUCards) {
        this.ABUCards = ABUCards;
    }

    @Override
    public String toString() {
        return "Response{" +
                "ABUCards=" + ABUCards +
                '}';
    }
}
