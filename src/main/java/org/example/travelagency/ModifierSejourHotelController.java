package org.example.travelagency;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import Entities.SejourHotel;
import Services.Impl.SejourHotelServiceImpl;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierSejourHotelController {
    @FXML
    private TextField nomHotelField;
    @FXML
    private TextField nbrChambresDispoField;
    @FXML
    private TextField adresseField;
    @FXML
    private Spinner<Integer> nbrEtoilesSpinner;
    @FXML
    private TextField tarifParNuitField;
    @FXML
    private TextField descriptionField;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;

    private SejourHotelServiceImpl SejourService ;

    private int IdSejour;

    @FXML
    public void initialize(int ID){

        IdSejour = ID;
        saveButton.setOnAction(event -> handleSaveButton());
        backButton.setOnAction(event-> {
            try {
                RedirectionConsultation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        SejourService = new SejourHotelServiceImpl();
        SejourHotel sejour =new SejourHotel();
        try{
            sejour = SejourService.getById(IdSejour);

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        nomHotelField.setText(sejour.getNomHotel());
        adresseField.setText(sejour.getAdresse());
        descriptionField.setText(sejour.getDescription());
        nbrChambresDispoField.setText(Integer.toString(sejour.getNbrChambresDispo()));
        tarifParNuitField.setText(Float.toString(sejour.getTarif()));
        nbrEtoilesSpinner.getValueFactory().setValue(sejour.getNbrEtoiles());

    }

    private void handleSaveButton() {
        if (isValidInput()) {
            SejourService = new SejourHotelServiceImpl();
            SejourHotel sejour = new SejourHotel();
            sejour.setId(IdSejour);
            sejour.setAdresse(adresseField.getText());
            sejour.setNbrEtoiles(nbrEtoilesSpinner.getValue());
            sejour.setNomHotel(nomHotelField.getText());
            sejour.setTarifParNuit(Double.parseDouble(tarifParNuitField.getText().trim()));
            sejour.setDescription(descriptionField.getText());
            sejour.setNbrChambresDispo(Integer.parseInt(nbrChambresDispoField.getText().trim()));
            try{
                SejourService.update(sejour);
                RedirectionConsultation();

            } catch (SQLException e){
                System.out.println(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isValidInput() {
        // Vérification du champ "Nom de l'Hôtel"
        String nomHotel = nomHotelField.getText().trim();
        if (nomHotel.isEmpty() || !nomHotel.matches("[a-zA-ZÀ-ÖØ-öø-ÿ ]+")) {
            showErrorAlert("Le nom de l'hôtel doit contenir uniquement des lettres et être obligatoire.");
            return false;
        }

        // Vérification du champ "Nombre de Chambres Disponibles"
        String nbrChambres = nbrChambresDispoField.getText().trim();
        if (nbrChambres.isEmpty() || !nbrChambres.matches("\\d+") || Integer.parseInt(nbrChambres) <= 0) {
            showErrorAlert("Le nombre de chambres disponibles doit être un entier positif.");
            return false;
        }

        // Vérification du champ "Adresse"
        String adresse = adresseField.getText().trim();
        if (adresse.isEmpty() || !adresse.matches("[a-zA-Z0-9À-ÖØ-öø-ÿ, ]+")) {
            showErrorAlert("L'adresse doit être alphanumérique et obligatoire.");
            return false;
        }

        // Vérification du champ "Nombre d'Étoiles"
        int nbrEtoiles = nbrEtoilesSpinner.getValue();
        if (nbrEtoiles < 1 || nbrEtoiles > 5) {
            showErrorAlert("Le nombre d'étoiles doit être compris entre 1 et 5.");
            return false;
        }

        // Vérification du champ "Tarif par Nuit"
        String tarifParNuit = tarifParNuitField.getText().trim();
        if (tarifParNuit.isEmpty() || !tarifParNuit.matches("\\d+(\\.\\d{1,2})?") || Double.parseDouble(tarifParNuit) <= 0) {
            showErrorAlert("Le tarif par nuit doit être un nombre positif avec au maximum deux décimales.");
            return false;
        }

        // Vérification du champ "Description" (facultatif)
        String description = descriptionField.getText().trim();
        if (!description.isEmpty() && !description.matches("[a-zA-ZÀ-ÖØ-öø-ÿ0-9,\\.\\- ]+")) {
            showErrorAlert("La description peut contenir des lettres, chiffres et ponctuation simple.");
            return false;
        }

        return true; // Tout est valide
    }
    private void showErrorAlert(String message) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Erreur de validation");
        errorAlert.setHeaderText("Saisie invalide");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private void RedirectionConsultation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConsuterSejourHotel.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur et passer l'ID
        ConsulterSejourHotelController controller = loader.getController();
        controller.initialize(IdSejour); // Méthode pour charger les données

        // Ouvrir la nouvelle scène
        Stage stage = new Stage();
        stage.setTitle("Détails du Séjour Hôtel");
        stage.setScene(new Scene(root));
        stage.show();
        Stage stage1 = (Stage) nomHotelField.getScene().getWindow();
        stage1.close();
    }



}
