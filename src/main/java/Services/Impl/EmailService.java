package Services.Impl;

import Services.IEmailService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService implements IEmailService {

    @Override
    public void sendEmail(String recipientEmail, String subject, String messageBody) {
        // Configuration du serveur SMTP de Gmail
        String host = "smtp.gmail.com";
        final String senderEmail = "testtravel546@gmail.com";
        final String appPassword = "jszi snsw wdcv buvz";

        // Propriétés de la connexion
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Ajout de cette ligne

        // Authentification avec le mot de passe d'application
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            // Envoi de l'email
            Transport.send(message);
            System.out.println("Email envoyé avec succès à : " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}

