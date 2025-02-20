package org.example.travelagency;

import Entities.Utilisateur;
import Services.Impl.UtilisateurServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeCLientController {

    @FXML
    private Label nomLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label telephoneLabel;

    @FXML
    private Label prenomLabel;

    @FXML
    private Label DateNaissanceLabel;

    @FXML
    private Button modifierProfilButton;

    @FXML
    private MenuItem goToSejours;

    @FXML
    private MenuItem goToVols;

    @FXML
    private MenuItem goToVoyages;

    @FXML
    private MenuItem goToReservations;

    @FXML
    private MenuItem goToAide;

    private UtilisateurServiceImpl UtilisateurService;

    private int IDClient;

    public void initialize() {
        UtilisateurService = new UtilisateurServiceImpl();

    }

    public void setClientID(int IDClient) {
        this.IDClient = IDClient;
        LoadData();
    }

    public void LoadData() {

        Utilisateur User = UtilisateurService.GetInfos(IDClient);

        nomLabel.setText(User.getNom());
        emailLabel.setText(User.getEmail());
        telephoneLabel.setText(User.getTelephone());
        prenomLabel.setText(User.getPrenom());
        DateNaissanceLabel.setText(String.valueOf(User.getDateNaissance()));


    }

    public void goToSejours() throws IOException {
        openWindow("ListeSejourClient.fxml", "Sejour Hotels", 1250, 400);
    }

    public void goToAide() throws IOException {
        openWindow("AideUser.fxml", "Aide", 1280, 800);
    }

    public void goToVols() throws IOException {
        openWindow("ListFlight.fxml", "Vols", 1280, 800);
    }

    public void goToVoyages() throws IOException {
        openWindow("ListVoyage.fxml", "Voyages", 1280, 800);
    }

    public void goToReservations() throws IOException {
        openWindow("AideUser.fxml", "Aide", 1280, 800);
    }


    private void openWindow(String fxml, String title, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}
