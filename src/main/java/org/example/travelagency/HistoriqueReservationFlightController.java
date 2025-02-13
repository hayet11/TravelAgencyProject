package org.example.travelagency;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import Entities.Reservation;
import Entities.Vol;
import Services.Impl.ReservationServiceImpl;
import Services.Impl.VolServicesImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HistoriqueReservationFlightController {
	@FXML
    private TableView<Reservation> tableView;
    @FXML
    private TableColumn<Reservation, String> colId;
    @FXML
    private TableColumn<Reservation, String> colDate;
    @FXML
    private TableColumn<Reservation, String> colNbrPart;
    @FXML 
    private TableColumn<Reservation, String> colModepaiemnt ;
    @FXML
    private TableColumn<Reservation, Void> colAction;
    @FXML
    private Button RetourButton ;
    public void initialize() {
        try {
            setupTable();
            addActionButtonsToTable();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setupTable() {
    	colId.setCellValueFactory(new PropertyValueFactory<>("ClientId"));
    	colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
    	colNbrPart.setCellValueFactory(new PropertyValueFactory<>("NbrParticipants"));
    	colModepaiemnt.setCellValueFactory(new PropertyValueFactory<>("ModePaiement"));
        
    }

    private void addActionButtonsToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox container = new HBox(5, editButton, deleteButton);

            {
                editButton.setStyle("-fx-background-color: #023047; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    onEdit(reservation);
                });

                deleteButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    onDelete(reservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }
    
    private void onEdit(Reservation reservation) {
    	System.out.println("Modifier reservation: " + reservation);
    	 try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateReservation.fxml"));
             Parent root = loader.load();

             UpdateReservationtController updateController = loader.getController();
             updateController.initData(reservation);

             Stage stage = (Stage) tableView.getScene().getWindow();
             stage.setTitle("Modifier Vol");
             stage.setScene(new Scene(root,400,250));
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    
    
    private void onDelete(Reservation reservation) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer ce vol ?");
        ButtonType buttonYes = new ButtonType("Oui", ButtonType.OK.getButtonData());
        ButtonType buttonNo = new ButtonType("Non", ButtonType.CANCEL.getButtonData());
        confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                ReservationServiceImpl reservationService = new ReservationServiceImpl();
                try {
                	reservationService.supprimer(reservation);
                    tableView.getItems().remove(reservation);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "La Reservation a été supprimé avec succès.");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression du Reservation.");
                    e.printStackTrace();
                }
            }
        });
        
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void retour () throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("FlightClient.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) RetourButton.getScene().getWindow();
        stage.setScene(scene);
    }

    private void loadData() throws SQLException {
    	ReservationServiceImpl reservationimp = new ReservationServiceImpl() ;
    	//ici objet client obligatoire pour recuperer son id dans cette exempel aa123
        List<Reservation> reservations = reservationimp.getAllById(7);
        ObservableList<Reservation> observableReservations = FXCollections.observableArrayList(reservations);
        tableView.setItems(observableReservations);
    }
}
