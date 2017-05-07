package com.joy.util.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {
	private MimeMessage message;
	private Session session;
	private Transport transport;
	private static String mailHost;
	private static String sender_username;
	private static String sender_password;
	private static Properties mail_properties = new Properties();
	private static final Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);

	static {
		InputStream in = MailUtil.class.getClassLoader().getResourceAsStream("mail.properties");
		try {
			mail_properties.load(in);
		} catch (IOException e) {
			LOGGER.error("load mail properties error.", e);
		}
		MailUtil.mailHost = mail_properties.getProperty("mail.smtp.host");
		MailUtil.sender_username = mail_properties.getProperty("mail.username");
		MailUtil.sender_password = mail_properties.getProperty("mail.password");
	}
	
	private MailUtil(){}

	public static MailUtil getInstance() {
		MailUtil mail = new MailUtil();
		mail.session = Session.getInstance(mail_properties);
		mail.session.setDebug(false);
		mail.message = new MimeMessage(mail.session);
		return mail;
	}

	public void sendEmail(String title, String content, String receive) {
		try {
			InternetAddress from = new InternetAddress(
					MimeUtility.encodeWord("系统") + " <" + MailUtil.sender_username + ">");
			this.message.setFrom(from);

			InternetAddress to = new InternetAddress(receive);
			this.message.setRecipient(Message.RecipientType.TO, to);

			this.message.setSubject(title);

			this.message.setContent(content, "text/html;charset=UTF-8");

			this.message.saveChanges();

			this.transport = this.session.getTransport("smtp");

			this.transport.connect(MailUtil.mailHost, MailUtil.sender_username, MailUtil.sender_password);

			this.transport.sendMessage(this.message, this.message.getAllRecipients());
		} catch (Exception e) {
			LOGGER.error("send mail error.", e);
		} finally {
			if (this.transport != null)
				try {
					this.transport.close();
				} catch (MessagingException e) {
					LOGGER.error("send mail close transport error.", e);
				}
		}
	}

	public void sendEmail(String title, String content, String receive, File attachment) {
		try {
			InternetAddress from = new InternetAddress(MailUtil.sender_username);
			this.message.setFrom(from);

			InternetAddress to = new InternetAddress(receive);
			this.message.setRecipient(Message.RecipientType.TO, to);

			this.message.setSubject(title);

			Multipart multipart = new MimeMultipart();

			BodyPart contentPart = new MimeBodyPart();
			contentPart.setContent(content, "text/html;charset=UTF-8");
			multipart.addBodyPart(contentPart);

			if (attachment != null) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment);
				attachmentBodyPart.setDataHandler(new DataHandler(source));

				attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
				multipart.addBodyPart(attachmentBodyPart);
			}

			this.message.setContent(multipart);

			this.message.saveChanges();

			this.transport = this.session.getTransport("smtp");

			this.transport.connect(MailUtil.mailHost, MailUtil.sender_username, MailUtil.sender_password);

			this.transport.sendMessage(this.message, this.message.getAllRecipients());
		} catch (Exception e) {
			LOGGER.error("send mail with attach error.", e);
		} finally {
			if (this.transport != null)
				try {
					this.transport.close();
				} catch (MessagingException e) {
					LOGGER.error("send mail with attach close transport error.", e);
				}
		}
	}
}
