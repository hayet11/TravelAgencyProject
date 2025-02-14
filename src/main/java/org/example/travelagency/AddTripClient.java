package org.example.travelagency;

import Entities.VoyageOrganise;
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
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

public class AddTripClient {

    @FXML
    private TextField numPlacesField;
    @FXML
    private Button increaseButton, decreaseButton, validateButton, cancelButton;

    private int numPlaces = 1; // Valeur initiale
    private VoyageOrganise voyage; // Déclaration de la variable voyage

    public void setVoyage(VoyageOrganise voyage) {
        this.voyage = voyage;
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
        VoyageOrganiseImpl voyageOrganiseImpl = new VoyageOrganiseImpl();
        String fileName = "Reservation_Voyage_" + voyage.getItineraires() + ".pdf";

        try {
            // Créer un writer pour le PDF
            PdfWriter writer = new PdfWriter(fileName);
            // Créer un document PDF
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdf);

            // Titre principal avec mise en forme
            document.add(new Paragraph("Réservation de Voyage")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Ligne de séparation décorative
            document.add(new LineSeparator(new SolidLine()));

            // Informations du voyage réservé
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

            document.add(new Paragraph("Tarif: " + voyage.getTarif()*Integer.parseInt(numPlacesField.getText()) + " TND")
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontColor(Color.BLUE));

            document.add(new Paragraph("Guide disponible: " + (voyage.isGuideDisponible() ? "Oui" : "Non"))
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontColor(Color.DARK_GRAY));

            // Ligne de séparation décorative
            document.add(new LineSeparator(new SolidLine()));

            // Confirmation de la réservation
            document.add(new Paragraph("\nVotre réservation a bien été prise en compte.")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20));

            // Ajouter un message de remerciement
            document.add(new Paragraph("Merci pour votre confiance !")
                    .setFontSize(12)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10));

            // Fermer le document
            document.close();

            // Afficher une alerte pour informer l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Réservation réussie");
            alert.setHeaderText("Votre réservation a été confirmée !");
            alert.setContentText("Un PDF de votre réservation a été généré.");
            alert.showAndWait();

            // Ouvrir le fichier PDF dans le programme par défaut
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                File pdfFile = new File(fileName);
                if (pdfFile.exists()) {
                    desktop.open(pdfFile);  // Ouvre le fichier dans le programme par défaut (navigateur ou lecteur PDF)
                }
            }
        } catch (Exception e) {
            // Gérer les erreurs lors de la création du PDF
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue.");
            alert.setContentText("Impossible de générer le PDF.");
            alert.showAndWait();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
