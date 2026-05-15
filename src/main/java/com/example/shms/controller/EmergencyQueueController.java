package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.model.EmergencyQueue;
import com.example.shms.model.Patient;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.ResultSet;

public class EmergencyQueueController {
    @FXML private ListView<String> emergencyList;
    @FXML private ListView<String> urgentList;
    @FXML private ListView<String> normalList;
    @FXML private Label messageLabel;
    @FXML private Label nextPatient;
    @FXML private Button callNextBtn;
    private final EmergencyQueue queue = new EmergencyQueue();
    private final DatabaseManager db=DatabaseManager.getInstance();
    @FXML
    private void initialize() {
        loadQueueFromDB();
        refreshLists();
    }
    private void loadQueueFromDB() {
        try {
            var rs=db.getAllPatients();
            while (rs.next()){
                Patient p=new Patient();
                p.setPatientID(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                p.setPhone(rs.getString("phone"));
                p.setDepartment(rs.getString("department"));
                p.setBloodType(rs.getString("bloodType"));
                p.setAddress(rs.getString("address"));
                p.setPriority(rs.getInt("priority"));
                queue.addPatient(p);
            }
        } catch (Exception e) {
            System.out.println("Error loading queue"+e.getMessage());
        }
    }
    private void refreshLists() {
        ObservableList<String> emergency= FXCollections.observableArrayList();
        ObservableList<String> urgent= FXCollections.observableArrayList();
        ObservableList<String> normal= FXCollections.observableArrayList();
        for(Patient p:queue.getEmergencyQueue()){
            emergency.add(p.getName()+"-"+p.getDepartment());
        }
        for(Patient p:queue.getUrgentQueue()){
            urgent.add(p.getName()+"-"+p.getDepartment());
        }
        for(Patient p:queue.getNormalQueue()){
            normal.add(p.getName()+"-"+p.getDepartment());
        }
        emergencyList.setItems(emergency);
        urgentList.setItems(urgent);
        normalList.setItems(normal);
        Patient next=queue.peekNext();
        if(next!=null)
            nextPatient.setText("Next: "+next.getName());
        else
            nextPatient.setText("No next patient");
    }
    @FXML
    private void handleCallNext(){
        Patient next=queue.getNext();
        if(next==null){
            nextPatient.setText("No next patient");
            return;
        }
        String room=getAvailableRoom();
        db.updateStatus(next.getPatientID(),"With Doctor");
        messageLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 14; -fx-font-weight: bold;");
        messageLabel.setText(next.getName()+" please proceed to "+room);
        refreshLists();
    }
    private String getAvailableRoom(){
        try{
            var rs=db.getAvailableRoom();
            if(rs!=null && rs.next()){
                return "Room "+rs.getString("roomNumber");
            }
        } catch (Exception e) {
            System.out.println("Error getting available rooms"+e.getMessage());
        }
        return "Room 1";
    }
}
