package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.AppointmentValidator;
import com.example.shms.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class BookAppointmentController {

    @FXML private ComboBox<String> patientCombo;
    @FXML private ComboBox<String> doctorCombo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeCombo;
    @FXML private TextArea notesField;
    @FXML private Label statusLabel;
    @FXML private HBox patientRow;

    private final DatabaseManager db=DatabaseManager.getInstance();
    private final AppointmentValidator validator=new AppointmentValidator(db);

    @FXML
    public void initialize(){
        timeCombo.getItems().addAll(
                "08:00","09:00","10:00","11:00",
                "12:00","13:00","14:00","15:00","16:00"
        );
        datePicker.setValue(java.time.LocalDate.now());
        loadPatients();
        loadDoctors();
        if("PATIENT".equals(SessionManager.getInstance().getLoggedInRole())){
            patientRow.setVisible(false);
            patientRow.setManaged(false);
        }
    }
    private void loadPatients(){
        patientCombo.getItems().clear();
        try{
            java.sql.ResultSet rs=db.getAllPatients();
            while(rs!=null && rs.next()){
                patientCombo.getItems().add(rs.getInt("id")+"-"+rs.getString("name"));
            }
        }catch(Exception e){
            System.out.println("loadPatients error: "+e.getMessage());
        }
    }
    private void loadDoctors(){
        doctorCombo.getItems().clear();
        try{
            java.sql.ResultSet rs=db.getAllDoctors();
            while(rs!=null && rs.next()){
                doctorCombo.getItems().add(rs.getInt("id")+"-"+rs.getString("name"));
            }
        }catch(Exception e){
            System.out.println("loadDoctors error: "+e.getMessage());
        }
    }
    @FXML
    private void handleBook(){
        String role=SessionManager.getInstance().getLoggedInRole();
        int patientId=-1;

        if("PATIENT".equals(role)){
            String username=SessionManager.getInstance().getLoggedInUser();
            try(java.sql.Statement st=db.getConnection().createStatement()){
                java.sql.ResultSet rs=st.executeQuery(
                        "SELECT id FROM patients WHERE username = '"+username+"' LIMIT 1"
                );
                if(rs.next()) patientId=rs.getInt("id");
            }catch(Exception e){
                System.out.println("Patient lookup error: "+e.getMessage());
            }
        }else{
            if(patientCombo.getValue()==null){
                statusLabel.setText("Please select a patient.");
                statusLabel.setStyle("-fx-text-fill: #c0392b;");
                return;
            }
            patientId=Integer.parseInt(patientCombo.getValue().split("-")[0]);
        }

        if(doctorCombo.getValue()==null || datePicker.getValue()==null || timeCombo.getValue()==null){
            statusLabel.setText("Please fill all fields.");
            statusLabel.setStyle("-fx-text-fill: #c0392b;");
            return;
        }

        int doctorId=Integer.parseInt(doctorCombo.getValue().split("-")[0]);

        try{
            com.example.shms.model.Appointment appt=new com.example.shms.model.Appointment(
                    0, patientId, doctorId,
                    datePicker.getValue(),
                    java.time.LocalTime.parse(timeCombo.getValue()),
                    "Scheduled",
                    notesField.getText()
            );
            validator.bookAppointment(appt);
            statusLabel.setText("Appointment booked successfully!");
            statusLabel.setStyle("-fx-text-fill: #27ae60;");
        }catch(com.example.shms.exception.AppointmentConflictException e){
            statusLabel.setText(e.getMessage());
            statusLabel.setStyle("-fx-text-fill: #c0392b;");
        }
    }
    @FXML
    private void handleClose(){
        ((javafx.stage.Stage) statusLabel.getScene().getWindow()).close();
    }
}