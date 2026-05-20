package com.example.shms.utils;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Prescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrescriptionController implements Initializable {

    @FXML private TextField  patientIdField;
    @FXML private TextField  medicineNameField;
    @FXML private TextField  dosageField;
    @FXML private TextField  durationField;
    @FXML private TextArea   instructionsField;

    @FXML private TableView<Prescription>       prescriptionTable;
    @FXML private TableColumn<Prescription, Integer> colId;
    @FXML private TableColumn<Prescription, Integer> colPatient;
    @FXML private TableColumn<Prescription, Integer> colDoctor;
    @FXML private TableColumn<Prescription, String>  colMedicine;
    @FXML private TableColumn<Prescription, String>  colDosage;
    @FXML private TableColumn<Prescription, String>  colDuration;
    @FXML private TableColumn<Prescription, String>  colInstructions;

    @FXML private VBox patientViewPane;
    @FXML private TextField patientSearchField;
    @FXML private VBox addFormPane;

    private final ObservableList<Prescription> allPrescriptions = FXCollections.observableArrayList();
    private final DatabaseManager db = DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadFromDatabase();
        styleTableHeader();
        prescriptionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        String role = SessionManager.getInstance().getLoggedInRole();
        if (role != null && role.equalsIgnoreCase("PATIENT")) {
            showPatientView();
        } else if (role != null && role.equalsIgnoreCase("DOCTOR")) {
            showDoctorView();
        } else {
            showPatientView();
        }
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colMedicine.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        colDosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colInstructions.setCellValueFactory(new PropertyValueFactory<>("instructions"));

        colId.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) { setText(null); return; }
                setText("RX-" + String.format("%03d", id));
                setStyle("-fx-text-fill: #1a6fba; -fx-font-weight: bold;");
            }
        });

        prescriptionTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);
                setStyle((!empty && getIndex() % 2 != 0)
                        ? "-fx-background-color: #f7f9fc;"
                        : "-fx-background-color: white;");
            }
        });

        prescriptionTable.setFixedCellSize(52);
        prescriptionTable.setItems(allPrescriptions);
    }

    private void loadFromDatabase() {
        allPrescriptions.clear();
        List<Prescription> list = db.getPrescriptionsByPatient(0); // load all
        // If DatabaseManager has getAllPrescriptions(), use that instead:
        // List<Prescription> list = db.getAllPrescriptions();
        if (list != null) allPrescriptions.addAll(list);
    }

    @FXML
    private void handleAddPrescription() {
        String patientIdText = patientIdField.getText().trim();
        String medicine      = medicineNameField.getText().trim();
        String dosage        = dosageField.getText().trim();
        String duration      = durationField.getText().trim();
        String instructions  = instructionsField.getText().trim();

        if (patientIdText.isEmpty() || medicine.isEmpty() ||
                dosage.isEmpty() || duration.isEmpty() || instructions.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "All fields are required.");
            return;
        }

        int patientId;
        try {
            patientId = Integer.parseInt(patientIdText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Patient ID must be a number.");
            return;
        }

        int newId = allPrescriptions.size() + 1;
        Prescription p = new Prescription(newId, patientId, 0, medicine, dosage, duration, instructions);

        db.addPrescription(p);
        allPrescriptions.add(p);
        prescriptionTable.setItems(allPrescriptions);

        clearForm();
    }

    private void clearForm() {
        patientIdField.clear();
        medicineNameField.clear();
        dosageField.clear();
        durationField.clear();
        instructionsField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void styleTableHeader() {
        prescriptionTable.widthProperty().addListener((obs, o, n) -> {
            javafx.scene.Node header = prescriptionTable.lookup("TableHeaderRow");
            if (header != null) {
                header.setStyle("-fx-background-color: #1a6fba;");
                header.lookupAll(".column-header .label").forEach(node ->
                        node.setStyle("-fx-text-fill: white; -fx-font-weight: bold;"));
            }
        });
    }
    @FXML
    private void handlePatientSearch(KeyEvent event) {
        String input = patientSearchField.getText().trim();
        if (input.isEmpty()) {
            prescriptionTable.setItems(allPrescriptions);
            return;
        }
        try {
            int patientId = Integer.parseInt(input);
            ObservableList<Prescription> filtered = FXCollections.observableArrayList(
                    db.getPrescriptionsByPatient(patientId)
            );
            prescriptionTable.setItems(filtered);
        } catch (NumberFormatException e) {
            prescriptionTable.setItems(FXCollections.emptyObservableList());
        }
    }
    public void showPatientView() {
        patientViewPane.setVisible(true);
        patientViewPane.setManaged(true);
        addFormPane.setVisible(false);
        addFormPane.setManaged(false);
        prescriptionTable.setItems(FXCollections.emptyObservableList());
    }

    public void showDoctorView() {
        patientViewPane.setVisible(false);
        patientViewPane.setManaged(false);
        addFormPane.setVisible(true);
        addFormPane.setManaged(true);
        prescriptionTable.setItems(allPrescriptions);
    }
    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
    }
}
