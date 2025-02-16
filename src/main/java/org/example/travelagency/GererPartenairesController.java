package org.example.travelagency;

import Entities.Partner;
import Services.Impl.PartnerService;
import enums.PartnerCategory;
import enums.PartnerType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GererPartenairesController {

    private final PartnerService partnerService = new PartnerService();
    @FXML
    private TableView<Partner> tablePartenaires;
    @FXML
    private TableColumn<Partner, String> colNom;
    @FXML
    private TableColumn<Partner, String> colCategory;
    @FXML
    private TableColumn<Partner, String> colType;
    @FXML
    private TableColumn<Partner, String> colEmail;
    @FXML
    private TableColumn<Partner, String> colTelephone;
    @FXML
    private TableColumn<Partner, String> colAdresse;
    @FXML
    private TextField txtNom;
    @FXML
    private ComboBox<PartnerCategory> cmbCategory;
    @FXML
    private ComboBox<PartnerType> cmbType;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelephone;
    @FXML
    private TextField txtAdresse;
    @FXML
    private Button btnHistorique;

    private ObservableList<Partner> partenaireList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty().asString());
        colType.setCellValueFactory(cellData -> cellData.getValue().typeProperty().asString());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        colTelephone.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        colAdresse.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        cmbCategory.setItems(FXCollections.observableArrayList(PartnerCategory.values()));
        cmbType.setItems(FXCollections.observableArrayList(PartnerType.values()));
        loadPartenaires();
        // Ajout du listener pour la sélection dans la table
        tablePartenaires.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Remplir les champs avec les données du partenaire sélectionné
                txtNom.setText(newValue.getName());
                cmbCategory.setValue(newValue.getCategory());
                cmbType.setValue(newValue.getType());
                txtEmail.setText(newValue.getEmail());
                txtTelephone.setText(newValue.getPhone());
                txtAdresse.setText(newValue.getAddress());
            }
        });
    }

    private void loadPartenaires() {
        partenaireList.clear();
        try {
            List<Partner> partners = partnerService.getAll();
            partenaireList.addAll(partners);
            tablePartenaires.setItems(partenaireList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterPartenaire() {
        if (!verifierChamps())
            return;
        try {
            Partner partner = new Partner(
                    txtNom.getText(),
                    0,  // ID généré automatiquement
                    cmbCategory.getValue(),
                    cmbType.getValue(),
                    txtEmail.getText(),
                    txtTelephone.getText(),
                    txtAdresse.getText()
            );
            partnerService.ajouter(partner);
            loadPartenaires();
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Ajout réussi", "Le partenaire a été ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Ajout échoué", "Une erreur est survenue lors de l'ajout du partenaire.");
            clearFields();
        }
        clearFields();
    }

    @FXML
    private void modifierPartenaire() {
        if (!verifierChamps())
            return;
        Partner selected = tablePartenaires.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setName(txtNom.getText());
                selected.setCategory(cmbCategory.getValue());
                selected.setType(cmbType.getValue());
                selected.setEmail(txtEmail.getText());
                selected.setPhone(txtTelephone.getText());
                selected.setAddress(txtAdresse.getText());

                partnerService.update(selected);
                loadPartenaires();
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Modification réussie", "Le partenaire a été modifié avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Modification échouée", "Une erreur est survenue lors de la modification du partenaire.");
                clearFields();
            }
        }
        clearFields();
    }

    @FXML
    private void supprimerPartenaire() {
        Partner selected = tablePartenaires.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                partnerService.supprimer(selected);
                loadPartenaires();
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Suppression réussie", "Le partenaire a été supprimé avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Suppression échouée", "Une erreur est survenue lors de la suppression du partenaire.");
                clearFields();
            }
        } else {
            afficherAlerte(Alert.AlertType.WARNING, "Sélection requise", "Aucun partenaire sélectionné", "Veuillez sélectionner un partenaire");
        }
        clearFields();
    }

    @FXML
    private void afficherHistorique() {
        Partner selected = tablePartenaires.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Sélection requise", "Aucun partenaire sélectionné", "Veuillez sélectionner un partenaire pour voir son historique.");
            return;
        }

        try {
            List<String> offres = partnerService.getOffresByPartner(selected.getId());
            afficherHistoriquePopup(selected.getName(), offres);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de récupérer l'historique", "Une erreur est survenue.");
        }
    }

    private void afficherHistoriquePopup(String partnerName, List<String> offres) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Historique des Offres");
        alert.setHeaderText("Offres de " + partnerName);

        if (offres.isEmpty()) {
            alert.setContentText("Aucune offre associée.");
        } else {
            alert.setContentText(String.join("\n", offres));
        }

        alert.showAndWait();
    }


    private void afficherAlerte(Alert.AlertType type, String titre, String header, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    private boolean verifierChamps() {
        if (txtNom.getText().isEmpty() || cmbCategory.getValue() == null || cmbType.getValue() == null
                || txtEmail.getText().isEmpty() || txtTelephone.getText().isEmpty() || txtAdresse.getText().isEmpty()) {

            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Champs manquants", "Veuillez remplir tous les champs obligatoires.");
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtNom.clear();
        txtEmail.clear();
        txtTelephone.clear();
        txtAdresse.clear();
        cmbCategory.setValue(null);
        cmbType.setValue(null);
    }


    @FXML
    private void changerVue(ActionEvent event) {
        try {
            Object source = event.getSource();
            String fxmlFile = null;

            if (source instanceof Hyperlink) {
                Hyperlink hyperlink = (Hyperlink) source;
                fxmlFile = (String) hyperlink.getUserData();
            } else if (source instanceof MenuItem) {
                MenuItem menuItem = (MenuItem) source;
                fxmlFile = (String) menuItem.getUserData();
            }
            if (fxmlFile != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/travelagency/" + fxmlFile));
                Parent newView = loader.load();
                AnchorPane rootPane = (AnchorPane) tablePartenaires.getScene().getRoot();
                rootPane.getChildren().setAll(newView);
            } else {
                afficherAlerte(Alert.AlertType.WARNING, "Navigation", null, "Page non définie");
            }
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Impossible de charger la page", null, "Une erreur est survenue lors du chargement de la page.");
        }
    }


}
