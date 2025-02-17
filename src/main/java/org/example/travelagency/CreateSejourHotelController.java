package org.example.travelagency;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import Entities.SejourHotel;
import Services.Impl.SejourHotelServiceImpl;
import javafx.stage.Stage;

public class CreateSejourHotelController {

    @FXML
    private TextField nomHotelField;

    @FXML
    private TextField adresseField;

    @FXML
    private TextField tarifParNuitField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Spinner<Integer> nbrChambresSpinner;

    @FXML
    private Spinner<Integer> nbrEtoilesSpinner;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        // Initialisation des valeurs des spinners
        nbrChambresSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        nbrEtoilesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));

        // Action du bouton "Créer"
        createButton.setOnAction(event -> handleCreateButton());

        // Action du bouton "Annuler"
        cancelButton.setOnAction(event -> handleCancelButton());
    }

    private void handleCreateButton() {
        if (isValidInput()) {
            // Création de l'objet SejourHotel
            SejourHotel sejour = new SejourHotel();
            sejour.setNomHotel(nomHotelField.getText().trim());
            sejour.setAdresse(adresseField.getText().trim());
            sejour.setTarifParNuit(Double.parseDouble(tarifParNuitField.getText().trim()));
            sejour.setNbrChambresDispo(nbrChambresSpinner.getValue());
            sejour.setDescription(descriptionField.getText().trim());
            sejour.setNbrEtoiles(nbrEtoilesSpinner.getValue());

            // Appel du service de création
            SejourHotelServiceImpl sejourService = new SejourHotelServiceImpl();
            try {
                sejourService.ajouter(sejour);
                showSuccessAlert("Séjour Hôtel créé avec succès !");
                Stage stage = (Stage) nomHotelField.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                showErrorAlert("Une erreur est survenue lors de la création : " + e.getMessage());
            }
        }
    }

    private void handleCancelButton() {
        // Réinitialiser tous les champs
        nomHotelField.clear();
        adresseField.clear();
        tarifParNuitField.clear();
        descriptionField.clear();
        nbrChambresSpinner.getValueFactory().setValue(1);
        nbrEtoilesSpinner.getValueFactory().setValue(3);
    }

    private boolean isValidInput() {
        // Vérification du champ "Nom de l'Hôtel"
        String nomHotel = nomHotelField.getText().trim();
        if (nomHotel.isEmpty() || !nomHotel.matches("[a-zA-Z ]+")) {
            showErrorAlert("Le nom de l'hôtel doit être alphabétique et obligatoire.");
            return false;
        }

        // Vérification du champ "Adresse"
        String adresse = adresseField.getText().trim();
        if (adresse.isEmpty() || !adresse.matches("[a-zA-Z0-9 ]+")) {
            showErrorAlert("L'adresse doit être alphanumérique sans symboles et obligatoire.");
            return false;
        }

        // Vérification du champ "Tarif par Nuit"
        String tarifParNuit = tarifParNuitField.getText().trim();
        if (tarifParNuit.isEmpty() || !tarifParNuit.matches("\\d+(\\.\\d{1,2})?")) {
            showErrorAlert("Le tarif par nuit doit être numérique avec une partie décimale et obligatoire.");
            return false;
        }

        // Vérification du champ "Description"
        String description = descriptionField.getText().trim();
        if (!description.isEmpty() && !description.matches("[a-zA-Z ]+")) {
            showErrorAlert("La description doit être alphabétique si elle est renseignée.");
            return false;
        }

        return true;
    }

    private void showSuccessAlert(String message) {
        Alert successAlert = new Alert(AlertType.INFORMATION);
        successAlert.setTitle("Succès");
        successAlert.setHeaderText(null);
        successAlert.setContentText(message);
        successAlert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Erreur de validation");
        errorAlert.setHeaderText("Saisie invalide");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
