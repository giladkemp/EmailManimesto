package com.example.emailmanifesto.Clients;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.util.BASE64EncoderStream;

public class GmailSMTPClient {
	private Session session;

	public static final String MAIL_SERVER = "smtp.gmail.com";
	public static final int PORT = 587;

	public static final GmailSMTPClient instance = new GmailSMTPClient();

	private GmailSMTPClient() {

	}

	private SMTPTransport connectToSMTP(String host, int port,
			String userEmail, String oauthToken, boolean debug)
			throws Exception {

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.sasl.enable", "false");
		session = Session.getInstance(props);
		session.setDebug(debug);

		final URLName unusedUrlName = null;
		SMTPTransport transport = new SMTPTransport(session, unusedUrlName);
		// null password forces OAUTH2 login
		transport.connect(host, port, userEmail, null);

		byte[] response = String.format("user=%s\1auth=Bearer %s\1\1",
				userEmail, oauthToken).getBytes();
		response = BASE64EncoderStream.encode(response);

		transport.issueCommand("AUTH XOAUTH2 " + new String(response), 235);

		return transport;
	}

	public synchronized void sendMail(String subject, String body, String user,
			String oauthToken, String recipients) throws Exception {
		SMTPTransport smtpTransport = connectToSMTP(MAIL_SERVER, PORT, user,
				oauthToken, true);

		MimeMessage message = new MimeMessage(session);
		DataHandler handler = new DataHandler(new ByteArrayDataSource(
				body.getBytes(), "text/plain"));
		message.setSender(new InternetAddress(user));
		message.setSubject(subject);
		message.setDataHandler(handler);

		//if more than one recipient, parse it as such
		if (recipients.indexOf(',') > 0)
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipients));
		else
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(
					recipients));
		smtpTransport.sendMessage(message, message.getAllRecipients());
	}
}
