package com.example.shms.controller;

import javafx.scene.control.Alert;

public class PharmacyController {
    @javafx.fxml.FXML private javafx.scene.control.TableView<com.example.shms.model.Medication> medicationTable;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.Medication, String> colName;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.Medication, String> colCategory;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.Medication, Integer> colStock;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.Medication, Integer> colMinStock;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.Medication, String> colPrice;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.Medication, String> colExpiry;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.Medication, String> colStatus;
    @javafx.fxml.FXML private javafx.scene.control.TableView<com.example.shms.model.ReorderRequest> reorderTable;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.ReorderRequest, String> colReorderMed;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.ReorderRequest, Integer> colQty;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.ReorderRequest, String> colSupplier;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.ReorderRequest, String> colReqDate;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.ReorderRequest, String> colExpDate;
    @javafx.fxml.FXML private javafx.scene.control.TableColumn<com.example.shms.model.ReorderRequest, String> colReorderStatus;
    @javafx.fxml.FXML private javafx.scene.control.Label totalMedsLabel;
    @javafx.fxml.FXML private javafx.scene.control.Label lowStockLabel;
    @javafx.fxml.FXML private javafx.scene.control.Label expiringLabel;
    @javafx.fxml.FXML private javafx.scene.control.Label pendingOrdersLabel;
    @javafx.fxml.FXML private javafx.scene.control.TextField searchField;

    private com.example.shms.database.DatabaseManager db =
            com.example.shms.database.DatabaseManager.getInstance();

    @javafx.fxml.FXML
    public void initialize() {
        setupMedicationTable();
        setupReorderTable();
        loadMedications();
        loadReorderRequests();
        loadStats();
    }

    private void setupMedicationTable() {
        colName.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getName() + "\n" + d.getValue().getForm()));
        colCategory.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getCategory()));
        colStock.setCellValueFactory(d ->
                new javafx.beans.property.SimpleIntegerProperty(d.getValue().getStock()).asObject());
        colMinStock.setCellValueFactory(d ->
                new javafx.beans.property.SimpleIntegerProperty(d.getValue().getMinStock()).asObject());
        colPrice.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        String.format("%.2f EGP", d.getValue().getUnitPrice())));
        colExpiry.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getExpiryDate()));
        colStatus.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getStatus()));
    }

    private void setupReorderTable() {
        colReorderMed.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getMedicationName()));
        colQty.setCellValueFactory(d ->
                new javafx.beans.property.SimpleIntegerProperty(
                        d.getValue().getQuantityRequested()).asObject());
        colSupplier.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getSupplier()));
        colReqDate.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getRequestDate()));
        colExpDate.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getExpectedDate()));
        colReorderStatus.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getStatus()));
    }

    private void loadMedications() {
        try {
            java.sql.ResultSet rs = db.getConnection().createStatement()
                    .executeQuery("SELECT * FROM medications ORDER BY name");
            java.util.List<com.example.shms.model.Medication> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(new com.example.shms.model.Medication(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("form"),
                        rs.getInt("stock"),
                        rs.getInt("minStock"),
                        rs.getDouble("unitPrice"),
                        rs.getString("expiryDate"),
                        rs.getString("status")
                ));
            }
            medicationTable.setItems(javafx.collections.FXCollections.observableArrayList(list));
        } catch (Exception e) {
            System.out.println("loadMedications error: " + e.getMessage());
        }
    }

    private void loadReorderRequests() {
        try {
            java.sql.ResultSet rs = db.getConnection().createStatement()
                    .executeQuery("SELECT * FROM reorderRequests ORDER BY requestDate DESC");
            java.util.List<com.example.shms.model.ReorderRequest> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(new com.example.shms.model.ReorderRequest(
                        rs.getInt("id"),
                        rs.getInt("medicationId"),
                        rs.getString("medicationName"),
                        rs.getInt("quantityRequested"),
                        rs.getString("supplier"),
                        rs.getString("requestDate"),
                        rs.getString("expectedDate"),
                        rs.getString("status")
                ));
            }
            reorderTable.setItems(javafx.collections.FXCollections.observableArrayList(list));
        } catch (Exception e) {
            System.out.println("loadReorders error: " + e.getMessage());
        }
    }

    private void loadStats() {
        try {
            java.sql.ResultSet rs = db.getConnection().createStatement()
                    .executeQuery("SELECT COUNT(*) as total," +
                            "SUM(CASE WHEN status='Low Stock' OR status='Out of Stock' THEN 1 ELSE 0 END) as low," +
                            "SUM(CASE WHEN status='Expiring Soon' THEN 1 ELSE 0 END) as expiring " +
                            "FROM medications");
            if (rs.next()) {
                totalMedsLabel.setText(String.valueOf(rs.getInt("total")));
                lowStockLabel.setText(String.valueOf(rs.getInt("low")));
                expiringLabel.setText(String.valueOf(rs.getInt("expiring")));
            }
            java.sql.ResultSet rs2 = db.getConnection().createStatement()
                    .executeQuery("SELECT COUNT(*) as pending FROM reorderRequests WHERE status='Pending'");
            if (rs2.next()) pendingOrdersLabel.setText(String.valueOf(rs2.getInt("pending")));
        } catch (Exception e) {
            System.out.println("loadStats error: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase();
        try {
            java.sql.ResultSet rs = db.getConnection().createStatement()
                    .executeQuery("SELECT * FROM medications WHERE LOWER(name) LIKE '%" + query + "%' " +
                            "OR LOWER(category) LIKE '%" + query + "%'");
            java.util.List<com.example.shms.model.Medication> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(new com.example.shms.model.Medication(
                        rs.getInt("id"), rs.getString("name"), rs.getString("category"),
                        rs.getString("form"), rs.getInt("stock"), rs.getInt("minStock"),
                        rs.getDouble("unitPrice"), rs.getString("expiryDate"), rs.getString("status")));
            }
            medicationTable.setItems(javafx.collections.FXCollections.observableArrayList(list));
        } catch (Exception e) {
            System.out.println("search error: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    private void filterAll() { loadMedications(); }

    @javafx.fxml.FXML
    private void filterLowStock() {
        filterByStatus("Low Stock");
    }

    @javafx.fxml.FXML
    private void filterExpiring() {
        filterByStatus("Expiring Soon");
    }

    @javafx.fxml.FXML
    private void filterInStock() {
        filterByStatus("In Stock");
    }
    @javafx.fxml.FXML
    private void handleAdd(){
        new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION,
                "Add medication form coming soon!").showAndWait();
    }

    private void filterByStatus(String status) {
        try {
            java.sql.ResultSet rs = db.getConnection().createStatement()
                    .executeQuery("SELECT * FROM medications WHERE status='" + status + "'");
            java.util.List<com.example.shms.model.Medication> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(new com.example.shms.model.Medication(
                        rs.getInt("id"), rs.getString("name"), rs.getString("category"),
                        rs.getString("form"), rs.getInt("stock"), rs.getInt("minStock"),
                        rs.getDouble("unitPrice"), rs.getString("expiryDate"), rs.getString("status")));
            }
            medicationTable.setItems(javafx.collections.FXCollections.observableArrayList(list));
        } catch (Exception e) {
            System.out.println("filter error: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    private void handleReorder() {
        com.example.shms.model.Medication selected =
                medicationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING,
                    "Please select a medication to reorder.").showAndWait();
            return;
        }
        try {
            String today = java.time.LocalDate.now().toString();
            String expected = java.time.LocalDate.now().plusDays(14).toString();
            db.getConnection().createStatement().executeUpdate(
                    "INSERT INTO reorderRequests (medicationId, medicationName, " +
                            "quantityRequested, supplier, requestDate, expectedDate, status) VALUES (" +
                            selected.getId() + ", '" + selected.getName() + "', 200, " +
                            "'MedSupply Co.', '" + today + "', '" + expected + "', 'Pending')");
            new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION,
                    "Reorder request created for " + selected.getName()).showAndWait();
            loadReorderRequests();
            loadStats();
        } catch (Exception e) {
            System.out.println("reorder error: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    private void handleReorderAll() {
        try {
            java.sql.ResultSet rs = db.getConnection().createStatement()
                    .executeQuery("SELECT * FROM medications WHERE status='Low Stock' OR status='Out of Stock'");
            String today = java.time.LocalDate.now().toString();
            String expected = java.time.LocalDate.now().plusDays(14).toString();
            int count = 0;
            while (rs.next()) {
                db.getConnection().createStatement().executeUpdate(
                        "INSERT INTO reorderRequests (medicationId, medicationName, " +
                                "quantityRequested, supplier, requestDate, expectedDate, status) VALUES (" +
                                rs.getInt("id") + ", '" + rs.getString("name") + "', 200, " +
                                "'MedSupply Co.', '" + today + "', '" + expected + "', 'Pending')");
                count++;
            }
            new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION,
                    count + " reorder requests created!").showAndWait();
            loadReorderRequests();
            loadStats();
        } catch (Exception e) {
            System.out.println("reorderAll error: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    private void handleBack() {
        com.example.shms.MainApp.navigateTo("dashboard", 1200, 700);
    }
}
