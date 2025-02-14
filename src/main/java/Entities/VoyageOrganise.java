package Entities;

import java.util.*;

public class VoyageOrganise extends Offre {

    private List<String> itineraires;
    private Date dateDepart;
    private List<String> pointsIneret;
    private boolean guideDisponible;
    private int NBPlacDisponible;

    public int getNBPlacDisponible() {
        return NBPlacDisponible;
    }

    public void setNBPlacDisponible(int NBPlacDisponible) {
        this.NBPlacDisponible = NBPlacDisponible;
    }

    public List<String> getItineraires() {
        return itineraires;
    }

    public void setItineraires(List<String> itineraires) {
        this.itineraires = itineraires;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public List<String> getPointsIneret() {
        return pointsIneret;
    }

    public void setPointsIneret(List<String> pointsIneret) {
        this.pointsIneret = pointsIneret;
    }

    public boolean isGuideDisponible() {
        return guideDisponible;
    }

    public void setGuideDisponible(boolean guideDisponible) {
        this.guideDisponible = guideDisponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoyageOrganise that)) return false;
        return guideDisponible == that.guideDisponible && Objects.equals(itineraires, that.itineraires) && Objects.equals(dateDepart, that.dateDepart) && Objects.equals(pointsIneret, that.pointsIneret)&& Objects.equals(NBPlacDisponible,that.NBPlacDisponible);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itineraires, dateDepart, pointsIneret, guideDisponible,NBPlacDisponible);
    }

    @Override
    public String toString() {
        return "VoyageOrganise{" +
                "Description=" + getDescription() +
                ",Tarif=" + getTarif() +
                ",itineraires=" + itineraires +
                ", dateDepart=" + dateDepart +
                ", pointsIneret=" + pointsIneret +
                ", guideDisponible=" + guideDisponible +
                ", Nombre de place disponible=" + NBPlacDisponible +

                '}';
    }
    public String getItinerairesAsString() {
        if (itineraires == null || itineraires.isEmpty()) {
            return "";
        }
        return String.join("/", itineraires); // Convertir la liste en une chaîne séparée par "/"
    }

    public void setItinerairesFromString(String itinerairesString) {
        if (itinerairesString == null || itinerairesString.isEmpty()) {
            this.itineraires = new ArrayList<>();
        } else {
            this.itineraires = Arrays.asList(itinerairesString.split("/")); // Convertir la chaîne en liste
        }
    }
}
