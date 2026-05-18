package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.MedicalRecord;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MedicalRecordsController implements Initializable {

    @FXML private TextField searchField;

    @FXML private TableView<MedicalRecord>              recordsTable;
    @FXML private TableColumn<MedicalRecord, String>    colNumber;
    @FXML private TableColumn<MedicalRecord, String>    colPatient;
    @FXML private TableColumn<MedicalRecord, String>    colDoctor;
    @FXML private TableColumn<MedicalRecord, String>    colDate;
    @FXML private TableColumn<MedicalRecord, String>    colDiagnosis;
    @FXML private TableColumn<MedicalRecord, String>    colTreatment;
    @FXML private TableColumn<MedicalRecord, String>    colStatus;

    private final ObservableList<MedicalRecord> allRecords = FXCollections.observableArrayList();
    private final DatabaseManager db = DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupColumns();
        loadFromDatabase();
        styleTableHeader();
        recordsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    private void setupColumns() {

        colNumber.setCellValueFactory(cell -> {
            int index = recordsTable.getItems().indexOf(cell.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(index));
        });
        colNumber.setStyle("-fx-alignment: CENTER;");


        colPatient.setCellValueFactory(cell ->
                new SimpleStringProperty("P-" + cell.getValue().getPatientId()));

        colDoctor.setCellValueFactory(cell ->
                new SimpleStringProperty("Dr. ID: " + cell.getValue().getDoctorId()));


        colDate.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDate()));

        colDiagnosis.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDiagnosis()));

        colTreatment.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getTreatment()));

        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { setText(null); setGraphic(null); return; }
                Label badge = new Label(status);
                badge.setPadding(new Insets(3, 10, 3, 10));
                badge.setStyle(badgeStyle(status));
                setGraphic(badge);
                setText(null);
                setAlignment(Pos.CENTER_LEFT);
            }
        });
        colStatus.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getStatus()));

        recordsTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(MedicalRecord item, boolean empty) {
                super.updateItem(item, empty);
                setStyle((!empty && getIndex() % 2 != 0)
                        ? "-fx-background-color: #f7f9fc;"
                        : "-fx-background-color: white;");
            }
        });

        recordsTable.setFixedCellSize(52);
        recordsTable.setItems(allRecords);
    }

    private void loadFromDatabase() {
        allRecords.clear();
        List<MedicalRecord> list = db.getAllMedicalRecords();
        if (list != null) allRecords.addAll(list);
    }

    @FXML
    private void onSearch(KeyEvent event) {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            recordsTable.setItems(allRecords);
            return;
        }

        ObservableList<MedicalRecord> filtered = FXCollections.observableArrayList();
        for (MedicalRecord r : allRecords) {
            String patientId = "p-" + r.getPatientId();
            String doctorId = "dr. id: " + r.getDoctorId();
            if (patientId.contains(query)
                    || doctorId.contains(query)
                    || r.getDiagnosis().toLowerCase().contains(query)
                    || r.getTreatment().toLowerCase().contains(query)
                    || r.getStatus().toLowerCase().contains(query)
                    || r.getDate().contains(query)) {
                filtered.add(r);
            }

        }


        recordsTable.setItems(filtered);

    }
    private void styleTableHeader() {
        recordsTable.widthProperty().addListener((obs, o, n) -> {
            javafx.scene.Node header = recordsTable.lookup("TableHeaderRow");
            if (header != null) {
                header.setStyle("-fx-background-color: #1a6fba;");
                header.lookupAll(".column-header .label").forEach(node ->
                        node.setStyle("-fx-text-fill: white; -fx-font-weight: bold;"));
            }
        });
    }

    private String badgeStyle(String status) {
        String base = "-fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 20;";
        return switch (status) {
            case "Active"    -> base + "-fx-text-fill: #1a6fba; -fx-background-color: #dbeafe;";
            case "Completed" -> base + "-fx-text-fill: #16a34a; -fx-background-color: #dcfce7;";
            case "Follow-up" -> base + "-fx-text-fill: #d97706; -fx-background-color: #fef3c7;";
            default          -> base + "-fx-text-fill: #555555; -fx-background-color: #eeeeee;";
        };
    }

    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
    }
}

