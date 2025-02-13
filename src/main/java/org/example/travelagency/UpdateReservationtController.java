package org.example.travelagency;


import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import Entities.Reservation;
import Services.Impl.ReservationServiceImpl;
import Services.Impl.VolServicesImpl;
import enums.TypeVol;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

public class UpdateReservationtController {
	
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
	
    private Reservation reservation ;
    
    private enums.ModePaiement ModePaiement;
    
    
    public void initialize() {
	    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
	    participantsSpinner.setValueFactory(valueFactory);
	}
    
    
    
    public void initData(Reservation reservation) {
		this.reservation = reservation;
		
		Date dateArrivee = (Date) reservation.getDate();
		System.out.println(dateArrivee);
		LocalDate localDate = dateArrivee.toLocalDate(); 
		dateArriveePicker.setValue(localDate);
		participantsSpinner.getValueFactory().setValue(reservation.getNbrParticipants());
		
		if ( reservation.getModePaiement().toString().equals("EN_LIGNE")) {
			EnligneCheckBox.setSelected(true) ;		
			SurPlaceCheckBox.setSelected(false);				
		}
		else {
			SurPlaceCheckBox.setSelected(true);	
			EnligneCheckBox.setSelected(false) ;		

		}
    }
    @FXML
    private void save() {
    	try {
    		System.out.println(dateArriveePicker.getValue());
			reservation.setDate( Date.valueOf(dateArriveePicker.getValue()));
			reservation.setNbrParticipants(participantsSpinner.getValue());
			reservation.setModePaiement(null);
			
			if (EnligneCheckBox.isSelected()) {
				reservation.setModePaiement(ModePaiement.EN_LIGNE);
			} else {
				reservation.setModePaiement(ModePaiement.SUR_PLACE);
			}
			ReservationServiceImpl reservationupdate = new ReservationServiceImpl() ;
			reservationupdate.update(reservation);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ListReservationHistoriques.fxml"));
			Scene scene = new Scene(loader.load());
			Stage stage = (Stage) delete.getScene().getWindow();
			stage.setScene(scene); 

		} catch (Exception e) {
			System.out.println("Erreur lors de la mise à jour !");
			e.printStackTrace();
		}
    	
    	
    	
    	
    }
    
    @FXML
    private void cancel() throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("FlightClient.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) delete.getScene().getWindow();
        stage.setScene(scene);    
        }

}
