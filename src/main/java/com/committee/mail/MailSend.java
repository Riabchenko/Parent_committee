package com.committee.mail;

public class MailSend {

	public boolean createMessage(String sender, String login, String password,
			String subject, String body, String recipients) {

		SimpleMail mai = new SimpleMail();
		try {
			if (mai.sendMail(login, password, subject, body, sender, recipients))
				return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
