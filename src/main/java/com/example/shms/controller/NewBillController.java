package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class NewBillController implements Initializable {

    @FXML private ComboBox<String> patientBox;
    @FXML private ComboBox<String> doctorBox;
    @FXML private TextField serviceField;
    @FXML private TextField amountField;
    @FXML private Label errorLabel;

    private ObservableList<Bill> allBills;
    private TableView<Bill> billingTable;

    private final DatabaseManager db=DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        errorLabel.setText("");
        loadPatients();
        loadDoctors();
    }

    private void loadPatients(){
        patientBox.getItems().clear();
        try(Statement st=db.getConnection().createStatement()){
            ResultSet rs=st.executeQuery("SELECT name FROM patients ORDER BY name");
            while(rs.next()){
                patientBox.getItems().add(rs.getString("name"));
            }
        }catch(Exception e){
            errorLabel.setText("Could not load patients.");
        }
    }

    private void loadDoctors(){
        doctorBox.getItems().clear();
        try(Statement st=db.getConnection().createStatement()){
            ResultSet rs=st.executeQuery("SELECT name FROM doctors ORDER BY name");
            while(rs.next()){
                doctorBox.getItems().add(rs.getString("name"));
            }
        }catch(Exception e){
            errorLabel.setText("Could not load doctors.");
        }
    }

    public void init(ObservableList<Bill> allBills, TableView<Bill> billingTable){
        this.allBills=allBills;
        this.billingTable=billingTable;
    }

    @FXML
    private void handleBack(){
        ((Stage) serviceField.getScene().getWindow()).close();
    }

    @FXML
    private void handleSubmit(){
        errorLabel.setText("");

        String patientName=patientBox.getValue();
        String doctorName=doctorBox.getValue();
        String service=serviceField.getText().trim();
        String amountText=amountField.getText().trim();

        if(patientName==null || doctorName==null || service.isEmpty() || amountText.isEmpty()){
            errorLabel.setText("All fields are required.");
            return;
        }

        double amount;
        try{
            amount=Double.parseDouble(amountText);
            if(amount<=0){ errorLabel.setText("Amount must be greater than zero."); return; }
        }catch(NumberFormatException ex){
            errorLabel.setText("Please enter a valid numeric amount.");
            return;
        }

        try(Statement st=db.getConnection().createStatement()){
            ResultSet patientRs=st.executeQuery("SELECT id FROM patients WHERE name = '"+patientName+"' LIMIT 1");
            if(!patientRs.next()){ errorLabel.setText("Could not resolve patient."); return; }
            int patientId=patientRs.getInt("id");

            String today=java.time.LocalDate.now().toString();

            db.getConnection().createStatement().executeUpdate(
                    "INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date,amountPaid) "+
                            "VALUES ("+patientId+",'"+patientName+"','"+doctorName+"','"+service+"',"+amount+",'Pending','Pending','"+today+"',0.0)"
            );

            ResultSet rs=db.getConnection().createStatement().executeQuery("SELECT last_insert_rowid() AS newId");
            String billNum=rs.next() ? "#B-"+String.format("%03d",rs.getInt("newId")) : "#B-???";

            allBills.setAll(db.getAllBills());
            billingTable.refresh();
            ((Stage) serviceField.getScene().getWindow()).close();

        }catch(SQLException e){
            errorLabel.setText("Database error: "+e.getMessage());
        }
    }
}