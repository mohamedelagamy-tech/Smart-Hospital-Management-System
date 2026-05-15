package com.example.shms.controller;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Appointment;
import com.example.shms.utils.AppointmentValidator;
import com.example.shms.exception.AppointmentConflictException;
import com.example.shms.utils.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalTime;
import java.time.LocalDate;

public class BookAppointmentController {
    @FXML private ComboBox<String> patientCombo;
    @FXML private ComboBox<String> doctorCombo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeCombo;
    @FXML private TextArea notesField;
    @FXML private Label statusLabel;
private DatabaseManager db=DatabaseManager.getInstance();
private AppointmentValidator validator=new AppointmentValidator(db);
@FXML
    public void intialize() {
    timeCombo.getItems().addAll(
            "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00");
    datePicker.setValue(LocalDate.now());
    loadPatients();
    loadDoctors();
}
private void loadPatients(){
    patientCombo.getItems().addAll("Patient 1","Patient 2", "Patient 3","Patient 4","Patient 5","Patient 6","Patient 7","Patient 8","Patient 9","Patient 10","Patient 11","Patient 12","Patient 13","Patient 14","Patient 15","Patient 16","Patient 17","Patient 18","Patient 19","Patient 20","Patient 21","Patient 22","Patient 23");
}
private void loadDoctors(){
    doctorCombo.getItems().addAll("Doctor 1","Doctor 2","Doctor 3","Doctor 4","Doctor 5","Doctor 6","Doctor 7","Doctor 8","Doctor 9");
}
@FXML
    private void handleBook(){
    if(patientCombo.getValue()==null||doctorCombo.getValue()==null||datePicker.getValue()==null||timeCombo.getValue()==null){
        statusLabel.setText("Please fill all fields");
        return;
    }
    try {
        Appointment appt = new Appointment(0,
                patientCombo.getSelectionModel().getSelectedIndex() + 1,
                doctorCombo.getSelectionModel().getSelectedIndex() + 1,
                datePicker.getValue(),
                LocalTime.parse(timeCombo.getValue()),
                "Scheduled",
                notesField.getText()
        );
        validator.bookAppointment(appt);
        statusLabel.setText("Appointment booked successfully!");
    }
    catch ( AppointmentConflictException e)
    {
        statusLabel.setText(e.getMessage());

    }
}
@FXML
    private void handleClose(){
    Stage stage=(Stage) statusLabel.getScene().getWindow();
    stage.close();
}
}

