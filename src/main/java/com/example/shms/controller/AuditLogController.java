package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AuditLogController implements Initializable {

    @FXML private TableView<ObservableList<String>> auditTable;
    @FXML private TableColumn<ObservableList<String>,String> colId;
    @FXML private TableColumn<ObservableList<String>,String> colUsername;
    @FXML private TableColumn<ObservableList<String>,String> colRole;
    @FXML private TableColumn<ObservableList<String>,String> colStatus;
    @FXML private TableColumn<ObservableList<String>,String> colTimestamp;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private Label recordCountLabel;

    private final DatabaseManager db = DatabaseManager.getInstance();
    private ObservableList<ObservableList<String>> allData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupColumns();
        setupFilters();
        loadData();
    }

    private void setupColumns() {
        colId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> data) {
                return new SimpleStringProperty(data.getValue().get(0));
            }
        });

        colUsername.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> data) {
                return new SimpleStringProperty(data.getValue().get(1));
            }
        });

        colRole.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> data) {
                return new SimpleStringProperty(data.getValue().get(2));
            }
        });

        colStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> data) {
                return new SimpleStringProperty(data.getValue().get(3));
            }
        });

        colStatus.setCellFactory(new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
            @Override
            public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> col) {
                return new TableCell<ObservableList<String>, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle("");
                        } else if (item.equals("SUCCESS")) {
                            setText(item);
                            setStyle("-fx-text-fill: #1D6A2E; -fx-font-weight: bold;");
                        } else {
                            setText(item);
                            setStyle("-fx-text-fill: #A32D2D; -fx-font-weight: bold;");
                        }
                    }
                };
            }
        });

        colTimestamp.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> data) {
                return new SimpleStringProperty(data.getValue().get(4));
            }
        });
    }

    private void setupFilters() {
        statusFilter.setItems(FXCollections.observableArrayList("All Statuses","SUCCESS","FAILED"));
        statusFilter.setValue("All Statuses");

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String oldVal, String newVal) {
                filterData();
            }
        });

        statusFilter.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String oldVal, String newVal) {
                filterData();
            }
        });
    }

    private void loadData() {
        allData.clear();
        try (Statement st = db.getConnection().createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM auditLog ORDER BY id DESC");
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(String.valueOf(rs.getInt("id")));
                row.add(rs.getString("username"));
                row.add(rs.getString("role"));
                row.add(rs.getString("status"));
                row.add(rs.getString("timestamp"));
                allData.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Audit log load failed: " + e.getMessage());
        }
        auditTable.setItems(allData);
        recordCountLabel.setText("Showing " + allData.size() + " records");
    }

    private void filterData() {
        String search = searchField.getText().toLowerCase();
        String status = statusFilter.getValue();
        ObservableList<ObservableList<String>> filtered = FXCollections.observableArrayList();
        for (ObservableList<String> row : allData) {
            boolean matchSearch = row.get(1).toLowerCase().contains(search);
            boolean matchStatus = status.equals("All Statuses") || row.get(3).equals(status);
            if (matchSearch && matchStatus) {
                filtered.add(row);
            }
        }
        auditTable.setItems(filtered);
        recordCountLabel.setText("Showing "+filtered.size()+" records");
    }

    @FXML
    private void handleClearLog() {
        try (Statement st = db.getConnection().createStatement()) {
            st.execute("DELETE FROM auditLog");
            loadData();
        } catch (SQLException e) {
            System.out.println("Failed to clear log: " + e.getMessage());
        }
    }

    @FXML
    private void handleExport() {
        try (FileWriter fw = new FileWriter("audit_log_export.txt")) {
            fw.write("Audit Log Export\n");
            for (ObservableList<String> row : allData) {
                fw.write("ID: " + row.get(0) + " | User: " + row.get(1) + " | Role: " + row.get(2) + " | Status: " + row.get(3) + " | Time: " + row.get(4) + "\n");
            }
            recordCountLabel.setText("Exported successfully!");
        } catch (IOException e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }
    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
    }
}