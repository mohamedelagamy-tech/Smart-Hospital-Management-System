package com.example.shms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        navigateTo("login.fxml",800,500);
        stage.setTitle("Hospital Mangement System");
        stage.show();
    }

    public static void navigateTo(String fxmlFile, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/example/shms/fxml/" + fxmlFile));
            Pane root = loader.load();
            Scene scene = new Scene(root,width,height);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Navigation failed: "+e.getMessage());
        }
    }
}