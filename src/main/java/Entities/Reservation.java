package Entities;

import enums.TypeOffre;

import java.util.Date;

public class Reservation {
    private int id;
    private Date date;                // Date de la réservation
    private int nbreParticipant;      // Nombre de participants
    private String modePaiement;      // Mode de paiement (ex: carte bancaire, espèces, etc.)
    private int clientId;
    private int offreId;
    private TypeOffre typeOffre;
    private int NbrNuitee;



    // Constructeur non paramétré
    public Reservation() {}

    public Reservation(int id,Date date, int nbreParticipant, String modePaiement, int clientId, int offreId, TypeOffre typeOffre) {
        this.date = date;
        this.id = id;
        this.nbreParticipant = nbreParticipant;
        this.modePaiement = modePaiement;
        this.clientId = clientId;
        this.offreId = offreId;
        this.typeOffre = typeOffre;
    }

    // Constructeur paramétré
    public Reservation(int id, Date date, int nbreParticipant, String modePaiement) {
        this.id = id;
        this.date = date;
        this.nbreParticipant = nbreParticipant;
        this.modePaiement = modePaiement;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNbreParticipant() {
        return nbreParticipant;
    }

    public void setNbreParticipant(int nbreParticipant) {
        this.nbreParticipant = nbreParticipant;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    // toString() pour afficher les informations de la réservation
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", date=" + date +
                ", nbreParticipant=" + nbreParticipant +
                ", modePaiement='" + modePaiement + '\'' +
                '}';
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getOffreId() {
        return offreId;
    }

    public void setOffreId(int offreId) {
        this.offreId = offreId;
    }

    public TypeOffre getTypeOffre() {
        return typeOffre;
    }

    public void setTypeOffre(TypeOffre typeOffre) {
        this.typeOffre = typeOffre;
    }

    public int getNbrNuitee() {
        return NbrNuitee;
    }

    public void setNbrNuitee(int nbrNuitee) {
        NbrNuitee = nbrNuitee;
    }
}
