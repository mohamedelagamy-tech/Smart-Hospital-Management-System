package com.example.shms;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Appointment;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class AppointmentManagementController {
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Integer> colId;
    @FXML private TableColumn<Appointment, Integer> colPatient;
    @FXML private TableColumn<Appointment, Integer> colDoctor;
    @FXML private TableColumn<Appointment, String> colDate;
    @FXML private TableColumn<Appointment, String> colTime;
    @FXML private TableColumn<Appointment, String> colStatus;
    @FXML private TableColumn<Appointment, String> colNotes;

    private DatabaseManager db = DatabaseManager.getInstance() ;
    @FXML
    public void intialize(){
      colId.setCellValueFactory(d-> new SimpleIntegerProperty(d.getValue().getAppointmentId()).asObject());
      colPatient.setCellValueFactory(d-> new SimpleIntegerProperty(d.getValue().getPatientId()).asObject());
      colDoctor.setCellValueFactory(d->new SimpleIntegerProperty(d.getValue().getDoctorId()).asObject());
      colDate.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getDate().toString()));
      colTime.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getTime().toString()));
      colStatus.setCellValueFactory(d-> new SimpleStringProperty(d.getValue().getStatus()));
      colNotes.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getNotes()));
      loadAppointments();
    }
private void loadAppointments() {
    java.util.List<Appointment> all = db.getAppointmentsByDoctor(0);
    appointmentTable.setItems(FXCollections.observableArrayList(all));
}
@FXML
        private void handleCancel(){
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected==null){
            new Alert(Alert.AlertType.WARNING,"Please select an appointment ").showAndWait();
            return;
        }
        Alert confirm = new Alert( Alert.AlertType.CONFIRMATION,"Cancel this appointment");
                confirm.showAndWait().ifPresent(r-> {
                    if (r == ButtonType.OK)
                        db.cancelAppointment(selected.getAppointmentId());
                    loadAppointments();

                });
}
}