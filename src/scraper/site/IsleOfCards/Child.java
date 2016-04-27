package scraper.site.IsleOfCards;

import com.google.gson.annotations.SerializedName;

public class Child {

    @SerializedName("CardCategory")
    private MagicSet set;

    public MagicSet getSet() {
        return set;
    }

    public void setSet(MagicSet set) {
        this.set = set;
    }
}

