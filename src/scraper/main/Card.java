package scraper.main;

import scraper.util.shared.SharedResources;

public class Card {
	private String set = "t";
	private String name = "t";
	private String foil = " ";
	private double mintPrice = -1.0;
	private double pldPrice = 0.0;
	private String quantity = "-1";
	private String site = "t";
	
	public String getQuantity() {
		return quantity;
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

	public double getPldPrice() {
		return pldPrice;
	}

	public void setPldPrice(String pldPrice) {
		try {
			this.pldPrice = Double.parseDouble(pldPrice.replace(",", "").replace("View Details", "").trim());
		} catch (Exception e) {}
	}
	
	
	public Card() {
		this.set = "";
		this.name = "";
		this.foil = "";
		this.mintPrice = 0.0;
	}

	public Card(int type, String text) {
		switch(type) {
			case 1 : 
				
				break;
			default :
				this.set = "";
				this.name = "";
				this.foil = "";
				this.mintPrice = 0.0;
				break;
		}
	}
	
	
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
	public String toStringForFile(int count) {
		String textColor;
		if(this.foil.equals("Foil"))
			textColor = "red";
		else
			textColor = "black";
		
		String backgroundColor;
		if((count%2)==0)
			backgroundColor = "#D3D3D3";
		else
			backgroundColor = "white";
		
		String temp = "<tr style=\"background-color:" + backgroundColor + ";\">\r\n" + 
						"<td style=\"color:" + textColor + ";\">" + this.name + "</td>\r\n" +
						"<td style=\"color:" + textColor + ";\">" + this.set + "</td>\r\n" +
						"<td style=\"color:" + textColor + ";\">" + this.mintPrice + "</td>\r\n" +
						"<td style=\"color:" + textColor + ";\">" + this.pldPrice + "</td>\r\n" +
						"<td style=\"color:" + textColor + ";\">" + this.quantity + "</td>\r\n" +
						"<td style=\"color:" + textColor + ";\">" + this.foil + "</td>\r\n" +
						"<td style=\"color:" + textColor + ";\">" + this.site + "</td>\r\n</tr>";
				
		return temp;
	}

	public String toString() {
		String temp  = String.format("%-" + (SharedResources.nameLength+5) + 
									"s %-" + (SharedResources.setLength+5) + 
									"s %-10s %-10s %-10s %-10s %-20s",this.name,this.set,this.mintPrice,this.pldPrice,this.quantity,this.foil,this.site);
		return temp;
	}
	
	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		set = set.replace(":","");

		if(set.contains("Deck") && !set.contains("Commander") && !set.contains("Packs") && !set.contains("masters")) {
			set = set.replace("Decks","").replace("Deck", "").replace("Series", "").trim();
		}

		String tempSet = set.toLowerCase();
		if(tempSet.contains("promo")) {
			set = "promos";
		}

        if (tempSet.contains("zendikar") && tempSet.contains("expeditions")) {
            set = "zendikar expeditions";
        }

        if(set.equalsIgnoreCase("futuresight"))
			set = "Future Sight";
		else
			if(set.equalsIgnoreCase("Portal 3 Kingdoms"))
				set = "Portal Three Kingdoms";
			else
				if(set.equalsIgnoreCase("Portal 1"))
					set = "Portal";
				else
					if(set.equalsIgnoreCase("Time Spiral Time Shifted") || set.equalsIgnoreCase("TimeShifted"))
						set = "Time Spiral - Timeshifted";
					else
						if(set.contains("vs")) {
							set = set.replace("Duel","").replace("Deck", "").replace("Decks","").replace(":","").trim();
							set = "Duel Decks: " + set;
						} else
							if(set.equalsIgnoreCase("Portal The Second Age"))
								set = "Portal Second Age";
							else
								if(set.contains("Collectors'") || set.contains("Collector's"))
									set = set.replace("-", "");
		
		if(set.contains("Revised") || set.contains("3rd"))
			set = "Revised";


        this.set = set.replace("M10","").replace("M11","").replace("M12","").replace("M13","").replace("M14","")
						.replace("M15","").replace("(","").replace(")","").replace("/", "")
						.replace("Core Set","").replace("Edition", "").replace("Gift Boxes:", "")
						.replace("Deck Series: ", "").replace("Promo Cards", "Promos")
						.replace("Promotional Cards","Promos").replace(": City of Guilds", "")
						.replace(" of Kamigawa", "").replace(".","").replace("Magic Cards","").replace("ColdSnap", "Coldsnap")
						.replace("Classic","").replace("Re-Release", "Theme Deck Reprints")
						.replace("\\","").replace("Collector's ", "CE").replace("Colectors'", "CE").replace("MTG","")
						.replace("Premium", "Duel Decks:").replace("PDS", "Duel Decks:").replace("City of Guilds","")
                .replace("\\s+", " ").replace("&", "and").replace("Gift Boxes", "").toLowerCase().replace("foil", "")
                .replace("singles", "").trim();
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.replace("-","").replace("Foil", "").replace("FOIL", "").trim().toLowerCase();
	}
	public String getFoil() {
		return foil;
	}
	public void setFoil(String foil) {
		this.foil = foil;
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
        if(set.equals(card.set) && name.equals(card.name) && foil.equals(card.foil) && (mintPrice == card.mintPrice) && (pldPrice == card.pldPrice) &&
        		quantity.equals(card.quantity) && site.equals(card.site)) {
        	return true;
        } else {
        	return false;
        }
    }
}
