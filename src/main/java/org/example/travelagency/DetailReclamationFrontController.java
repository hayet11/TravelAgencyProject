package org.example.travelagency;

import Entities.Reclamation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import service.ReclamationServiceImpl;

import java.io.IOException;
import java.sql.SQLException;

public class DetailReclamationFrontController {

    @FXML
    private TextField emailField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField etatField;
    @FXML
    private TextArea reponseField;
    @FXML
    private Button quitterButton;

    private Reclamation reclamation;
    private ReclamationServiceImpl reclamationService;

    public DetailReclamationFrontController() {
        reclamationService = new ReclamationServiceImpl();
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        try {
            // Fetch the reclamation with its response from the database
            Reclamation reclamationWithReponse = reclamationService.getReclamationWithReponse(reclamation.getId());

            if (reclamationWithReponse != null) {
                // Populate the fields with data from the retrieved Reclamation object
                emailField.setText(reclamationWithReponse.getEmail());
                typeField.setText(reclamationWithReponse.getTypeReclamation().toString());
                descriptionField.setText(reclamationWithReponse.getDescription());
                etatField.setText(reclamationWithReponse.getEtat());

                // Check if the reclamation has a response
                if ("Traité".equals(reclamationWithReponse.getEtat())) {
                    reponseField.setEditable(false);
                    if (reclamationWithReponse.getReponse() != null) {
                        reponseField.setText(reclamationWithReponse.getReponse().getContent());
                    } else {
                        reponseField.setText("No response available");
                    }
                } else {
                    reponseField.setEditable(true);
                }
            } else {
                System.out.println("Reclamation with ID " + reclamation.getId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onQuitterButtonClicked() {
        loadScene("/org/example/travelagency/ListReclamationFront.fxml");
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
            Stage stage = (Stage) quitterButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}