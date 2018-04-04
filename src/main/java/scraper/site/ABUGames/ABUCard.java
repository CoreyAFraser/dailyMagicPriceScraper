package scraper.site.ABUGames;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ABUCard {

    @SerializedName("card_style")
    public List<String> foil = null;
    @SerializedName("magic_edition")
    public List<String> set = null;
    @SerializedName("simple_title")
    public String name;
    @SerializedName("buy_list_quantity")
    public int quantity;
    @SerializedName("buy_price")
    public double price;
    @SerializedName("condition")
    public String condition;

    public List<String> getSet() {
        return set;
    }

    public void setSet(List<String> set) {
        this.set = set;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public List<String> getFoil() {
        return foil;
    }

    public void setFoil(List<String> foil) {
        this.foil = foil;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "ABUCard{" +
                "foil=" + foil +
                ", set=" + set +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", condition='" + condition + '\'' +
                '}';
    }
}
