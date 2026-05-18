package com.example.shms.controller;
import  com.example.shms.database.DatabaseManager;
import com.example.shms.model.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.util.List;

public class CalendarController {
    @FXML
    private Label monthLabel;
    @FXML
    private GridPane calendarGrid;

    private com.example.shms.database.DatabaseManager db = com.example.shms.database.DatabaseManager.getInstance();
    private java.time.LocalDate currentMonth = java.time.LocalDate.now().withDayOfMonth(1);

    @FXML
    public void initialize() {
        loadCalendar();
    }

    private void loadCalendar() {
        calendarGrid.getChildren().clear();
        String month = currentMonth.getMonth().toString();
        monthLabel.setText(month.charAt(0) + month.substring(1).toLowerCase() + " " + currentMonth.getYear());
        int firstDay = (currentMonth.getDayOfWeek().getValue() % 7 + 1) % 7;
        int daysInMonth = currentMonth.lengthOfMonth();
        List<Appointment> all = db.getAppointmentsByPatient(0);

        int col = firstDay;
        int row = 0;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.withDayOfMonth(day);
            VBox cell = new VBox(2);

            boolean isToday = date.equals(LocalDate.now());
            cell.setStyle("-fx-border-color:#e8eaf0; -fx-border-width:0.5;" + "-fx-padding:6; -fx-min-height:80;" + (isToday ? "-fx-background-color#EEF2FF;" : "-fx-background-color:white;"));
            Label dayLabel = new Label(String.valueOf(day));
            if (isToday) {
                dayLabel.setStyle("-fx-background-color:#185FA5; -fx-text-fill:white;" +
                        "-fx-font-weight:bold; -fx-font-size:11;" +
                        "-fx-background-radius:99; -fx-min-width:22;" +
                        "-fx-min-height:22; -fx-alignment:CENTER;");
            } else {
                dayLabel.setStyle("-fx-font-weight:bold; -fx-font-size:12; -fx-text-fill:#444;");
            }
            cell.getChildren().add(dayLabel);
            for (Appointment a : all) {
                if (a.getDate().equals(date)) {
                    String color, textColor;
                    if ("Scheduled".equals(a.getStatus())) {
                        color = "#E6F1FB";
                        textColor = "#0C447C";
                    } else if ("Completed".equals(a.getStatus())) {
                        color = "#EAF3DE";
                        textColor = "#27500A";
                    } else {
                        color = "#FCEBEB";
                        textColor = "#791F1F";
                    }
                    Label pill = new Label(a.getTime().toString().substring(0, 5) + "" + a.getStatus());
                    pill.setStyle("-fx-background-color:" + color + "; -fx-test-fill:" + textColor + ";-fx-background-radius:99; -fx-padding:2 6;" + "-fx-font-size:10; -fx-font-weight:bold;");
                    cell.getChildren().add(pill);
                }
            }
            calendarGrid.add(cell, col, row);
            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void handlePrevMonth() {
        currentMonth = currentMonth.minusMonths(1);
        loadCalendar();
    }

    @FXML
    private void handleNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        loadCalendar();
    }

    @FXML
    private void handleBack() {
        com.example.shms.MainApp.navigateTo("dashboard", 1200, 700);
    }
}


