package Entities;

import java.util.Objects;

public class Reclamation {
    private int id;
    private String email;
    private TypeReclamation typeReclamation; // Changed to Enum
    private String description;
    private String etat;
    private Reponse reponse;
    private int reponseId;

    // Default constructor
    public Reclamation() {}

    // Constructor without the Reponse
    public Reclamation(int id, String email, TypeReclamation typeReclamation, String description, String etat) {
        this.id = id;
        this.email = email;
        this.typeReclamation = typeReclamation;
        this.description = description;
        this.etat = etat;
        this.reponse = null;
        this.reponseId = 0;
    }

    // Constructor with the Reponse
    public Reclamation(int id, String email, TypeReclamation typeReclamation, String description, String etat, Reponse reponse) {
        this.id = id;
        this.email = email;
        this.typeReclamation = typeReclamation;
        this.description = description;
        this.etat = etat;
        this.reponse = reponse;
        this.reponseId = reponse != null ? reponse.getId() : 0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TypeReclamation getTypeReclamation() {
        return typeReclamation;
    }

    public void setTypeReclamation(TypeReclamation typeReclamation) {
        this.typeReclamation = typeReclamation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Reponse getReponse() {
        return reponse;
    }

    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
        this.reponseId = (reponse != null) ? reponse.getId() : 0;
    }

    public int getReponseId() {
        return reponseId;
    }

    public void setReponseId(int reponseId) {
        this.reponseId = reponseId;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", typeReclamation=" + typeReclamation +
                ", description='" + description + '\'' +
                ", etat='" + etat + '\'' +
                ", reponseId=" + reponseId +
                ", reponse=" + (reponse != null ? reponse.getId() : "No response yet") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reclamation that = (Reclamation) o;
        return id == that.id &&
                reponseId == that.reponseId &&
                Objects.equals(email, that.email) &&
                typeReclamation == that.typeReclamation &&
                Objects.equals(description, that.description) &&
                Objects.equals(etat, that.etat) &&
                Objects.equals(reponse, that.reponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, typeReclamation, description, etat, reponse, reponseId);
    }
}