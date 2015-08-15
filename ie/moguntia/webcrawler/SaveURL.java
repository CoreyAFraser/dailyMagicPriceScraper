package ie.moguntia.webcrawler;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Class with static methods that can save URLs and extract links
 * Works also as a standalone program, usage is: java SaveURL <url> [<file>]
 * If file is not specified, all links on the specified url are printed to the
 * standard console.
 *
 * This code is in the public domain.
 *
 * @author Andreas Hess <andreas.hess@ucd.ie>, 01/02/2003
 */

public class SaveURL
{

	/**
	 * Opens a buffered stream on the url and copies the contents to writer
	 */
	public static void saveURL(URL url, Writer writer)
		throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		for (int c = in.read(); c != -1; c = in.read()) {
			writer.write(c);
		}
	}

	/**
	 * Opens a buffered stream on the url and copies the contents to OutputStream
	 */
	public static void saveURL(URL url, OutputStream os)
		throws IOException {
		InputStream is = url.openStream();
		byte[] buf = new byte[1048576];
		int n = is.read(buf);
		while (n != -1) {
			os.write(buf, 0, n);
			n = is.read(buf);
		}
	}

	/**
	 * Writes the contents of the url to a string by calling saveURL with a
	 * string writer as argument
	 */
	public static String getURL(URL url)
		throws IOException {
		StringWriter sw = new StringWriter();
		saveURL(url, sw);
		return sw.toString();
	}

	/**
	 * Writes the contents of the url to a new file by calling saveURL with
	 * a file writer as argument
	 */
	public static void writeURLtoFile(URL url, String filename)
		throws IOException {
		// FileWriter writer = new FileWriter(filename);
		// saveURL(url, writer);
		// writer.close();
		FileOutputStream os = new FileOutputStream(filename);
		saveURL(url, os);
		os.close();
	}

	/**
	 * Extract links directly from a URL by calling extractLinks(getURL())
	 */
	public static Vector extractLinks(URL url)
		throws IOException {
		return extractLinks(getURL(url));
	}

	public static Map extractLinksWithText(URL url)
		throws IOException {
		return extractLinksWithText(getURL(url));
	}

	/**
     * Extract links from a html page given as a raw and a lower case string
     * In order to avoid the possible double conversion from mixed to lower case
     * a second method is provided, where the conversion is done externally.
     */
    public static Vector extractLinks(String rawPage, String page) {
		int index = 0;
		Vector links = new Vector();
		while ((index = page.indexOf("<a ", index)) != -1)
		{
		    if ((index = page.indexOf("href", index)) == -1) break;
		    if ((index = page.indexOf("=", index)) == -1) break;
		    String remaining = rawPage.substring(++index);
		    StringTokenizer st 
				= new StringTokenizer(remaining, "\t\n\r\"'>#");
		    String strLink = st.nextToken();
			if (! links.contains(strLink)) links.add(strLink);
		}
		return links;
    }


	/**
	 * Extract links (key) with link text (value)
	 * Note that due to the nature of a Map only one link text is returned per
	 * URL, even if a link occurs multiple times with different texts.
	 */
	public static Map extractLinksWithText(String rawPage, String page) {
		int index = 0;
		Map links = new HashMap();
		while ((index = page.indexOf("<a ", index)) != -1)
		{
			int tagEnd = page.indexOf(">", index);
		    if ((index = page.indexOf("href", index)) == -1) break;
		    if ((index = page.indexOf("=", index)) == -1) break;
			int endTag = page.indexOf("</a", index);
		    String remaining = rawPage.substring(++index);
		    StringTokenizer st 
				= new StringTokenizer(remaining, "\t\n\r\"'>#");
		    String strLink = st.nextToken();
			String strText = "";
			if (tagEnd != -1 && tagEnd + 1 <= endTag) {
				strText = rawPage.substring(tagEnd + 1, endTag);
			}
			strText = strText.replaceAll("\\s+", " ");
			links.put(strLink, strText);
		}
		return links;
		
	}
    
    /**
	 * Extract links from a html page given as a String
	 * The return value is a vector of strings. This method does neither check
	 * the validity of its results nor does it care about html comments, so
	 * links that are commented out are also retrieved.
	 */
	public static Vector extractLinks(String rawPage) {
        return extractLinks(rawPage, rawPage.toLowerCase().replaceAll("\\s", " "));
	}

	public static Map extractLinksWithText(String rawPage) {
        return extractLinksWithText(rawPage, rawPage.toLowerCase().replaceAll("\\s", " "));
	}

	/**
	 * As a standalone program this class is capable of copying a url to a file
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 1) {
                URL url = new URL(args[0]);
                System.out.println("Content-Type: " +
                    url.openConnection().getContentType());
// 				Vector links = extractLinks(url);
// 				for (int n = 0; n < links.size(); n++) {
// 					System.out.println((String) links.elementAt(n));
// 				}
				Set links = extractLinksWithText(url).entrySet();
				Iterator it = links.iterator();
				while (it.hasNext()) {
					Map.Entry en = (Map.Entry) it.next();
					String strLink = (String) en.getKey();
					String strText = (String) en.getValue();
					System.out.println(strLink + " \"" + strText + "\" ");
				}
				return;
			} else if (args.length == 2) {
				writeURLtoFile(new URL(args[0]), args[1]);
				return;
			}
		} catch (Exception e) {
			System.err.println("An error occured: ");
			e.printStackTrace();
// 			System.err.println(e.toString());
		}
		// Display usage information
		// (If the program had done anything sensible, we wouldn't be here.)
		System.err.println("Usage: java SaveURL <url> [<file>]");
		System.err.println("Saves a URL to a file.");
		System.err.println("If no file is given, extracts hyperlinks on url to console.");
	}
}
