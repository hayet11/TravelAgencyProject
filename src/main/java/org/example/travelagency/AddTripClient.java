package org.example.travelagency;

import Entities.Reservation;
import Entities.VoyageOrganise;
import Services.Impl.ReservationServiceImpl;
import Services.Impl.VoyageOrganiseImpl;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import enums.ModePaiement;
import enums.TypeOffre;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddTripClient {

    @FXML
    private TextField numPlacesField;
    @FXML
    private Button increaseButton, decreaseButton, validateButton, cancelButton;

    private int numPlaces = 1; // Valeur initiale
    private VoyageOrganise voyage;
    private String login;

    public void setVoyage(VoyageOrganise voyage) {
        this.voyage = voyage;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @FXML
    public void initialize() {
        numPlacesField.setText(String.valueOf(numPlaces));

        increaseButton.setOnAction(event -> increasePlaces());
        decreaseButton.setOnAction(event -> decreasePlaces());
        validateButton.setOnAction(event -> validateSelection());
        cancelButton.setOnAction(event -> closeWindow());
    }

    private void increasePlaces() {
        numPlaces++;
        numPlacesField.setText(String.valueOf(numPlaces));
    }

    private void decreasePlaces() {
        if (numPlaces > 1) {
            numPlaces--;
            numPlacesField.setText(String.valueOf(numPlaces));
        }
    }

    private void validateSelection() {
        if (voyage == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun voyage sélectionné.");
            return;
        }

        // Vérification du nombre de places saisies
        int placesReservees;
        try {
            placesReservees = Integer.parseInt(numPlacesField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un nombre valide de places.");
            return;
        }

        if (voyage.getNBPlacDisponible() < placesReservees) {
            showAlert(Alert.AlertType.WARNING, "Réservation impossible",
                    "Il ne reste que " + voyage.getNBPlacDisponible() + " places disponibles.");
            return;
        }


        // Mise à jour du nombre de places disponibles
        voyage.setNBPlacDisponible(voyage.getNBPlacDisponible() - placesReservees);
        VoyageOrganiseImpl voyageOrganiseImpl = new VoyageOrganiseImpl();

        try {
            voyageOrganiseImpl.update(voyage); // Mise à jour en base de données
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de mettre à jour le voyage.");
            return;
        }


       // Création de la réservation
        Reservation reservation = new Reservation(
                voyage.getId(),
                voyage.getDateDepart(),
               Integer.parseInt(numPlacesField.getText()),
                "Sur place",
                1, // Utilisation du login
                voyage.getId(),
                TypeOffre.VoyageOrganise
        );

        // Ajout de la réservation en base de données
        ReservationServiceImpl implementation = new ReservationServiceImpl();
        try {
            implementation.ajouter(reservation);

//            // Chargement de la nouvelle scène
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookingList.fxml"));
//            Scene scene = new Scene(loader.load());
//            Stage stage = (Stage) delete.getScene().getWindow();
//            stage.setScene(scene);
//            stage.setTitle("Liste des Voyages");
//            stage.show();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de sauvegarder la réservation.");
            return;
        }

        // Génération du PDF
        generatePDF(voyage, placesReservees);
    }


    private void generatePDF(VoyageOrganise voyage, int places) {
        String fileName = "Reservation_Voyage_" + voyage.getItineraires() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdf);

            document.add(new Paragraph("Réservation de Voyage")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            document.add(new LineSeparator(new SolidLine()));

            document.add(new Paragraph("Itinéraire: " + voyage.getItineraires())
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(10));

            document.add(new Paragraph("Description: " + voyage.getDescription())
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph("Date de départ: " + voyage.getDateDepart())
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph("Tarif: " + voyage.getTarif() * places + " TND")
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontColor(Color.BLUE));

            document.add(new Paragraph("Guide disponible: " + (voyage.isGuideDisponible() ? "Oui" : "Non"))
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontColor(Color.DARK_GRAY));

            document.add(new LineSeparator(new SolidLine()));

            document.add(new Paragraph("\nVotre réservation a bien été prise en compte.")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20));

            document.add(new Paragraph("Merci pour votre confiance !")
                    .setFontSize(12)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10));

            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Réservation réussie", "Un PDF de votre réservation a été généré.");

            File pdfFile = new File(fileName);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de générer le PDF.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
