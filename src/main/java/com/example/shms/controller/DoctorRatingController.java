package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Doctor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DoctorRatingController {

    @FXML private Label    avatarLabel;
    @FXML private Label    doctorNameLabel;
    @FXML private Label    doctorSpecLabel;
    @FXML private Label    dischargeLabel;
    @FXML private Button   star1Btn;
    @FXML private Button   star2Btn;
    @FXML private Button   star3Btn;
    @FXML private Button   star4Btn;
    @FXML private Button   star5Btn;
    @FXML private Label    ratingLabel;
    @FXML private TextArea commentArea;
    @FXML private Button   submitBtn;
    @FXML private Label    successLabel;

    private final DatabaseManager db = DatabaseManager.getInstance();
    private DoctorController doctorController;
    private Doctor doctor;
    private int selectedRating = 0;
    private Button[] stars;

    private static final String[] RATING_LABELS = {"", "Poor", "Fair", "Good", "Very Good", "Excellent"};
    private static final String[] RATING_COLORS = {"", "#EF4444", "#F59E0B", "#F59E0B", "#84CC16", "#16A34A"};

    @FXML
    private void initialize() {
        stars = new Button[]{star1Btn, star2Btn, star3Btn, star4Btn, star5Btn};
        renderStars(0);
    }

    public void setDoctorController(DoctorController controller) {
        this.doctorController = controller;
    }

    public void setDoctor(Doctor d) {
        this.doctor = d;
        doctorNameLabel.setText(d.getName());
        doctorSpecLabel.setText(d.getDepartment());
        avatarLabel.setText(getInitials(d.getName()));
        dischargeLabel.setText("✓ Discharged today");
    }

    @FXML private void handleStar1() { selectRating(1); }
    @FXML private void handleStar2() { selectRating(2); }
    @FXML private void handleStar3() { selectRating(3); }
    @FXML private void handleStar4() { selectRating(4); }
    @FXML private void handleStar5() { selectRating(5); }

    private void selectRating(int rating) {
        selectedRating = rating;
        renderStars(rating);
        ratingLabel.setText(RATING_LABELS[rating]);
        ratingLabel.setStyle("-fx-text-fill: " + RATING_COLORS[rating] + "; -fx-font-weight: bold;");
        submitBtn.setDisable(false);
    }

    private void renderStars(int filled) {
        for (int i = 0; i < stars.length; i++) {
            String color = (i < filled) ? (filled >= 4 ? "#16A34A" : "#F59E0B") : "#D1D5DB";
            stars[i].setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-font-size: 36px;" +
                            "-fx-cursor: hand;" +
                            "-fx-text-fill: " + color + ";"
            );
        }
    }

    @FXML
    private void handleSubmit() {
        if (selectedRating == 0) return;
        String comment = commentArea.getText().trim();
        try {
            db.saveDoctorRating(doctor.getID(), selectedRating, comment);
        } catch (Exception e) {
            System.out.println("error saving rating " + e.getMessage());
        }
        submitBtn.setDisable(true);
        successLabel.setText("✓ Thank you! Your rating has been submitted.");
        successLabel.setVisible(true);
    }

    @FXML
    private void handleBack() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) commentArea.getScene().getWindow();
        stage.close();
    }

    private String getInitials(String fullName) {
        if (fullName == null || fullName.isEmpty()) return "DR";
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }
}
