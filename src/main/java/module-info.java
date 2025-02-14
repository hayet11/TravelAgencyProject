module org.example.travelagency {
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires org.controlsfx.controls;
    requires java.desktop; // Inclut déjà java.datatransfer, donc pas besoin de l'ajouter séparément
    requires kernel;
    requires layout;
    opens Entities to javafx.base;
    opens org.example.travelagency to javafx.fxml;
    exports org.example.travelagency;
    exports Entities;
    exports enums;
}
