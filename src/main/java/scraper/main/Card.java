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
		} catch(Exception e) {}
	}

	public void setPldPrice(String pldPrice) {
		try {
			this.pldPrice = Double.parseDouble(pldPrice.replace(",", "").replace("View Details", "").trim());
		} catch (Exception e) {}
	}
	
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

    String toStringForFile(int count) {
        String textColor;
		if(this.foil.equals("Foil"))
			textColor = priceType.getFoilTextColor();
		else
			textColor = priceType.getTextColor();

		String backgroundColor;
		if((count%2)==0)
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
        return String.format("%-" + (SharedResources.nameLength + 5) +
                "s %-" + (SharedResources.setLength + 5) +
                "s %-10s %-10s %-10s %-10s %-20s", this.name, this.set, this.mintPrice, this.pldPrice, this.quantity, this.foil, this.site);
    }

	public String getSet() {
		return set;
	}

    public void setSet(String set) {
	    set = set.toLowerCase();

        if(set.equalsIgnoreCase("futuresight"))
			set = "future sight";
		else
			if(set.equalsIgnoreCase("portal 3 kingdoms"))
				set = "portal three kingdoms";
			else
				if(set.equalsIgnoreCase("portal 1"))
					set = "portal";
				else
					if(set.equalsIgnoreCase("time spiral time shifted") || set.equalsIgnoreCase("timeshifted"))
						set = "time spiral timeshifted";
					else
						if(set.contains("vs")) {
							set = set.replace("duel","").replace("deck", "").replace("decks","").replace(":","").trim();
							set = "duel decks: " + set;
						} else
							if(set.equalsIgnoreCase("portal the second age"))
								set = "portal second age";
                            else
                                if(set.contains("revised") || set.contains("3rd"))
                                    set = "revised";
                                else
                                    if(set.contains("deck") && !set.contains("commander") && !set.contains("packs") && !set.contains("masters"))
                                        set = set.replace("decks","").replace("deck", "").replace("series", "").trim();
                                    else
                                        if(set.contains("promo") || set.contains("promotional cards"))
                                            set = "promos";
                                        else if (set.contains("zendikar") && (set.contains("expeditions") || set.contains("exps")))
											set = "zendikar expeditions";

        this.set = set.replace(":","").replace("(","").replace(")","").replace("/", "").replace("\\","").replace("&", "and").replace(".","").replace("-", "")
                .replace("m10","").replace("m11","").replace("m12","").replace("m13","").replace("m14","").replace("m15","")
                .replace("core set","").replace("edition", "").replace("gift boxes", "").replace("deck series ", "")
                .replace("promo cards", "promos").replace("city of guilds", "").replace("of kamigawa", "").replace("magic cards","")
                .replace("classic","").replace("re-release", "theme deck reprints").replace("collector's ", "ce").replace("colectors'", "ce")
                .replace("mtg","").replace("premium", "duel decks:").replace("pds", "duel decks").replace("gift boxes", "")
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
		if(obj == null) {
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
