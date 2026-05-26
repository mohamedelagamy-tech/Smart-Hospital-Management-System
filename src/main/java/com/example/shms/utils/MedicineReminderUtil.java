package com.example.shms.utils;

import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Prescription;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.example.shms.utils.SessionManager;

import java.util.List;

public class MedicineReminderUtil {

    public static void showReminders() {
        String role = SessionManager.getInstance().getLoggedInRole();
        if (role == null || !role.equalsIgnoreCase("Patient")) return;

        DatabaseManager db = DatabaseManager.getInstance();

        List<Prescription> prescriptions = db.getPrescriptionsByPatient(0);

        if (prescriptions == null || prescriptions.isEmpty()) return;

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label header = new Label("You have " + prescriptions.size() + " active medicine reminder(s) today:");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #1a6fba;");
        content.getChildren().add(header);

        for (Prescription p : prescriptions) {
            VBox card = new VBox(3);
            card.setPadding(new Insets(8));
            card.setStyle(
                    "-fx-background-color: #f0f6ff;" +
                            "-fx-border-color: #c5d8ee;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;"
            );

            Label medicine = new Label("💊 " + p.getMedicineName() + "  —  " + p.getDosage());
            medicine.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

            Label instructions = new Label("📋 " + p.getInstructions());
            instructions.setStyle("-fx-font-size: 12px; -fx-text-fill: #4a5568;");

            Label duration = new Label("⏳ Duration: " + p.getDuration());
            duration.setStyle("-fx-font-size: 12px; -fx-text-fill: #9aa5b4;");

            card.getChildren().addAll(medicine, instructions, duration);
            content.getChildren().add(card);
        }

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("🔔 Medicine Reminders");
        alert.setHeaderText("Today's Medicine Reminders");
        alert.getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setMinWidth(420);
        alert.getDialogPane().setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #1a6fba;" +
                        "-fx-border-width: 2;"
        );
        alert.showAndWait();
    }
}