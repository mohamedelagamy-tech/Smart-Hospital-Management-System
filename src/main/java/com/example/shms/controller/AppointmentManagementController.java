package com.example.shms.controller;
import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Appointment;
import com.example.shms.utils.SessionManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentManagementController {

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> colId;
    @FXML
    private TableColumn<Appointment, String> colPatient;
    @FXML
    private TableColumn<Appointment, String> colDoctor;
    @FXML
    private TableColumn<Appointment, String> colDate;
    @FXML
    private TableColumn<Appointment, String> colTime;
    @FXML
    private TableColumn<Appointment, String> colStatus;
    @FXML
    private TableColumn<Appointment, String> colNotes;
    @FXML
    private javafx.scene.control.TextField searchField;
    @FXML
    private Button bookBtn;

    private DatabaseManager db = DatabaseManager.getInstance();


    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getAppointmentId()).asObject());
        colPatient.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(db.getPatientName(d.getValue().getPatientId())));
        colDoctor.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(db.getDoctorName(d.getValue().getDoctorId())));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDate().toString()));
        colTime.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTime().toString()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));
        colNotes.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNotes()));
        loadAppointments();

        }

    private void loadAppointments(){
        try{
            String role = com.example.shms.utils.SessionManager.getInstance().getLoggedInRole();
            String query;
            if ("PATIENT".equals(role)) {
                int patientId = com.example.shms.utils.SessionManager.getInstance().getLoggedInPatientId();
                query = "SELECT a.id, p.name as patientName, d.name as doctotName," +
                        "a.date,a.time,a.Status, a.notes, a.patientId, a.doctorId" +
                        "FROM appointments a" +
                        "JOIN patients p ON a.patientId = p.id" +
                        "JOIN doctors d ON a.doctorId = d.id" +
                        "WHERE a.patientId=" + patientId +
                        "ORDER BY a.date, a.time";
            }
            else if ("DOCTOR".equals(role)){
                int doctorId = com.example.shms.utils.SessionManager.getInstance().getLoggedInDoctorId();
                query = "SELECT a.id, p.name as patientName, d.name as doctotName," +
                        "a.date,a.time,a.Status, a.notes, a.patientId, a.doctorId" +
                        "FROM appointments a" +
                        "JOIN patients p ON a.patientId = p.id" +
                        "JOIN doctors d ON a.doctorId = d.id" +
                        "WHERE a.patientId=" + doctorId +
                        "ORDER BY a.date, a.time";
            }
            else {
                query = "SELECT a.id, p.name as patientName, d.name as doctotName," +
                        "a.date,a.time,a.Status, a.notes, a.patientId, a.doctorId" +
                        "FROM appointments a" +
                        "JOIN patients p ON a.patientId = p.id" +
                        "JOIN doctors d ON a.doctorId = d.id" +
                        "ORDER BY a.date, a.time";
            }
            java.sql.ResultSet rs = db.getConnection().createStatement().executeQuery(query);
            java.util.List<Appointment> all = new java.util.ArrayList<>();
            while (rs.next()){
                Appointment a = new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patientId"),
                        rs.getInt("doctorId"),
                        java.time.LocalDate.parse(rs.getString("date")),
                        java.time.LocalTime.parse(rs.getString("time")),
                        rs.getString("status") !=null ? rs.getString("status"): "Scheduled",
                        rs.getString("notes") !=null ? rs.getString("notes"):""
                );
                all.add(a);
            }
            appointmentTable.setItems(
                    javafx.collections.FXCollections.observableArrayList(all));
        }
        catch (Exception e){
                System.out.println("loadAppointments error:"+e.getMessage());
            }
    }
    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase();
        java.util.List<com.example.shms.model.Appointment> all = db.getAppointmentsByDoctor(0);
        java.util.List<com.example.shms.model.Appointment> filtered = all.stream().filter(a -> a.getStatus().toLowerCase().contains(query) || a.getDate().toString().contains(query) || a.getNotes().toLowerCase().contains(query)).collect(java.util.stream.Collectors.toList());
        appointmentTable.setItems(javafx.collections.FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void filterAll() {
        loadAppointments();
    }

    @FXML
    private void filterScheduled() {
        appointmentTable.setItems(FXCollections.observableArrayList(
                appointmentTable.getItems().filtered(a->a.getStatus().equals("Scheduled"))
        ));
    }

    @FXML
    private void filterCompleted() {
        appointmentTable.setItems(FXCollections.observableArrayList(
                appointmentTable.getItems().filtered(a->a.getStatus().equals("Completed"))
        ));
    }

    @FXML
    private void filterCancelled() {
        appointmentTable.setItems(FXCollections.observableArrayList(
                appointmentTable.getItems().filtered(a->a.getStatus().equals("Cancelled"))
        ));
    }

    private void filterByStatus(String status) {
        List<Appointment> filtered = db.getAppointmentsByDoctor(0).stream().filter(a -> a.getStatus().equals(status)).collect(Collectors.toList());
        appointmentTable.setItems(FXCollections.observableArrayList(filtered));
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

    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
        getClass().getResource("/fxml/bookAppointmentView.fxml");
    }
    @FXML private void handleRefresh(){
        loadAppointments();
    }
}