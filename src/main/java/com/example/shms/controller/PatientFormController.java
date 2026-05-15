package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Patient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PatientFormController {
    @FXML private Label formTitle;
    @FXML private TextField nameField,ageField,phoneField,addressField;
    @FXML private ComboBox<String> genderCombo, bloodTypeCombo,departmentCombo,priorityCombo;
    @FXML private Label nameError,ageError,phoneError;
    @FXML private Button saveBtn,cancelBtn;
    private final DatabaseManager db=DatabaseManager.getInstance();
    private PatientController patientController;
    private Patient editingPatient=null;
    public void setPatientController(PatientController patientController) {
        this.patientController = patientController;
    }
    @FXML private void initialize() {
        genderCombo.setItems(FXCollections.observableArrayList("Male","Female"));
        bloodTypeCombo.setItems(FXCollections.observableArrayList("A+","A-","B+","B-","AB+","AB-","O+","O-"));
        departmentCombo.setItems(FXCollections.observableArrayList("Cardiology","Pediatrics","Emergency","Neurology","Orthopedics","Dermatology","Oncology","Radiology"));
        priorityCombo.setItems(FXCollections.observableArrayList("1-Emergency","2-Urgent","3-Normal"));
    }
    public void setPatient(Patient patient) {
        this.editingPatient = patient;
        formTitle.setText("Edit patient");
        nameField.setText(patient.getName());
        ageField.setText(patient.getAge()+"");
        phoneField.setText(patient.getPhone());
        addressField.setText(patient.getAddress());
        genderCombo.setValue(patient.getGender());
        bloodTypeCombo.setValue(patient.getBloodType());
        departmentCombo.setValue(patient.getDepartment());
        priorityCombo.setValue(patient.getPriority()+"-"+getPriorityLabel(patient.getPriority()));
    }
    private String getPriorityLabel(int p){
        return switch (p){
            case 1 -> "Emergency";
            case 2 -> "Urgent";
            case 3 -> "Normal";
            default -> "";
        };
    }
    @FXML private void handleSave(){
        if(!validate())
            return;
        String name=nameField.getText().trim();
        int age=Integer.parseInt(ageField.getText().trim());
        String gender=genderCombo.getValue();
        String phone=phoneField.getText().trim();
        String address=addressField.getText().trim();
        String bloodType=bloodTypeCombo.getValue();
        String department=departmentCombo.getValue();
        int priority=Integer.parseInt(priorityCombo.getValue().substring(0,1));
        if(editingPatient==null)
            db.addPatient(name,age,gender,phone,address,bloodType,department);
        else
            db.editPatient(editingPatient.getPatientID(),name,age,gender,phone,address,bloodType,department);
        patientController.loadPatients();
        closeForm();
    }
    private boolean validate(){
        boolean valid=true;
        nameError.setText("");
        ageError.setText("");
        phoneError.setText("");
        if(nameField.getText().trim().isEmpty()){
            nameError.setText("Name can't be empty");
            valid=false;
        }
        try {
            int age=Integer.parseInt(ageField.getText().trim());
            if(age<0||age>120){
                ageError.setText("Age must be between 0 and 120");
                valid=false;
            }
        }catch (NumberFormatException e){
            ageError.setText("Age must be a number");
            valid=false;
        }
        String phone=phoneField.getText().trim();
        if(phone.isEmpty()){
            phoneError.setText("Phone number can't be empty");
            valid=false;
        }else if(phone.length()!=10&&phone.length()!=11){
            phoneError.setText("Phone number must be 10-11 digits");
            valid=false;
        }
        if (genderCombo.getValue()==null) {
            showAlert("Please enter a gender");
            valid=false;
        }
        if (bloodTypeCombo.getValue()==null) {
            showAlert("Please enter a blood type");
            valid=false;
        }
        if (departmentCombo.getValue()==null) {
            showAlert("Please select a department");
            valid=false;
        }
        if (priorityCombo.getValue()==null) {
            showAlert("Please select a priority");
            valid=false;
        }
        return valid;
    }
    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML private void handleCancel(){
        closeForm();
    }
    private void closeForm(){
        Stage stage=(Stage)cancelBtn.getScene().getWindow();
        stage.close();
    }
}
