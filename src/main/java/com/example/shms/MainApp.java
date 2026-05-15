package com.example.shms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shms/fxml/PatientView.fxml"));
        Scene scene = new Scene(loader.load(),900,600);
        stage.setTitle("Hospital management system");
        stage.setScene(scene);
        stage.show();
    }
}
