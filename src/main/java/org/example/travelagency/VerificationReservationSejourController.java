package org.example.travelagency;

import Entities.Reservation;
import Entities.SejourHotel;
import Services.Impl.ReservationServiceImpl;
import Services.Impl.SejourHotelServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class VerificationReservationSejourController {

    @FXML
    private Label nomHotelLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label nbrEtoilesLabel;

    @FXML
    private Label tarifParNuitLabel;

    @FXML
    private Label nbrParticipantsLabel;

    @FXML
    private Label dateArriveeLabel;

    @FXML
    private Label nbrNuiteesLabel;

    @FXML
    private Label modePaiementLabel;

    @FXML
    private Label prixTotalLabel;

    @FXML
    private Button confirmerButton;

    @FXML
    private Button retourButton;

    private Reservation reservation;



    public void initialize(Reservation res) throws SQLException {
        confirmerButton.setOnAction(event -> {
            try {
                reserver();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        retourButton.setOnAction(event ->{
            Stage stage = (Stage) adresseLabel.getScene().getWindow();
            stage.close();
        });
        SejourHotelServiceImpl sejourService = new SejourHotelServiceImpl();
        reservation = res;
        SejourHotel sejour = sejourService.getById(reservation.getOffreId());
        nomHotelLabel.setText(sejour.getNomHotel());
        adresseLabel.setText(sejour.getAdresse());
        tarifParNuitLabel.setText(String.valueOf(sejour.getTarifParNuit()));
        nbrEtoilesLabel.setText(String.valueOf(sejour.getNbrEtoiles()));
        nbrParticipantsLabel.setText(String.valueOf(reservation.getNbreParticipant()));
        nbrNuiteesLabel.setText(String.valueOf(reservation.getNbrNuitee()));
        prixTotalLabel.setText(String.valueOf(sejour.getTarifParNuit()* reservation.getNbrNuitee()* reservation.getNbreParticipant()));
        modePaiementLabel.setText(String.valueOf(reservation.getModePaiement()));
        dateArriveeLabel.setText(String.valueOf(reservation.getDate()));

    }

    private void reserver() throws SQLException {
        ReservationServiceImpl reservationService = new ReservationServiceImpl();
        reservationService.ajouter(reservation);
        showSuccessAlert("Reservation ajoutée avec succées !!");
        Stage stage = (Stage) adresseLabel.getScene().getWindow();
        stage.close();
    }

    private void showSuccessAlert(String message) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Succès");
        successAlert.setHeaderText(null);
        successAlert.setContentText(message);
        successAlert.showAndWait();
    }
}
