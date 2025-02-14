package org.example.travelagency;

import Entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatReclamationBackController {

    @FXML
    private PieChart pieChart;

    private service.ReclamationServiceImpl reclamationService;

    public StatReclamationBackController() {
        reclamationService = new service.ReclamationServiceImpl();
    }

    @FXML
    public void initialize() {
        loadPieChart();
    }

    private void loadPieChart() {
        try {
            List<Reclamation> reclamationsList = reclamationService.getAll();
            Map<String, Integer> typeCounts = new HashMap<>();

            for (Reclamation reclamation : reclamationsList) {
                String type = reclamation.getTypeReclamation().toString();
                typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
            }

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (Map.Entry<String, Integer> entry : typeCounts.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

            pieChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onListeReclamationsButtonClicked() {
        loadScene("/org/example/travelagency/ListReclamationBack.fxml");
    }

    @FXML
    private void onStatistiquesButtonClicked() {
        loadScene("/org/example/travelagency/StatReclamationBack.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) pieChart.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}