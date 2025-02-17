package org.example.travelagency;

import Entities.SejourHotel;
import Services.Impl.SejourHotelServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteSejourHotelController {
    @FXML
    private Label nomHotelLabel;

    @FXML
    private Label nbrChambresDispoLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label nbrEtoilesLabel;

    @FXML
    private Label tarifParNuitLabel ;

    @FXML
    private Label descriptionLabel ;

    @FXML
    private Button Delete;

    @FXML
    private Button Retour;

    private SejourHotelServiceImpl SejourService ;

    private int IdSejour;

    @FXML
    public void initialize(int ID){
        Retour.setOnAction(event -> {
            try {
                RedirectionConsultation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Delete.setOnAction(event -> handleDeleteButton());
        IdSejour=ID;
        SejourService = new SejourHotelServiceImpl();
        SejourHotel sejour =new SejourHotel();
        try{
            sejour = SejourService.getById(IdSejour);

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        nomHotelLabel.setText(sejour.getNomHotel());
        adresseLabel.setText(sejour.getAdresse());
        descriptionLabel.setText(sejour.getDescription());
        nbrChambresDispoLabel.setText(Integer.toString(sejour.getNbrChambresDispo()));
        tarifParNuitLabel.setText(Float.toString(sejour.getTarif()));
        nbrEtoilesLabel.setText(Integer.toString(sejour.getNbrEtoiles()));

    }

    private void handleDeleteButton() {
        SejourService = new SejourHotelServiceImpl();
        SejourHotel sejour = new SejourHotel();
        sejour.setId(IdSejour);
        try{
            SejourService.supprimer(sejour);
            Stage stage1 = (Stage) nomHotelLabel.getScene().getWindow();
            stage1.close();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private void RedirectionConsultation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConsuterSejourHotel.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur et passer l'ID
        ConsulterSejourHotelController controller = loader.getController();
        controller.initialize(IdSejour); // Méthode pour charger les données

        // Ouvrir la nouvelle scène
        Stage stage = new Stage();
        stage.setTitle("Détails du Séjour Hôtel");
        stage.setScene(new Scene(root));
        stage.show();
        Stage stage1 = (Stage) nomHotelLabel.getScene().getWindow();
        stage1.close();
    }


}
