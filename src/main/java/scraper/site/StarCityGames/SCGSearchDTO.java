package scraper.site.StarCityGames;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

class SCGSearchDTO {

    @SerializedName("search")
    private String setName;

    private List<List<SCGCard>> results = new ArrayList<>();

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    List<List<SCGCard>> getResults() {
        return results;
    }
}
