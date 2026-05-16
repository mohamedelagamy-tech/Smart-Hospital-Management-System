package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.AppointmentValidator;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class BookAppointmentController {
    @FXML private ComboBox<String> patientCombo;
    @FXML private ComboBox<String> doctorCombo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeCombo;
    @FXML private TextArea notesField;
    @FXML private Label statusLabel;

    private DatabaseManager db=DatabaseManager.getInstance();
    private AppointmentValidator validator = new AppointmentValidator(db);

@FXML
public void initialize(){
timeCombo.getItems().addAll("08:00","09:00");
datePicker.setValue(java.time.LocalDate.now());
loadPatients();
loadDoctors();
}
private void loadPatients(){
    try{
        java.sql.ResultSet rs = db.getAllPatients();
        while (rs != null && rs.next()){
            patientCombo.getItems().add(rs.getInt("PatientID")+"-"+rs.getString("Name"));
        }
    } catch (Exception e) {
       System.out.println("loadPatient error"+e.getMessage());
    }
}
private void loadDoctors(){
    try{
        java.sql.ResultSet rs = db.getAllDoctors();
        while (rs!= null && rs.next()){
            doctorCombo.getItems().add(rs.getInt("docotrID")+"-"+rs.getString("Name"));
        }
    }
    catch ( Exception e){
        System.out.println("loadDoctors error:"+ e.getMessage());
    }
}
@javafx.fxml.FXML
    private void handleBook(){
    if ( patientCombo.getValue()==null || doctorCombo.getValue()==null || datePicker.getValue()==null ||timeCombo.getValue()==null) {
        statusLabel.setText("Please fill all fields");
        return;
    }
    try{
        com.example.shms.model.Appointment appt = new com.example.shms.model.Appointment(0,
                patientCombo.getSelectionModel().getSelectedIndex() +1 ,
                doctorCombo.getSelectionModel().getSelectedIndex() +1 ,
                datePicker.getValue(),
                java.time.LocalTime.parse(timeCombo.getValue()),
                "Scheduled",
                notesField.getText()
                );
        validator.bookAppointment(appt);
        statusLabel.setText("Appointment booked successfully!");
    }
    catch(com.example.shms.exception.AppointmentConflictException e){
        statusLabel.setText(e.getMessage());
    }
    }
    @javafx.fxml.FXML
    private void handleClose(){
    javafx.stage.Stage stage =(javafx.stage.Stage ) statusLabel.getScene().getWindow();
    stage.close();
    }
}

