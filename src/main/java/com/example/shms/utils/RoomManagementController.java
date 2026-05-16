package com.example.shms.utils;

import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Room;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RoomManagementController implements Initializable {

    @FXML private TextField  searchField;
    @FXML private FlowPane   cardsPane;
    @FXML private ScrollPane scrollPane;

    private final List<Room> allRooms = new ArrayList<>();
    private final List<VBox> allCards = new ArrayList<>();

    private final DatabaseManager db = DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadRoomsFromDatabase();
        buildAllCards();
    }

    private void loadRoomsFromDatabase() {
        allRooms.clear();
        List<Room> rooms = db.getAllRooms();   // your teammate's method
        if (rooms != null) {
            allRooms.addAll(rooms);
        }
    }

    private void buildAllCards() {
        cardsPane.getChildren().clear();
        allCards.clear();
        for (Room room : allRooms) {
            VBox card = buildCard(room);
            allCards.add(card);
            cardsPane.getChildren().add(card);
        }
    }

    private VBox buildCard(Room room) {

        Label idLabel = new Label("Room " + room.getRoomNumber());
        idLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1a1d23;");


        Label badge = new Label(room.getRoomStatus());
        badge.setStyle(badgeStyle(room.getRoomStatus()));

        HBox topRow = new HBox(8, idLabel, badge);
        topRow.setAlignment(Pos.CENTER_LEFT);


        Label typeLabel = new Label(room.getDepartment());
        typeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a5568;");

        Label detailLabel = new Label(getDetailText(room));
        detailLabel.setStyle(isOccupied(room.getRoomStatus())
                ? "-fx-font-size: 13px; -fx-text-fill: #4a5568;"
                : "-fx-font-size: 13px; -fx-text-fill: #9aa5b4;");

        VBox card = new VBox(6, topRow, typeLabel, detailLabel);
        card.setStyle("-fx-background-color: white; -fx-border-color: #c5d8ee; " +
                "-fx-border-radius: 12; -fx-background-radius: 12; -fx-border-width: 1.2;");
        card.setPrefSize(220, 155);
        card.setPadding(new Insets(18));
        card.setUserData(room);

        if (room.getRoomStatus().equalsIgnoreCase("Available")) {
            Button assignBtn = new Button("Assign Patient");
            assignBtn.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
                    "-fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand;");
            assignBtn.setOnAction(e -> showAssignDialog(room));
            card.getChildren().add(assignBtn);
            if (room.getRoomStatus().equalsIgnoreCase("Occupied")) {
                Button releaseBtn = new Button("Release Room");
                releaseBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; " +
                        "-fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand;");
                releaseBtn.setOnAction(e -> showReleaseDialog(room));
                card.getChildren().add(releaseBtn);
            }

        }
        return card;
    }

    private String getDetailText(Room room) {
        if (isOccupied(room.getRoomStatus())) {
            int patientId = room.getAssignedPatientId();
            return patientId > 0 ? "Patient ID: " + patientId : "Occupied";
        } else if (room.getRoomStatus().equalsIgnoreCase("Cleaning")) {
            return "Being cleaned";
        } else {
            return "Ready for admission";
        }
    }

    @FXML
    private void onSearch(KeyEvent event) {
        String query = searchField.getText().trim().toLowerCase();
        for (VBox card : allCards) {
            Room room = (Room) card.getUserData();
            boolean match = query.isEmpty()
                    || String.valueOf(room.getRoomNumber()).contains(query)
                    || room.getDepartment().toLowerCase().contains(query)
                    || room.getRoomStatus().toLowerCase().contains(query)
                    || room.getRoomtype().toLowerCase().contains(query);
            card.setVisible(match);
            card.setManaged(match);
        }
    }


    @FXML
    public void onRefresh() {
        loadRoomsFromDatabase();
        buildAllCards();
    }


    private boolean isOccupied(String status) {
        return status != null && status.equalsIgnoreCase("Occupied");
    }

    private String badgeStyle(String status) {
        String base = "-fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; " +
                "-fx-background-radius: 20; -fx-padding: 3 10 3 10;";
        if (status == null) return base + "-fx-background-color: #9aa5b4;";
        return switch (status.toLowerCase()) {
            case "occupied"  -> base + "-fx-background-color: #ef4444;";
            case "available" -> base + "-fx-background-color: #22c55e;";
            case "cleaning"  -> base + "-fx-background-color: #f97316;";
            default          -> base + "-fx-background-color: #9aa5b4;";
        };
    }
}
