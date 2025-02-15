package org.example.travelagency;

import Entities.VoyageOrganise;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

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

}
