package org.example.travelagency;

import Entities.*;
import Services.Impl.SejourHotelServiceImpl;
import enums.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReservationSejourHotelController {

    @FXML
    private Label nomHotelLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label nbrEtoilesLabel;

    @FXML
    private Label tarifParNuitLabel;

    @FXML
    private Spinner<Integer> nbrParticipantsSpinner;

    @FXML
    private Spinner<Integer> nbrNuiteesSpinner;

    @FXML
    private DatePicker dateArriveePicker;

    @FXML
    private ComboBox<String> modePaiementComboBox;

    @FXML
    private Button confirmerButton;

    @FXML
    private Button annulerButton;

    private int IdSejour;

    private SejourHotelServiceImpl SejourService;

    @FXML
    public void initialize(int ID) throws SQLException {

        IdSejour=ID;

        confirmerButton.setOnAction(event -> {
            try {
                ConfirmerRes();
            } catch (SQLException|IOException e) {
//                throw new RuntimeException(e);
            }
        });
        annulerButton.setOnAction(event ->AnuulerRes());

        SejourService = new SejourHotelServiceImpl();
        try{
            SejourHotel sejour = SejourService.getById(IdSejour);
            nomHotelLabel.setText(sejour.getNomHotel());
            adresseLabel.setText(sejour.getAdresse());
            nbrEtoilesLabel.setText(String.valueOf(sejour.getNbrEtoiles()));
            tarifParNuitLabel.setText(String.valueOf(sejour.getTarifParNuit()));

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        nbrParticipantsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
        nbrNuiteesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        // Initialiser la liste déroulante des modes de paiement
        modePaiementComboBox.getItems().addAll("En ligne", "Sur place");
        modePaiementComboBox.setValue("En ligne"); // Valeur par défaut
    }


    private void ConfirmerRes() throws IOException, SQLException {
        if(isValidDate()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ValiderReservationSerjour.fxml"));
            Parent root = loader.load();
            Reservation reservation = new Reservation();
            reservation.setClientId(5);
            reservation.setOffreId(IdSejour);
            reservation.setTypeOffre(TypeOffre.SEJOUR_HOTEL);
            reservation.setNbreParticipant(nbrParticipantsSpinner.getValue());
//            reservation.set(nbrNuiteesSpinner.getValue());
            Date date = new Date(dateArriveePicker.getValue().getYear(),dateArriveePicker.getValue().getMonthValue(),dateArriveePicker.getValue().getDayOfMonth());
            reservation.setDate(date);
            if(modePaiementComboBox.getValue().equals("En ligne"))
                reservation.setModePaiement("En_ligne");
            else
                reservation.setModePaiement("Sur_Place");


            VerificationReservationSejourController  controller = loader.getController();
            controller.initialize(reservation); // Méthode pour charger les données

            Stage stage = new Stage();
            stage.setTitle("Validation de la reservation");
            stage.setScene(new Scene(root));
            stage.show();
        }


    }

    private void AnuulerRes(){
        Stage stage = (Stage) tarifParNuitLabel.getScene().getWindow();
        stage.close();
    }

    public boolean isValidDate() {
        LocalDate selectedDate = dateArriveePicker.getValue();
        if (selectedDate == null) {
            showErrorAlert("Veuillez sélectionner une date d'arrivée !");
            return false;
        }
        if (selectedDate.isBefore(LocalDate.now())) {
            showErrorAlert("La date d'arrivée doit être aujourd'hui ou plus tard !");
            return false;
        }
        return true;
    }

    private void showErrorAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Erreur de validation");
        errorAlert.setHeaderText("Saisie invalide");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
