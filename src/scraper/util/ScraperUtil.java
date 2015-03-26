package scraper.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import scraper.util.shared.SharedResources;

public class ScraperUtil {
	
	public static String getPage(URL url){
		String html = "";
	    URLConnection urlConnection;
		try {
			urlConnection = url.openConnection();
		
			BufferedReader dis  = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			
		    String tmp = "";   
		    while ((tmp = dis.readLine()) != null) {  
		    	html += " " + tmp;  
		    }  
		    dis.close(); 
	    
		} catch (IOException e) {
			e.printStackTrace(SharedResources.logger);
		}  
		return html;
	}
	
	
	public static String getHREF(String link) {
		int beginIndex = 0,
			endIndex = 0;
		String href;
		
		beginIndex = link.indexOf("href=")+6;
		endIndex = link.indexOf("\"",beginIndex);
		
		href = link.substring(beginIndex,endIndex);
		
		return href;
	}
	
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
	
	public static void log(Object text) {
		SharedResources.logger.println(text);
		System.out.println(text);
	}

}
