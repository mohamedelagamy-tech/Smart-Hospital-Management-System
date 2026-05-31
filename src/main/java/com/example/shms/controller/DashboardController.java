package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label userLabel;
    @FXML private Label roleLabel;
    @FXML private Label clockLabel;
    @FXML private Label statPatients;
    @FXML private Label statDoctors;
    @FXML private Label statAppointments;
    @FXML private Label statRooms;
    @FXML private Label userInitials;
    @FXML private Label labelMain;
    @FXML private Label labelMedical;
    @FXML private Label labelAdmin;
    @FXML private Label statRevenue;
    @FXML private Label statOutstanding;
    @FXML private Label statEmergency;
    @FXML private Label statPrescriptions;

    @FXML private Button btnDashboard;
    @FXML private Button btnPatients;
    @FXML private Button btnDoctors;
    @FXML private Button btnAppointments;
    @FXML private Button btnRecords;
    @FXML private Button btnPrescriptions;
    @FXML private Button btnEmergency;
    @FXML private Button btnBilling;
    @FXML private Button btnRooms;
    @FXML private Button btnDepartments;
    @FXML private Button btnAuditLog;
    @FXML private ImageView logoView;
    @FXML private Button btnStatistics;
    @FXML
    private void handleBack() {
        MainApp.navigateTo("dashboard", 1200, 700);
    }

    private final SessionManager session = SessionManager.getInstance();
    private final DatabaseManager db = DatabaseManager.getInstance();

    @Override
    public void initialize(URL url,ResourceBundle rb){
        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
        logoView.setImage(logo);

        setActiveButton(btnDashboard);
        setupUserInfo();
        setupClock();
        setupRoleBasedMenu();
        loadStats();
        autoRefresh();
    }

    private void autoRefresh() {
        Thread refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(30000);
                        javafx.application.Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                loadStats();
                            }
                        });
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        refreshThread.setDaemon(true);
        refreshThread.start();
    }

    private void setupUserInfo(){
        String user = session.getLoggedInUser();
        String role = session.getLoggedInRole();
        String initials = user.substring(0, Math.min(2, user.length())).toUpperCase();
        userLabel.setText(user);
        roleLabel.setText(role);
        int hour=LocalDateTime.now().getHour();
        String greeting;
        if(hour<12){
            greeting="Good morning";
        }else if(hour<17){
            greeting="Good afternoon";
        }else{
            greeting="Good evening";
        }
        welcomeLabel.setText(greeting+", "+user+" 👋");
        userInitials.setText(initials);
    }

    private void setupClock(){
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1),new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String time = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("EEE dd MMM  •  hh:mm a"));
                clockLabel.setText(time);
            }
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void setupRoleBasedMenu(){
        String role = session.getLoggedInRole();
        switch (role) {
            case "DOCTOR":
                btnDoctors.setVisible(false);
                btnDoctors.setManaged(false);
                btnAuditLog.setVisible(false);
                btnAuditLog.setManaged(false);
                btnDepartments.setVisible(false);
                btnDepartments.setManaged(false);
                btnRooms.setVisible(false);
                btnRooms.setManaged(false);
                labelAdmin.setVisible(false);
                labelAdmin.setManaged(false);
                btnRecords.setVisible(false);
                break;
            case "NURSE":
                btnDoctors.setVisible(false);
                btnDoctors.setManaged(false);
                btnBilling.setVisible(false);
                btnBilling.setManaged(false);
                btnAuditLog.setVisible(false);
                btnAuditLog.setManaged(false);
                btnDepartments.setVisible(false);
                btnDepartments.setManaged(false);
                btnPrescriptions.setVisible(false);
                btnPrescriptions.setManaged(false);
                labelAdmin.setVisible(false);
                labelAdmin.setManaged(false);
                break;
            case "RECEPTIONIST":
                btnDoctors.setVisible(false);
                btnDoctors.setManaged(false);
                btnRecords.setVisible(false);
                btnRecords.setManaged(false);
                btnPrescriptions.setVisible(false);
                btnPrescriptions.setManaged(false);
                btnAuditLog.setVisible(false);
                btnAuditLog.setManaged(false);
                btnDepartments.setVisible(false);
                btnDepartments.setManaged(false);
                btnStatistics.setVisible(false);
                btnStatistics.setManaged(false);
                btnRooms.setVisible(false);
                btnRooms.setManaged(false);
                break;
            default:
                break;
        }
    }

    private void loadStats(){
        try(Statement st=db.getConnection().createStatement()){
            ResultSet rs;

            rs=st.executeQuery("SELECT COUNT(*) FROM patients");
            statPatients.setText(String.valueOf(rs.getInt(1)));

            rs=st.executeQuery("SELECT COUNT(*) FROM doctors");
            statDoctors.setText(String.valueOf(rs.getInt(1)));

            rs=st.executeQuery("SELECT COUNT(*) FROM appointments");
            statAppointments.setText(String.valueOf(rs.getInt(1)));

            ResultSet available=st.executeQuery("SELECT COUNT(*) FROM rooms WHERE status = 'Available'");
            ResultSet total=st.executeQuery("SELECT COUNT(*) FROM rooms");
            statRooms.setText(available.getInt(1)+"/"+total.getInt(1));

            ResultSet rev=st.executeQuery("SELECT COALESCE(SUM(amount),0) FROM bills WHERE status = 'Paid'");
            statRevenue.setText("EGP "+String.format("%,.0f",rev.getDouble(1)));

            ResultSet out=st.executeQuery("SELECT COALESCE(SUM(amount),0) FROM bills WHERE status IN ('Pending','Overdue','Partially Paid')");
            statOutstanding.setText("EGP "+String.format("%,.0f",out.getDouble(1)));

            ResultSet emg=st.executeQuery("SELECT COUNT(*) FROM patients WHERE priority = 1");
            statEmergency.setText(String.valueOf(emg.getInt(1)));

            ResultSet presc=st.executeQuery("SELECT COUNT(*) FROM prescriptions");
            statPrescriptions.setText(String.valueOf(presc.getInt(1)));

        }catch(SQLException e){
            System.out.println("Stats load failed: "+e.getMessage());
        }
    }

    private void setActiveButton(Button active){
        Button[] all = {btnDashboard,btnPatients,btnDoctors,btnAppointments,
                btnRecords,btnPrescriptions,btnEmergency,btnBilling,
                btnRooms,btnDepartments,btnAuditLog, btnStatistics};


        for(Button btn : all){
            btn.getStyleClass().remove("nav-button-active");
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-font-size: 12px; -fx-alignment: CENTER_LEFT; -fx-padding: 9 12; -fx-background-radius: 8; -fx-cursor: hand;");
        }
        active.setStyle("-fx-background-color: #1F4E79; -fx-text-fill: white; -fx-font-size: 12px; -fx-alignment: CENTER_LEFT; -fx-padding: 9 12; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;");
    }

    @FXML
    private void showDashboard(){
        setActiveButton(btnDashboard);
        MainApp.navigateTo("dashboard",1200,700);
    }


    @FXML
    private void handleLogout(){
        session.stopSessionTimer();
        session.logout();
        MainApp.navigateTo("login",800,500);
    }
    @FXML
    private void showPatients(){
        setActiveButton(btnPatients);
        MainApp.navigateTo("PatientView",1200,700);
    }
    @FXML
    private void showDoctors(){
        setActiveButton(btnDoctors);
        MainApp.navigateTo("DoctorView",1200,700);
    }
    @FXML
    private void showAppointments(){
        setActiveButton(btnAppointments);
        MainApp.navigateTo("appointment-management-view", 1200,700);
    }

    @FXML
    private void showRecords(){
        setActiveButton(btnRecords);
        MainApp.navigateTo("MedicalRecordsScreen",1200,700);
    }
    @FXML
    private void showPrescriptions(){
        setActiveButton(btnPrescriptions);
        MainApp.navigateTo("PrescriptionScreen",1200,700);
    }
    @FXML
    private void showEmergency(){
        setActiveButton(btnEmergency);
        MainApp.navigateTo("EmergencyQueueView",1200,700);
    }
    @FXML
    private void showBilling(){
        setActiveButton(btnBilling);
        MainApp.navigateTo("BillingScreen",1200,700);
    }
    @FXML
    private void showRooms(){
        setActiveButton(btnRooms);
        MainApp.navigateTo("RoomManagement",1200,700);
    }
    @FXML
    private void showDepartments(){
        setActiveButton(btnDepartments);
        MainApp.navigateTo("DepartmentScreen",1200,700);
    }
    @FXML private void showAuditLog(){
        setActiveButton(btnAuditLog);
        MainApp.navigateTo("auditLog",1200,700);
    }
    @FXML
    private void showStatistics() {
        setActiveButton(btnStatistics);
        MainApp.navigateTo("StatisticsDashboard", 1200, 700);
    }
    @FXML
    private void showCalendar(){
        MainApp.navigateTo("CalendarView",900,700);
    }
}