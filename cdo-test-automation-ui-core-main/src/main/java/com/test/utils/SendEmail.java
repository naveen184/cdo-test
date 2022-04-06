package com.test.utils;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.aventstack.extentreports.Status;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

public class SendEmail {

	public static void sendEmailWithAttachment() {
		if (SystemProperties.EMAIL.equals(true)) {
			try {
				String sender = SystemProperties.EMAIL_SENDER;
				String rec = SystemProperties.EMAIL_RECEIVER;
				String sub = SystemProperties.EMAIL_SUBJECT;
				String host=SystemProperties.EMAIL_HOST;
				String port =SystemProperties.EMAIL_PORT;
				InternetAddress[] distributionList = InternetAddress.parse(rec, false);

				String from = sender;

				String subject = sub;

				Properties props = new Properties();
				props.put("mail.smtp.host", host);
				props.put("mail.smtp.port", port);
				Session session = Session.getDefaultInstance(props, null);
				session.setDebug(false);

				Message msg = new MimeMessage(session);

				String message = "Please find the attached extent report.";

				msg.setFrom(new InternetAddress(from));
				msg.setRecipients(Message.RecipientType.TO, distributionList);
				msg.setSubject(subject);
				BodyPart messageBodyPart1 = new MimeBodyPart();
				messageBodyPart1.setText(message);
				MimeBodyPart messageBodyPart2 = new MimeBodyPart();

				String filename = System.getProperty("user.dir") + "\\target\\extent-reports.zip";// change accordingly

				DataSource source = new FileDataSource(filename);
				messageBodyPart2.setDataHandler(new DataHandler(source));
				messageBodyPart2.setFileName(filename);

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart1);
				multipart.addBodyPart(messageBodyPart2);

				msg.setSentDate(new Date());
				msg.setContent(multipart);
				Transport.send(msg);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {

			Reporting.logReporter(Status.WARNING, "Email property is false.");

			
		}
	}

}