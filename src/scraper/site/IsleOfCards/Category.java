package scraper.site.IsleOfCards;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private List<Child> children = new ArrayList<>();

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }
}