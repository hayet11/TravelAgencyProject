package Entities;
//subclass
public class SejourHotel extends Offre {
    private String nomHotel;
    private int nbrChambresDispo;
    private String adresse;
    private int nbrEtoiles;
    private double tarifParNuit;
    private double noteMoyenne;

    // Default constructor
    public SejourHotel() {
    }

    // Parameterized constructor
    public SejourHotel(int id, String description, float tarif, String nomHotel, int nbrChambresDispo, String adresse, int nbrEtoiles, double tarifParNuit, double noteMoyenne) {
        super(id, description, tarif);
        this.nomHotel = nomHotel;
        this.nbrChambresDispo = nbrChambresDispo;
        this.adresse = adresse;
        this.nbrEtoiles = nbrEtoiles;
        this.tarifParNuit = tarifParNuit;
        this.noteMoyenne = noteMoyenne;
    }

    // Getters and setters
    public String getNomHotel() {
        return nomHotel;
    }

    public void setNomHotel(String nomHotel) {
        this.nomHotel = nomHotel;
    }

    public int getNbrChambresDispo() {
        return nbrChambresDispo;
    }

    public void setNbrChambresDispo(int nbrChambresDispo) {
        this.nbrChambresDispo = nbrChambresDispo;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getNbrEtoiles() {
        return nbrEtoiles;
    }

    public void setNbrEtoiles(int nbrEtoiles) {
        this.nbrEtoiles = nbrEtoiles;
    }

    public double getTarifParNuit() {
        return tarifParNuit;
    }

    public void setTarifParNuit(double tarifParNuit) {
        this.tarifParNuit = tarifParNuit;
    }

    public double getNoteMoyenne() {
        return noteMoyenne;
    }

    public void setNoteMoyenne(double noteMoyenne) {
        this.noteMoyenne = noteMoyenne;
    }

    // toString method
    @Override
    public String toString() {
        return "SejourHotel{" +
                "nomHotel='" + nomHotel + '\'' +
                ", nbrChambresDispo=" + nbrChambresDispo +
                ", adresse='" + adresse + '\'' +
                ", nbrEtoiles=" + nbrEtoiles +
                ", tarifParNuit=" + tarifParNuit +
                ", noteMoyenne=" + noteMoyenne +
                '}';
    }


}
