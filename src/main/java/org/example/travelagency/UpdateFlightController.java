package org.example.travelagency;

import Entities.Vol;
import Services.Impl.VolServicesImpl;
import enums.TypeVol;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;
import java.io.IOException;
import java.sql.Date;

public class UpdateFlightController {

    private TypeVol typeVol;
    private Vol vol;
    private float tarif;
    private Vol volActuel;
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
    private TextArea descriptionField;
    @FXML
    private CheckBox allerCheckBox;
    @FXML
    private CheckBox retourCheckBox;
    @FXML
    private CheckBox allerRetourCheckBox;
    @FXML
    private TextField tariffField;

    public void initData(Vol vol) {
        this.volActuel = vol;
        descriptionField.setText(vol.getDescription());
        tariffField.setText(String.valueOf(vol.getTarif()));
        compagnieField.setText(vol.getCompagnie());
        aeroportDepartField.setText(vol.getAeroportDepart());
        aeroportArriveeField.setText(vol.getAeroportArrivee());
        heureDepartField.setText(vol.getHeureDepart().toString());
        heureArriveeField.setText(vol.getHeureArrivee().toString());
        TypeVol typeVol = vol.getType();
        if (typeVol != null) {
            switch (typeVol) {
                case aller:
                    allerCheckBox.setSelected(true);
                    break;
                case retour:
                    retourCheckBox.setSelected(true);
                    break;
                case aller_retour:
                    allerRetourCheckBox.setSelected(true);
                    break;
            }
        }
        if (vol.getDateArrivee() != null) {
            Date dateArrivee = (Date) vol.getDateArrivee();
            System.out.println(dateArrivee);
            LocalDate localDate = dateArrivee.toLocalDate(); // Conversion de java.sql.Date en LocalDate
            dateArriveePicker.setValue(localDate);
        }
    }

    @FXML
    private void save() throws SQLException {
        try {
            volActuel.setDescription(descriptionField.getText());
            volActuel.setTarif(Float.parseFloat(tariffField.getText()));
            volActuel.setCompagnie(compagnieField.getText());
            volActuel.setAeroportDepart(aeroportDepartField.getText());
            volActuel.setAeroportArrivee(aeroportArriveeField.getText());
            volActuel.setHeureDepart(java.sql.Time.valueOf(heureDepartField.getText()));
            volActuel.setHeureArrivee(java.sql.Time.valueOf(heureArriveeField.getText()));
            if (allerCheckBox.isSelected()) {
                volActuel.setType(TypeVol.aller);
            } else if (retourCheckBox.isSelected()) {
                volActuel.setType(TypeVol.retour);
            } else {
                volActuel.setType(TypeVol.aller_retour);
            }
            if (dateArriveePicker.getValue() != null) {
                volActuel.setDateArrivee(Date.valueOf(dateArriveePicker.getValue()));
            }
            VolServicesImpl srv = new VolServicesImpl();
            srv.update(volActuel);
            Stage stage = (Stage) dateArriveePicker.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour !");
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel() throws IOException {
        Stage stage = (Stage) dateArriveePicker.getScene().getWindow();
        stage.close();
    }

}