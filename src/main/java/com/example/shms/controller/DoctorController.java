package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;

public class DoctorController {

    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Doctor, Integer> idCol;
    @FXML private TableColumn<Doctor, String>  nameCol;
    @FXML private TableColumn<Doctor, String>  departmentCol;
    @FXML private TableColumn<Doctor, Double>  salaryCol;
    @FXML private TableColumn<Doctor, String>  workingDaysCol;
    @FXML private TableColumn<Doctor, String>  workingHoursCol;
    @FXML private TableColumn<Doctor, Double>  ratingCol;
    @FXML private TableColumn<Doctor, String>  statusCol;
    @FXML private TableColumn<Doctor, Void>    actionsCol;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> departmentFilter;
    @FXML private ComboBox<String> sortCombo;
    @FXML private Button allBtn, availableBtn, busyBtn, inSurgeryBtn, addBtn, resetBtn;
    @FXML private Label statusLabel;

    private final DatabaseManager db = DatabaseManager.getInstance();
    private ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    private String currentStatusFilter = "all";

    @FXML
    private void initialize() {
        setupColumns();
        setupActions();
        setupDepartmentFilter();
        loadDoctors();
        setupSearch();
        sortCombo.getItems().addAll("ID", "Name", "Department", "Salary", "Rating");
        sortCombo.setValue("ID");
        sortCombo.setOnAction(e -> sortBy(sortCombo.getValue()));
    }

    private void setupColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        workingDaysCol.setCellValueFactory(new PropertyValueFactory<>("workingDays"));
        workingHoursCol.setCellValueFactory(new PropertyValueFactory<>("workingHours"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("averageRating"));

        // Rating column — formatted to 2 decimal places
        ratingCol.setCellFactory(col -> new TableCell<Doctor, Double>() {
            @Override
            protected void updateItem(Double rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f", rating));
                }
            }
        });

        // Status column — color coded green / orange / red
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<Doctor, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "Available"  -> setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
                        case "Busy"       -> setStyle("-fx-text-fill: #D97706; -fx-font-weight: bold;");
                        case "In Surgery" -> setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold;");
                        default           -> setStyle("");
                    }
                }
            }
        });
    }

    private void setupActions() {
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn   = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox   buttons   = new HBox(5, editBtn, deleteBtn);
            {
                editBtn.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11;");
                deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11;");
                editBtn.setOnAction(e -> {
                    Doctor d = getTableView().getItems().get(getIndex());
                    handleEditDoctor(d);
                });
                deleteBtn.setOnAction(e -> {
                    Doctor d = getTableView().getItems().get(getIndex());
                    handleDeleteDoctor(d);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size())
                    setGraphic(null);
                else
                    setGraphic(buttons);
            }
        });
    }

    private void setupDepartmentFilter() {
        departmentFilter.getItems().addAll("All", "Cardiology", "Pediatrics", "Emergency",
                "Neurology", "Orthopedics", "Dermatology", "Oncology", "Radiology");
        departmentFilter.setValue("All");
        departmentFilter.setOnAction(e -> filterDoctors());
    }

    public void loadDoctors() {
        doctorList.clear();
        try {
            ResultSet rs = db.getAllDoctors();
            while (rs.next()) {
                Doctor d = new Doctor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("department"),
                        rs.getString("status"),
                        rs.getDouble("salary"),
                        rs.getString("workingDays"),
                        rs.getString("workingHours"),
                        rs.getDouble("totalRating"),
                        rs.getInt("ratingCount")
                );
                doctorList.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Error loading doctors: " + e.getMessage());
        }
        Collections.sort(doctorList);
        filterDoctors();
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterDoctors());
    }

    private void filterDoctors() {
        String keyword    = searchField.getText().toLowerCase();
        String department = departmentFilter.getValue();
        ObservableList<Doctor> filtered = FXCollections.observableArrayList();

        for (Doctor d : doctorList) {
            boolean matchSearch     = d.getName().toLowerCase().contains(keyword)
                    || String.valueOf(d.getID()).contains(keyword);
            boolean matchDepartment = department == null || department.equals("All")
                    || department.equals(d.getDepartment());
            boolean matchStatus     = currentStatusFilter.equals("all")
                    || d.getStatus().equalsIgnoreCase(currentStatusFilter);
            if (matchSearch && matchDepartment && matchStatus)
                filtered.add(d);
        }
        doctorTable.setItems(filtered);
        doctorTable.refresh();
        statusLabel.setText("Showing: " + filtered.size() + " of " + doctorList.size() + " doctors");
    }

    @FXML private void handleAll()       { currentStatusFilter = "all";        filterDoctors(); }
    @FXML private void handleAvailable() { currentStatusFilter = "Available";  filterDoctors(); }
    @FXML private void handleBusy()      { currentStatusFilter = "Busy";       filterDoctors(); }
    @FXML private void handleInSurgery() { currentStatusFilter = "In Surgery"; filterDoctors(); }

    @FXML
    private void handleReset() {
        searchField.clear();
        departmentFilter.setValue("All");
        currentStatusFilter = "all";
        sortCombo.setValue("ID");
        Collections.sort(doctorList);
        filterDoctors();
    }

    @FXML
    private void handleAddDoctor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DoctorForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Doctor");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            DoctorFormController formController = loader.getController();
            formController.setDoctorController(this);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Error opening add form: " + e.getMessage());
        }
    }

    private void handleEditDoctor(Doctor d) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DoctorForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Doctor");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            DoctorFormController formController = loader.getController();
            formController.setDoctorController(this);
            formController.setDoctor(d);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Error opening edit form: " + e.getMessage());
        }
    }

    private void handleDeleteDoctor(Doctor d) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Doctor");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Delete " + d.getName() + "?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                db.removeDoctor(d.getID());
                loadDoctors();
            }
        });
    }

    private void sortBy(String mode) {
        switch (mode) {
            case "ID"         -> Collections.sort(doctorList);
            case "Name"       -> doctorList.sort(Doctor.By_NAME);
            case "Department" -> doctorList.sort(Doctor.By_DEPARTMENT);
            case "Salary"     -> doctorList.sort(Doctor.By_SALARY);
            case "Rating"     -> doctorList.sort(Doctor.By_AVERAGE_RATING);
        }
        filterDoctors();
    }

    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
    }
}

