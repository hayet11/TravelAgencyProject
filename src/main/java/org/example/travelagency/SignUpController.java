package org.example.travelagency;
import Entities.Utilisateur;
import Services.Impl.UtilisateurServiceImpl;
import enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class SignUpController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField telephoneField;
    @FXML private TextField emailField;
    @FXML private DatePicker dobPicker;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private Button signUpButton;

    private final UtilisateurServiceImpl utilisateurService = new UtilisateurServiceImpl();

    @FXML
    public void initialize() {
        // Ajouter les rôles à la liste déroulante
        roleChoiceBox.getItems().addAll("SUPPORT_TECH", "AGENT", "CLIENT");
    }

    @FXML
    public void handleSignUp() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();
        LocalDate dob = dobPicker.getValue();
        String roleStr = roleChoiceBox.getValue();
        System.out.println(email);
        if (nom.isEmpty() || prenom.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                telephone.isEmpty() || email.isEmpty() || roleStr == null || dob == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas !");
            return;
        }

        Role role = Role.valueOf(roleStr);
        Utilisateur utilisateur = new Utilisateur(0, nom, prenom, password, dob, telephone, email, role);

        if (utilisateurService.sInscrire(utilisateur)) {
            showAlert("Succès", "Inscription réussie !");
            clearFields();
            openLoginPage();
        } else {
            showAlert("Erreur", "L'inscription a échoué. Cet email est déjà utilisé.");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        telephoneField.clear();
        emailField.clear();
        roleChoiceBox.setValue(null);
        dobPicker.setValue(null);
    }
    @FXML
    private void openLoginPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) signUpButton.getScene().getWindow();  // Récupère la fenêtre actuelle
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page de connexion.");
        }
    }
}
