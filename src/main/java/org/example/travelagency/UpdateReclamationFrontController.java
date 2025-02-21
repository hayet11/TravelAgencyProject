package org.example.travelagency;

import Entities.Reclamation;
import Entities.TypeReclamation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;

public class UpdateReclamationFrontController {

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<TypeReclamation> typeComboBox;

    @FXML
    private TextField etatField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Button quitterButton;

    @FXML
    private Button modifierButton;

    @FXML
    private Button ajouterReclamationButton;

    @FXML
    private Button vosReclamationsButton;

    private Reclamation reclamationToUpdate;

    public void initialize() {
        // Populate the ComboBox with Enum values
        typeComboBox.getItems().setAll(TypeReclamation.values());
    }

    @FXML
    public void onModifierButtonClicked(MouseEvent event) {
        if (validateFields()) {
            String email = emailField.getText();
            TypeReclamation type = typeComboBox.getValue();
            String description = descriptionField.getText();
            String etat = etatField.getText();

            Reclamation updatedReclamation = new Reclamation(reclamationToUpdate.getId(), email, type, description, etat, reclamationToUpdate.getReponse());

            service.ReclamationServiceImpl reclamationService = new service.ReclamationServiceImpl();
            try {
                reclamationService.update(updatedReclamation);
                showSuccessPopup();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        // Validate type
        if (typeComboBox.getValue() == null) {
            showError(typeComboBox, "Champ obligatoire");
            isValid = false;
        } else {
            clearError(typeComboBox);
        }

        // Validate description
        if (descriptionField.getText().isEmpty()) {
            showError(descriptionField, "Champ obligatoire");
            isValid = false;
        } else {
            clearError(descriptionField);
        }

        return isValid;
    }

    private void showError(Control field, String message) {
        field.setStyle("-fx-border-color: red; -fx-background-color: pink;");
        if (field instanceof TextInputControl) {
            ((TextInputControl) field).setPromptText(message);
        } else if (field instanceof ComboBox) {
            ((ComboBox<?>) field).setPromptText(message);
        }
    }

    private void clearError(Control field) {
        field.setStyle("");
        if (field instanceof TextInputControl) {
            ((TextInputControl) field).setPromptText("");
        } else if (field instanceof ComboBox) {
            ((ComboBox<?>) field).setPromptText("");
        }
    }

    private void showSuccessPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/travelagency/SuccessPopup.fxml"));
            Parent root = loader.load();

            // Get the controller
            SuccessPopupController controller = loader.getController();

            // Pass the current stage to the pop-up controller
            Stage currentStage = (Stage) modifierButton.getScene().getWindow(); // Use the appropriate button reference
            controller.setCurrentStage(currentStage);

            // Create a new stage for the pop-up
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 300, 150)); // Set a larger size for the pop-up
            popupStage.setTitle("Succès");

            // Show the pop-up
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void onQuitterButtonClicked(MouseEvent event) {
        loadScene("/org/example/travelagency/ListReclamationFront.fxml");
    }

    @FXML
    public void onAjouterReclamationButtonClicked(MouseEvent event) {
        loadScene("/org/example/travelagency/AddReclamationFront.fxml");
    }

    @FXML
    public void onVosReclamationsButtonClicked(MouseEvent event) {
        loadScene("/org/example/travelagency/ListReclamationFront.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) quitterButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReclamationToUpdate(Reclamation reclamation) {
        this.reclamationToUpdate = reclamation;
        emailField.setText(reclamation.getEmail());
        typeComboBox.setValue(reclamation.getTypeReclamation());
        descriptionField.setText(reclamation.getDescription());
        etatField.setText(reclamation.getEtat());
    }
}