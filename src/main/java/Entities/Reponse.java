package Entities;
import java.util.Objects;

public class Reponse {
    private int id;
    private String content;
    private Reclamation reclamation; // Optional back-reference to Reclamation

    // Default constructor
    public Reponse() {}

    // Parameterized constructor
    public Reponse(int id, String content) {
        this.id = id;
        this.content = content;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Reclamation getReclamation() {
        return reclamation; // Getter for reclamation
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation; // Setter for reclamation
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", reclamationId=" + (reclamation != null ? reclamation.getId() : "No reclamation") + // Optionally show associated reclamation
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reponse reponse = (Reponse) o;
        return id == reponse.id &&
                Objects.equals(content, reponse.content) &&
                Objects.equals(reclamation, reponse.reclamation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, reclamation);
    }
}
