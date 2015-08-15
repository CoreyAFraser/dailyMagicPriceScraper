package ie.moguntia.webcrawler;

import ie.moguntia.threads.*;
import java.net.*;
import java.io.*;
import java.util.Vector;

public class WSDLCrawlerThread extends ControllableThread {
	public void process(Object o) {
		// The objects that we're dealing with here a strings for urls
		try {
			URL pageURL = (URL) o;
            String mimetype = pageURL.openConnection().getContentType();
            // Discard all non-text files
            // Further assumptions on the mime type should not be made, because
            // some WSDLs advertise themselves as text/plain, others as text/xml
            // Anyway, we should try to identify WSDL pages by the definitions-
            // tag rather than by content-type.
            if (!mimetype.startsWith("text")) return;
			String rawPage = SaveURL.getURL(pageURL);
            // I don't know if it is legal, but we also want to identify the
            // file as WSDL if the definitions-tag is not spelled in small
            // letters.
            String smallPage = rawPage.toLowerCase().replaceAll("\\s", " ");
			if (smallPage.indexOf("<definitions") != -1) {
				// assume that it's a wsdl
				String filename = pageURL.getPath();
                // add a unique number as filename prefix
                int serviceId = tc.getUniqueNumber();
                filename = ((URLQueue) queue).getFilenamePrefix() + serviceId +
                    "-" + filename.substring(filename.lastIndexOf('/') + 1);
                // add suffix if not already there
                if (!filename.toLowerCase().endsWith(".wsdl"))
                    filename += ".wsdl";
				FileWriter fw = new FileWriter(filename);
				fw.write(rawPage);
				fw.close();
                // now try to retrieve the home page for the wsdl we have just
                // downloaded and store it as well
                URL homepage = new URL(pageURL.getProtocol(),
                    pageURL.getHost(),
                    "");    // leave filename blank
                filename = ((URLQueue) queue).getFilenamePrefix() + serviceId +
                    "-" + homepage.getHost() + ".html";
                SaveURL.writeURLtoFile(homepage, filename);
			} else {
				// treat the url a a html file and try to extract links
				Vector links = SaveURL.extractLinks(rawPage, smallPage);
				// Convert each link text to a url and enque
				for (int n = 0; n < links.size(); n++) {
					try {
						// urls might be relative to current page
						URL link = new URL(pageURL,
										   (String) links.elementAt(n));
						queue.push(link, level + 1);
					} catch (MalformedURLException e) {
						// Ignore malformed URLs, the link extractor might
						// have failed.
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// process of this object has failed, but we just ignore it here
		}
	}
}
