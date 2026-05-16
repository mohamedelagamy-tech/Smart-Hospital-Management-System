package com.example.shms.utils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RoomManagementController implements Initializable {

    // ── Injected from FXML ───────────────────────────────────────────────────
    @FXML private TextField searchField;
    @FXML private FlowPane  cardsPane;
    @FXML private ScrollPane scrollPane;

    // ── Data ─────────────────────────────────────────────────────────────────
    enum Status { OCCUPIED, AVAILABLE, CLEANING }

    static class Room {
        String id, type, detail;
        Status status;
        Room(String id, String type, Status status, String detail) {
            this.id = id; this.type = type;
            this.status = status; this.detail = detail;
        }
    }

    private final List<Room> allRooms = new ArrayList<>();
    private final List<VBox> allCards = new ArrayList<>();

    // ── initialize() ────────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadRooms();
        buildAllCards();
    }

    private void loadRooms() {
        allRooms.add(new Room("Room 101", "General",   Status.OCCUPIED,  "Patient: Sarah Johnson"));
        allRooms.add(new Room("Room 102", "General",   Status.AVAILABLE, "Ready for admission"));
        allRooms.add(new Room("Room 103", "General",   Status.CLEANING,  "Being cleaned"));
        allRooms.add(new Room("Room 201", "ICU",       Status.OCCUPIED,  "Patient: Michael Chen"));
        allRooms.add(new Room("Room 202", "ICU",       Status.OCCUPIED,  "Patient: Emma Davis"));
        allRooms.add(new Room("Room 203", "ICU",       Status.AVAILABLE, "Ready for admission"));
        allRooms.add(new Room("Room 301", "Surgery",   Status.AVAILABLE, "Ready for admission"));
        allRooms.add(new Room("Room 302", "Surgery",   Status.AVAILABLE, "Ready for admission"));
        allRooms.add(new Room("Room ER-1","Emergency", Status.OCCUPIED,  "Patient: Robert Taylor"));
        allRooms.add(new Room("Room ER-2","Emergency", Status.AVAILABLE, "Ready for admission"));
    }

    // ── Build all cards once, then show/hide on search ───────────────────────
    private void buildAllCards() {
        for (Room room : allRooms) {
            VBox card = buildCard(room);
            allCards.add(card);
            cardsPane.getChildren().add(card);
        }
    }

    private VBox buildCard(Room room) {
        // ── Top row: room ID + badge ──
        Label idLabel = new Label(room.id);
        idLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1a1d23;");

        Label badge = new Label(badgeText(room.status));
        badge.setStyle(badgeStyle(room.status));

        HBox topRow = new HBox(8, idLabel, badge);
        topRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // ── Type ──
        Label typeLabel = new Label(room.type);
        typeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a5568;");

        // ── Detail ──
        Label detailLabel = new Label(room.detail);
        detailLabel.setStyle(room.status == Status.OCCUPIED
                ? "-fx-font-size: 13px; -fx-text-fill: #4a5568;"
                : "-fx-font-size: 13px; -fx-text-fill: #9aa5b4;");

        // ── Assemble card ──
        VBox card = new VBox(6, topRow, typeLabel, detailLabel);
        card.setStyle("-fx-background-color: white; -fx-border-color: #c5d8ee; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-width: 1.2;");
        card.setPrefSize(220, 155);
        card.setPadding(new Insets(18));

        // store room reference for search
        card.setUserData(room);
        return card;
    }

    // ── Search handler ────────────────────────────────────────────────────────
    @FXML
    private void onSearch(KeyEvent event) {
        String query = searchField.getText().trim().toLowerCase();
        for (VBox card : allCards) {
            Room room = (Room) card.getUserData();
            boolean match = query.isEmpty()
                    || room.id.toLowerCase().contains(query)
                    || room.type.toLowerCase().contains(query)
                    || room.detail.toLowerCase().contains(query)
                    || room.status.name().toLowerCase().contains(query);
            card.setVisible(match);
            card.setManaged(match); // removes space when hidden
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private String badgeText(Status s) {
        return switch (s) {
            case OCCUPIED  -> "Occupied";
            case AVAILABLE -> "Available";
            case CLEANING  -> "Cleaning";
        };
    }

    private String badgeStyle(Status s) {
        String base = "-fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 3 10 3 10;";
        return switch (s) {
            case OCCUPIED  -> base + "-fx-background-color: #ef4444;";
            case AVAILABLE -> base + "-fx-background-color: #22c55e;";
            case CLEANING  -> base + "-fx-background-color: #f97316;";
        };
    }
}
