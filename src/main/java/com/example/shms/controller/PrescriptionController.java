package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Prescription;
import com.example.shms.utils.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import java.util.List;

public class PrescriptionController {

    @FXML private TextField patientIdField;
    @FXML private TextField medicineNameField;
    @FXML private TextField dosageField;
    @FXML private TextField durationField;
    @FXML private TextArea instructionsField;
    @FXML private Label patientNameLabel;
    @FXML private TableView<Prescription> prescriptionTable;
    @FXML private TableColumn<Prescription, String> colId;
    @FXML private TableColumn<Prescription, String> colPatient;
    @FXML private TableColumn<Prescription, String> colDoctor;
    @FXML private TableColumn<Prescription, String> colMedicine;
    @FXML private TableColumn<Prescription, String> colDosage;
    @FXML private TableColumn<Prescription, String> colDuration;
    @FXML private TableColumn<Prescription, String> colInstructions;
    @FXML private VBox patientViewPane;
    @FXML private TextField patientSearchField;
    @FXML private VBox addFormPane;

    private final ObservableList<Prescription> allPrescriptions = FXCollections.observableArrayList();
    private final DatabaseManager db = DatabaseManager.getInstance();

    @FXML private void initialize(){
        setupTableColumns();
        prescriptionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        prescriptionTable.setFixedCellSize(52);
        String role = SessionManager.getInstance().getLoggedInRole();
        if(role.equals("PATIENT")){
            showPatientView();
            loadPatientPrescriptions();
        }else{
            showDoctorView();
            loadFromDatabase();
        }
    }
    private void setupTableColumns() {
        colId.setCellValueFactory(cell ->
                new SimpleStringProperty("RX-" + String.format("%03d", cell.getValue().getId())));
        colPatient.setCellValueFactory(cell -> {
            String name = db.getPatientName(cell.getValue().getPatientId());
            return new SimpleStringProperty(name != null ? name : "Unknown");
        });
        colDoctor.setCellValueFactory(cell -> {
            String name = db.getDoctorName(cell.getValue().getDoctorId());
            return new SimpleStringProperty(name != null ? name : "—");
        });
        colMedicine.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().getMedicineName()));
        colDosage.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().getDosage()));
        colDuration.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().getDuration()));
        colInstructions.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().getInstructions()));
        prescriptionTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Prescription item,boolean empty){
                super.updateItem(item, empty);
                setStyle((!empty && getIndex() % 2 != 0)
                        ? "-fx-background-color: #f7f9fc;"
                        : "-fx-background-color: white;");
            }
        });
        prescriptionTable.setItems(allPrescriptions);
    }
    private void loadFromDatabase() {
        allPrescriptions.clear();
        List<Prescription> list = db.getAllPrescriptions();
        if(list != null) allPrescriptions.addAll(list);
    }
    private void loadPatientPrescriptions(){
        String username = SessionManager.getInstance().getLoggedInUser();
        try(java.sql.Statement st=db.getConnection().createStatement()){
            java.sql.ResultSet rs=st.executeQuery("SELECT id, name FROM patients WHERE username = '"+username+"' LIMIT 1");
            if(!rs.next()) return;
            int patientId = rs.getInt("id");
            String name = rs.getString("name");
            patientNameLabel.setText("Showing prescriptions for: "+name);
            prescriptionTable.setItems(FXCollections.observableArrayList(db.getPrescriptionsByPatient(patientId)));
        }catch(Exception e){
            System.out.println("Patient prescriptions load failed: " + e.getMessage());
        }
    }
    @FXML
    private void handleAddPrescription(){
        String patientIdText = patientIdField.getText().trim();
        String medicine = medicineNameField.getText().trim();
        String dosage = dosageField.getText().trim();
        String duration = durationField.getText().trim();
        String instructions = instructionsField.getText().trim();
        if(patientIdText.isEmpty() || medicine.isEmpty() || dosage.isEmpty() || duration.isEmpty() || instructions.isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Missing Fields","All fields are required.");
            return;
        }
        int patientId;
        try{
            patientId = Integer.parseInt(patientIdText);
        }catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input","Patient ID must be a number.");
            return;
        }
        int doctorId=0;
        try{
            String username = SessionManager.getInstance().getLoggedInUser();
            java.sql.ResultSet rs = db.getConnection().createStatement().executeQuery(
                    "SELECT id FROM doctors WHERE username = '"+username+"' LIMIT 1"
            );
            if(rs.next())
                doctorId=rs.getInt("id");
        }catch(Exception e){
            System.out.println("Doctor lookup failed: " + e.getMessage());
        }
        Prescription p=new Prescription(0,patientId,doctorId,medicine,dosage,duration,instructions);
        db.addPrescription(p);
        loadFromDatabase();
        clearForm();
    }
    private void clearForm(){
        patientIdField.clear();
        medicineNameField.clear();
        dosageField.clear();
        durationField.clear();
        instructionsField.clear();
    }
    private void showAlert(Alert.AlertType type,String title,String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
    @FXML private void handlePatientSearch(KeyEvent event){
        String input = patientSearchField.getText().trim();
        if(input.isEmpty()){
            prescriptionTable.setItems(allPrescriptions);
            return;
        }
        try{
            int patientId = Integer.parseInt(input);
            prescriptionTable.setItems(FXCollections.observableArrayList(
                    db.getPrescriptionsByPatient(patientId)
            ));
        }catch(NumberFormatException e){
            prescriptionTable.setItems(FXCollections.emptyObservableList());
        }
    }
    public void showPatientView(){
        patientViewPane.setVisible(true);
        patientViewPane.setManaged(true);
        addFormPane.setVisible(false);
        addFormPane.setManaged(false);
        prescriptionTable.setItems(FXCollections.emptyObservableList());
    }
    public void showDoctorView(){
        patientViewPane.setVisible(false);
        patientViewPane.setManaged(false);
        addFormPane.setVisible(true);
        addFormPane.setManaged(true);
        prescriptionTable.setItems(allPrescriptions);
    }
    @FXML private void handleBack() {
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }
}