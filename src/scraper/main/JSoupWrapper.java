package scraper.main;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSoupWrapper {

    public static Document connect(String url) throws Exception{
        return Jsoup.connect(url)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                .get();
    }

    public static Document connect(String url, int timeOut) throws Exception{
        return Jsoup.connect(url)
                .timeout(timeOut)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                .get();
    }
}
