package org.example.travelagency;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import Entities.Vol;
import Services.Impl.VolServicesImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class FlightClientController {
	@FXML
	private TableView<Vol> tableView;
	@FXML
	private TableColumn<Vol, String> colDescription;
	@FXML
	private TableColumn<Vol, Float> colTarif;
	@FXML
	private TableColumn<Vol, String> colType;
	@FXML
	private TableColumn<Vol, String> colCompagnie;
	@FXML
	private TableColumn<Vol, String> colDepart;
	@FXML
	private TableColumn<Vol, String> colArrivee;
	@FXML
	private TableColumn<Vol, String> colHeureDepart;
	@FXML
	private TableColumn<Vol, String> colHeureArrivee;
	@FXML
	private TableColumn<Vol, Void> colAction;
	@FXML
	private Button ajouterbutton;

	@FXML
	private Button HistoriqueButton ;
	@FXML
	private TextField idarptdp ;
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
		colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		colTarif.setCellValueFactory(new PropertyValueFactory<>("tarif"));
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));
		colCompagnie.setCellValueFactory(new PropertyValueFactory<>("compagnie"));
		colDepart.setCellValueFactory(new PropertyValueFactory<>("aeroportDepart"));
		colArrivee.setCellValueFactory(new PropertyValueFactory<>("aeroportArrivee"));
		colHeureDepart.setCellValueFactory(new PropertyValueFactory<>("heureDepart"));
		colHeureArrivee.setCellValueFactory(new PropertyValueFactory<>("heureArrivee"));
	}

	private void addActionButtonsToTable() {
		colAction.setCellFactory(param -> new TableCell<Vol, Void>() {
			private final Button reserver = new Button("Reserver");
			private final HBox container = new HBox(5, reserver);

			{
				reserver.setStyle("-fx-background-color: #023047; -fx-text-fill: white;");

				reserver.setOnAction(event -> {
					Vol vol = getTableView().getItems().get(getIndex());
					Reserver(vol) ;
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null); // Supprime le bouton pour les cellules vides
				} else {
					setGraphic(container); // Ajoute le bouton pour les cellules non vides
				}
			}


		});
	}

	private void loadData() throws SQLException {

		VolServicesImpl voyageOrganiseImpl = new VolServicesImpl();
		List<Vol> voyages = voyageOrganiseImpl.getAll();
		ObservableList<Vol> observableVoyages = FXCollections.observableArrayList(voyages);
		tableView.setItems(observableVoyages);
	}

	@FXML
	private void search () {

		try {
			if (idarptdp.getText().equals("")) {
				initialize() ;
			}
			else {
				afficherVols(idarptdp.getText());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de la recherche des vols.");
		}


	}

	public void afficherVols(String aeroportArrivee) throws SQLException {

		List<Vol> vols = new VolServicesImpl().getByAeroportArrivee(aeroportArrivee);
		ObservableList<Vol> data = FXCollections.observableArrayList(vols);
		tableView.setItems(data);
		setupTable() ;
	}

	@FXML
	private void historique() throws IOException {
		//System.out.println("fi west historique");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ListReservationHistoriques.fxml"));
		Scene scene = new Scene(loader.load());
		Stage stage = (Stage) HistoriqueButton.getScene().getWindow();
		stage.setScene(scene);
	}

	private void Reserver(Vol vol ) {
		System.out.println(vol.toString());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AddReservation.fxml"));
			Parent root = loader.load();
			AddReservationController addController = loader.getController();
			addController.initData(vol);
			Stage stage = (Stage) tableView.getScene().getWindow();
			stage.setTitle("Reserver Vol");
			stage.setScene(new Scene(root,400,250));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
