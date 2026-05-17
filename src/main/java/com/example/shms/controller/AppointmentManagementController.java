package com.example.shms.controller;
import com.example.shms.MainApp;
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
    @FXML
    private javafx.scene.control.TextField searchField;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> colId;
    @FXML
    private TableColumn<Appointment, Integer> colPatient;
    @FXML
    private TableColumn<Appointment, Integer> colDoctor;
    @FXML
    private TableColumn<Appointment, String> colDate;
    @FXML
    private TableColumn<Appointment, String> colTime;
    @FXML
    private TableColumn<Appointment, String> colStatus;
    @FXML
    private TableColumn<Appointment, String> colNotes;

    private DatabaseManager db = DatabaseManager.getInstance();

    @FXML
    public void intialize() {
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getAppointmentId()).asObject());
        colPatient.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPatientId()).asObject());
        colDoctor.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getDoctorId()).asObject());
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDate().toString()));
        colTime.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTime().toString()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));
        colNotes.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNotes()));
        loadAppointments();
    }

    private void loadAppointments() {
        java.util.List<Appointment> all = db.getAppointmentsByDoctor(0);
        appointmentTable.setItems(FXCollections.observableArrayList(all));
    }

    @FXML
    private void handleCancel() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an appointment ").showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Cancel this appointment");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK)
                db.cancelAppointment(selected.getAppointmentId());
            loadAppointments();

        });
    }

    @FXML
    private void handleBook() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/bookAppointmentView.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Book Appointment");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
            loadAppointments();
        } catch (Exception e) {
            System.out.println("Error opening booking from :" + e.getMessage());
        }
    }
@FXML private void handleSearch(){
        String query=searchField.getText().toLowerCase();
        java.util.List<com.example.shms.model.Appointment> all=db.getAppointmentsByDoctor(0);
        java.util.List<com.example.shms.model.Appointment> filtered = all.stream().filter(a->a.getStatus().toLowerCase().contains(query) || a.getDate().toString().contains(query) || a.getNotes().toLowerCase().contains(query)).collect(java.util.stream.Collectors.toList());
        appointmentTable.setItems(javafx.collections.FXCollections.observableArrayList(filtered));
    }
    @FXML private void filteredAll(){
        loadAppointments();
    }
    @FXML private void filterScheduled(){
        filterByStatus("Scheduled");
    }
    @FXML private void filterCompleted(){
        filterByStatus("Completed");
    }
    @FXML private void filterCancelled(){
        filterByStatus("Cancelled");
    }
    private void filterByStatus(String status){
        java.util.List<com.example.shms.model.Appointment> all = db.getAppointmentsByDoctor(0);
        java.util.List<com.example.shms.model.Appointment> filtered = all.stream().filter(a->a.getStatus().equals(status)).collect(java.util.stream.Collectors.toList());
        appointmentTable.setItems(javafx.collections.FXCollections.observableArrayList(filtered));
    }
    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
    }
}
