module org.example.travelagency {
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires kernel;
    requires layout;

    opens org.example.travelagency to javafx.fxml;
    exports org.example.travelagency;
    exports Entities;
    exports enums;
}