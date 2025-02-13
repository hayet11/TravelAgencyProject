package org.example.travelagency;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Services.Impl.AgentServiceImpl;
import Services.Impl.VolServicesImpl;
import Services.Impl.VoyageOrganiseImpl;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class WelcomePageController {

    @FXML
    private Label Name;
    @FXML
    private TableView<StatsRow> statsTable;
    @FXML
    private TableColumn<StatsRow, String> categoryColumn;
    @FXML
    private TableColumn<StatsRow, Float> todayColumn;
    @FXML
    private TableColumn<StatsRow, Float> generalColumn;
    @FXML
    private TableColumn<StatsRow, String> sumColumn;
    @FXML
    private Label totalSumLabel;

    private final AgentServiceImpl agentService = new AgentServiceImpl();
    private final VolServicesImpl volService = new VolServicesImpl();
    private final VoyageOrganiseImpl voyageService = new VoyageOrganiseImpl();

    public void initialize() throws SQLException {
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        todayColumn.setCellValueFactory(new PropertyValueFactory<>("today"));
        generalColumn.setCellValueFactory(new PropertyValueFactory<>("general"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        loadData();
    }

    private void loadData() throws SQLException {
        ObservableList<StatsRow> data = FXCollections.observableArrayList();
        float sumGeneral = voyageService.SumOrganisedTrip() + volService.SumFlights() + agentService.StatsSumStay();

        data.add(new StatsRow("Voyages réservés", agentService.StatsBookingToDay(), agentService.StatsBookingGeneral(), String.valueOf(voyageService.SumOrganisedTrip())));
        data.add(new StatsRow("Vols actifs",volService.StatsFlightToDay(), volService.StatsFlightGneral(), String.valueOf(volService.SumFlights())));
        data.add(new StatsRow("Séjours réservés", voyageService.StatsOrganisedTripToDay(), voyageService.StatsOrganisedTripGeneral(), String.valueOf(agentService.StatsSumStay())));

        statsTable.setItems(data);
        totalSumLabel.setText(String.format("%.2f TND", sumGeneral));
    }

    public void setUserName(String userName) {
        Name.setText(userName);
    }

    public static class StatsRow {
        private final String category;
        private final Integer today;
        private final Integer general;
        private final String sum;

        public StatsRow(String category, Integer today, Integer general, String sum) {
            this.category = category;
            this.today = today;
            this.general = general;
            this.sum = sum;
        }

        public String getCategory() { return category; }
        public Integer getToday() { return today; }
        public Integer getGeneral() { return general; }
        public String getSum() { return sum; }
    }

    public void AddTrip() throws IOException {
        openWindow("AddTripForm.fxml", "Ajout d'un voyage", 744, 500);
    }

    public void FlightList() throws IOException {
        openWindow("ListFlight.fxml", "Liste des vols", 1250, 400);
    }

    public void AddFlight() throws IOException {
        openWindow("AddFlight.fxml", "Ajouter un vol", 1250, 400);
    }

    public void GoListTrip() throws IOException {
        openWindow("BookingList.fxml", "Liste des voyages", 800, 400);
    }

    private void openWindow(String fxml, String title, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void changerVue(ActionEvent event) {
        try {
            if (event.getSource() instanceof MenuItem) {
                MenuItem item = (MenuItem) event.getSource();
                String fxmlFile = (String) item.getUserData(); // Correction ici
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent newView = loader.load();

                AnchorPane rootPane = (AnchorPane) statsTable.getScene().getRoot();
                rootPane.getChildren().setAll(newView);
            } else {
                System.out.println("L'élément source n'est pas un MenuItem mais un " + event.getSource().getClass());
            }
        } catch (IOException e) {
            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page", "Une erreur est survenue lors du chargement de la page.");
        }
    }
}
