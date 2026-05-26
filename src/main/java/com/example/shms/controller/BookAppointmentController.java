package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.exception.AppointmentConflictException;
import com.example.shms.model.Appointment;
import com.example.shms.utils.AppointmentValidator;
import com.example.shms.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalTime;

public class BookAppointmentController {
    @FXML private ComboBox<String> patientCombo;
    @FXML private ComboBox<String> doctorCombo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeCombo;
    @FXML private TextArea notesField;
    @FXML private Label statusLabel;

    private DatabaseManager db=DatabaseManager.getInstance();
    private AppointmentValidator validator = new AppointmentValidator(db);

    @FXML public void initialize(){
        timeCombo.getItems().addAll("08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00");
        datePicker.setValue(java.time.LocalDate.now());
        loadDoctors();

        if("PATIENT".equals(SessionManager.getInstance().getLoggedInRole())){
            String username = SessionManager.getInstance().getLoggedInUser();
            try{
                java.sql.ResultSet rs = db.getConnection().createStatement()
                        .executeQuery("SELECT id, name FROM patients WHERE username = '"+username+"' LIMIT 1");
                if(rs.next()){
                    patientCombo.getItems().add(rs.getInt("id")+"-"+rs.getString("name"));
                    patientCombo.getSelectionModel().selectFirst();
                }
            }catch(Exception e){
                System.out.println("Patient load error: "+e.getMessage());
            }
            patientCombo.setDisable(true);
        } else {
            try{
                java.sql.ResultSet rs = db.getAllPatients();
                while(rs != null && rs.next()){
                    patientCombo.getItems().add(rs.getInt("id")+"-"+rs.getString("name"));
                }
            }catch(Exception e){
                System.out.println("loadPatients error: "+e.getMessage());
            }
        }
    }
    private void loadDoctors(){
        try{
            java.sql.ResultSet rs = db.getAllDoctors();
            while (rs!= null && rs.next()){
                doctorCombo.getItems().add(rs.getInt("id")+"-"+rs.getString("Name"));
            }
        }
        catch ( Exception e){
            System.out.println("loadDoctors error:"+ e.getMessage());
        }
    }
    @FXML private void handleBook(){
        if(patientCombo.getValue() == null || doctorCombo.getValue() == null
                || datePicker.getValue() == null || timeCombo.getValue() == null){
            statusLabel.setText("Please fill all fields.");
            return;
        }
        try {
            int patientId=Integer.parseInt(patientCombo.getValue().split("-")[0].trim());
            int doctorId=Integer.parseInt(doctorCombo.getValue().split("-")[0].trim());

            Appointment appt = new Appointment(0,patientId,doctorId,datePicker.getValue(),LocalTime.parse(timeCombo.getValue()),"Scheduled",
                    notesField.getText()
            );
            validator.bookAppointment(appt);
            statusLabel.setText("✅ Appointment booked successfully!");
        }catch(AppointmentConflictException e){
            statusLabel.setText("⚠️ "+e.getMessage());
        }
    }
    @FXML private void handleClose(){
        Stage stage =(javafx.stage.Stage ) statusLabel.getScene().getWindow();
        stage.close();
    }
}