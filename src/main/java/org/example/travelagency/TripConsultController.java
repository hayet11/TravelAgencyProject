package org.example.travelagency;

import Entities.VoyageOrganise;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class TripConsultController {

    @FXML
    private Label destinationLabel;
    @FXML
    private Label dateDepartLabel;
    @FXML
    private Label tariffLabel;
    @FXML
    private Label guideLabel;

    @FXML
    private TextArea itinerariesArea;
    @FXML
    private TextArea pointsOfInterestArea;
    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button closeButton;

    private VoyageOrganise voyage;
    public void setVoyage(VoyageOrganise voyage) {
        this.voyage = voyage;
        initialize();
    }
    @FXML
    public void initialize() {
        if (voyage != null) {
            destinationLabel.setText("Destination: " + voyage.getItineraires());
            dateDepartLabel.setText("Date de départ: " + voyage.getDateDepart().toString());
            tariffLabel.setText("Tarif: " + voyage.getTarif());
            guideLabel.setText("Guide disponible: " + (voyage.isGuideDisponible() ? "Oui" : "Non"));

            itinerariesArea.setText(voyage.getItineraires().toString());
            pointsOfInterestArea.setText(voyage.getPointsIneret().toString());
            descriptionArea.setText(voyage.getDescription());
        } else {
            System.err.println("L'objet VoyageOrganise est null dans TripConsultController.");
        }
    }
    public void AddTrip() throws IOException {
        openWindow("AddTripForm.fxml", "Ajout d'un voyage", 744, 500);
    }

    public void FlightList() throws IOException {
        openWindow("ListFlight.fxml", "Liste des vols", 1250, 400);
    }

    public void AddFlight() throws IOException {
        openWindow("AddFlight.fxml", "Ajouter un vol", 1250, 400);
    }

    public void GoListTrip() throws IOException {
        openWindow("BookingList.fxml", "Liste des voyages", 800, 400);
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
