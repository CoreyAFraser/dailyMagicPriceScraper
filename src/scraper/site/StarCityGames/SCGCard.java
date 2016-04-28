package scraper.site.StarCityGames;

import scraper.main.Card;

public class SCGCard {

    private String name;
    private String condition;
    private Boolean foil;
    private String language;
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFoil() {
        return foil;
    }

    public void setFoil(Boolean foil) {
        this.foil = foil;
    }

    public Card toCardWithSet(String set) {
        Card card = new Card();
        card.setSet(set);
        card.setName(name);
        card.setFoil(foil ? "Foil" : "");
        card.setMintPrice(price);
        card.setSite("SCG");

        return card;
    }

    public boolean isAddable() {
        return condition.equalsIgnoreCase("NM/M") && language.equalsIgnoreCase("English");
    }
}
