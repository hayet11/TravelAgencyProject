package org.example.travelagency;

import Entities.VoyageOrganise;
import Services.Impl.VoyageOrganiseImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BookingListController {

    @FXML
    private TableView<VoyageOrganise> tableView;

    @FXML
    private TableColumn<VoyageOrganise, Date> dateDepartCol;
    @FXML
    private TableColumn<VoyageOrganise, String> itineraryCol;
    @FXML
    private TableColumn<VoyageOrganise, Boolean> guideCol;
    @FXML
    private TableColumn<VoyageOrganise, Float> tariffCol;
    @FXML
    private TableColumn<VoyageOrganise, String> descriptionCol;
    @FXML
    private TableColumn<VoyageOrganise, Void> actionCol;

    @FXML
    private DatePicker datePicker;

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
        tableView.setEditable(true);
        dateDepartCol.setCellValueFactory(new PropertyValueFactory<>("dateDepart"));
        itineraryCol.setCellValueFactory(new PropertyValueFactory<>("itineraires"));
        guideCol.setCellValueFactory(new PropertyValueFactory<>("guideDisponible"));
        tariffCol.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void addActionButtonsToTable() {
        actionCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<VoyageOrganise, Void> call(final TableColumn<VoyageOrganise, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Modifier");
                    private final Button deleteButton = new Button("Supprimer");

                    {
                        editButton.setOnAction(event -> {
                            VoyageOrganise voyage = getTableView().getItems().get(getIndex());
                            onEdit(voyage);
                        });

                        deleteButton.setOnAction(event -> {
                            VoyageOrganise voyage = getTableView().getItems().get(getIndex());
                            onDelete(voyage);
                        });

                        HBox actionBox = new HBox(10, editButton, deleteButton);
                        setGraphic(actionBox);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(getGraphic());
                        }
                    }
                };
            }
        });
    }

    private void loadData() throws SQLException {
        VoyageOrganiseImpl voyageOrganiseImpl = new VoyageOrganiseImpl();
        List<VoyageOrganise> voyages = voyageOrganiseImpl.getAll();
        ObservableList<VoyageOrganise> observableVoyages = FXCollections.observableArrayList(voyages);
        tableView.setItems(observableVoyages);
    }

    @FXML
    public void onSearch(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            try {
                loadData();
                tableView.refresh();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            filterDataByDate(selectedDate);
        }
        datePicker.setValue(null);
    }

    private void filterDataByDate(LocalDate selectedDate) {
        try {
            VoyageOrganiseImpl voyageOrganiseImpl = new VoyageOrganiseImpl();
            List<VoyageOrganise> filteredVoyages = voyageOrganiseImpl.getByDate(selectedDate);
            ObservableList<VoyageOrganise> filteredData = FXCollections.observableArrayList(filteredVoyages);
            tableView.setItems(filteredData);
            tableView.refresh();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void onDelete(VoyageOrganise voyage) {
        try {
            VoyageOrganiseImpl voyageOrganiseImpl = new VoyageOrganiseImpl();
            voyageOrganiseImpl.supprimer(voyage);
            loadData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Supprission");
            alert.setHeaderText(null);
            alert.setContentText("la supprission c'est fait avec succès !");
            alert.showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void onEdit(VoyageOrganise voyage) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("UpdateFomr.fxml"));
            AnchorPane root = loader.load();

            UpdateTripController modifyFormController = loader.getController();
            modifyFormController.setVoyage(voyage);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Modifier Voyage");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                AnchorPane rootPane = (AnchorPane) tableView.getScene().getRoot();
                rootPane.getChildren().setAll(newView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
