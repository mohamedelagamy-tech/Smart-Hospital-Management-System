package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Bill;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class NewBillController implements Initializable {

    @FXML private ComboBox<String> patientBox;
    @FXML private TextField        serviceField;
    @FXML private TextField        amountField;
    @FXML private ComboBox<String> statusBox;

    private ObservableList<Bill>  allBills;
    private TableView<Bill>       billingTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientBox.getItems().addAll(
                "Sarah Johnson",
                "Michael Chen",
                "Emma Davis",
                "Robert Taylor"
        );

        statusBox.getItems().addAll("Pending", "Paid", "Overdue");
        statusBox.setValue("Pending");
    }

    public void init(ObservableList<Bill> allBills, TableView<Bill> billingTable) {
        this.allBills     = allBills;
        this.billingTable = billingTable;
    }
    @FXML
    private void handleBack() {
        ((Stage) serviceField.getScene().getWindow()).close();
    }

    @FXML
    private void handleSubmit() {
        String selectedPatient = patientBox.getValue();
        String service         = serviceField.getText().trim();
        String amountText      = amountField.getText().trim();
        String status          = statusBox.getValue();

        if (selectedPatient == null || service.isEmpty() || amountText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "All fields are required.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Please enter a valid number for the amount.");
            return;
        }

        String billNum = "INV-" + (allBills.size() + 1);
        String today   = java.time.LocalDate.now().toString();
        Bill newBill   = new Bill(billNum, selectedPatient, service, today, amount, status);
        DatabaseManager db = DatabaseManager.getInstance();
        allBills.add(newBill);
        billingTable.setItems(allBills);
        db.addBill(newBill);
        ((Stage) serviceField.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
