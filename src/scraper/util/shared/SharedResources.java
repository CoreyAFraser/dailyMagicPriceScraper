package scraper.util.shared;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import scraper.main.Card;

public class SharedResources {
	public static int nameLength = 0;
    public static int setLength = 0;
    public static WebDriver driver;
    public static Long incrBegin;
	public static Long incrEnd;
	public static ArrayList<Card> cards;
	public static PrintWriter logger;
	public static Long begin;
	public static Long end;
}
