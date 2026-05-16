package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.EmergencyQueue;
import com.example.shms.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;


public class EmergencyQueueController {
    @FXML private Label messageLabel;
    @FXML private Label nextPatient;
    @FXML private Button callNextBtn;
    @FXML private Label emergencyCountLabel;
    @FXML private Label urgentCountLabel;
    @FXML private Label normalCountLabel;
    @FXML private Label queueCountLabel;
    @FXML private Label calledHeading;
    @FXML private Label calledEmptyLabel;
    @FXML private ListView<String>waitingListView;
    @FXML private ListView<String>calledListView;
    private final EmergencyQueue queue =EmergencyQueue.getInstance();
    private final DatabaseManager db=DatabaseManager.getInstance();
    private final ObservableList<String> calledItems=FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        calledListView.setItems(calledItems);
        waitingListView.setCellFactory(list->new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                    setStyle("");
                }else if(item.contains("Emergency")) {
                    setText(item);
                    setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold; " +
                            "-fx-background-color: #FEE2E2; -fx-background-radius: 8; " +
                            "-fx-padding: 10;");
                }else if(item.contains("Urgent")) {
                    setText(item);
                    setStyle("-fx-text-fill: #B45309; -fx-font-weight: bold; " +
                            "-fx-background-color: #FEF3C7; -fx-background-radius: 8; " +
                            "-fx-padding: 10;");
                }else if(item.contains("Normal")) {
                    setText(item);
                    setStyle("-fx-text-fill: #166534; -fx-font-weight: bold; " +
                            "-fx-background-color: #DCFCE7; -fx-background-radius: 8; " +
                            "-fx-padding: 10;");
                }
            }
        });
        loadQueueFromDB();
        refreshAll();
    }
    private void loadQueueFromDB() {
        try {
            var rs=db.getAllPatients();
            while (rs.next()){
                Patient p= new Patient();
                p.setPatientID(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                p.setPhone(rs.getString("phone"));
                p.setDepartment(rs.getString("department"));
                p.setBloodType(rs.getString("bloodType"));
                p.setAddress(rs.getString("address"));
                p.setPriority(rs.getInt("priority"));
                try{
                    queue.addPatient(p);
                }catch(Exception ex){
                    System.out.println("Skipping duplicate patient"+ex.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading queue"+e.getMessage());
        }
    }
    private void refreshAll() {
        refreshSummaryCards();
        refreshWaitingList();
        refreshNextPatientLabel();
    }
    private void refreshSummaryCards(){
        int e=queue.getEmergencyQueue().size();
        int u=queue.getUrgentQueue().size();
        int n=queue.getNormalQueue().size();
        emergencyCountLabel.setText(e+"");
        urgentCountLabel.setText(u+"");
        normalCountLabel.setText(n+"");
        queueCountLabel.setText("Waiting Queue ("+(e+u+n)+") ");
    }
    private void refreshWaitingList(){
        ObservableList<String> items=FXCollections.observableArrayList();
        for(Patient p:queue.getEmergencyQueue()){
            items.add(p.getName()+" Age: "+p.getAge()+" Blood: "+p.getBloodType()+" Emergency");
        }
        for(Patient p:queue.getUrgentQueue()){
            items.add(p.getName()+" Age: "+p.getAge()+" Blood: "+p.getBloodType()+ " Urgent");
        }
        for (Patient p:queue.getNormalQueue()){
            items.add(p.getName()+ " Age: "+p.getAge()+" Blood: "+p.getBloodType()+ " Normal");
        }
        waitingListView.setItems(items);
    }
    private void refreshNextPatientLabel(){
        Patient next=queue.peekNext();
        nextPatient.setText(next!=null? "Next: "+next.getName():"Next: None");
    }
    @FXML
    private void handleCallNext(){
        playDing();
        Patient next=queue.getNext();
        if(next==null){
            nextPatient.setText("No next patient");
            return;
        }
        String room=getAvailableRoom();
        db.updateStatus(next.getPatientID(),"With Doctor");
        calledItems.add(next.getName()+"  →  " + room);
        calledHeading.setText("Called ("+calledItems.size()+")");
        calledEmptyLabel.setVisible(false);
        messageLabel.setText(next.getName()+" please proceed to room "+room);
        refreshAll();
    }
    @FXML
    private void handleReset(){
        queue.clear();
        calledItems.clear();
        calledHeading.setText("Called (0)");
        calledEmptyLabel.setVisible(true);
        messageLabel.setText("");
        loadQueueFromDB();
        refreshAll();
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
    private void playDing(){
        try {
            var url=getClass().getResource("/com/example/shms/sounds/ding.mp3");
            if(url==null){
                return;
            }
            Media sound=new Media(url.toString());
            MediaPlayer player=new MediaPlayer(sound);
            player.play();
        } catch (Exception e) {
            System.out.println("Error playing sound "+e.getMessage());
        }
    }
    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
    }
}
