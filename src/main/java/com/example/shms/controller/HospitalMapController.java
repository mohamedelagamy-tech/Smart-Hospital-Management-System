package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.SessionManager;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HospitalMapController implements Initializable {

    @FXML private VBox floorSidebar;
    @FXML private Label mapTitleLabel;
    @FXML private Label statAvailable;
    @FXML private Label statOccupied;
    @FXML private Label statCleaning;
    @FXML private Label statMaintenance;
    @FXML private FlowPane roomsPane;
    @FXML private Label liveLabel;

    private final DatabaseManager db = DatabaseManager.getInstance();
    private int currentFloor=1;
    private Timeline autoRefresh;

    private static final String[] FLOOR_NAMES={
            "Ground — Emergency",
            "Floor 1 — ICU",
            "Floor 2 — General",
            "Floor 3 — Private",
            "Floor 4 — Emergency",
            "Floor 5 — General",
            "Floor 6 — Private",
            "Floor 7 — ICU",
            "Floor 8 — Emergency",
            "Floor 9 — General",
            "Floor 10 — Private"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildFloorSidebar();
        loadFloor(1);
        startAutoRefresh();
    }

    private void buildFloorSidebar() {
        floorSidebar.getChildren().clear();

        Label title = new Label("FLOOR");
        title.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #AAAAAA; -fx-padding: 8 8 4 8;");
        floorSidebar.getChildren().add(title);

        int[] floors = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        String[] labels = {"Floor 1\nICU", "Floor 2\nGeneral", "Floor 3\nPrivate",
                "Floor 4\nEmergency", "Floor 5\nGeneral", "Floor 6\nPrivate",
                "Floor 7\nICU", "Floor 8\nEmergency", "Floor 9\nGeneral",
                "Floor 10\nPrivate"};

        for (int i = 0; i < floors.length; i++) {
            final int floor = floors[i];
            Button btn = new Button(labels[i]);
            btn.setId("floorBtn" + floor);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle(floor == 1
                    ? "-fx-background-color: #1F4E79; -fx-text-fill: white; -fx-font-size: 11px; -fx-alignment: CENTER_LEFT; -fx-padding: 9 12; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;"
                    : "-fx-background-color: transparent; -fx-text-fill: #555555; -fx-font-size: 11px; -fx-alignment: CENTER_LEFT; -fx-padding: 9 12; -fx-background-radius: 8; -fx-cursor: hand;");
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    selectFloor(floor);
                }
            });
            floorSidebar.getChildren().add(btn);
        }

        Button emergencyBtn = new Button("🚨 Emergency\nGround Floor");
        emergencyBtn.setId("floorBtn0");
        emergencyBtn.setMaxWidth(Double.MAX_VALUE);
        emergencyBtn.setStyle("-fx-background-color: #FCEBEB; -fx-text-fill: #A32D2D; -fx-font-size: 11px; -fx-alignment: CENTER_LEFT; -fx-padding: 9 12; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;");
        emergencyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectFloor(4);
            }
        });
        floorSidebar.getChildren().add(emergencyBtn);
    }

    private void selectFloor(int floor) {
        currentFloor = floor;

        for (javafx.scene.Node node : floorSidebar.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (btn.getId() != null && btn.getId().equals("floorBtn" + floor)) {
                    btn.setStyle("-fx-background-color: #1F4E79; -fx-text-fill: white; -fx-font-size: 11px; -fx-alignment: CENTER_LEFT; -fx-padding: 9 12; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;");
                } else if (btn.getId() != null && btn.getId().equals("floorBtn0")) {
                    btn.setStyle("-fx-background-color: #FCEBEB; -fx-text-fill: #A32D2D; -fx-font-size: 11px; -fx-alignment: CENTER_LEFT; -fx-padding: 9 12; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;");
                } else {
                    btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-font-size: 11px; -fx-alignment: CENTER_LEFT; -fx-padding: 9 12; -fx-background-radius: 8; -fx-cursor: hand;");
                }
            }
        }
        loadFloor(floor);
    }

    private void loadFloor(int floor) {
        roomsPane.getChildren().clear();

        int minRoom = floor * 100;
        int maxRoom = floor * 100 + 99;

        int available = 0, occupied = 0, cleaning = 0, maintenance = 0;
        List<RoomData> rooms = new ArrayList<>();

        try (Statement st = db.getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(
                    "SELECT r.*, p.name as patientName, p.department as patientDept " +
                            "FROM rooms r LEFT JOIN patients p ON r.assignedPatientID = p.id " +
                            "WHERE CAST(r.roomNumber AS INTEGER) >= " + minRoom +
                            " AND CAST(r.roomNumber AS INTEGER) <= " + maxRoom +
                            " ORDER BY CAST(r.roomNumber AS INTEGER)");

            while (rs.next()) {
                RoomData room = new RoomData();
                room.id = rs.getInt("id");
                room.number = rs.getString("roomNumber");
                room.type = rs.getString("roomtype");
                room.status = rs.getString("status");
                room.patientName = rs.getString("patientName");
                room.patientDept = rs.getString("patientDept");
                room.patientId = rs.getInt("assignedPatientID");
                rooms.add(room);

                switch (room.status != null ? room.status.toLowerCase() : "") {
                    case "available": available++; break;
                    case "occupied": occupied++; break;
                    case "cleaning": cleaning++; break;
                    case "maintenance": maintenance++; break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Map load failed: " + e.getMessage());
        }

        statAvailable.setText(String.valueOf(available));
        statOccupied.setText(String.valueOf(occupied));
        statCleaning.setText(String.valueOf(cleaning));
        statMaintenance.setText(String.valueOf(maintenance));

        String floorName = floor <= 10 ? "Floor " + floor : "Floor " + floor;
        mapTitleLabel.setText("🛏  Hospital Map — Floor " + floor);

        addFacilityCard("🛗","ELEVATOR","Lift","#E3F2FD","#42A5F5");
        addFacilityCard("👩‍⚕️","NURSES\nSTATION","Station", "#E8F5E9","#4CAF50");
        addFacilityCard("🚻","BATHROOM","Facility","#F3E5F5","#AB47BC");
        addFacilityCard("🍽️","PANTRY","Facility","#FFF8E1","#FFB300");

        for(RoomData room : rooms){
            addRoomCard(room);
        }

        FadeTransition fade = new FadeTransition(Duration.millis(200), roomsPane);
        fade.setFromValue(0.6);
        fade.setToValue(1.0);
        fade.play();
    }

    private void addFacilityCard(String icon, String name, String type, String bg, String border){
        VBox card=new VBox(4);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(90, 80);
        card.setPadding(new Insets(8));
        card.setStyle("-fx-background-color: "+bg+"; -fx-background-radius: 8; " +
                "-fx-border-color: "+border+"; -fx-border-radius: 8; -fx-border-width: 2;");

        Label iconLabel=new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        Label nameLabel=new Label(name);
        nameLabel.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #333; -fx-text-alignment: center;");
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setWrapText(true);

        card.getChildren().addAll(iconLabel,nameLabel);
        roomsPane.getChildren().add(card);
    }

    private void addRoomCard(RoomData room){
        String status=room.status != null ? room.status : "Available";
        String bg=getRoomBg(status);
        String border=getRoomBorder(status);
        String textColor=getRoomTextColor(status);
        String icon=getRoomIcon(status, room.type);

        VBox card=new VBox(3);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(90, 80);
        card.setPadding(new Insets(6));
        card.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 8; " +
                "-fx-border-color: " + border + "; -fx-border-radius: 8; -fx-border-width: 2; -fx-cursor: hand;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");

        Label numLabel = new Label(room.number);
        numLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");

        Label typeLabel = new Label(room.type != null ? room.type : "General");
        typeLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: " + textColor + "; -fx-opacity: 0.8;");

        Label patientLabel = new Label(room.patientName != null
                ? room.patientName.split(" ")[0] : status.equals("Available") ? "Open" : "—");
        patientLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #666;");
        patientLabel.setWrapText(true);
        patientLabel.setAlignment(Pos.CENTER);

        card.getChildren().addAll(iconLabel, numLabel, typeLabel, patientLabel);

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 8; " +
                    "-fx-border-color: " + border + "; -fx-border-radius: 8; -fx-border-width: 2; " +
                    "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);");
            card.setScaleX(1.05);
            card.setScaleY(1.05);
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 8; " +
                    "-fx-border-color: " + border + "; -fx-border-radius: 8; -fx-border-width: 2; -fx-cursor: hand;");
            card.setScaleX(1.0);
            card.setScaleY(1.0);
        });

        card.setOnMouseClicked(e -> showRoomDetails(room));

        roomsPane.getChildren().add(card);
    }

    private void showRoomDetails(RoomData room){
        String status=room.status != null ? room.status : "Unknown";
        String patient=room.patientName != null ? room.patientName : "None";
        String dept=room.patientDept != null ? room.patientDept : "—";

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room "+room.number);
        alert.setHeaderText("Room "+room.number+" — "+(room.type != null ? room.type : "General"));
        alert.setContentText(
                "Status: "+status+"\n"+
                        "Patient: "+patient+"\n" +
                        "Department: "+dept+"\n" +
                        "Room ID: "+room.id
        );
        alert.show();
    }

    private String getRoomBg(String status){
        if(status == null){
            return "#E8F5E9";
        }
        switch(status.toLowerCase()){
            case "occupied": return "#FCEBEB";
            case "cleaning": return "#FFF3E0";
            case "maintenance": return "#F5F5F5";
            default: return "#E8F5E9";
        }
    }

    private String getRoomBorder(String status){
        if(status == null){
            return "#4CAF50";
        }
        switch(status.toLowerCase()){
            case "occupied": return "#EF5350";
            case "cleaning": return "#FFA726";
            case "maintenance": return "#9E9E9E";
            default: return "#4CAF50";
        }
    }

    private String getRoomTextColor(String status){
        if(status == null){
            return "#2E7D32";
        }
        switch(status.toLowerCase()){
            case "occupied": return "#C62828";
            case "cleaning": return "#E65100";
            case "maintenance": return "#424242";
            default: return "#2E7D32";
        }
    }

    private String getRoomIcon(String status, String type){
        if(status != null && status.equalsIgnoreCase("cleaning")){
            return "🧹";
        }if(status != null && status.equalsIgnoreCase("maintenance")){
            return "🔧";
        }if(type != null){
            switch(type.toLowerCase()){
                case "icu": return "🏥";
                case "emergency": return "🚨";
                case "private": return "🛏";
                default: return "🛏";
            }
        }
        return "🛏";
    }

    private void startAutoRefresh(){
        autoRefresh=new Timeline(new KeyFrame(Duration.seconds(30), new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                loadFloor(currentFloor);
            }
        }));
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();
    }

    @FXML private void handleBack() {
        if(autoRefresh != null){
            autoRefresh.stop();
        }
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }

    @FXML private void handleRefresh(){
        loadFloor(currentFloor);
    }

    private static class RoomData {
        int id;
        String number;
        String type;
        String status;
        String patientName;
        String patientDept;
        int patientId;
    }
}