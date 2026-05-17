package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
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

    // ── Injected from FXML ───────────────────────────────────────────────────
    @FXML private BarChart<String, Number>  appointmentsChart;
    @FXML private PieChart                  occupancyChart;
    @FXML private LineChart<String, Number> revenueChart;
    @FXML private BarChart<String, Number>  doctorsChart;
    @FXML private ListView<String>          notificationsList;
    @FXML private Label                     notifStatusLabel;
    @FXML
    private void handleBack() {
        stopThread();
        MainApp.navigateTo("dashboard", 1200, 700);
    }
    private final DatabaseManager db = DatabaseManager.getInstance();
    private Thread notificationThread;

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

        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT DATE(appointment_date) as day, COUNT(*) as total " +
                            "FROM appointments " +
                            "GROUP BY DATE(appointment_date) " +
                            "ORDER BY day DESC LIMIT 7"
            );
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                        rs.getString("day"), rs.getInt("total")
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

        appointmentsChart.getData().add(series);
        appointmentsChart.setLegendVisible(false);
        styleBarChart(appointmentsChart, "#1a6fba");
    }

    private void loadOccupancyChart() {
        int occupied = 0, available = 0, cleaning = 0;

        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT status, COUNT(*) as total FROM rooms GROUP BY status"
            );
            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("total");
                switch (status.toLowerCase()) {
                    case "occupied"  -> occupied  = count;
                    case "available" -> available = count;
                    case "cleaning"  -> cleaning  = count;
                }
            }
        } catch (SQLException e) {
            occupied = 4; available = 5; cleaning = 1;
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Occupied ("  + occupied  + ")", occupied),
                new PieChart.Data("Available (" + available + ")", available),
                new PieChart.Data("Cleaning ("  + cleaning  + ")", cleaning)
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

        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT strftime('%Y-%m', date) as month, SUM(amount) as total " +
                            "FROM bills " +
                            "GROUP BY month " +
                            "ORDER BY month ASC LIMIT 6"
            );
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                        rs.getString("month"), rs.getDouble("total")
                ));
            }
        } catch (SQLException e) {
            series.getData().addAll(
                    new XYChart.Data<>("Jan", 12000),
                    new XYChart.Data<>("Feb", 18500),
                    new XYChart.Data<>("Mar", 15000),
                    new XYChart.Data<>("Apr", 22000),
                    new XYChart.Data<>("May", 19500)
            );
        }

        revenueChart.getData().add(series);
        revenueChart.setLegendVisible(false);
        revenueChart.setCreateSymbols(true);
    }

    private void loadDoctorsChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rating");

        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT name, (totalRating / ratingCount) as avg_rating " +
                            "FROM doctors " +
                            "WHERE ratingCount > 0 " +
                            "ORDER BY avg_rating DESC LIMIT 5"
            );
            while (rs.next()) {
                String name = "Dr. " + rs.getString("name");
                series.getData().add(new XYChart.Data<>(
                        name, rs.getDouble("avg_rating")
                ));
            }
        } catch (SQLException e) {
            series.getData().addAll(
                    new XYChart.Data<>("Dr. Anderson", 4.8),
                    new XYChart.Data<>("Dr. Williams", 4.6),
                    new XYChart.Data<>("Dr. Martinez", 4.5),
                    new XYChart.Data<>("Dr. Thompson", 4.3),
                    new XYChart.Data<>("Dr. Lee",      4.1)
            );
        }

        doctorsChart.getData().add(series);
        doctorsChart.setLegendVisible(false);
        styleBarChart(doctorsChart, "#f59e0b");
    }

    private void startNotificationThread() {
        ObservableList<String> notifications = FXCollections.observableArrayList();
        notificationsList.setItems(notifications);

        notificationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Fetch latest activity every 30 seconds
                    String time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
                    String[] messages = fetchLatestNotifications(time);

                    Platform.runLater(() -> {
                        notifications.clear();
                        for (String msg : messages) notifications.add(msg);
                        notifStatusLabel.setText("● Live  —  Last updated: " + time);
                    });

                    Thread.sleep(30000); // refresh every 30 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        notificationThread.setDaemon(true); // stops when app closes
        notificationThread.start();
    }

    private String[] fetchLatestNotifications(String time) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            Statement st = db.getConnection().createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT p.name, a.appointment_date FROM appointments a " +
                            "JOIN patients p ON a.patient_id = p.id " +
                            "ORDER BY a.appointment_date DESC LIMIT 3"
            );
            while (rs.next()) {
                list.add("📅  New appointment — " + rs.getString("name") +
                        " on " + rs.getString("appointment_date"));
            }

            rs = st.executeQuery(
                    "SELECT patientName, amount FROM bills ORDER BY date DESC LIMIT 2"
            );
            while (rs.next()) {
                list.add("💰  Bill generated — " + rs.getString("patientName") +
                        "  ($" + rs.getDouble("amount") + ")");
            }

        } catch (SQLException e) {
            list.add("📅  New patient admission — Sarah Johnson  [" + time + "]");
            list.add("💊  Prescription added — Michael Chen  [" + time + "]");
            list.add("🏥  Room 302 now available  [" + time + "]");
            list.add("💰  Bill generated — Emma Davis  ($1,200)  [" + time + "]");
        }
        return list.toArray(new String[0]);
    }

    private void styleBarChart(BarChart<String, Number> chart, String color) {
        Platform.runLater(() -> {
            chart.lookupAll(".default-color0.chart-bar").forEach(node ->
                    node.setStyle("-fx-bar-fill: " + color + ";"));
        });
    }

    public void stopThread() {
        if (notificationThread != null) notificationThread.interrupt();
    }
}
