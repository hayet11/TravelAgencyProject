package Entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Aide {
    private final IntegerProperty id;
    private final StringProperty question;
    private final StringProperty reponse;

    public Aide(int id, String question, String reponse) {
        this.id = new SimpleIntegerProperty(id);
        this.question = new SimpleStringProperty(question);
        this.reponse = new SimpleStringProperty(reponse);
    }

    // Getters pour les propriétés
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty questionProperty() {
        return question;
    }

    public StringProperty reponseProperty() {
        return reponse;
    }

    // Getters classiques
    public int getId() {
        return id.get();
    }

    // Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public String getQuestion() {
        return question.get();
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public String getReponse() {
        return reponse.get();
    }

    public void setReponse(String reponse) {
        this.reponse.set(reponse);
    }
}
