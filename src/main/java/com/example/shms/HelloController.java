package com.example.shms;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML private void openAppointments(){
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/appointment-management-view.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Appointment Management");
            stage.setScene(new javafx.scene.Scene(root, 700, 500));
            stage.show();
        }
        catch(Exception e){
            System.out.println("Error:"+e.getMessage());
        }
    }
    @FXML private void openCalender(){
        try{
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/calender-view.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Appointment Calender");
            stage.setScene(new javafx.scene.Scene(root,700,600));
            stage.show();
        } catch (Exception e) {
            System.out.println("Error:"+e.getMessage());
        }
    }
    @FXML private void openHistory(){
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/AppointmentHistoryView.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Appointment History");
            stage.setScene(new javafx.scene.Scene(root, 700, 500));
            stage.show();
        } catch (Exception e) {
            System.out.println("Error:"+e.getMessage());
        }
    }

}
