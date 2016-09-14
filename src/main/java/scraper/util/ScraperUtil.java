package scraper.util;

import scraper.util.shared.SharedResources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScraperUtil {

	public static void calculateElapsedTime() {
		SharedResources.incrEnd = System.currentTimeMillis();
		Long elapsed = (SharedResources.incrEnd - SharedResources.incrBegin)/1000;
		
		double hours = elapsed/3600.0;
		int hrs = (int)hours;
		double minutes = (hours - hrs)*60.0;
		int mins = (int)minutes;
		double seconds = (minutes - mins)*60.0;
		log("Time Elapsed: " + hrs + " hours " + mins + " minutes " + seconds + " seconds");
		SharedResources.incrBegin = System.currentTimeMillis();
	}
	
	public static void calculateTotalElapsedTime() {
		SharedResources.end = System.currentTimeMillis();
		Long elapsed = (SharedResources.end - SharedResources.begin)/1000;
		
		double hours = elapsed/3600.0;
		int hrs = (int)hours;
		double minutes = (hours - hrs)*60.0;
		int mins = (int)minutes;
		double seconds = (minutes - mins)*60.0;
		log("Total Time Elapsed: " + hrs + " hours " + mins + " minutes " + seconds + " seconds");
	}
	
	public static void log(Object info) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String text = dateFormat.format(cal.getTime()) + ":  " + info.toString();
		
		SharedResources.logger.println(text);
		System.out.println(text);
	}

	public static void log(Object[] info) {
		for (Object element : info) {
			log(element);
		}
	}
}
