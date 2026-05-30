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

    @FXML private BarChart<String,Number> appointmentsChart;
    @FXML private PieChart occupancyChart;
    @FXML private LineChart<String,Number> revenueChart;
    @FXML private BarChart<String,Number> doctorsChart;
    @FXML private PieChart billStatusChart;
    @FXML private ListView<String> notificationsList;
    @FXML private Label notifStatusLabel;

    private final DatabaseManager db=DatabaseManager.getInstance();
    private Thread notificationThread;

    @Override
    public void initialize(URL location,ResourceBundle resources){
        loadAppointmentsChart();
        loadOccupancyChart();
        loadRevenueChart();
        loadDoctorsChart();
        loadBillStatusChart();
        startNotificationThread();
    }

    private void loadAppointmentsChart(){
        XYChart.Series<String,Number> scheduled=new XYChart.Series<>();
        scheduled.setName("Scheduled");
        XYChart.Series<String,Number> completed=new XYChart.Series<>();
        completed.setName("Completed");
        XYChart.Series<String,Number> cancelled=new XYChart.Series<>();
        cancelled.setName("Cancelled");

        try(Statement st=db.getConnection().createStatement();
            ResultSet rs=st.executeQuery(
                    "SELECT status, COUNT(*) AS total FROM appointments GROUP BY status")){
            while(rs.next()){
                String status=rs.getString("status");
                int total=rs.getInt("total");
                switch(status){
                    case "Scheduled"->scheduled.getData().add(new XYChart.Data<>("Scheduled",total));
                    case "Completed"->completed.getData().add(new XYChart.Data<>("Completed",total));
                    case "Cancelled"->cancelled.getData().add(new XYChart.Data<>("Cancelled",total));
                }
            }
        }catch(SQLException e){
            scheduled.getData().add(new XYChart.Data<>("Scheduled",6));
            completed.getData().add(new XYChart.Data<>("Completed",3));
            cancelled.getData().add(new XYChart.Data<>("Cancelled",1));
        }

        appointmentsChart.getData().setAll(scheduled,completed,cancelled);
        appointmentsChart.setLegendVisible(true);
        Platform.runLater(()->{
            appointmentsChart.lookupAll(".default-color0.chart-bar")
                    .forEach(n->n.setStyle("-fx-bar-fill: #534AB7;"));
            appointmentsChart.lookupAll(".default-color1.chart-bar")
                    .forEach(n->n.setStyle("-fx-bar-fill: #27ae60;"));
            appointmentsChart.lookupAll(".default-color2.chart-bar")
                    .forEach(n->n.setStyle("-fx-bar-fill: #e74c3c;"));
        });
    }
    private void loadOccupancyChart(){
        int occupied=0,available=0,cleaning=0;
        try(Statement st=db.getConnection().createStatement();
            ResultSet rs=st.executeQuery("SELECT status, COUNT(*) AS total FROM rooms GROUP BY status")){
            while(rs.next()){
                switch(rs.getString("status").toLowerCase()){
                    case "occupied"->occupied=rs.getInt("total");
                    case "available"->available=rs.getInt("total");
                    case "cleaning"->cleaning=rs.getInt("total");
                }
            }
        }catch(SQLException ignored){}
        ObservableList<PieChart.Data> data=FXCollections.observableArrayList(
                new PieChart.Data("Occupied ("+occupied+")",Math.max(occupied,0.01)),
                new PieChart.Data("Available ("+available+")",Math.max(available,0.01)),
                new PieChart.Data("Cleaning ("+cleaning+")",Math.max(cleaning,0.01))
        );
        occupancyChart.setData(data);
        Platform.runLater(()->{
            if(data.size()>0) data.get(0).getNode().setStyle("-fx-pie-color: #e74c3c;");
            if(data.size()>1) data.get(1).getNode().setStyle("-fx-pie-color: #27ae60;");
            if(data.size()>2) data.get(2).getNode().setStyle("-fx-pie-color: #f97316;");
            occupancyChart.lookupAll(".chart-legend-item-symbol").forEach(node ->{
                javafx.scene.control.Label parent=(javafx.scene.control.Label)node.getParent();
                String label=parent.getText();
                if(label.startsWith("Occupied")) node.setStyle("-fx-background-color:#e74c3c;");
                if(label.startsWith("Available")) node.setStyle("-fx-background-color:#27ae60");
                if(label.startsWith("Cleaning")) node.setStyle("-fx-background-color:#f97316");
            });
        });
    }
    private void loadRevenueChart(){
        XYChart.Series<String,Number> series=new XYChart.Series<>();
        series.setName("Revenue (EGP)");
        try(Statement st=db.getConnection().createStatement();
            ResultSet rs=st.executeQuery(
                    "SELECT date, SUM(amount) AS total FROM bills "+
                            "WHERE status='Paid' GROUP BY date ORDER BY date ASC LIMIT 7")){
            while(rs.next())
                series.getData().add(new XYChart.Data<>(rs.getString("date"),rs.getDouble("total")));
        }catch(SQLException e){
            series.getData().addAll(
                    new XYChart.Data<>("Jan",12000),new XYChart.Data<>("Feb",18500),
                    new XYChart.Data<>("Mar",15000),new XYChart.Data<>("Apr",22000),
                    new XYChart.Data<>("May",19500)
            );
        }
        revenueChart.getData().setAll(series);
        revenueChart.setLegendVisible(false);
        revenueChart.setCreateSymbols(true);
    }
    private void loadDoctorsChart(){
        XYChart.Series<String,Number> series=new XYChart.Series<>();
        series.setName("Patients");
        try(Statement st=db.getConnection().createStatement();
            ResultSet rs=st.executeQuery(
                    "SELECT department, COUNT(*) AS total FROM patients "+
                            "GROUP BY department ORDER BY total DESC LIMIT 8")){
            while(rs.next())
                series.getData().add(new XYChart.Data<>(rs.getString("department"),rs.getInt("total")));
        }catch(SQLException e){
            series.getData().addAll(
                    new XYChart.Data<>("Cardiology",22),
                    new XYChart.Data<>("Oncology",18),
                    new XYChart.Data<>("Neurology",15),
                    new XYChart.Data<>("Orthopedics",12),
                    new XYChart.Data<>("Dermatology",9)
            );
        }
        doctorsChart.getData().setAll(series);
        doctorsChart.setLegendVisible(false);
        styleBarChart(doctorsChart,"#1D9E75");
    }
    private void loadBillStatusChart(){
        int paid=0,pending=0,partial=0,overdue=0;
        try(Statement st=db.getConnection().createStatement();
            ResultSet rs=st.executeQuery("SELECT status, COUNT(*) AS total FROM bills GROUP BY status")){
            while(rs.next()){
                switch(rs.getString("status")){
                    case "Paid"->paid=rs.getInt("total");
                    case "Pending"->pending=rs.getInt("total");
                    case "Partially Paid"->partial=rs.getInt("total");
                    case "Overdue"->overdue=rs.getInt("total");
                }
            }
        }catch(SQLException ignored){}
        ObservableList<PieChart.Data> data=FXCollections.observableArrayList(
                new PieChart.Data("Paid ("+paid+")",Math.max(paid,0.01)),
                new PieChart.Data("Pending ("+pending+")",Math.max(pending,0.01)),
                new PieChart.Data("Partial ("+partial+")",Math.max(partial,0.01)),
                new PieChart.Data("Overdue ("+overdue+")",Math.max(overdue,0.01))
        );
        billStatusChart.setData(data);
        Platform.runLater(()->{
            if(data.size()>0) data.get(0).getNode().setStyle("-fx-pie-color: #27ae60;");
            if(data.size()>1) data.get(1).getNode().setStyle("-fx-pie-color: #7f8c8d;");
            if(data.size()>2) data.get(2).getNode().setStyle("-fx-pie-color: #e67e22;");
            if(data.size()>3) data.get(3).getNode().setStyle("-fx-pie-color: #e74c3c;");
           billStatusChart.lookupAll(".chart-legend-item-symbol").forEach(node ->{
               javafx.scene.control.Label parent=(javafx.scene.control.Label)node.getParent();
               String label=parent.getText();
               if(label.startsWith("Paid")) node.setStyle("-fx-background-color:#27ae60;");
               if(label.startsWith("Pending")) node.setStyle("-fx-background-color:#7f8c8d;");
               if(label.startsWith("Partial")) node.setStyle("-fx-background-color:#e67e22;");
               if(label.startsWith("Overdue")) node.setStyle("-fx-background-color:#e74c3c;");

           });
        });
    }
    private void startNotificationThread(){
        ObservableList<String> notifications=FXCollections.observableArrayList();
        notificationsList.setItems(notifications);
        notificationThread=new Thread(()->{
            while(!Thread.currentThread().isInterrupted()){
                try{
                    String time=LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
                    String[] messages=fetchLatestNotifications(time);
                    Platform.runLater(()->{
                        notifications.setAll(messages);
                        notifStatusLabel.setText("● Live — Last updated: "+time);
                    });
                    Thread.sleep(30_000);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        });
        notificationThread.setDaemon(true);
        notificationThread.start();
    }
    private String[] fetchLatestNotifications(String time){
        ObservableList<String> list=FXCollections.observableArrayList();
        try(Statement st=db.getConnection().createStatement()){
            ResultSet rs=st.executeQuery(
                    "SELECT p.name, a.date FROM appointments a "+
                            "JOIN patients p ON a.patientId=p.id ORDER BY a.id DESC LIMIT 3"
            );
            while(rs.next())
                list.add("📅 Appointment — "+rs.getString("name")+" on "+rs.getString("date"));
            rs=st.executeQuery(
                    "SELECT p.name, b.amount FROM bills b "+
                            "JOIN patients p ON b.patientID=p.id ORDER BY b.id DESC LIMIT 2"
            );
            while(rs.next())
                list.add("💰 Bill — "+rs.getString("name")+" — EGP "+String.format("%.0f",rs.getDouble("amount")));
        }catch(SQLException ignored){
            list.add("📅 New admission — Sara Mohamed ["+time+"]");
            list.add("💊 Prescription added — Omar Ali ["+time+"]");
            list.add("💰 Bill generated — Nour Hassan ["+time+"]");
        }
        return list.toArray(new String[0]);
    }
    private void styleBarChart(BarChart<String,Number> chart,String color){
        Platform.runLater(()->
                chart.lookupAll(".default-color0.chart-bar")
                        .forEach(node->node.setStyle("-fx-bar-fill: "+color+";"))
        );
    }
    public void stopThread(){
        if(notificationThread!=null) notificationThread.interrupt();
    }
    @FXML private void handleBack(){
        stopThread();
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }
}