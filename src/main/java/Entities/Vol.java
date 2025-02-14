package Entities;

import enums.TypeVol;

import java.util.Date;
import java.util.Objects;

public class Vol extends Offre{
    private TypeVol type;
    private String compagnieDepart;
    private String aeroportDepart;
    private String aeroportArrivee;
    private Date heureDepart;
    private Date heureArrivee;
    private Date dateArrivee;
    public Vol(){
    }

    public Vol(int id, String description, float tarif, TypeVol type, String compagnieDepart, String aeroportDepart, String aeroportArrivee, Date heureDepart, Date heureArrivee, Date dateArrivee) {
        super(id, description, tarif);
        this.type = type;
        this.compagnieDepart = compagnieDepart;
        this.aeroportDepart = aeroportDepart;
        this.aeroportArrivee = aeroportArrivee;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.dateArrivee = dateArrivee;
    }


    public TypeVol getType() {
        return type;
    }

    public void setType(TypeVol type) {
        this.type = type;
    }

    public String getCompagnie() {
        return compagnieDepart;
    }

    public void setCompagnie(String compagnie) {
        this.compagnieDepart = compagnie;
    }

    public String getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(String aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public String getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(String aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }

    public Date getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(Date heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vol vol)) return false;
        if (!super.equals(o)) return false;
        return  type == vol.type && Objects.equals(compagnieDepart, vol.compagnieDepart) && Objects.equals(aeroportDepart, vol.aeroportDepart) && Objects.equals(aeroportArrivee, vol.aeroportArrivee) && Objects.equals(heureDepart, vol.heureDepart) && Objects.equals(heureArrivee, vol.heureArrivee)&& Objects.equals(dateArrivee, vol.dateArrivee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, compagnieDepart, aeroportDepart, aeroportArrivee, heureDepart, heureArrivee,dateArrivee);
    }

    public String getCompagnieDepart() {
        return compagnieDepart;
    }

    public void setCompagnieDepart(String compagnieDepart) {
        this.compagnieDepart = compagnieDepart;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    @Override
    public String toString() {
        return "Vol{" +
                "id='" + getId() + '\''+
                ", type='" + type + '\''+
                ", compagnie='" + compagnieDepart + '\'' +
                ", aeroportDepart='" + aeroportDepart + '\'' +
                ", aeroportArrivee='" + aeroportArrivee + '\'' +
                ", heureDepart=" + heureDepart +
                ", heureArrivee=" + heureArrivee +
                ", dateArrivee='" + dateArrivee + '\'' +
                '}';
    }
}
