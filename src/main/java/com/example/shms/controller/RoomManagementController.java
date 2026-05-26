package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Room;
import com.example.shms.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RoomManagementController implements Initializable {

    @FXML private TextField searchField;
    @FXML private FlowPane cardsPane;
    @FXML private ScrollPane scrollPane;
    @FXML private ToggleButton btnAll;
    @FXML private ToggleButton btnAvailable;
    @FXML private ToggleButton btnOccupied;
    @FXML private ToggleButton btnCleaning;

    private final List<Room> allRooms=new ArrayList<>();
    private final List<VBox> allCards=new ArrayList<>();
    private final DatabaseManager db=DatabaseManager.getInstance();
    private String currentFilter="All";

    @Override
    public void initialize(URL location,ResourceBundle resources){
        loadRoomsFromDatabase();
        buildAllCards();
        cardsPane.setPrefWrapLength(1100);
        setupFilterButtons();
    }
    private void setupFilterButtons(){
        ToggleGroup group=new ToggleGroup();
        btnAll.setToggleGroup(group);
        btnAvailable.setToggleGroup(group);
        btnOccupied.setToggleGroup(group);
        btnCleaning.setToggleGroup(group);
        btnAll.setSelected(true);
        group.selectedToggleProperty().addListener((obs,oldVal,newVal)->{
            if(newVal==null){ group.selectToggle(oldVal); return; }
            currentFilter=((ToggleButton)newVal).getText();
            applyFilter();
        });
    }
    private void loadRoomsFromDatabase(){
        allRooms.clear();
        List<Room> rooms=db.getAllRooms();
        if(rooms!=null) allRooms.addAll(rooms);
    }
    private void buildAllCards(){
        cardsPane.getChildren().clear();
        allCards.clear();
        for(Room room:allRooms){
            VBox card=buildCard(room);
            allCards.add(card);
            cardsPane.getChildren().add(card);
        }
    }
    private VBox buildCard(Room room){
        Label accentBar=new Label();
        accentBar.setMaxWidth(Double.MAX_VALUE);
        accentBar.setPrefHeight(5);
        accentBar.setStyle("-fx-background-color: "+accentColor(room.getRoomStatus())+";"+
                "-fx-background-radius: 10 10 0 0;");

        Label roomLabel=new Label("Room "+room.getRoomNumber());
        roomLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1a1d23;");

        Label badge=new Label(room.getRoomStatus());
        badge.setStyle(badgeStyle(room.getRoomStatus()));

        HBox topRow=new HBox(8,roomLabel,badge);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label typeLabel=new Label(room.getRoomtype()!=null ? room.getRoomtype() : "General");
        typeLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #1F4E79;"+
                "-fx-background-color: #e8f0fb; -fx-background-radius: 4; -fx-padding: 2 8 2 8;");

        Label detailLabel=new Label(getDetailText(room));
        detailLabel.setStyle(isOccupied(room.getRoomStatus())
                ? "-fx-font-size: 12px; -fx-text-fill: #4a5568; -fx-font-weight: bold;"
                : "-fx-font-size: 12px; -fx-text-fill: #9aa5b4;");

        VBox content=new VBox(6,topRow,typeLabel,detailLabel);
        content.setPadding(new Insets(14));

        VBox card=new VBox(0,accentBar,content);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10;"+
                "-fx-border-color: #eeeeee; -fx-border-radius: 10; -fx-border-width: 1;");
        card.setPrefSize(228,140);
        card.setUserData(room);

        if(room.getRoomStatus().equalsIgnoreCase("Available")){
            Button assignBtn=new Button("Assign Patient");
            assignBtn.setStyle("-fx-background-color: #1F4E79; -fx-text-fill: white;"+
                    "-fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand; -fx-padding: 5 12;");
            assignBtn.setOnAction(e->showAssignDialog(room));
            content.getChildren().add(assignBtn);
        }
        return card;
    }
    private String accentColor(String status){
        if(status==null) return "#9aa5b4";
        return switch(status.toLowerCase()){
            case "occupied"->"#e74c3c";
            case "available"->"#27ae60";
            case "cleaning"->"#f97316";
            default->"#9aa5b4";
        };
    }
    private String getDetailText(Room room){
        if(isOccupied(room.getRoomStatus())){
            int patientId=room.getAssignedPatientId();
            if(patientId>0){
                String name=db.getPatientName(patientId);
                return name!=null ? "👤 "+name : "Patient ID: "+patientId;
            }
            return "Occupied";
        }else if(room.getRoomStatus().equalsIgnoreCase("Cleaning")){
            return "Being cleaned";
        }else{
            return "Ready for admission";
        }
    }
    private void applyFilter(){
        String query=searchField.getText().trim().toLowerCase();
        for(VBox card:allCards){
            Room room=(Room)card.getUserData();
            boolean matchFilter=currentFilter.equals("All")||room.getRoomStatus().equalsIgnoreCase(currentFilter);
            boolean matchSearch=query.isEmpty()
                    ||String.valueOf(room.getRoomNumber()).contains(query)
                    ||room.getRoomStatus().toLowerCase().contains(query)
                    ||(room.getRoomtype()!=null&&room.getRoomtype().toLowerCase().contains(query));
            card.setVisible(matchFilter&&matchSearch);
            card.setManaged(matchFilter&&matchSearch);
        }
    }
    @FXML private void onSearch(KeyEvent event){
        applyFilter();
    }
    @FXML public void onRefresh(){
        loadRoomsFromDatabase();
        buildAllCards();
    }
    private boolean isOccupied(String status){
        return status!=null&&status.equalsIgnoreCase("Occupied");
    }
    private String badgeStyle(String status){
        String base="-fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold;"+
                "-fx-background-radius: 20; -fx-padding: 3 10 3 10;";
        if(status==null) return base+"-fx-background-color: #9aa5b4;";
        return switch(status.toLowerCase()){
            case "occupied"->base+"-fx-background-color: #e74c3c;";
            case "available"->base+"-fx-background-color: #27ae60;";
            case "cleaning"->base+"-fx-background-color: #f97316;";
            default->base+"-fx-background-color: #9aa5b4;";
        };
    }
    private void showAssignDialog(Room room){
        TextInputDialog dialog=new TextInputDialog();
        dialog.setTitle("Assign Room");
        dialog.setHeaderText("Assign Room "+room.getRoomNumber());
        dialog.setContentText("Enter Patient ID:");
        dialog.showAndWait().ifPresent(input->{
            try{
                int patientId=Integer.parseInt(input.trim());
                db.assignRoom(room.getId(),patientId);
                loadRoomsFromDatabase();
                buildAllCards();
            }catch(NumberFormatException e){
                new Alert(Alert.AlertType.ERROR,"Invalid Patient ID").show();
            }
        });
    }
    @FXML private void handleBack(){
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }
}