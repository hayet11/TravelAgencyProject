package org.example.travelagency;

import Entities.VoyageOrganise;
import Services.Impl.VoyageOrganiseImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class UpdateTripController {

    @FXML private DatePicker dateDepartPicker;
    @FXML private ComboBox<String> itineraryComboBox;
    @FXML private CheckComboBox<String> pointsOfInterestComboBox;
    @FXML private CheckBox guideCheckBox;
    @FXML private TextField tariffField;
    @FXML private TextField placesDispoField;
    @FXML private TextArea descriptionField;

    private VoyageOrganise voyage;
    private final Map<String, List<String>> pointsOfInterestMap = new HashMap<>();

    public void setVoyage(VoyageOrganise voyage)  {
        this.voyage = voyage;
        LocalDate localDate = LocalDate.parse(voyage.getDateDepart().toString());
        dateDepartPicker.setValue(localDate);

        if (voyage.getItineraires().size() > 0) {
            for (String itineraire : voyage.getItineraires()) {
                itineraryComboBox.getSelectionModel().select(itineraire);
            }
        }

        if (voyage.getPointsIneret().size() > 0) {
            for (String point : voyage.getPointsIneret()) {
                pointsOfInterestComboBox.getCheckModel().check(point);
            }
        }

        guideCheckBox.setSelected(voyage.isGuideDisponible());
        tariffField.setText(String.valueOf(voyage.getTarif()));
        descriptionField.setText(voyage.getDescription());
        placesDispoField.setText(String.valueOf(voyage.getNBPlacDisponible()));
    }

    @FXML
    public void initialize() {
        // Define the itineraries and their associated points of interest
        pointsOfInterestMap.put("Paris", List.of(
                "Tour Eiffel", "Louvre", "Notre-Dame",
                "Musée d'Orsay", "Montmartre", "Champs-Élysées"
        ));
        pointsOfInterestMap.put("Londres", List.of(
                "Big Ben", "London Eye", "Buckingham Palace",
                "Tate Modern", "Westminster Abbey", "Hyde Park"
        ));
        pointsOfInterestMap.put("Rome", List.of(
                "Colisée", "Vatican", "Fontaine de Trevi",
                "Panthéon", "Basilique Saint-Pierre", "Piazza Navona"
        ));
        pointsOfInterestMap.put("Berlin", List.of(
                "Porte de Brandebourg", "Mur de Berlin", "Île aux Musées",
                "Alexanderplatz", "Parc Tiergarten", "Reichstag"
        ));
        pointsOfInterestMap.put("Madrid", List.of(
                "Plaza Mayor", "Musée du Prado", "Parc du Retiro",
                "Palais Royal", "Temple de Debod", "Gran Vía"
        ));

        // Add itineraries to the ComboBox
        itineraryComboBox.getItems().addAll(pointsOfInterestMap.keySet());

        // Add listener to dynamically update points of interest
        itineraryComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            updatePointsOfInterest(newValue);
        });
    }


    private void updatePointsOfInterest(String itinerary) {
        pointsOfInterestComboBox.getItems().clear();
        if (itinerary != null) {
            pointsOfInterestComboBox.getItems().addAll(pointsOfInterestMap.getOrDefault(itinerary, List.of()));
        }
    }

    @FXML
    private void onSave() throws SQLException {
        if (dateDepartPicker.getValue() == null || tariffField.getText().isEmpty() || placesDispoField.getText().isEmpty()) {
            showError("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        voyage.setDateDepart(java.sql.Date.valueOf(dateDepartPicker.getValue()));
        voyage.setItineraires(List.of(itineraryComboBox.getValue()));
        voyage.setPointsIneret(pointsOfInterestComboBox.getCheckModel().getCheckedItems());
        voyage.setGuideDisponible(guideCheckBox.isSelected());

        try {
            voyage.setTarif(Float.parseFloat(tariffField.getText()));
            voyage.setNBPlacDisponible(Integer.parseInt(placesDispoField.getText()));
        } catch (NumberFormatException e) {
            showError("Veuillez entrer des valeurs numériques valides.");
            return;
        }

        voyage.setDescription(descriptionField.getText());
        new VoyageOrganiseImpl().update(voyage);
        showConfirmation("Les informations du voyage ont été mises à jour avec succès.");
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) dateDepartPicker.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
