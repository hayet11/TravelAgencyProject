package org.example.travelagency;

import Entities.Vol;
import Services.Impl.VolServicesImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableColumn<Vol, String> colDescription;
    @FXML
    private TableColumn<Vol, Float> colTarif;
    @FXML
    private TableColumn<Vol, String> colType;
    @FXML
    private TableColumn<Vol, String> colCompagnie;
    @FXML
    private TableColumn<Vol, String> colDepart;
    @FXML
    private TableColumn<Vol, String> colArrivee;
    @FXML
    private TableColumn<Vol, String> colHeureDepart;
    @FXML
    private TableColumn<Vol, String> colHeureArrivee;
    @FXML
    private TableColumn<Vol, Void> colAction;
    @FXML
    private Button ajouterbutton;
    @FXML
    private Button searchButton ;
    @FXML
    private TextField idarptdp ;
    public void initialize() {
        try {
            setupTable();
            addActionButtonsToTable();
            loadData();
        } catch (SQLException ex) {
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
                editButton.setStyle("-fx-background-color: #023047; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    Vol vol = getTableView().getItems().get(getIndex());
                    onEdit(vol);
                });

                deleteButton.setOnAction(event -> {
                    Vol vol = getTableView().getItems().get(getIndex());
                    onDelete(vol);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void loadData() throws SQLException {

        VolServicesImpl voyageOrganiseImpl = new VolServicesImpl();
        List<Vol> voyages = voyageOrganiseImpl.getAll();
        ObservableList<Vol> observableVoyages = FXCollections.observableArrayList(voyages);
        tableView.setItems(observableVoyages);
    }

    private void onEdit(Vol vol) {
        System.out.println("Modifier vol: " + vol);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateFlight.fxml"));
            Parent root = loader.load();

            UpdateFlightController updateController = loader.getController();
            updateController.initData(vol);

            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setTitle("Modifier Vol");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onAddButtonClick() {
        try {
            System.out.println("ok");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddFlight.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ajouterbutton.getScene().getWindow();
            stage.setTitle("Formulaire Ajout Vol");
            stage.setScene(scene);
        } catch (Exception e) {
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
                VolServicesImpl voyageOrganiseImpl = new VolServicesImpl();
                try {
                    voyageOrganiseImpl.supprimer(vol);
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
    private void search () {

        try {
            if (idarptdp.getText().equals("")) {
                initialize() ;
            }
            else {
                afficherVols(idarptdp.getText());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la recherche des vols.");
        }


    }

    public void afficherVols(String aeroportArrivee) throws SQLException {
        List<Vol> vols = new VolServicesImpl().getByAeroportArrivee(aeroportArrivee);
        ObservableList<Vol> data = FXCollections.observableArrayList(vols);
        tableView.setItems(data);
        setupTable() ;
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
