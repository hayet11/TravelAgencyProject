package org.example.travelagency;

import Entities.Vol;
import Services.Impl.VolServicesImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFlightController {

    private enums.TypeVol typeVol;
    private Vol vol;
    private float tarif;
    @FXML
    private Button delete;
    @FXML
    private DatePicker dateArriveePicker;
    @FXML
    private TextField compagnieField;
    @FXML
    private TextField aeroportDepartField;
    @FXML
    private TextField aeroportArriveeField;
    @FXML
    private TextField heureDepartField;
    @FXML
    private TextField heureArriveeField;
    @FXML
    private TextField tariffField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private CheckBox allerCheckBox;
    @FXML
    private CheckBox retourCheckBox;

    @FXML
    private void initialize() {
        vol = new Vol();
    }

    @FXML
    private void onSave() throws SQLException {
        if (compagnieField.getText().isEmpty() || aeroportDepartField.getText().isEmpty()
                || aeroportArriveeField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        Date dateArrivee = java.sql.Date.valueOf(dateArriveePicker.getValue());
        String compagnie = compagnieField.getText();
        String aeroportDepart = aeroportDepartField.getText();
        String aeroportArrivee = aeroportArriveeField.getText();
        String heureDepart = heureDepartField.getText();
        String heureArrivee = heureArriveeField.getText();
        if (allerCheckBox.isSelected() && retourCheckBox.isSelected()) {
            typeVol = typeVol.aller_retour;
        } else if (retourCheckBox.isSelected()) {
            typeVol = typeVol.retour;
        } else if (allerCheckBox.isSelected()) {
            typeVol = typeVol.aller;
        } else {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez sélectionner un type de vol.");
            return;
        }

        try {
            tarif = Float.parseFloat(tariffField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Format incorrect", "Le tarif doit être un nombre valide.");
            return;
        }

        String description = descriptionField.getText();
        vol.setDateArrivee(dateArrivee);
        vol.setCompagnie(compagnie);
        vol.setAeroportDepart(aeroportDepart);
        vol.setAeroportArrivee(aeroportArrivee);
        vol.setTarif(tarif);
        vol.setType(typeVol);
        System.out.println(vol.getType());

        try {
            vol.setHeureDepart(parseTime(heureDepart));
            vol.setHeureArrivee(parseTime(heureArrivee));
        } catch (ParseException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "L'heure doit être au format HH:mm (ex: 14:30).");
            return;
        }

        vol.setDescription(description);
        VolServicesImpl volServices = new VolServicesImpl();
        try {
            volServices.ajouter(vol);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le vol a été enregistré avec succès.");
            Stage stage = (Stage) dateArriveePicker.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'enregistrement.");
        }
    }

    @FXML
    private void onCancel() throws IOException {
        Stage stage = (Stage) dateArriveePicker.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Date parseTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.parse(time);
    }
}