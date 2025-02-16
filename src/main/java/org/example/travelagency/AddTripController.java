package org.example.travelagency;

import Entities.Partner;
import Entities.VoyageOrganise;
import Services.Impl.PartnerService;
import Services.Impl.VoyageOrganiseImpl;
import enums.TypeOffre;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTripController {

    private final Map<String, List<String>> pointsOfInterestMap = new HashMap<>();
    private final PartnerService partnerService = new PartnerService();
    private final List<Partner> partenairesList = partnerService.getAll();
    @FXML
    private DatePicker dateDepartPicker;
    @FXML
    private ComboBox<String> itineraryComboBox;
    @FXML
    private CheckComboBox<String> pointsOfInterestComboBox;
    @FXML
    private CheckComboBox<Partner> partenairesComboBox;
    @FXML
    private TextField tariffField;
    @FXML
    private TextField nbPlaceDisponibleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private CheckBox guideCheckBox;

    public AddTripController() throws SQLException {
    }

    @FXML
    public void initialize() {
        pointsOfInterestMap.put("Paris", List.of("Tour Eiffel", "Louvre", "Notre-Dame", "Musée d'Orsay", "Montmartre", "Champs-Élysées"));
        pointsOfInterestMap.put("Londres", List.of("Big Ben", "London Eye", "Buckingham Palace", "Tate Modern", "Westminster Abbey", "Hyde Park"));
        pointsOfInterestMap.put("Rome", List.of("Colisée", "Vatican", "Fontaine de Trevi", "Panthéon", "Basilique Saint-Pierre", "Piazza Navona"));
        pointsOfInterestMap.put("Berlin", List.of("Porte de Brandebourg", "Mur de Berlin", "Île aux Musées", "Alexanderplatz", "Parc Tiergarten", "Reichstag"));
        pointsOfInterestMap.put("Madrid", List.of("Plaza Mayor", "Musée du Prado", "Parc du Retiro", "Palais Royal", "Temple de Debod", "Gran Vía"));

        itineraryComboBox.getItems().addAll(pointsOfInterestMap.keySet());
        itineraryComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> updatePointsOfInterest(newValue));

        partenairesComboBox.setConverter(new StringConverter<Partner>() {
            @Override
            public String toString(Partner partner) {
                return partner == null ? "" : partner.getName() + " - " + partner.getCategory();
            }

            @Override
            public Partner fromString(String string) {
                return null;
            }
        });

        // Ajout des objets Partner dans le CheckComboBox
        partenairesComboBox.getItems().addAll(partenairesList);
    }

    private void updatePointsOfInterest(String itinerary) {
        pointsOfInterestComboBox.getItems().clear();
        if (itinerary != null) {
            pointsOfInterestComboBox.getItems().addAll(pointsOfInterestMap.getOrDefault(itinerary, List.of()));
        }
    }

    @FXML
    private void onSave() throws SQLException {
        if (dateDepartPicker.getValue() == null || tariffField.getText().isEmpty() || nbPlaceDisponibleField.getText().isEmpty() || itineraryComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs requis.");
            return;
        }

        LocalDate today = LocalDate.now();
        if (dateDepartPicker.getValue().isBefore(today)) {
            showAlert(Alert.AlertType.WARNING, "Date invalide", "La date de départ ne peut pas être antérieure à aujourd'hui.");
            return;
        }

        float tarif;
        try {
            tarif = Float.parseFloat(tariffField.getText());
            if (tarif < 0) {
                showAlert(Alert.AlertType.WARNING, "Valeur incorrecte", "Le tarif doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Format incorrect", "Le tarif doit être un nombre valide.");
            return;
        }

        int nbPlaces;
        try {
            nbPlaces = Integer.parseInt(nbPlaceDisponibleField.getText());
            if (nbPlaces <= 0) {
                showAlert(Alert.AlertType.WARNING, "Valeur incorrecte", "Le nombre de places doit être un entier positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Format incorrect", "Le nombre de places doit être un entier valide.");
            return;
        }

        // Création de l'objet VoyageOrganise
        VoyageOrganise voyage = new VoyageOrganise();
        voyage.setDateDepart(Date.valueOf(dateDepartPicker.getValue()));
        voyage.setTarif(tarif);
        voyage.setNBPlacDisponible(nbPlaces);
        voyage.setItineraires(Collections.singletonList(itineraryComboBox.getValue()));
        voyage.setDescription(descriptionField.getText());
        voyage.setGuideDisponible(guideCheckBox.isSelected());

        // Ajout du voyage via le service
        VoyageOrganiseImpl vo = new VoyageOrganiseImpl();
        int voyageId = vo.ajouterEtRecupererId(voyage);

        // Récupérer la liste des partenaires sélectionnés
        List<Partner> selectedPartners = partenairesComboBox.getCheckModel().getCheckedItems();
        for (Partner partner : selectedPartners) {
            int partnerId = partner.getId();
            partnerService.associatePartnerToOffer(partnerId, voyageId, TypeOffre.VoyageOrganise.toString());
        }
        // Message de confirmation
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Le voyage a été enregistré avec succès !");
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) dateDepartPicker.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
