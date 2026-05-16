package com.example.shms.utils;

import com.example.shms.model.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RoomScreen {
    private ObservableList<Room> allRooms;
    private FlowPane roomGrid;
    private TextField searchField;

    public void show(Stage stage) {
        Label title = new Label("Room Management");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#1a1a1a"));

        searchField = new TextField();
        searchField.setPromptText("🔍  Search rooms or patients...");
        searchField.setPrefHeight(45);
        searchField.setMaxWidth(Double.MAX_VALUE);
        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 0 16;" +
                        "-fx-font-size: 14px;"
        );
        searchField.textProperty().addListener((obs, o, n) -> filterRooms(n));

        allRooms = FXCollections.observableArrayList(
                DatabaseManager.getInstance().getAllRooms()
        );
        roomGrid = new FlowPane();
        roomGrid.setHgap(16);
        roomGrid.setVgap(16);
        roomGrid.setPadding(new Insets(8, 0, 0, 0));
        buildGrid(allRooms);

        VBox layout = new VBox(20, title, searchField, roomGrid);
        layout.setPadding(new Insets(32));
        layout.setStyle("-fx-background-color: #f0f2f5;");

        ScrollPane scroll = new ScrollPane(layout);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f2f5; -fx-background: #f0f2f5;");

        Scene scene = new Scene(scroll, 1100, 650);
        stage.setTitle("SHMS - Room Management");
        stage.setScene(scene);
        stage.show();
    }
    private void buildGrid(ObservableList<Room> rooms) {
        roomGrid.getChildren().clear();
        for (Room room : rooms) {
            roomGrid.getChildren().add(makeRoomCard(room));
        }
    }

    private VBox makeRoomCard(Room room) {

        String roomName = room.getRoomNumber() == -1 ? "Room ER-1"
                : room.getRoomNumber() == -2 ? "Room ER-2"
                : "Room " + room.getRoomNumber();

        Label nameLabel = new Label(roomName);
        nameLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.web("#1a1a1a"));

        Label badge = new Label(room.getStatus());
        badge.setPadding(new Insets(4, 12, 4, 12));
        badge.setFont(Font.font("System", FontWeight.BOLD, 12));
        badge.setTextFill(Color.WHITE);
        switch (room.getStatus()) {
            case "Occupied":
                badge.setStyle("-fx-background-color: #e74c3c; -fx-background-radius: 20;");
                break;
            case "Available":
                badge.setStyle("-fx-background-color: #27ae60; -fx-background-radius: 20;");
                break;
            case "Cleaning":
                badge.setStyle("-fx-background-color: #e67e22; -fx-background-radius: 20;");
                break;
            default:
                badge.setStyle("-fx-background-color: #95a5a6; -fx-background-radius: 20;");
        }
        HBox nameRow = new HBox(8, nameLabel, badge);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        Label deptLabel = new Label(room.getDepartment());
        deptLabel.setFont(Font.font("System", 13));
        deptLabel.setTextFill(Color.web("#555555"));

        String infoText;
        switch (room.getRoomStatus()) {
            case "Occupied":
                infoText = "Patient: " + room.getPatientName();
                break;
            case "Cleaning":
                infoText = "Being cleaned";
                break;
            default:
                infoText = "Ready for admission";
        }
        Label infoLabel = new Label(infoText);
        infoLabel.setFont(Font.font("System", 13));
        infoLabel.setTextFill(room.getStatus().equals("Occupied")
                ? Color.web("#1a1a1a")
                : Color.web("#aaaaaa"));


    }}
}
