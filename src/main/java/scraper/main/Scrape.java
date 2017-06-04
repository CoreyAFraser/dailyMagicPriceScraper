package scraper.main;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import scraper.site.StarCityGames.StarCityGames;
import scraper.site.StrikeZone;
import scraper.site.TrollAndToad;
import scraper.site.selenium.ABUGames;
import scraper.site.selenium.CardKingdom;
import scraper.site.selenium.ChannelFireball;
import scraper.site.selenium.UntappedGames;
import scraper.util.CardFoilComparator;
import scraper.util.ScraperUtil;
import scraper.util.shared.SharedResources;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Scrape extends TimerTask {

	private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	private String eol = "<br>";
	private String message = "Price List" + eol;
    private String logFileName;

	private CardFoilComparator cardFoilComparator = new CardFoilComparator();

	public void run() {

		try {
			Date date = new Date();
            logFileName = dateTimeFormat.format(date) + "_log.txt";
			SharedResources.logger = new PrintWriter("." + File.separator + "logs" + File.separator
					+ logFileName, "UTF-8");

			ScraperUtil.log("Started Successfully");

			SharedResources.incrBegin = System.currentTimeMillis();
			SharedResources.begin = System.currentTimeMillis();
			SharedResources.cards = new ArrayList<>();
			try {
				try {
					ScraperUtil.log("StrikeZone Starting");
					StrikeZone.getCards(); ScraperUtil.log("StrikeZone Done");
				} catch (java.io.FileNotFoundException e) {
					message = message + "\r\nStrikeZone appears to be down";
					ScraperUtil.log("StrikeZone Error");
                    ScraperUtil.log(e.getStackTrace());
                }
				sortCards();

				try {
					ScraperUtil.log("Troll and Toad Starting");
					TrollAndToad.getCards();
					ScraperUtil.log("Troll and Toad Done");
				} catch(java.io.FileNotFoundException e) {
					message = message + eol + "Troll and Toad appears to be down";
					ScraperUtil.log("Troll and Toad Error");
                    ScraperUtil.log(e.getStackTrace());
                }
				sortCards();

                try {
                    ScraperUtil.log("SCG Starting");
                    StarCityGames.getCards();
                    ScraperUtil.log("SCG Done");
                } catch (java.io.FileNotFoundException e) {
                    message = message + eol + "SCG appears to be down";
                    ScraperUtil.log("SCG Error");
                    e.printStackTrace(SharedResources.logger);
                }
                sortCards();

				ScraperUtil.log("Starting Browser");
				SharedResources.driver = new FirefoxDriver();
				SharedResources.driver.manage().window().maximize();
				ScraperUtil.log("Browser Open");
				ScraperUtil.calculateElapsedTime();



				try {
					ScraperUtil.log("Untapped Games Starting");
					UntappedGames.getCards();
					ScraperUtil.log("Untapped Games Done");
				} catch (java.io.FileNotFoundException e) {
					message = message + eol + "Untapped Games appears to be down";
					ScraperUtil.log("Untapped Games Error");
                    ScraperUtil.log(e.getStackTrace());
                }
				sortCards();

				try {
					ScraperUtil.log("ABU Starting");
					ABUGames.getCards();
					ScraperUtil.log("ABU Done");
				} catch (java.io.FileNotFoundException e) {
					message = message + eol +  "ABU appears to be down";
					ScraperUtil.log("ABU Error");
                    ScraperUtil.log(e.getStackTrace());
                }
				sortCards();

				try {
					ScraperUtil.log("Channel Fireball Starting");
					ChannelFireball.getCards();
					ScraperUtil.log("Channel Fireball Done");
				} catch (java.io.FileNotFoundException e) {
					message = message + eol + "Channel Fireball appears to be down";
					ScraperUtil.log("Channel Fireball Error");
                    ScraperUtil.log(e.getStackTrace());
				} catch (WebDriverException we) {
					message = message + eol + "Channel Fireball has a javascript issue";
					ScraperUtil.log("Channel Fireball WebDriver Error");
					ScraperUtil.log(we.getStackTrace());
				}
				sortCards();

                try {
                    ScraperUtil.log("CardKingdom Starting");
                    CardKingdom.getCards();
                    ScraperUtil.log("CardKingdom Done");
                } catch (java.io.FileNotFoundException e) {
                    message = message + eol + "CardKingdom appears to be down";
                    ScraperUtil.log("CardKingdom Error");
                    e.printStackTrace(SharedResources.logger);
                }

                sortCards();

				ScraperUtil.log("Sorting Cards");

				SharedResources.driver.quit();


				/* cards.addAll(GamingEtc.getCards()); /* try {
				 * ScraperUtil.log("TJ Games Starting"); TJ.getCards();
				 * ScraperUtil.log("TJ Games Done"); } catch
				 * (java.io.FileNotFoundException e) { message = message + eol +
				 * "TJ Games appears to be down";
				 * ScraperUtil.log("TJ Games Error");
				 * e.printStackTrace(SharedResources.logger); }
				 * ScraperUtil.calculateElapsedTime();
				 */

				/*try {
					ScraperUtil.log("BGs Starting");
					BGs.getCards();
					ScraperUtil.log("Bgs Done");
				} catch (java.io.FileNotFoundException e) {
					message = message + eol + "BGs appears to be down";
					ScraperUtil.log("BGs Error");
					e.printStackTrace(SharedResources.logger);
				}
				ScraperUtil.calculateElapsedTime();*/

				/*try {
					ScraperUtil.log("IsleOfCards Starting");
					IsleOfCards.getCards();
					ScraperUtil.log("IsleOfCards Done");
				} catch (java.io.FileNotFoundException e) {
					message = message + eol + "IsleOfCards appears to be down";
					ScraperUtil.log("IsleOfCards Error");
					e.printStackTrace(SharedResources.logger);
				}

				sortCards();*/

			} catch (IOException e) {
                ScraperUtil.log(e.getStackTrace());
                ScraperUtil.log("IOException");
				SharedResources.driver.quit();
			} catch (Exception e) {
				ScraperUtil.log(e);
				ScraperUtil.log(e.getStackTrace());
                ScraperUtil.log("Exception");
				SharedResources.driver.quit();
			}

			sortCards();

			sendEmail("Pat", "patbrodericksnc@yahoo.com", false);
			ScraperUtil.calculateTotalElapsedTime();

            try {
                cleanUpOldLogsAndLists();
            } catch (Exception e) {
                ScraperUtil.log(e.getStackTrace());
            }

			ScraperUtil.calculateTotalElapsedTime();
			SharedResources.logger.close();

            sendEmail("Corey", "CoreyAFraser@gmail.com", true);
        } catch (Exception e) {
			ScraperUtil.log(e);
			ScraperUtil.log(e.getStackTrace());
        }
		SharedResources.logger.close();
	}

	private void sortCards() {
		try {
			SharedResources.cards.sort((one, two) -> Double.compare(one.getMintPrice(),two.getMintPrice()));
			ScraperUtil.log("Cards sorted by price");
			SharedResources.cards.sort(cardFoilComparator);
			ScraperUtil.log("Cards sorted by Foil");
			SharedResources.cards.sort((one, two) -> one.getName().compareTo(two.getName()));
			ScraperUtil.log("Cards sorted by name");
			SharedResources.cards.sort((one, two) -> one.getSet().compareTo(two.getSet()));
			ScraperUtil.log("Cards sorted by set");
			ScraperUtil.calculateElapsedTime();
			ScraperUtil.log("Cards added: " + SharedResources.cards.size());
		} catch (Exception e) {
            ScraperUtil.log(e.getStackTrace());
            ScraperUtil.log(e.getCause());
        }
	}

	private void zipFile(String path, String file) {
		byte[] buffer = new byte[1024];
		try {
			FileOutputStream fos = new FileOutputStream(path + file + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			ZipEntry ze = new ZipEntry(file + ".html");
			zos.putNextEntry(ze);
			FileInputStream in = new FileInputStream(path + file + ".html");

			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			in.close();
			zos.closeEntry();

			zos.close();

        } catch (IOException e) {
            ScraperUtil.log(e.getStackTrace());
        }
	}

    private void sendEmail(String name, String eMail, boolean includeLogs) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path;
		String fileName;

		try {
			path = "." + File.separator + "priceLists" + File.separator;
			Date date = new Date();
			fileName = name + "-pricelist-" + dateFormat.format(date);
			PrintWriter writer = new PrintWriter(path + fileName + ".html",
					"UTF-8");
			ScraperUtil.log("Writing Cards to File");
			writer.println("<html>\r\n<head>\r\n<title>\r\nMTG List - Buylist Report\r\n</title>\r\n</head>\r\n<body>\r\n<table>");

			String textColor = "black";
			String backgroundColor = "#D3D3D3";

			String headers = "<tr style=\"background-color:" + backgroundColor + ";\">\r\n" +
					"<td style=\"color:" + textColor + ";\"> Name </td>\r\n" +
					"<td style=\"color:" + textColor + ";\"> Set </td>\r\n" +
					"<td style=\"color:" + textColor + ";\"> Mint Price </td>\r\n" +
					"<td style=\"color:" + textColor + ";\"> Played Price</td>\r\n" +
					"<td style=\"color:" + textColor + ";\"> Quantity </td>\r\n" +
					"<td style=\"color:" + textColor + ";\"> Price Type </td>\r\n" +
					"<td style=\"color:" + textColor + ";\"> Foil </td>\r\n" +
					"<td style=\"color:" + textColor + ";\"> Set </td>\r\n</tr>";
			writer.println(headers);

			int count = 1;
			for (Card card : SharedResources.cards) {
				writer.println(card.toStringForFile(count));
				count++;
				writer.flush();
			}

			writer.println("</table>\r\n</body>\r\n</html>");

			writer.close();
			ScraperUtil.log("Zipping File " + fileName + ".html to " + fileName
					+ ".zip");
			zipFile(path, fileName);
			ScraperUtil.log("Sending List to " + eMail + "  " + fileName
					+ ".zip");
            if (includeLogs) {
                Gmail.send(eMail, message, "." + File.separator + "priceLists" + File.separator + fileName + ".zip",
                        "." + File.separator + "logs" + File.separator + logFileName);
            } else {
                Gmail.send(eMail, message, "." + File.separator + "priceLists" + File.separator + fileName + ".zip");
            }
            ScraperUtil.log("Done sending E-mail");
			ScraperUtil.calculateElapsedTime();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
            ScraperUtil.log(e.getStackTrace());
        }
	}

    private void cleanUpOldLogsAndLists() {
        ScraperUtil.log("Cleaning up Old Lists and Logs");
        File priceLists = new File("." + File.separator + "priceLists");
        File logs = new File("." + File.separator + "logs");
        cleanUp(priceLists);
        cleanUp(logs);
    }

    private void cleanUp(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                cleanUp(f);
            }
        } else {
            long diff = new Date().getTime() - file.lastModified();
            if (diff > 7 * 24 * 60 * 60 * 1000) {
                file.delete();
            }
        }
    }

}
