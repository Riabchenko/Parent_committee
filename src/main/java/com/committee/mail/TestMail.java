package com.committee.mail; 

import javax.activation.DataHandler; 
import javax.activation.DataSource; 
import javax.activation.FileDataSource; 
import javax.mail.*; 
import javax.mail.internet.*; 
import java.io.UnsupportedEncodingException; 
import java.util.ArrayList; 
import java.util.Properties; 
import java.util.Set;
 
public class TestMail { 
    static final String ENCODING = "UTF-8"; 

    public static void sendSimpleMessage(String login, String password, String from, String to, String content, String subject, String smtpPort, String smtpHost) throws MessagingException, UnsupportedEncodingException { 
        Properties props = System.getProperties(); 
        props.put("mail.smtp.port", smtpPort); 
        props.put("mail.smtp.host", smtpHost); 
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.mime.charset", ENCODING); 
 
        Authenticator auth = new MyAuthenticator(login, password); 
        Session session = Session.getDefaultInstance(props, null); 
 
        Message msg = new MimeMessage(session); 
        msg.setFrom(new InternetAddress(from)); 
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to)); 
        msg.setSubject(subject); 
        msg.setText(content); 
        Transport.send(msg); 
    } 
 
    public static void sendMultiMessage(String login, String password, String from, String to, String content, String subject, String attachment, String smtpPort, String smtpHost) throws MessagingException, UnsupportedEncodingException { 
        Properties props = System.getProperties(); 
        props.put("mail.smtp.port", smtpPort); 
        props.put("mail.smtp.host", smtpHost); 
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.mime.charset", ENCODING); 
 
        Authenticator auth = new MyAuthenticator(login, password); 
        Session session = Session.getDefaultInstance(props, auth); 
        session.setDebug(true);
        
        MimeMessage msg = new MimeMessage(session); 
 
        msg.setFrom(new InternetAddress(from)); 
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to)); 
        msg.setSubject(subject, ENCODING); 
 
        BodyPart messageBodyPart = new MimeBodyPart(); 
        messageBodyPart.setContent(content, "text/plain; charset=" + ENCODING + ""); 
        Multipart multipart = new MimeMultipart(); 
        multipart.addBodyPart(messageBodyPart); 
 
        MimeBodyPart attachmentBodyPart = new MimeBodyPart(); 
        DataSource source = new FileDataSource(attachment); 
        attachmentBodyPart.setDataHandler(new DataHandler(source)); 
        attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName())); 
        multipart.addBodyPart(attachmentBodyPart); 
 
        msg.setContent(multipart); 
 
        Transport transport = session.getTransport("smtps");
        transport.connect(smtpHost, 465, "platmargo", "zaqwsxzaq");
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close(); 
    } 
} 
 
class MyAuthenticator extends Authenticator { 
    private String user; 
    private String password; 
 
    MyAuthenticator(String user, String password) { 
        this.user = user; 
        this.password = password; 
    } 
 
    public PasswordAuthentication getPasswordAuthentication() { 
        String user = this.user; 
        String password = this.password; 
        return new PasswordAuthentication(user, password); 
    } 
    
} 