package org.example.travelagency;

import Entities.Utilisateur;
import Services.Impl.UtilisateurServiceImpl;
import enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.io.IOException;

import static enums.Role.CLIENT;

public class SignInController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private UtilisateurServiceImpl utilisateurService = new UtilisateurServiceImpl();

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = loginField.getText().trim();
        String password = passwordField.getText();

        // Vérification des champs vides
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !", Alert.AlertType.ERROR);
            return;
        }
        // Vérification des identifiants
        UtilisateurServiceImpl utilisateurService = new UtilisateurServiceImpl();
        Utilisateur utilisateur = utilisateurService.seConnecter(email, password);
        if (utilisateur != null) {
//            showAlert("Succès", "Connexion réussie !", Alert.AlertType.INFORMATION);
            redirigerUtilisateur(utilisateur);
        } else {
            showAlert("Erreur", "Email ou mot de passe incorrect !", Alert.AlertType.ERROR);
        }

    }

    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirigerUtilisateur(Utilisateur utilisateur) {
        String fxmlFile = "";

        switch (utilisateur.getRole()) {
            case AGENT:
                fxmlFile = "WelcomePage.fxml";  // Remplace par le fichier FXML de l'interface Agent
                break;
            case CLIENT:
                fxmlFile = "WelcomeClinet.fxml";
                break;
            case SUPPORT_TECH:
                fxmlFile = "listeAgents.fxml";  // Remplace par le fichier FXML de l'interface Support Technique
                break;
            default:
                System.out.println("Aucun rôle défini !");
                return;  // Si aucun rôle défini, on ne redirige pas
        }

        // Charge l'interface correspondante

            try {

                if (utilisateur.getRole()== CLIENT){
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Parent root = fxmlLoader.load();
                    WelcomeCLientController controller = fxmlLoader.getController();
                    controller.setClientID(utilisateur.getId());
                    Scene scene = new Scene(root);
                    System.out.println("ID dans login"+utilisateur.getId());
                    Stage stage = new Stage();
                    stage.setTitle("Login");
                    stage.setScene(scene);
                    stage.show();
                }
                else{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = (Stage) loginButton.getScene().getWindow(); // Utilise l'élément de la scène actuelle pour récupérer le stage
                    stage.setScene(scene);
                    stage.setTitle("Interface " + utilisateur.getRole());  // Définit le titre de la fenêtre selon le rôle
                    stage.show();
                }

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de charger l'interface de l'utilisateur.", Alert.AlertType.ERROR);
            }


    }

    @FXML
    private void handleForgotPassword() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ForgotPassword.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = (Stage) loginButton.getScene().getWindow(); // Tu peux utiliser un autre élément de la scène actuelle pour récupérer le stage
            stage.setScene(scene);
            stage.setTitle("Mot de passe oublié");
            stage.setWidth(1280);  // Largeur de la fenêtre
            stage.setHeight(800);  // Hauteur de la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateNewAccount(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = (Stage) loginButton.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(scene);
            stage.setTitle("Créer un nouveau compte");
            stage.setWidth(1280);  // Largeur de la fenêtre
            stage.setHeight(800);  // Hauteur de la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
