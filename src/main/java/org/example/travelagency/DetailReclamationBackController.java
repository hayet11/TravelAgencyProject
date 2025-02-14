package org.example.travelagency;

import Entities.Reclamation;
import Entities.Reponse;
import Entities.TypeReclamation;
import service.ReclamationServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;

public class DetailReclamationBackController {

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
    private Button validerButton;
    @FXML
    private Button quitterButton;

    private Reclamation reclamation;



    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        ReclamationServiceImpl reclamationService = new ReclamationServiceImpl();
        try {
            // Retrieve the reclamation with response
            Reclamation reclamationWithReponse = reclamationService.getReclamationWithReponse(reclamation.getId());

            if (reclamationWithReponse != null) {
                // Update fields with data from the retrieved Reclamation object
                emailField.setText(reclamationWithReponse.getEmail());
                typeField.setText(reclamationWithReponse.getTypeReclamation().toString());
                descriptionField.setText(reclamationWithReponse.getDescription());
                etatField.setText(reclamationWithReponse.getEtat());

                // Check for the response
                if ("Traité".equals(reclamationWithReponse.getEtat())) {
                    reponseField.setEditable(false);
                    if (reclamationWithReponse.getReponse() != null) {
                        reponseField.setText(reclamationWithReponse.getReponse().getContent());
                    } else {
                        reponseField.setText("No response available");
                    }
                } else {
                    reponseField.setEditable(true);
                    validerButton.setVisible(true);
                }
            } else {
                System.out.println("Reclamation with ID " + reclamation.getId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onValiderButtonClicked() {
        String reponseText = reponseField.getText();
        if (reponseText.isEmpty()) {
            reponseField.setStyle("-fx-border-color: red; -fx-background-color: pink;");
            return;
        }

        // Create a new response
        Reponse reponse = new Reponse(0, reponseText);
        reclamation.setReponse(reponse);
        reclamation.setEtat("Traité");

        // Update the reclamation in the database
        service.ReclamationServiceImpl reclamationService = new service.ReclamationServiceImpl();
        service.ReponseServiceImpl reponseService = new service.ReponseServiceImpl();

        try {
            // Save the response first
            reponseService.ajouter(reponse);

            // Update the reclamation with the response ID
            reclamationService.update(reclamation);

            // Show success popup
            showSuccessPopup();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onListeReclamationsButtonClicked(ActionEvent event) { // Updated method
        loadScene("/org/example/travelagency/ListReclamationBack.fxml");
    }

    @FXML
    private void onStatistiquesButtonClicked(ActionEvent event) { // Updated method
        loadScene("/org/example/travelagency/StatReclamationBack.fxml");
    }

    @FXML
    private void onQuitterButtonClicked() {
        loadScene("/org/example/travelagency/ListReclamationBack.fxml");
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

    private void showSuccessPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/travelagency/SuccessPopup.fxml"));
            Parent root = loader.load();

            // Get the controller for the popup
            SuccessPopupController controller = loader.getController();

            // Pass the current stage (associated with DetailReclamationBack) to the popup controller
            Stage currentStage = (Stage) validerButton.getScene().getWindow(); // Using the validerButton or any relevant button
            controller.setCurrentStage(currentStage);

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 300, 150)); // Adjust the size as needed
            popupStage.setTitle("Succès");

            // Show the popup
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}