package org.example.travelagency;

import Entities.Utilisateur;
import Services.Impl.UtilisateurServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
    public void setClientID(int IDClient){
        this.IDClient =IDClient ;
        LoadData();
    }
    public void LoadData(){

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


    private void openWindow(String fxml, String title, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
