package com.example.shms.controller;

import javafx.fxml.FXML;

public class AppointmentHistoryController {
    @FXML private javafx.scene.control.ComboBox<String> patientCombo;
    @FXML private javafx.scene.control.TableView<com.example.shms.model.Appointment> historyTable;
    @FXML private javafx.scene.control.TableColumn<com.example.shms.model.Appointment,String> colDate;
    @FXML private javafx.scene.control.TableColumn<com.example.shms.model.Appointment,String> colTime;
    @FXML private javafx.scene.control.TableColumn<com.example.shms.model.Appointment,String> colDoctor;
    @FXML private javafx.scene.control.TableColumn<com.example.shms.model.Appointment,String> colStatus;
    @FXML private javafx.scene.control.TableColumn<com.example.shms.model.Appointment,String> colNotes;

    private com.example.shms.database.DatabaseManager db = com.example.shms.database.DatabaseManager.getInstance();
    @FXML public void initialize(){
        colDate.setCellValueFactory(d-> new javafx.beans.property.SimpleStringProperty(d.getValue().getDate().toString()));
        colTime.setCellValueFactory(d-> new javafx.beans.property.SimpleStringProperty(d.getValue().getTime().toString()));
        colDoctor.setCellValueFactory(d-> new javafx.beans.property.SimpleStringProperty("Doctor"+d.getValue().getDoctorId()));
        colStatus.setCellValueFactory(d-> new javafx.beans.property.SimpleStringProperty(d.getValue().getStatus()));
        colNotes.setCellValueFactory(d-> new javafx.beans.property.SimpleStringProperty(d.getValue().getNotes()));
        loadpatients();
    }
    private void loadpatients(){
        try{
            java.sql.ResultSet rs = db.getAllPatients();
            while (rs != null && rs.next()){
                patientCombo.getItems().add(rs.getInt("patientID")+"-"+rs.getString("Name"));
            }
        }
        catch (Exception e){
            System.out.println("loadPatients errot:"+e.getMessage());
        }
    }
    @FXML private void handleSearch(){
        if (patientCombo.getValue()==null) return;
        int patientId = patientCombo.getSelectionModel().getSelectedIndex()+1;
        com.example.shms.utils.AppointmentHistory history = new com.example.shms.utils.AppointmentHistory(db);
        java.util.List<com.example.shms.model.Appointment> list = history.getHistory(patientId);
        historyTable.setItems(javafx.collections.FXCollections.observableArrayList(list));
    }
}
