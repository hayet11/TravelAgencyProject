package org.example.travelagency;
import Services.Impl.EmailService;
import Services.Impl.UtilisateurServiceImpl;
import Utils.DataSource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class ForgotPasswordController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField codeField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    private UtilisateurServiceImpl utilisateurService = new UtilisateurServiceImpl();
    private String verificationCode;
    private String userEmail;

    @FXML
    private void sendVerificationCode() {
        userEmail = emailField.getText().trim();
        if (userEmail.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer votre email.");
            return;
        }
        /*UtilisateurServiceImpl utilisateurService = new UtilisateurServiceImpl();
        if (!UtilisateurServiceImpl.emailExiste(email)) {
            showAlert("Erreur", "Cet email n'existe pas.");
            return;
        }*/

        verificationCode = generateVerificationCode();
        EmailService emailService = new EmailService();
        emailService.sendEmail(userEmail, "Code de vérification", "Votre code de vérification est : " + verificationCode);

        showAlert("Succès", "Un code de vérification a été envoyé à votre email.");
    }

    @FXML
    private void resetPassword() {
        String enteredCode = codeField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!enteredCode.equals(verificationCode)) {
            showAlert("Erreur", "Le code de vérification est incorrect.");
            return;
        }

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un nouveau mot de passe.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }

        updatePassword(userEmail, newPassword);

        showAlert("Succès", "Votre mot de passe a été réinitialisé avec succès.");
        backToLogin();
    }
    /*
    public void resetPassword(String email) {
    String newPassword = generateRandomPassword(); // Générer un mot de passe temporaire
    String hashedPassword = hashPassword(newPassword); // Hacher le mot de passe avant stockage

    String sql = "UPDATE users SET password = ? WHERE email = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, hashedPassword);
        stmt.setString(2, email);
        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected > 0) {
            emailService.sendEmail(email, "Réinitialisation du mot de passe",
                "Votre nouveau mot de passe est : " + newPassword);
            System.out.println("Mot de passe mis à jour et email envoyé !");
        } else {
            System.out.println("Aucun compte trouvé avec cet email !");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


     */

    @FXML
    private void backToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random rand = new Random();
        return String.format("%06d", rand.nextInt(1000000));
    }

    private void sendEmail(String recipient, String code) {
        final String username = "testtravel546@gmail.com"; // Remplace par ton email
        final String password = "jszi snsw wdcv buvz"; // Remplace par ton mot de passe

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Code de réinitialisation de mot de passe");
            message.setText("Votre code de vérification est : " + code);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void updatePassword(String email, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Ouverture de la connexion
            conn = DataSource.getInstance().getConn();
            if (conn == null || conn.isClosed()) {
                throw new SQLException("La connexion à la base de données a échoué.");
            }

            // Préparation de la requête
            stmt = conn.prepareStatement(query);
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connexion ouverte.");
            } else {
                System.out.println("Connexion fermée.");
            }
            // Exécution de la requête
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Affiche l'erreur pour le debug
        } finally {
            // Fermer la connexion et la déclaration
            try {
                if (stmt != null) stmt.close();
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
