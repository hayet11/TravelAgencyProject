package org.example.travelagency;

import Entities.SejourHotel;
import Services.Impl.SejourHotelServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ConsulterSejourHotelController {
    @FXML
    private Label nomHotelLabel;

    @FXML
    private Label nbrChambresDispoLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label nbrEtoilesLabel;

    @FXML
    private Label tarifParNuitLabel ;

    @FXML
    private Label descriptionLabel ;

    @FXML
    private Button Modifier;

    @FXML
    private Button Suprimmer;

    private SejourHotelServiceImpl SejourService ;

    private int IdSejour;

    @FXML
    public void initialize(int ID){
        SejourService = new SejourHotelServiceImpl();
        IdSejour=ID;
        Modifier.setOnAction(event -> {
            try {
                RedirectionModification(IdSejour);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Suprimmer.setOnAction(event -> {
            try {
                RedirectionSuppression(IdSejour);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        SejourHotel sejour =new SejourHotel();
        try{
            sejour = SejourService.getById(IdSejour);

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        nomHotelLabel.setText(sejour.getNomHotel());
        adresseLabel.setText(sejour.getAdresse());
        descriptionLabel.setText(sejour.getDescription());
        nbrChambresDispoLabel.setText(Integer.toString(sejour.getNbrChambresDispo()));
        tarifParNuitLabel.setText(Double.toString(sejour.getTarifParNuit()));
        nbrEtoilesLabel.setText(Integer.toString(sejour.getNbrEtoiles()));

    }

    private void RedirectionModification(int idSejour) throws  IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifierSejourHotel.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur et passer l'ID
        ModifierSejourHotelController  controller = loader.getController();
        controller.initialize(idSejour); // Méthode pour charger les données

        // Ouvrir la nouvelle scène
        Stage stage = new Stage();
        stage.setTitle("Modifier détails du Séjour Hôtel");
        stage.setScene(new Scene(root));
        stage.show();
        Stage stage1 = (Stage) tarifParNuitLabel.getScene().getWindow();
        stage1.close();

    }

    private void RedirectionSuppression(int idSejour) throws  IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteSejourHotel.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur et passer l'ID
        DeleteSejourHotelController  controller = loader.getController();
        controller.initialize(idSejour); // Méthode pour charger les données

        // Ouvrir la nouvelle scène
        Stage stage = new Stage();
        stage.setTitle("Retirer un Séjour Hôtel");
        stage.setScene(new Scene(root));
        stage.show();
        Stage stage1 = (Stage) tarifParNuitLabel.getScene().getWindow();
        stage1.close();

    }

 


}
