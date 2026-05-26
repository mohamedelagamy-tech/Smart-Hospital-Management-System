package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Department;
import com.example.shms.model.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentController {

    @FXML private TableView<Department> departmentTable;
    @FXML private TableColumn<Department, Integer> idCol;
    @FXML private TableColumn<Department, String> nameCol;
    @FXML private TableColumn<Department, Integer> doctorCountCol;
    @FXML private TextField searchField;
    @FXML private Label statsLabel;
    @FXML private Label detailNameLabel;
    @FXML private ListView<String> doctorListView;

    private final DatabaseManager db = DatabaseManager.getInstance();
    private ObservableList<Department> departmentList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupColumns();
        loadDepartments();
        setupSearch();
        setupSelectionListener();
    }

    private void setupColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("departmentID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        doctorCountCol.setCellFactory(col -> new TableCell<Department, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Department dept = getTableView().getItems().get(getIndex());
                    setText(String.valueOf(dept.getDoctors().size()));
                }
            }
        });
    }

    private void loadDepartments() {
        departmentList.clear();
        try {
            ResultSet rs = db.getConnection().createStatement()
                    .executeQuery("SELECT * FROM departments ORDER BY depName");
            while (rs.next()) {
                Department dept = new Department(
                        rs.getInt("id"),
                        rs.getString("depName"),
                        "",
                        ""
                );
                ResultSet docRs = db.getDoctorsByDepartment(dept.getDepartmentName());
                if (docRs != null) {
                    while (docRs.next()) {
                        Doctor doc = new Doctor(
                                docRs.getInt("id"),
                                docRs.getString("name"),
                                docRs.getString("email"),
                                docRs.getString("password"),
                                docRs.getString("department"),
                                docRs.getString("status"),
                                docRs.getDouble("salary"),
                                docRs.getString("workingDays"),
                                docRs.getString("workingHours"),
                                docRs.getDouble("totalRating"),
                                docRs.getInt("ratingCount")
                        );
                        dept.addDoctor(doc);
                    }
                }
                departmentList.add(dept);
            }
        } catch (SQLException e) {
            System.out.println("Error loading departments: " + e.getMessage());
        }
        departmentTable.setItems(departmentList);
        statsLabel.setText("Showing " + departmentList.size() + " of " + departmentList.size() + " departments");
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterDepartments());
    }

    private void filterDepartments() {
        String keyword = searchField.getText().toLowerCase();
        ObservableList<Department> filtered = FXCollections.observableArrayList();
        for (Department d : departmentList) {
            if (d.getDepartmentName().toLowerCase().contains(keyword))
                filtered.add(d);
        }
        departmentTable.setItems(filtered);
        departmentTable.refresh();
        statsLabel.setText("Showing " + filtered.size() + " of " + departmentList.size() + " departments");
    }

    private void setupSelectionListener() {
        departmentTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, selected) -> {
                    if (selected != null)
                        showDepartmentDetail(selected);
                });
    }

    private void showDepartmentDetail(Department dept) {
        detailNameLabel.setText(dept.getDepartmentName());
        ObservableList<String> doctorItems = FXCollections.observableArrayList();
        for (Doctor d : dept.getDoctors()) {
            doctorItems.add(d.getName() + "  |  " + d.getStatus());
        }
        if (doctorItems.isEmpty())
            doctorItems.add("No doctors assigned to this department.");
        doctorListView.setItems(doctorItems);
        doctorListView.setCellFactory(col -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("Available"))
                        setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
                    else if (item.contains("Busy"))
                        setStyle("-fx-text-fill: #D97706; -fx-font-weight: bold;");
                    else if (item.contains("In Surgery"))
                        setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold;");
                    else
                        setStyle("");
                }
            }
        });
    }

    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
    }
}