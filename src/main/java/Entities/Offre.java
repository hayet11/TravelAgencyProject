package Entities;

import java.util.List;

public abstract class Offre {
    List<Partner> partners;
    private int id;
    private String description;
    private float tarif;

    // Default constructor
    public Offre() {
    }

    // Parameterized constructor
    public Offre(int id, String description, float tarif) {
        this.id = id;
        this.description = description;
        this.tarif = tarif;
    }

    // Getters and setters
    public List<Partner> getPartners() {
        return partners;
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTarif() {
        return tarif;
    }

    public void setTarif(float tarif) {
        this.tarif = tarif;
    }

    // toString method
    @Override
    public String toString() {
        return "Offre{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", tarif=" + tarif +
                '}';
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offre offre = (Offre) o;
        return id == offre.id;
    }
}

