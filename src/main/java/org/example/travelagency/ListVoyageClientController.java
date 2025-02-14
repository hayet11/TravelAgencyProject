package org.example.travelagency;

import Entities.VoyageOrganise;
import Services.Impl.VoyageOrganiseImpl;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.itextpdf.kernel.color.*;

import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ListVoyageClientController {

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
        setupTable();
        addActionButtonsToTable();
        loadData();
    }

    private void setupTable() {
        tableView.setEditable(false);
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
                    private final Button reserveButton = new Button("Réserver");
                    private final Button consultButton = new Button("Consulter");
                    private final HBox actionBox = new HBox(10, reserveButton, consultButton);

                    {
                        reserveButton.setOnAction(event -> {
                            VoyageOrganise voyage = getTableView().getItems().get(getIndex());
                            onReserve(voyage);
                        });

                        consultButton.setOnAction(event -> {
                            VoyageOrganise voyage = getTableView().getItems().get(getIndex());
                            onConsult(voyage);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(actionBox);
                        }
                    }
                };
            }
        });
    }

    private void loadData() {
        try {
            VoyageOrganiseImpl voyageOrganiseImpl = new VoyageOrganiseImpl();
            List<VoyageOrganise> voyages = voyageOrganiseImpl.getAll();
            ObservableList<VoyageOrganise> observableVoyages = FXCollections.observableArrayList(voyages);
            tableView.setItems(observableVoyages);
        } catch (SQLException ex) {
            System.err.println("Impossible de récupérer les voyages : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
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

    @FXML
    private void onSearch() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            loadData();
            tableView.refresh();
        } else {
            filterDataByDate(selectedDate);
        }
        datePicker.setValue(null);
    }

    private void onReserve(VoyageOrganise voyage) {
        try {
            // Charger le fichier FXML pour la page de consultation
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddTripClient.fxml"));
            Parent root = fxmlLoader.load();

//            // Récupérer le contrôleur correct
            AddTripClient controller = fxmlLoader.getController(); // This should be AddTripClient, not TripConsultController
            controller.setVoyage(voyage);  // Passer l'objet voyage à la page de consultation

            // Créer et afficher la nouvelle scène
            Stage stage = new Stage();
            stage.setTitle("Consulter le voyage");
            stage.setScene(new Scene(root, 744, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


//
    }

    private void onConsult(VoyageOrganise voyage) {
        try {
            // Charger le fichier FXML pour la page de consultation
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TripConsult.fxml"));
            Parent root = fxmlLoader.load();

            // Récupérer le contrôleur de la page de consultation
            TripConsultController controller = fxmlLoader.getController();
            controller.setVoyage(voyage);  // Passer l'objet voyage à la page de consultation

            // Créer et afficher la nouvelle scène
            Stage stage = new Stage();
            stage.setTitle("Consulter le voyage");
            stage.setScene(new Scene(root, 744, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
