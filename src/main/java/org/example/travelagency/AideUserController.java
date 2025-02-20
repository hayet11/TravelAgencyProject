package org.example.travelagency;

import Entities.Aide;
import Services.Impl.AideServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class AideUserController {

    private final AideServiceImpl aideService = new AideServiceImpl();
    private final ObservableList<Aide> faqList = FXCollections.observableArrayList();
    @FXML
    private TextField txtRecherche;
    @FXML
    private TableView<Aide> tableFAQ;
    @FXML
    private TableColumn<Aide, String> colQuestion;
    @FXML
    private TableColumn<Aide, String> colReponse;
    @FXML
    private TextField txtNouvelleQuestion;

    @FXML
    public void initialize() throws SQLException {
        colQuestion.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        colReponse.setCellValueFactory(cellData -> cellData.getValue().reponseProperty());
        loadFAQs();
    }

    public void resetPage() throws SQLException {
        txtRecherche.clear();
        txtNouvelleQuestion.clear();
        loadFAQs();
    }

    private void loadFAQs() throws SQLException {
        faqList.setAll(aideService.getAll());
        tableFAQ.setItems(faqList);
    }

    @FXML
    private void rechercherQuestion() {
        String searchText = txtRecherche.getText().toLowerCase();
        if (searchText.isEmpty()) {
            tableFAQ.setItems(faqList);
        } else {
            ObservableList<Aide> filteredList = faqList.filtered(aide ->
                    aide.getQuestion().toLowerCase().contains(searchText)
            );
            tableFAQ.setItems(filteredList);
        }
    }

    @FXML
    private void envoyerQuestion() throws SQLException {
        String question = txtNouvelleQuestion.getText().trim();
        if (!question.isEmpty()) {
            Aide newAide = new Aide(0, question, "En attente");
            aideService.ajouter(newAide);
            loadFAQs();
            txtNouvelleQuestion.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer une question valide.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
                AnchorPane rootPane = (AnchorPane) tableFAQ.getScene().getRoot();
                rootPane.getChildren().setAll(newView);
            } else {
                showAlert(Alert.AlertType.WARNING, "Navigation", "Page non définie");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Impossible de charger la page", "Une erreur est survenue lors du chargement de la page.");
        }
    }
}

