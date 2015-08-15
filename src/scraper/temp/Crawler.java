package scraper.temp;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.net.MalformedURLException;

class CrawlTest
{
	public static Queue<String> getUrls(String line)
	{
		Queue<String> urls = new LinkedList<String>();
		String temp;
		int indexS=0,
			indexE=0;
		while ((line.length()!=0)&&(indexS!=-1)&&(indexE!=-1))
		{
			indexS = line.indexOf("\"http");
			if(indexS != -1)
			{
				temp = line.substring(indexS+1);

				indexE = temp.indexOf("\"");
				if(indexE != -1)
				{
					urls.add(temp.substring(0,indexE));
					line = temp.substring(indexE+1);
				}
			}
		}
		return urls;
	}
	
	public static String getPath(String s)
	{
		String path = s.substring(s.indexOf("//")+2);

		if((path.lastIndexOf("/")!=path.length()-1)&&(path.lastIndexOf("/")!=-1))
			path = path.substring(0,path.lastIndexOf("/")+1);
		
		// Need to add a place to search for illegal chars and remove/replace them
		return path;
	}
	
	public static String getFileName(String s)
	{
		String fileName = s.substring(s.indexOf("//")+2);
		
		if((fileName.lastIndexOf("/")!=fileName.length()-1)&&(fileName.lastIndexOf("/")!=-1))
			{
			fileName = fileName.substring(fileName.lastIndexOf("/")+1);
			if(fileName.lastIndexOf(".")!=-1)
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
		else
			fileName = "index";
			
		return fileName;
	}
	
	public static void main(String args[]) throws InterruptedException
	{
		Queue<String> urlQ = new LinkedList<String>();
		//if(args[0]==null)
			urlQ.add("http://en.wikipedia.org/wiki/Chmod");
		//else
		//	urlQ.add(args[0]);
		//urlQ.add("http://en.wikipedia.org/wiki/Presidents_of_the_United_States");
		int counter=0;
		String eol = System.getProperty("line.separator");
		String basePath = ".." + File.separator + "Crawler" + File.separator + "Crawl" + File.separator;
		boolean success = (new File(basePath)).mkdirs();
		if(success)
		{
			try
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(basePath + "map.txt"));
			
				while((urlQ.peek()!= null)&&(counter<5))
				{
					String currUrl = urlQ.poll();
					String fileName = getFileName(currUrl);
					System.out.println("Checking URL # " + counter + "  " + currUrl);
					String path = basePath + getPath(currUrl);
					boolean success2 = (new File(path)).mkdirs();
					System.out.println(path);
					System.out.println(fileName);
			
					if(success2)
					{
						try
						{
							URL url = new URL(currUrl);
							out.write(currUrl+eol);
					
							int t=0;
					
							BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
							BufferedWriter out1 = new BufferedWriter(new FileWriter(path + fileName + ".html"));
							BufferedWriter out2 = new BufferedWriter(new FileWriter(path + fileName + ".txt"));
			
							System.out.println("Counting links in this URL");
							String strLine;
							while ((strLine = reader.readLine()) != null)
							{
								out1.write(strLine+eol);
			
								Queue<String> tempQ = getUrls(strLine);		
								while(tempQ.peek()!=null)
								{
									urlQ.add(tempQ.peek());
									out2.write(tempQ.poll()+eol);
									t++;
									//counter++;
								}		
							}
							System.out.println(t + " links found");
							t=0;
							reader.close();
							out1.close();
							out2.close();
							counter++;
							System.out.println("Five Second Pause");
							Thread.sleep(5000);
					
						} 
						catch (MalformedURLException e)
						{
						e.printStackTrace();
						}  
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			out.close();
			}
			catch (IOException e)
			{
			e.printStackTrace();
			}
		
		}
	}
}