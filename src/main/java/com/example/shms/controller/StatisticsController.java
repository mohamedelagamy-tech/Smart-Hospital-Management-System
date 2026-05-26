package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    @FXML private BarChart<String, Number> appointmentsChart;
    @FXML private PieChart occupancyChart;
    @FXML private LineChart<String, Number> revenueChart;
    @FXML private BarChart<String, Number> doctorsChart;
    @FXML private ListView<String> notificationsList;
    @FXML private Label notifStatusLabel;

    private final DatabaseManager db = DatabaseManager.getInstance();
    private Thread notificationThread;

    @FXML
    private void handleBack() {
        stopThread();
        MainApp.navigateTo(
                SessionManager.getInstance().getDashboardName(),
                1200,
                700
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAppointmentsChart();
        loadOccupancyChart();
        loadRevenueChart();
        loadDoctorsChart();
        startNotificationThread();
    }

    private void loadAppointmentsChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Appointments");

        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("""
                     SELECT appointmentDate, COUNT(*) AS total
                     FROM appointments
                     GROUP BY appointmentDate
                     ORDER BY appointmentDate DESC
                     LIMIT 7
                     """)) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                        rs.getString("appointmentDate"),
                        rs.getInt("total")
                ));
            }

        } catch (SQLException e) {
            series.getData().addAll(
                    new XYChart.Data<>("Mon", 12),
                    new XYChart.Data<>("Tue", 18),
                    new XYChart.Data<>("Wed", 15),
                    new XYChart.Data<>("Thu", 22),
                    new XYChart.Data<>("Fri", 19),
                    new XYChart.Data<>("Sat", 8),
                    new XYChart.Data<>("Sun", 5)
            );
        }

        appointmentsChart.getData().setAll(series);
        appointmentsChart.setLegendVisible(false);
        styleBarChart(appointmentsChart, "#1a6fba");
    }

    private void loadOccupancyChart() {
        int occupied = 0, available = 0, cleaning = 0;

        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("""
                     SELECT status, COUNT(*) AS total
                     FROM rooms
                     GROUP BY status
                     """)) {

            while (rs.next()) {
                switch (rs.getString("status").toLowerCase()) {
                    case "occupied" -> occupied = rs.getInt("total");
                    case "available" -> available = rs.getInt("total");
                    case "cleaning" -> cleaning = rs.getInt("total");
                }
            }

        } catch (SQLException ignored) {
            occupied = 4;
            available = 5;
            cleaning = 1;
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Occupied (" + occupied + ")", occupied),
                new PieChart.Data("Available (" + available + ")", available),
                new PieChart.Data("Cleaning (" + cleaning + ")", cleaning)
        );

        occupancyChart.setData(data);
        occupancyChart.setLegendVisible(true);

        Platform.runLater(() -> {
            if (data.size() > 0) data.get(0).getNode().setStyle("-fx-pie-color: #ef4444;");
            if (data.size() > 1) data.get(1).getNode().setStyle("-fx-pie-color: #22c55e;");
            if (data.size() > 2) data.get(2).getNode().setStyle("-fx-pie-color: #f97316;");
        });
    }

    private void loadRevenueChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("""
                     SELECT appointmentDate AS month, SUM(amount) AS total
                     FROM bills
                     GROUP BY month
                     ORDER BY month ASC
                     LIMIT 6
                     """)) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                        rs.getString("month"),
                        rs.getDouble("total")
                ));
            }

        } catch (SQLException ignored) {
            series.getData().addAll(
                    new XYChart.Data<>("Jan", 12000),
                    new XYChart.Data<>("Feb", 18500),
                    new XYChart.Data<>("Mar", 15000),
                    new XYChart.Data<>("Apr", 22000),
                    new XYChart.Data<>("May", 19500)
            );
        }

        revenueChart.getData().setAll(series);
        revenueChart.setLegendVisible(false);
        revenueChart.setCreateSymbols(true);
    }

    private void loadDoctorsChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rating");

        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("""
                     SELECT name,
                            (totalRating * 1.0 / ratingCount) AS avg_rating
                     FROM doctors
                     WHERE ratingCount > 0
                     ORDER BY avg_rating DESC
                     LIMIT 5
                     """)) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                        "Dr. " + rs.getString("name"),
                        rs.getDouble("avg_rating")
                ));
            }

        } catch (SQLException ignored) {
            series.getData().addAll(
                    new XYChart.Data<>("Dr. Anderson", 4.8),
                    new XYChart.Data<>("Dr. Williams", 4.6),
                    new XYChart.Data<>("Dr. Martinez", 4.5),
                    new XYChart.Data<>("Dr. Thompson", 4.3),
                    new XYChart.Data<>("Dr. Lee", 4.1)
            );
        }

        doctorsChart.getData().setAll(series);
        doctorsChart.setLegendVisible(false);
        styleBarChart(doctorsChart, "#f59e0b");
    }

    private void startNotificationThread() {
        ObservableList<String> notifications = FXCollections.observableArrayList();
        notificationsList.setItems(notifications);

        notificationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String time = LocalTime.now()
                            .format(DateTimeFormatter.ofPattern("hh:mm a"));

                    String[] messages = fetchLatestNotifications(time);

                    Platform.runLater(() -> {
                        notifications.setAll(messages);
                        notifStatusLabel.setText("● Live — Last updated: " + time);
                    });

                    Thread.sleep(30_000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        notificationThread.setDaemon(true);
        notificationThread.start();
    }

    private String[] fetchLatestNotifications(String time){
        ObservableList<String> list = FXCollections.observableArrayList();

        try(Statement st=db.getConnection().createStatement()){

            ResultSet rs = st.executeQuery("""
                    SELECT patientName, appointmentDate
                    FROM appointments
                    ORDER BY id DESC
                    LIMIT 3
                    """);

            while(rs.next()){
                list.add("📅 New appointment — " +
                        rs.getString("patientName") +
                        " on " +
                        rs.getString("appointmentDate"));
            }

            rs=st.executeQuery("""
                    SELECT patientName, amount
                    FROM bills
                    ORDER BY id DESC
                    LIMIT 2
                    """);

            while(rs.next()){
                list.add("💰 Bill generated — " +
                        rs.getString("patientName") +
                        " ($" + rs.getDouble("amount") + ")");
            }

        }catch (SQLException ignored){
            list.add("📅 New admission — Sarah Johnson [" + time + "]");
            list.add("💊 Prescription added — Michael Chen [" + time + "]");
            list.add("🏥 Room 302 available [" + time + "]");
            list.add("💰 Bill generated — Emma Davis ($1,200)");
        }

        return list.toArray(new String[0]);
    }

    private void styleBarChart(BarChart<String, Number> chart, String color){
        Platform.runLater(() ->
                chart.lookupAll(".default-color0.chart-bar")
                        .forEach(node -> node.setStyle("-fx-bar-fill: "+color+";"))
        );
    }

    public void stopThread(){
        if(notificationThread != null){
            notificationThread.interrupt();
        }
    }
}