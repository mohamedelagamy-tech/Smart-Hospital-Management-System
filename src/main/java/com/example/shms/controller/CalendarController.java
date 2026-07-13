package com.example.shms.controller;
import com.example.shms.MainApp;
import  com.example.shms.database.DatabaseManager;
import com.example.shms.model.Appointment;
import com.example.shms.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.format.TextStyle;
import java.util.Locale;
import com.example.shms.utils.LanguageManager;
import javafx.scene.control.Button;


public class CalendarController {
    @FXML
    private Label monthLabel;
    @FXML
    private GridPane calendarGrid;

    private com.example.shms.database.DatabaseManager db = com.example.shms.database.DatabaseManager.getInstance();
    private java.time.LocalDate currentMonth = java.time.LocalDate.now().withDayOfMonth(1);
    private String currentFilter="All";
    @FXML private Button backBtn;

    @FXML private Label titleLabel;
    @FXML private Label statusLabel;

    @FXML private Button allBtn;
    @FXML private Button scheduledBtn;
    @FXML private Button completedBtn;
    @FXML private Button cancelledBtn;

    @FXML private Label sunLabel;
    @FXML private Label monLabel;
    @FXML private Label tueLabel;
    @FXML private Label wedLabel;
    @FXML private Label thuLabel;
    @FXML private Label friLabel;
    @FXML private Label satLabel;

    @FXML
    public void initialize() {

        if (LanguageManager.isArabic()) {

            backBtn.setText("→ رجوع");

            titleLabel.setText("تقويم المواعيد");

            statusLabel.setText("الحالة");

            allBtn.setText("الكل");
            scheduledBtn.setText("مجدول");
            completedBtn.setText("مكتمل");
            cancelledBtn.setText("ملغي");

            sunLabel.setText("الأحد");
            monLabel.setText("الاثنين");
            tueLabel.setText("الثلاثاء");
            wedLabel.setText("الأربعاء");
            thuLabel.setText("الخميس");
            friLabel.setText("الجمعة");
            satLabel.setText("السبت");
        }

        loadCalendar();
    }

    private void loadCalendar() {
        calendarGrid.getChildren().clear();

        Locale locale = LanguageManager.isArabic()
                ? new Locale("ar")
                : Locale.ENGLISH;

        String month = currentMonth.getMonth()
                .getDisplayName(TextStyle.FULL, locale);

        monthLabel.setText(month + " " + currentMonth.getYear());
        int dow = currentMonth.withDayOfMonth(1).getDayOfWeek().getValue();
        int firstDay = dow == 7 ? 0 : dow;
        int daysInMonth = currentMonth.lengthOfMonth();
        List<Appointment> all = new java.util.ArrayList<>();
        try {
            java.sql.Statement st = db.getConnection().createStatement();
            java.sql.ResultSet rs = st.executeQuery("SELECT * FROM appointments");
            while (rs.next()) {
                all.add(new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patientId"),
                        rs.getInt("doctorId"),
                        java.time.LocalDate.parse(rs.getString("date")),
                        java.time.LocalTime.parse(rs.getString("time")),
                        rs.getString("status"),
                        rs.getString("notes") != null ? rs.getString("notes") : ""
                ));
            }
        } catch (Exception e) {
            System.out.println("Calender loading failed" + e.getMessage());
        }

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
                   if( !currentFilter.equals("All") && !a.getStatus().equals(currentFilter)) continue;
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
                    String status = a.getStatus();

                    if (com.example.shms.utils.LanguageManager.isArabic()) {
                        switch (status) {
                            case "Scheduled":
                                status = "مجدول";
                                break;
                            case "Completed":
                                status = "مكتمل";
                                break;
                            case "Cancelled":
                                status = "ملغي";
                                break;
                        }
                    }

                    Label pill = new Label(
                            a.getTime().toString().substring(0, 5)
                                    + " "
                                    + translateStatus(a.getStatus())
                    );
                    pill.setStyle("-fx-background-color:" + color + "; -fx-text-fill:" + textColor + ";-fx-background-radius:99; -fx-padding:2 6;" + "-fx-font-size:10; -fx-font-weight:bold;");
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
    private void handleFilterAll() {
        currentFilter = "All";
        loadCalendar();
    }
    @FXML private void handleFilterScheduled(){
        currentFilter="Scheduled";
        loadCalendar();
}
@FXML private void handleFilterCompleted(){
    currentFilter="Completed";
    loadCalendar();
}
@FXML private void handleFilterCancelled() {
    currentFilter = "Cancelled";
    loadCalendar();
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
    private String translateStatus(String status) {

        if (!LanguageManager.isArabic()) {
            return status;
        }

        return switch (status) {
            case "Scheduled" -> "مجدول";
            case "Completed" -> "مكتمل";
            case "Cancelled" -> "ملغي";
            default -> status;
        };
    }
    @FXML
    private void handleBack() {
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }
}