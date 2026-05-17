package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Doctor;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class DoctorFormController {
    @FXML private Label      formTitle;
    @FXML private TextField  nameField;
    @FXML private TextField  emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> departmentField;
    @FXML private ComboBox<String> statusField;
    @FXML private TextField  salaryField;
    @FXML private TextField  workingDaysField;
    @FXML private TextField  workingHoursField;

    @FXML private Label nameError;
    @FXML private Label emailError;
    @FXML private Label passwordError;
    @FXML private Label departmentError;
    @FXML private Label statusError;
    @FXML private Label salaryError;
    @FXML private Label workingDaysError;
    @FXML private Label workingHoursError;

    private final DatabaseManager db = DatabaseManager.getInstance();
    private DoctorController doctorController;
    private Doctor existingDoctor = null;

    @FXML
    private void initialize() {
        departmentField.getItems().addAll("Cardiology", "Pediatrics", "Emergency",
                "Neurology", "Orthopedics", "Dermatology", "Oncology", "Radiology");
        statusField.getItems().addAll("Available", "Busy", "In Surgery");
    }

    public void setDoctorController(DoctorController controller) {
        this.doctorController = controller;
    }

    public void setDoctor(Doctor d) {
        this.existingDoctor = d;
        formTitle.setText("Edit Doctor");
        nameField.setText(d.getName());
        emailField.setText(d.getEmail());
        passwordField.setText(d.getPassword());
        departmentField.setValue(d.getDepartment());
        statusField.setValue(d.getStatus());
        salaryField.setText(String.valueOf(d.getSalary()));
        workingDaysField.setText(d.getWorkingDays());
        workingHoursField.setText(d.getWorkingHours());
    }




    @FXML
    private void handleSave() {
        if (!validate()) return;

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String department = departmentField.getValue();
        String status = statusField.getValue();
        double salary = Double.parseDouble(salaryField.getText().trim());
        String workingDays = workingDaysField.getText().trim();
        String workingHours = workingHoursField.getText().trim();

        if (existingDoctor == null) {

            db.addDoctor(name, email, password, department, status, salary, workingDays, workingHours);
        } else {

            db.editDoctor(existingDoctor.getID(), name, email, password, department, status, salary, workingDays, workingHours);
        }

        doctorController.loadDoctors();
        closeStage();
    }

    private boolean validate() {
        boolean valid = true;

        nameError.setText("");
        emailError.setText("");
        passwordError.setText("");
        departmentError.setText("");
        statusError.setText("");
        salaryError.setText("");
        workingDaysError.setText("");
        workingHoursError.setText("");

        if (nameField.getText().trim().isEmpty()) {
            nameError.setText("Name is required.");
            valid = false;
        }

        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            emailError.setText("Email is required.");
            valid = false;
        } else if (!email.contains("@") || !email.contains(".")) {
            emailError.setText("Enter a valid email address.");
            valid = false;
        }

        if (passwordField.getText().trim().isEmpty()) {
            passwordError.setText("Password is required.");
            valid = false;
        } else if (passwordField.getText().trim().length() < 6) {
            passwordError.setText("Password must be at least 6 characters.");
            valid = false;
        }

        if (departmentField.getValue() == null) {
            departmentError.setText("Please select a department.");
            valid = false;
        }

        if (statusField.getValue() == null) {
            statusError.setText("Please select a status.");
            valid = false;
        }

        String salaryText = salaryField.getText().trim();
        if (salaryText.isEmpty()) {
            salaryError.setText("Salary is required.");
            valid = false;
        } else {
            try {
                double salary = Double.parseDouble(salaryText);
                if (salary < 0) {
                    salaryError.setText("Salary cannot be negative.");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                salaryError.setText("Salary must be a number.");
                valid = false;
            }
        }

        if (workingDaysField.getText().trim().isEmpty()) {
            workingDaysError.setText("Working days are required.");
            valid = false;
        }

        if (workingHoursField.getText().trim().isEmpty()) {
            workingHoursError.setText("Working hours are required.");
            valid = false;
        }

        return valid;
    }


    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }


}
