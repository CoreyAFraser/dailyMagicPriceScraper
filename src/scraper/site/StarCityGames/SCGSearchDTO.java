package scraper.site.StarCityGames;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SCGSearchDTO {

    @SerializedName("search")
    private String setName;

    private List<List<SCGCard>> results = new ArrayList<List<SCGCard>>();

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public List<List<SCGCard>> getResults() {
        return results;
    }

    public void setResults(List<List<SCGCard>> results) {
        this.results = results;
    }
}
