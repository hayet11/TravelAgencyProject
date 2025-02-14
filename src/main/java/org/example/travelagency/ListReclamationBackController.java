package org.example.travelagency;

import Entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; // Add this import
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListReclamationBackController {

    @FXML
    private TableView<Reclamation> tableView;
    @FXML
    private TableColumn<Reclamation, String> colEmail;
    @FXML
    private TableColumn<Reclamation, String> colType;
    @FXML
    private TableColumn<Reclamation, String> colDescription;
    @FXML
    private TableColumn<Reclamation, String> colEtat;
    @FXML
    private TableColumn<Reclamation, Void> colActions;
    @FXML
    private TextField searchField;

    private service.ReclamationServiceImpl reclamationService;

    public ListReclamationBackController() {
        reclamationService = new service.ReclamationServiceImpl();
    }

    @FXML
    public void initialize() {
        // Bind columns to Reclamation properties
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeReclamation"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        colEmail.setSortable(true);
        colType.setSortable(true);
        colDescription.setSortable(true);
        colEtat.setSortable(true);

        // Add "Voir Détail" button to the "Actions" column
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button voirDetailButton = new Button("Voir Détail");

            {
                // Set button action
                voirDetailButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    openDetailView(reclamation);
                });

                // Center the button
                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(voirDetailButton);
                }
            }
        });

        // Load all reclamations
        loadReclamations();



        // Add dynamic search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterReclamations(newValue);
        });
    }

    @FXML
    private void onListeReclamationsButtonClicked(ActionEvent event) { // Updated method
        loadScene("/org/example/travelagency/ListReclamationBack.fxml");
    }

    @FXML
    private void onStatistiquesButtonClicked(ActionEvent event) { // Updated method
        loadScene("/org/example/travelagency/StatReclamationBack.fxml");
    }

    private void loadReclamations() {
        try {
            List<Reclamation> reclamationsList = reclamationService.getAll();
            ObservableList<Reclamation> observableReclamations = FXCollections.observableArrayList(reclamationsList);
            tableView.setItems(observableReclamations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterReclamations(String searchText) {
        try {
            List<Reclamation> reclamationsList = reclamationService.getAll();
            ObservableList<Reclamation> filteredReclamations = FXCollections.observableArrayList();

            for (Reclamation reclamation : reclamationsList) {
                if (reclamation.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                        reclamation.getTypeReclamation().toString().toLowerCase().contains(searchText.toLowerCase()) ||
                        reclamation.getDescription().toLowerCase().contains(searchText.toLowerCase()) ||
                        reclamation.getEtat().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredReclamations.add(reclamation);
                }
            }

            tableView.setItems(filteredReclamations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openDetailView(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/travelagency/DetailReclamationBack.fxml"));
            Parent root = loader.load();


            // Pass the selected reclamation to the detail controller
            DetailReclamationBackController controller = loader.getController();
            controller.setReclamation(reclamation);

            // Replace the current scene with the detail view
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de la Réclamation");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}