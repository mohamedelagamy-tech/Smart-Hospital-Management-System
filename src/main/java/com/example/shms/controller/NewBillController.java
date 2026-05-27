package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class NewBillController implements Initializable {

    @FXML private TextField patientSearchField;
    @FXML private ListView<String> patientResults;
    @FXML private ComboBox<String> doctorBox;
    @FXML private TextField serviceField;
    @FXML private TextField amountField;
    @FXML private Label errorLabel;

    private ObservableList<Bill> allBills;
    private TableView<Bill> billingTable;
    private int selectedPatientId=-1;
    private String selectedPatientName="";

    private final DatabaseManager db=DatabaseManager.getInstance();

    @Override
    public void initialize(URL location,ResourceBundle resources){
        errorLabel.setText("");
        loadDoctors();
        patientResults.setVisible(false);
        patientResults.setManaged(false);
        patientResults.setPrefHeight(120);

        patientSearchField.textProperty().addListener((obs,oldVal,newVal)->{
            if(newVal.trim().isEmpty()){
                patientResults.setVisible(false);
                patientResults.setManaged(false);
                selectedPatientId=-1;
                return;
            }
            searchPatients(newVal.trim());
        });

        patientResults.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)->{
            if(newVal==null) return;
            try(Statement st=db.getConnection().createStatement()){
                String name=newVal.contains(" (ID:") ? newVal.substring(0,newVal.indexOf(" (ID:")) : newVal;
                ResultSet rs=st.executeQuery("SELECT id FROM patients WHERE name='"+name+"' LIMIT 1");
                if(rs.next()){
                    selectedPatientId=rs.getInt("id");
                    selectedPatientName=name;
                    patientSearchField.setText(name);
                    patientResults.setVisible(false);
                    patientResults.setManaged(false);
                }
            }catch(Exception e){
                System.out.println("Patient select error: "+e.getMessage());
            }
        });
    }
    private void searchPatients(String query){
        try(java.sql.Statement st=db.getConnection().createStatement()){
            java.sql.ResultSet rs=st.executeQuery(
                    "SELECT id, name FROM patients WHERE name LIKE '%"+query+"%' OR CAST(id AS TEXT) LIKE '%"+query+"%' LIMIT 6"
            );
            ObservableList<String> results=FXCollections.observableArrayList();
            while(rs.next())
                results.add(rs.getString("name")+" (ID: "+rs.getInt("id")+")");
            patientResults.setItems(results);
            patientResults.setVisible(!results.isEmpty());
            patientResults.setManaged(!results.isEmpty());
        }catch(Exception e){
            System.out.println("Patient search error: "+e.getMessage());
        }
    }
    private void loadDoctors(){
        doctorBox.getItems().clear();
        try(Statement st=db.getConnection().createStatement()){
            ResultSet rs=st.executeQuery("SELECT name FROM doctors ORDER BY name");
            while(rs.next())
                doctorBox.getItems().add(rs.getString("name"));
        }catch(Exception e){
            errorLabel.setText("Could not load doctors.");
        }
    }
    public void init(ObservableList<Bill> allBills,TableView<Bill> billingTable){
        this.allBills=allBills;
        this.billingTable=billingTable;
    }
    @FXML private void handleBack(){
        ((Stage) serviceField.getScene().getWindow()).close();
    }
    @FXML private void handleSubmit(){
        errorLabel.setText("");
        String doctorName=doctorBox.getValue();
        String service=serviceField.getText().trim();
        String amountText=amountField.getText().trim();

        if(selectedPatientId==-1){ errorLabel.setText("Please select a patient."); return; }
        if(doctorName==null){ errorLabel.setText("Please select a doctor."); return; }
        if(service.isEmpty()){ errorLabel.setText("Service is required."); return; }
        if(amountText.isEmpty()){ errorLabel.setText("Amount is required."); return; }

        double amount;
        try{
            amount=Double.parseDouble(amountText);
            if(amount<=0){ errorLabel.setText("Amount must be greater than zero."); return; }
        }catch(NumberFormatException ex){
            errorLabel.setText("Please enter a valid numeric amount.");
            return;
        }

        try{
            String today=java.time.LocalDate.now().toString();
            db.getConnection().createStatement().executeUpdate(
                    "INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date,amountPaid) "+
                            "VALUES ("+selectedPatientId+",'"+selectedPatientName+"','"+doctorName+"','"+service+"',"+amount+",'Pending','Pending','"+today+"',0.0)"
            );
            allBills.setAll(db.getAllBills());
            billingTable.refresh();
            ((Stage) serviceField.getScene().getWindow()).close();
        }catch(SQLException e){
            errorLabel.setText("Database error: "+e.getMessage());
        }
    }
}