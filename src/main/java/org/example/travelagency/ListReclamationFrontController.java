package org.example.travelagency;

import Entities.Reclamation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ListReclamationFrontController {

    @FXML
    private TableView<Reclamation> reclamationTable;
    @FXML
    private TableColumn<Reclamation, String> emailColumn;
    @FXML
    private TableColumn<Reclamation, String> typeColumn;
    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;
    @FXML
    private TableColumn<Reclamation, String> etatColumn;
    @FXML
    private TableColumn<Reclamation, String> actionColumn;
    @FXML
    private Button trierButton;

    private String connectedUserEmail;
    private service.ReclamationServiceImpl reclamationService;

    public ListReclamationFrontController() {
        reclamationService = new service.ReclamationServiceImpl();
    }

    @FXML
    public void initialize() {
        initializeTable();
        // Do not call loadReclamations() here
    }

    private void initializeTable() {
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeReclamation().toString()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        etatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat()));

        typeColumn.setSortable(true);
        descriptionColumn.setSortable(true);
        etatColumn.setSortable(true);

        actionColumn.setCellFactory(param -> new TableCell<Reclamation, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    HBox hbox = new HBox(5); // Container for buttons
                    hbox.setAlignment(Pos.CENTER);

                    if ("En cours".equals(reclamation.getEtat())) {
                        Button modifyButton = new Button("Modifier");
                        modifyButton.setOnAction(event -> {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/travelagency/UpdateReclamationFront.fxml"));
                                Parent root = loader.load();
                                UpdateReclamationFrontController controller = loader.getController();
                                controller.setReclamationToUpdate(reclamation);
                                Stage stage = (Stage) modifyButton.getScene().getWindow();
                                stage.setScene(new Scene(root));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        hbox.getChildren().add(modifyButton);
                    }

                    if ("Traité".equals(reclamation.getEtat())) {
                        Button voirDetailButton = new Button("Voir Détail");
                        voirDetailButton.setOnAction(event -> {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/travelagency/DetailReclamationFront.fxml"));
                                Parent root = loader.load();
                                DetailReclamationFrontController controller = loader.getController();
                                controller.setReclamation(reclamation);
                                Stage stage = (Stage) voirDetailButton.getScene().getWindow();
                                stage.setScene(new Scene(root));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        hbox.getChildren().add(voirDetailButton);
                    }

                    setGraphic(hbox);
                }
            }
        });
    }

    public void setConnectedUserEmail(String email) {
        this.connectedUserEmail = email;
        System.out.println("Connected user email set to: " + email); // Debug log
        loadReclamations(); // Load reclamations after setting the email
    }

    private void loadReclamations() {
        try {
            List<Reclamation> reclamationsList = reclamationService.getAll();
            ObservableList<Reclamation> observableReclamations = FXCollections.observableArrayList(
                    reclamationsList.stream()
                            .filter(reclamation -> reclamation.getEmail().equals(connectedUserEmail))
                            .collect(Collectors.toList())
            );
            reclamationTable.setItems(observableReclamations);
            System.out.println("Loaded " + observableReclamations.size() + " reclamations for email: " + connectedUserEmail); // Debug log
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void reloadReclamations() {
        loadReclamations();
    }

    @FXML
    private void onAjouterReclamationButtonClicked() {
        loadScene("/org/example/travelagency/AddReclamationFront.fxml");
    }

    @FXML
    private void onVosReclamationsButtonClicked() {
        loadScene("/org/example/travelagency/ListReclamationFront.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) reclamationTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}