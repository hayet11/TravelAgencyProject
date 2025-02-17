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

public class ConsulterSejourClientController {
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
    private Button Reserver;

    @FXML
    private Button Retour;

    private SejourHotelServiceImpl SejourService ;

    private int IdSejour;

    @FXML
    public void initialize(int ID){
        SejourService = new SejourHotelServiceImpl();
        IdSejour=ID;
        Reserver.setOnAction(event -> {
            try {
                RedirectionReservation(IdSejour);
            } catch ( SQLException|IOException e) {
                throw new RuntimeException(e);
            }
        });
        Retour.setOnAction(event -> {
            Stage stage1 = (Stage) nomHotelLabel.getScene().getWindow();
            stage1.close();
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

    private void RedirectionReservation(int idSejour) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReserverSejour.fxml"));
        Parent root = loader.load();

        ReservationSejourHotelController  controller = loader.getController();
        controller.initialize(idSejour);

        Stage stage = new Stage();
        stage.setTitle("Reserver Séjour Hôtel");
        stage.setScene(new Scene(root));
        stage.show();
        Stage stage1 = (Stage) tarifParNuitLabel.getScene().getWindow();
        stage1.close();

    }

}
