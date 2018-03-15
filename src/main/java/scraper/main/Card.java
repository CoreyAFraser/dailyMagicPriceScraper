package scraper.main;

import scraper.util.shared.SharedResources;

public class Card {

    private String set = "t";
    private String name = "t";
    private String foil = " ";
    private double mintPrice = -1.0;
    private double pldPrice = -1.0;
    private String quantity = "-1";
    private String site = "t";
    private PriceType priceType = PriceType.BUYLIST;

    public Card() {
        this.set = "";
        this.name = "";
        this.foil = "";
        this.mintPrice = 0.0;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getMintPrice() {
        return mintPrice;
    }

    public void setMintPrice(String mintPrice) {
        try {
            this.mintPrice = Double.parseDouble(mintPrice.replace(",", "").replace("View Details", "").trim());
        } catch (Exception e) {
        }
    }

    public void setPldPrice(String pldPrice) {
        try {
            this.pldPrice = Double.parseDouble(pldPrice.replace(",", "").replace("View Details", "").trim());
        } catch (Exception e) {
        }
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    String toStringForFile(int count) {
        String textColor;
        if (this.foil.equals("Foil"))
            textColor = priceType.getFoilTextColor();
        else
            textColor = priceType.getTextColor();

        String backgroundColor;
        if ((count % 2) == 0)
            backgroundColor = "#D3D3D3";
        else
            backgroundColor = "white";

        return "<tr style=\"background-color:" + backgroundColor + ";\">\r\n" +
                "<td style=\"color:" + textColor + ";\">" + this.name + "</td>\r\n" +
                "<td style=\"color:" + textColor + ";\">" + this.set + "</td>\r\n" +
                "<td style=\"color:" + textColor + ";\">" + this.mintPrice + "</td>\r\n" +
                "<td style=\"color:" + textColor + ";\">" + this.pldPrice + "</td>\r\n" +
                "<td style=\"color:" + textColor + ";\">" + this.quantity + "</td>\r\n" +
                "<td style=\"color:" + textColor + ";\">" + this.priceType.name() + "</td>\r\n" +
                "<td style=\"color:" + textColor + ";\">" + this.foil + "</td>\r\n" +
                "<td style=\"color:" + textColor + ";\">" + this.site + "</td>\r\n</tr>";
    }

    public String toString() {
        return String.format("%-" + (SharedResources.getNameLength() + 5) +
                "s %-" + (SharedResources.getSetLength() + 5) +
                "s %-10s %-10s %-10s %-10s %-20s", this.name, this.set, this.mintPrice, this.pldPrice, this.quantity, this.foil, this.site);
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        set = set.toLowerCase();

        if (set.equalsIgnoreCase("futuresight"))
            set = "future sight";

        if (set.equalsIgnoreCase("portal 3 kingdoms"))
            set = "portal three kingdoms";

        if (set.equalsIgnoreCase("portal 1"))
            set = "portal";

        if (set.equalsIgnoreCase("time spiral time shifted") || set.equalsIgnoreCase("timeshifted"))
            set = "time spiral timeshifted";

        if (set.equalsIgnoreCase("portal the second age"))
            set = "portal second age";

        if (set.contains("revised") || set.contains("3rd"))
            set = "revised";

        if (set.contains("promo") || set.contains("promotional cards"))
            set = "promos";

        if ((set.contains("zendikar") && (set.contains("expeditions")) || set.contains("exps")))
            set = "zendikar expeditions";

        if (!set.contains("commander") && !set.contains("conspiracy") && !set.contains("master") && !set.contains("from the vault") && !set.contains("ftv")) {
            if (set.contains("2010"))
                set = "m10";

            if (set.contains("2011"))
                set = "m11";

            if (set.contains("2012"))
                set = "m12";

            if (set.contains("2013"))
                set = "m13";

            if (set.contains("2014"))
                set = "m14";

            if (set.contains("2015"))
                set = "m15";
        }

        if (set.equals("commander"))
            set = "commander 2011";

        if (set.equals("conspiracy"))
            set = "conspiracy 2014";

        if (set.contains("duel") && !set.contains("planeswalker")) {
            set = set.replace("decks", "").replace("deck", "").replace("duel", "").replace(":", "").replace("\\.", "").trim();
            set = "duel decks: " + set;
        }

        if (set.contains("premium") || set.contains("pds")) {
            set = set.replace("premium", "").replace("deck", "").replace("series", "").replace(":", "").replace("\\.", "").trim();
            set = "pds: " + set;
        }

        if (set.contains("from the vault")) {
            if (set.contains("2008"))
                set = set.replace("2008", "").trim();

            if (set.contains("2009"))
                set = set.replace("2009", "").trim();

            if (set.contains("2010"))
                set = set.replace("2010", "").trim();

            if (set.contains("2011"))
                set = set.replace("2011", "").trim();

            if (set.contains("2012"))
                set = set.replace("2012", "").trim();

            if (set.contains("2013"))
                set = set.replace("2013", "").trim();

            if (set.contains("2014"))
                set = set.replace("2014", "").trim();

            if (set.contains("2015"))
                set = set.replace("2015", "").trim();

            if (set.contains("2016"))
                set = set.replace("2016", "").trim();
        }

        if (set.contains("bulk")) {
            set = "bulk";
        }

        if (set.contains("modern event")) {
            set = "march of the multitudes";
        }

        if (set.equals("modern masters"))
            set = "modern masters 2013";

        if (set.contains("world championships"))
            set = "world championships";

        if (set.equals("planechase"))
            set = "planechase 2009";

        if (set.equals("plane cards"))
            set = "planechase planes 2009";

        if (set.equals("plane cards anthology") || set.equals("planechase anthology planar"))
            set = "planechase planes anthology";

        if (set.equals("portal 3k"))
            set = "portal three kingdoms";

        if (set.equals("portal ii") || set.equals("portal the second age"))
            set = "portal second age";

        if (set.equals("archenemy"))
            set = "archenemy 2010";

        if (set.equals("starter series"))
            set = "starter 1999";

        if (set.contains("timeshifted"))
            set = "timeshifted";

        if (set.contains("invocations"))
            set = "amonkhet invocations";

        if (set.contains("inventions"))
            set = "kaladesh inventions";

        this.set = set.replace(":", "").replace("(", "").replace(")", "").replace("/", "").replace("\\", "").replace("&", "and").replace(".", "").replace("-", "")
                .replace("core set", "").replace("edition", "").replace("gift boxes", "")
                .replace("promo cards", "promos").replace("city of guilds", "").replace("of kamigawa", "").replace("magic cards", "")
                .replace("classic", "").replace("re-release", "theme deck reprints").replace("andamp;", "and")
                .replace("mtg", "").replace("gift boxes", "")
                .replace("foil", "").replace("singles", "").replace("\\s+", " ").trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase().replace("\n", "").replace("-", "").replace("foil", "").replace("zendikar expeditions", "").replace("expeditions", "").trim();
    }

    public String getFoil() {
        return foil;
    }

    public void setFoil(String foil) {
        this.foil = foil;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Card))
            return false;
        if (obj == this)
            return true;

        Card card = (Card) obj;
        return set.equals(card.set) && name.equals(card.name) && foil.equals(card.foil) && (mintPrice == card.mintPrice) && (pldPrice == card.pldPrice) &&
                quantity.equals(card.quantity) && site.equals(card.site);
    }

    public enum PriceType {
        RETAIL("blue", "green"),
        BUYLIST("black", "red");

        private String textColor;
        private String foilTextColor;

        PriceType(String textColor, String foilTextColor) {
            this.textColor = textColor;
            this.foilTextColor = foilTextColor;
        }

        public String getTextColor() {
            return textColor;
        }

        public String getFoilTextColor() {
            return foilTextColor;
        }
    }
}
