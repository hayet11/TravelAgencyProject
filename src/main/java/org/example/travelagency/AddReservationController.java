package org.example.travelagency;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.CheckBox;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import Entities.Reservation;
import Entities.Vol;
import Services.Impl.ReservationServiceImpl;
import Services.Impl.VolServicesImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class AddReservationController {
	
	@FXML
	private DatePicker dateArriveePicker ;
	
	@FXML
	private CheckBox EnligneCheckBox;
	
	@FXML
	private CheckBox SurPlaceCheckBox ;
	
	@FXML
	private Spinner<Integer> participantsSpinner ;
	
	@FXML
	private Button delete ;
	
    private enums.ModePaiement ModePaiement ;
    
    private Vol vol ;

	//private Utilisateur user ;

	@FXML
	//paramatre methode initData Vol vol Utilisateur user
	public void initData(Vol vol ) {
		this.vol = vol ;
		//this.user=user ;
	}
	
	public void initialize() {
	    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
	    participantsSpinner.setValueFactory(valueFactory);
	}
	
	@FXML
	private void onSave() throws IOException {
		
		if (!EnligneCheckBox.isSelected() && !SurPlaceCheckBox.isSelected()  ) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs obligatoires.");
            return;
        }
		
		if (EnligneCheckBox.isSelected()) {
			ModePaiement = ModePaiement.EN_LIGNE ;
		}
		else {
			ModePaiement = ModePaiement.SUR_PLACE ;
		}
		Reservation reservation = new Reservation(
			Date.valueOf(dateArriveePicker.getValue()),
			participantsSpinner.getValue() ,
			ModePaiement,
			//cclientId ===== user.getId() ;
			7,
			vol.getId(),
			enums.TypeOffre.VOL
		) ;
		ReservationServiceImpl implementaion = new ReservationServiceImpl() ;
		try {
			implementaion.ajouter(reservation);
	        showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation a été enregistré avec succès.");
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("FlightClient.fxml"));
	        Scene scene = new Scene(loader.load());
	        Stage stage = (Stage) delete.getScene().getWindow();
	        stage.setScene(scene);
	        stage.setTitle("Liste des Vols");
	        stage.show();
		} catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'enregistrement.");	
            e.printStackTrace();
		}			
	}
       
	private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
	@FXML
	private void onCancel() throws IOException {
		
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("FlightClient.fxml"));
	        Scene scene = new Scene(loader.load());
	        Stage stage = (Stage) delete.getScene().getWindow();
	        stage.setScene(scene);
	        
	}	

}