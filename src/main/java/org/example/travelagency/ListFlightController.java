package org.example.travelagency;

import Entities.Vol;
import Services.Impl.VolServicesImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListFlightController {
    private final VolServicesImpl volService = new VolServicesImpl();

    @FXML
    private TableView<Vol> tableView;
    @FXML
    private TableColumn<Vol, String> colDescription, colType, colCompagnie, colDepart, colArrivee, colHeureDepart, colHeureArrivee;
    @FXML
    private TableColumn<Vol, Float> colTarif;
    @FXML
    private TableColumn<Vol, Void> colAction;
    @FXML
    private Button searchButton;
    @FXML
    private TextField idarptdp;

    public void initialize() {
        try {
            setupTable();
            addActionButtonsToTable();
            loadData();
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'initialisation des vols : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void setupTable() {
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colTarif.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCompagnie.setCellValueFactory(new PropertyValueFactory<>("compagnie"));
        colDepart.setCellValueFactory(new PropertyValueFactory<>("aeroportDepart"));
        colArrivee.setCellValueFactory(new PropertyValueFactory<>("aeroportArrivee"));
        colHeureDepart.setCellValueFactory(new PropertyValueFactory<>("heureDepart"));
        colHeureArrivee.setCellValueFactory(new PropertyValueFactory<>("heureArrivee"));
    }

    private void addActionButtonsToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox container = new HBox(5, editButton, deleteButton);

            {
                styleButton(editButton, "#023047");
                styleButton(deleteButton, "#d32f2f");

                editButton.setOnAction(event -> onEdit(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> onDelete(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void styleButton(Button button, String color) {
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
    }

    private void loadData() throws SQLException {
        List<Vol> vols = volService.getAll();
        tableView.setItems(FXCollections.observableArrayList(vols));
    }

    private void onEdit(Vol vol) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateFlight.fxml"));
            Parent root = loader.load();

            UpdateFlightController updateController = loader.getController();
            updateController.initData(vol);

            Stage newStage = new Stage();
            newStage.setTitle("Modifier Vol");
            newStage.setScene(new Scene(root, 700, 600));
            newStage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page de modification : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void onDelete(Vol vol) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer ce vol ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonType.OK.getButtonData());
        ButtonType buttonNo = new ButtonType("Non", ButtonType.CANCEL.getButtonData());
        confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                try {
                    volService.supprimer(vol);
                    tableView.getItems().remove(vol);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Le vol a été supprimé avec succès.");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression du vol.");
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void search() {
        try {
            if (idarptdp.getText().isEmpty()) {
                loadData();
            } else {
                afficherVols(idarptdp.getText());
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de vols : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void afficherVols(String aeroportArrivee) throws SQLException {
        List<Vol> vols = volService.getByAeroportArrivee(aeroportArrivee);
        tableView.setItems(FXCollections.observableArrayList(vols));
    }

    @FXML
    private void Refresh() {
        try {
            loadData();
            tableView.refresh();
        } catch (SQLException ex) {
            System.err.println("Erreur lors du rafraîchissement des vols : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}