package com.example.shms.utils;


import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Bill;
import com.example.shms.model.DischargeSummary;
import com.example.shms.model.Patient;
import com.example.shms.model.Prescription;
import com.example.shms.model.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DischargeSummaryController implements Initializable {

    @FXML private ComboBox<String>   patientComboBox;

    @FXML private Label lblName;
    @FXML private Label lblPatientId;
    @FXML private Label lblAdmission;
    @FXML private Label lblDischarge;
    @FXML private Label lblRoom;
    @FXML private Label lblDoctor;
    @FXML private Label lblDiagnosis;
    @FXML private Label lblRoomCharges;
    @FXML private Label lblMedicineCharges;
    @FXML private Label lblConsultationCharges;
    @FXML private Label lblTotalBill;

    @FXML private TableView<Prescription>            prescriptionsTable;
    @FXML private TableColumn<Prescription, String>  colMedicine;
    @FXML private TableColumn<Prescription, String>  colDosage;
    @FXML private TableColumn<Prescription, String>  colDuration;

    private final DatabaseManager db = DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadPatients();
        clearSummary();
    }

    private void setupTable() {
        colMedicine.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        colDosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));

        prescriptionsTable.widthProperty().addListener((obs, o, n) -> {
            javafx.scene.Node header = prescriptionsTable.lookup("TableHeaderRow");
            if (header != null) {
                header.setStyle("-fx-background-color: #dbeafe;");
            }
        });
    }

    private void loadPatients() {
        try {
            ResultSet rs = db.getAllPatients();
            while (rs != null && rs.next()) {
                String entry = rs.getString("name") + " (P-" + rs.getInt("id") + ")";
                patientComboBox.getItems().add(entry);
            }
        } catch (SQLException e) {
            System.out.println("Error loading patients: " + e.getMessage());
        }
    }

    @FXML
    private void onPatientSelected() {
        String selected = patientComboBox.getValue();
        if (selected == null) return;

        int patientId = extractPatientId(selected);
        if (patientId == -1) return;

        populateSummary(patientId, selected);
    }

    private void populateSummary(int patientId, String selectedName) {

        String name = selectedName.substring(0, selectedName.lastIndexOf(" ("));
        lblName.setText(name);
        lblPatientId.setText("P-" + patientId);
        lblAdmission.setText("5/10/2026");
        lblDischarge.setText(LocalDate.now().toString());

        String roomInfo = "N/A";
        List<Room> rooms = db.getAllRooms();
        if (rooms != null) {
            for (Room r : rooms) {
                if (r.getAssignedPatientId() == patientId) {
                    roomInfo = "Room " + r.getRoomNumber() + " - " + r.getDepartment();
                    break;
                }
            }
        }
        lblRoom.setText(roomInfo);

        String doctorInfo = "N/A";
        try {
            ResultSet rs = db.getAllDoctors();
            while (rs != null && rs.next()) {
                doctorInfo = "Dr. " + rs.getString("name");
                break;
            }
        } catch (SQLException e) {
            System.out.println("Error loading doctor: " + e.getMessage());
        }
        lblDoctor.setText(doctorInfo);

        lblDiagnosis.setText("See medical records");

        List<Prescription> prescriptions = db.getPrescriptionsByPatient(patientId);
        ObservableList<Prescription> presObs = FXCollections.observableArrayList();
        if (prescriptions != null) presObs.addAll(prescriptions);
        prescriptionsTable.setItems(presObs);

        double roomCharges         = 0;
        double medicineCharges     = 0;
        double consultationCharges = 0;
        double totalBill           = 0;

        List<Bill> bills = db.getBillsByPatient(patientId);
        if (bills != null) {
            for (Bill b : bills) {
                String service = b.getService().toLowerCase();
                if (service.contains("room"))         roomCharges         += b.getAmount();
                else if (service.contains("medicine") || service.contains("prescription"))
                    medicineCharges     += b.getAmount();
                else if (service.contains("consult")) consultationCharges += b.getAmount();
                else                                   consultationCharges += b.getAmount();
                totalBill += b.getAmount();
            }
        }

        lblRoomCharges.setText("$" + String.format("%.0f", roomCharges));
        lblMedicineCharges.setText("$" + String.format("%.0f", medicineCharges));
        lblConsultationCharges.setText("$" + String.format("%.0f", consultationCharges));
        lblTotalBill.setText("$" + String.format("%.0f", totalBill));

        DischargeSummary summary = new DischargeSummary(
                0, patientId, prescriptions != null ? prescriptions : List.of(),
                0, totalBill
        );
        summary.writeToFile();

    }
    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
    }
    @FXML
    private void handlePrint() {
        String selected = patientComboBox.getValue();
        if (selected == null) {
            showAlert("Please select a patient first.");
            return;
        }
        int patientId = extractPatientId(selected);
        List<Prescription> prescriptions = db.getPrescriptionsByPatient(patientId);

        double totalBill = 0;
        List<Bill> bills = db.getBillsByPatient(patientId);
        if (bills != null) for (Bill b : bills) totalBill += b.getAmount();

        DischargeSummary summary = new DischargeSummary(
                0, patientId,
                prescriptions != null ? prescriptions : List.of(),
                0, totalBill
        );
        summary.writeToFile();

        final int finalPatientId = patientId;
        final double finalTotalBill = totalBill;
        final String finalDoctorName = lblDoctor.getText();
        final String finalRoomInfo = lblRoom.getText();

        Thread emailThread = new Thread(new Runnable() {
            @Override
            public void run(){
                try{
                    String patientEmail=db.getPatientEmail(finalPatientId);
                    String patientName=db.getPatientName(finalPatientId);
                    if(patientEmail != null && !patientEmail.isEmpty()){
                        EmailService.sendDischargeSummary(patientEmail,patientName,finalDoctorName,finalRoomInfo,String.format("%.0f", finalTotalBill));
                    }
                } catch(Exception e){
                    System.out.println("Failed to send discharge email: "+e.getMessage());
                }
            }
        });
        emailThread.setDaemon(true);
        emailThread.start();

        showAlert("Email has been sent");
        showAlert("Discharge summary saved as discharge_" + patientId + ".txt");
    }

    private int extractPatientId(String entry) {
        try {
            int start = entry.lastIndexOf("P-") + 2;
            int end   = entry.lastIndexOf(")");
            return Integer.parseInt(entry.substring(start, end).trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private void clearSummary() {
        lblName.setText("-");
        lblPatientId.setText("-");
        lblAdmission.setText("-");
        lblDischarge.setText("-");
        lblRoom.setText("-");
        lblDoctor.setText("-");
        lblDiagnosis.setText("-");
        lblRoomCharges.setText("$0");
        lblMedicineCharges.setText("$0");
        lblConsultationCharges.setText("$0");
        lblTotalBill.setText("$0");
        prescriptionsTable.setItems(FXCollections.emptyObservableList());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Discharge Summary");
        alert.setContentText(message);
        alert.show();
    }
}

