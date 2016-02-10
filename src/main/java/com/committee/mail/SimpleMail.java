package com.committee.mail;

import java.security.Security;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/*
 * Безопасность и вход/Ненадежные приложения заблокированы - нужно разрешить доступ ненадежным приложениям
 */
import org.apache.log4j.Logger;
import com.committee.model.dao.AccessDAO;

public class SimpleMail {
	private String mailhost = "smtp.gmail.com";
	private String login;
	private String password;
	private String smtpPort = "465";

	// Log4j logger
	private static final Logger log = Logger.getLogger(SimpleMail.class);

	public synchronized boolean sendMail(final String login,
			final String password, String subject, String body, String sender,
			String recipients) throws Exception {
		this.login = login;
		this.password = password;

		boolean s = false;
		try {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", mailhost);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.quitwait", "false");

			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(login, password);
						}
					});

			MimeMessage message = new MimeMessage(session);
			message.setSender(new InternetAddress(sender));
			// String from_addr;
			// StringBuilder header = "From: %s\n" % from_addr;
			// header += "To: %s\n" % ",".join(to_addr_list);
			// header += "Cc: %s\n"% ",".join(cc_addr_list);
			// header += "Subject: %s\n\n" % subject;
			// message = header + message;
			message.setSubject(subject);
			message.setContent(body, "text/plain");
			if (recipients.indexOf(',') > 0) // через запятую указываем получателей
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipients));
			else
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(
						recipients));

			session.setDebug(true);
			Transport transport = session.getTransport("smtps");
			transport.connect(mailhost, Integer.valueOf(smtpPort), login, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			s = true;
		}
		catch (Exception e) {
			log.error("Message didn't send! " + "\nSubject: " + subject + "\nBody: "
					+ body + "\nSender: " + sender + "\nRecipients: " + recipients);
		}

		return s;
	}

}