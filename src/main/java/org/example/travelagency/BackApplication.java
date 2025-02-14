package org.example.travelagency;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BackApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/travelagency/ListReclamationBack.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root, 1250, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Back Office");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}