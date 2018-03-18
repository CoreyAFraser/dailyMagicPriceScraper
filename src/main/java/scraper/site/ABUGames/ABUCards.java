package scraper.site.ABUGames;
import com.google.gson.annotations.SerializedName;

public class ABUCards {
    @SerializedName("response")
    public Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ABUCards{" +
                "response=" + response +
                '}';
    }
}