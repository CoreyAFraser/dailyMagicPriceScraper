package scraper.main;

import scraper.util.ScraperUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

class Gmail {

    static void send(String eMail, String message, String... files) {
        final String userName = "DailyMagicList@gmail.com";
        final String password = "dailyMagicList1";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(userName));
            InternetAddress[] toAddresses = { new InternetAddress(eMail) };
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
	        msg.setSubject("Price List");
	        msg.setSentDate(new Date());
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (String file : files) {
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.attachFile(file);
                multipart.addBodyPart(attachPart);
            }

	        msg.setContent(multipart);
	        Transport.send(msg);

        } catch (Exception e) {
            ScraperUtil.log(e.getStackTrace());
        }
	}
}
