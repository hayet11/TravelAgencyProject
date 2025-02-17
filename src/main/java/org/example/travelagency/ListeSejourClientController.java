package org.example.travelagency;
import Entities.SejourHotel;
import Services.Impl.SejourHotelServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListeSejourClientController {

    @FXML
    private TextField nomHotelField;

    @FXML
    private TextField AdresseField ;
    @FXML
    private TextField tarifMaxField;

    @FXML
    private TableView<SejourHotel> tableView; // Spécifie le type SejourHotel

    @FXML
    private TableColumn<SejourHotel, String> nomHotelCol;

    @FXML
    private TableColumn<SejourHotel, Integer> nbrEtoilesCol;

    @FXML
    private TableColumn<SejourHotel, Double> tarifParNuitCol;

    @FXML
    private TableColumn<SejourHotel, String> adresseCol;

    @FXML
    private TableColumn<SejourHotel, Button> actionCol;

    @FXML
    private Button searchButton ;

    private ObservableList<SejourHotel> sejourHotelList = FXCollections.observableArrayList();

    private SejourHotelServiceImpl SejourService ;
    @FXML
    public void initialize() {


        // Initialisation des colonnes de la TableView
        nomHotelCol.setCellValueFactory(new PropertyValueFactory<>("nomHotel"));
        nbrEtoilesCol.setCellValueFactory(new PropertyValueFactory<>("nbrEtoiles"));
        tarifParNuitCol.setCellValueFactory(new PropertyValueFactory<>("tarifParNuit"));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        SejourService = new SejourHotelServiceImpl();
        searchButton.setOnAction(event -> onSearch());


        // Initialisation de la colonne des actions
        actionCol.setCellFactory(param -> new TableCell<SejourHotel, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button btn = new Button("Consulter");
                    btn.setOnAction(event -> {
                        SejourHotel sejour = getTableView().getItems().get(getIndex());
                        try {
                            RedirectionConsultation(sejour.getId());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Consulter: " + sejour.getNomHotel());
                    });
                    setGraphic(btn);
                }
            }
        });




        // Remplissage de la liste initiale de SejourHotel

        List<SejourHotel> Sejours = new ArrayList<>();
        try {
            Sejours = SejourService.getAll();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        sejourHotelList = FXCollections.observableArrayList(Sejours);
        tableView.setItems(sejourHotelList);

    }


    @FXML
    private void RedirectionConsultation(int idSejour) throws  IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConsultationSejourClient.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur et passer l'ID
        ConsulterSejourClientController controller = loader.getController();
        controller.initialize(idSejour); // Méthode pour charger les données

        // Ouvrir la nouvelle scène
        Stage stage = new Stage();
        stage.setTitle("Détails du Séjour Hôtel");
        stage.setScene(new Scene(root));
        stage.show();

    }
    @FXML
    private void onSearch() {
        // Récupération des valeurs des critères de recherche

        String nomHotel = nomHotelField.getText().toLowerCase();
//        Integer nbrEtoiles = nbrEtoilesSpinner.getValue();
        String adresse = AdresseField.getText().toLowerCase();
        float tarifMax;
        if (tarifMaxField.getText().isEmpty()){
            tarifMax = 0f;
        }
        else{
            tarifMax = Float.parseFloat(tarifMaxField.getText()) ;
        }

        System.out.println(nomHotel +" " +adresse +" " +tarifMax);
        SejourHotel sejour = new SejourHotel();
        sejour.setNomHotel(nomHotel);
        sejour.setAdresse(adresse);
//        sejour.setNbrEtoiles(nbrEtoiles);
        sejour.setTarifParNuit(tarifMax);
        List<SejourHotel> FiltredList = new ArrayList<>();
        try {
            FiltredList = SejourService.getSejourHotelByCriteria(sejour);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        // Mettre à jour la TableView avec la liste filtrée
        tableView.setItems(FXCollections.observableArrayList(FiltredList));

    }
}
