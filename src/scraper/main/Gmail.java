package scraper.main;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import scraper.util.shared.SharedResources;

public class Gmail {

	public static void send(String eMail,String path, String fileName, String message) {

		final String userName = "DailyMagicList@gmail.com";
		final String password = "dailyMagicListBanana";

		 // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        
			try {
				msg.setFrom(new InternetAddress(userName));
			
		
	        InternetAddress[] toAddresses = { new InternetAddress(eMail) };
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
	        msg.setSubject("Price List");
	        msg.setSentDate(new Date());
	 
	        // creates message part
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setContent(message, "text/html");
	 
	        // creates multi-part
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(messageBodyPart);
	 
	        MimeBodyPart attachPart = new MimeBodyPart();
	        
	        attachPart.attachFile("./" + path +fileName);
	                
	        multipart.addBodyPart(attachPart);
	
	 
	        // sets the multi-part as e-mail's content
	        msg.setContent(multipart);
	        
	        // sends the e-mail
	        Transport.send(msg);
	        
		} catch (AddressException e) {
			e.printStackTrace(SharedResources.logger);
		} catch (MessagingException e) {
			e.printStackTrace(SharedResources.logger);
		} catch (IOException e) {
			e.printStackTrace(SharedResources.logger);
		}
        
	}

}
