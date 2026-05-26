package com.example.shms;

import com.example.shms.utils.MedicineReminderUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class MainApp extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("Hospital management system");
        navigateTo("login",900,600);
        stage.show();
        MedicineReminderUtil.showReminders();
    }
    public static void navigateTo(String fxmlFile, int width, int height){
        try{
            if (primaryStage==null){
                System.out.println("Stage is null");
                return;
            }
            FXMLLoader loader=new FXMLLoader(MainApp.class.getResource("/fxml/"+fxmlFile +".fxml"));
            Scene scene=new Scene(loader.load(),width,height);
            primaryStage.setScene(scene);

            FadeTransition fade= new FadeTransition(Duration.millis(300),scene.getRoot());
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();
        }catch(Exception e){
            System.out.println("Error navigating to "+fxmlFile+": "+e.getMessage());
            e.printStackTrace();
        }
    }
}