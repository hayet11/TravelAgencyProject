package org.example.travelagency;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SuccessPopupController {

    @FXML
    private Button suivantButton;

    private Stage currentStage; // Store the current stage

    // Method to set the current stage
    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    @FXML
    private void onSuivantButtonClicked() {
        // Close the pop-up
        Stage popupStage = (Stage) suivantButton.getScene().getWindow();
        popupStage.close();

        // Redirect to ListReclamationFront in the same window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/travelagency/ListReclamationFront.fxml"));
            Parent root = loader.load();

            // Set the new scene in the current stage
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Liste des Réclamations");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}