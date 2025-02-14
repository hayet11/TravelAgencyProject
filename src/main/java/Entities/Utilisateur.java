package Entities;

import enums.Role;
import javafx.beans.property.*;

import java.time.LocalDate;

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String motDePasse;
    private LocalDate dateNaissance;
    private String telephone;
    private String email;
    private Role role; // Ajout du rôle

    //javaFX properties
    private IntegerProperty Id = new SimpleIntegerProperty();
    private StringProperty Nom = new SimpleStringProperty();
    private StringProperty Prenom = new SimpleStringProperty();
    private StringProperty MotDePasse = new SimpleStringProperty();
    private ObjectProperty<LocalDate> DateNaissance = new SimpleObjectProperty<>();
    private StringProperty Telephone = new SimpleStringProperty();
    private StringProperty Email = new SimpleStringProperty();
    private ObjectProperty<Role> Role = new SimpleObjectProperty<>();

    // Constructeur par défaut
    public Utilisateur() {}

    // Constructeur avec paramètres
    public Utilisateur(int id, String nom, String prenom, String motDePasse, LocalDate dateNaissance, String telephone, String email, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.email = email;
        this.role = role;

        this.Id = new SimpleIntegerProperty(id);
        this.Nom = new SimpleStringProperty(nom);
        this.Prenom = new SimpleStringProperty(prenom);
        this.MotDePasse = new SimpleStringProperty(motDePasse);
        this.DateNaissance = new SimpleObjectProperty<>(dateNaissance);
        this.Telephone = new SimpleStringProperty(telephone);
        this.Email = new SimpleStringProperty(email);
        this.Role = new SimpleObjectProperty<>(role);


    }
    public StringProperty telephoneProperty() {
        return Telephone;
    }

    public IntegerProperty idProperty() {
        return Id;
    }

    public StringProperty nomProperty() {
        return Nom;
    }

    public StringProperty prenomProperty() {
        return Prenom;
    }

    public StringProperty motDePasseProperty() {
        return MotDePasse;
    }

    public ObjectProperty<LocalDate> dateNaissanceProperty() {
        return DateNaissance;
    }

    public StringProperty emailProperty() {
        return Email;
    }

    public ObjectProperty<Role> roleProperty() {
        return Role;
    }





    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Méthode toString() pour affichage
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
