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

    private final SessionManager session = SessionManager.getInstance();
    private final DatabaseManager db = DatabaseManager.getInstance();

    @Override
    public void initialize(URL url,ResourceBundle rb){
        setupUserInfo();
        setupClock();
        setupRoleBasedMenu();
        loadStats();
    }

    private void setupUserInfo(){
        String user = session.getLoggedInUser();
        String role = session.getLoggedInRole();
        userLabel.setText(user);
        roleLabel.setText(role);
        welcomeLabel.setText("Good morning, " + user);
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
                btnAuditLog.setVisible(false);
                btnDepartments.setVisible(false);
                btnRooms.setVisible(false);
                break;
            case "NURSE":
                btnDoctors.setVisible(false);
                btnBilling.setVisible(false);
                btnAuditLog.setVisible(false);
                btnDepartments.setVisible(false);
                btnPrescriptions.setVisible(false);
                break;
            case "RECEPTIONIST":
                btnDoctors.setVisible(false);
                btnRecords.setVisible(false);
                btnPrescriptions.setVisible(false);
                btnAuditLog.setVisible(false);
                btnDepartments.setVisible(false);
                break;
            case "PATIENT":
                btnDoctors.setVisible(false);
                btnRecords.setVisible(false);
                btnEmergency.setVisible(false);
                btnBilling.setVisible(false);
                btnRooms.setVisible(false);
                btnDepartments.setVisible(false);
                btnAuditLog.setVisible(false);
                break;
            default:
                break;
        }
    }

    private void loadStats(){
        try (Statement st= db.getConnection().createStatement()) {
            ResultSet rs;

            rs= st.executeQuery("SELECT COUNT(*) FROM patients");
            statPatients.setText(String.valueOf(rs.getInt(1)));

            rs= st.executeQuery("SELECT COUNT(*) FROM doctors");
            statDoctors.setText(String.valueOf(rs.getInt(1)));

            rs= st.executeQuery("SELECT COUNT(*) FROM appointments");
            statAppointments.setText(String.valueOf(rs.getInt(1)));

            ResultSet available = st.executeQuery("SELECT COUNT(*) FROM rooms WHERE status = 'Available'");
            ResultSet total = st.executeQuery("SELECT COUNT(*) FROM rooms");
            statRooms.setText(available.getInt(1) + "/" + total.getInt(1));

        } catch (SQLException e) {
            System.out.println("Stats load failed: "+e.getMessage());
        }
    }

    @FXML
    private void showDashboard(){
        MainApp.navigateTo("dashboard.fxml",1200,700);
    }
    @FXML
    private void handleLogout(){
        session.stopSessionTimer();
        session.logout();
        MainApp.navigateTo("login.fxml",800,500);
    }
    @FXML
    private void showPatients(){
        MainApp.navigateTo("PatientView.fxml",1200,700);
    }
    @FXML
    private void showDoctors(){
        MainApp.navigateTo("DoctorView.fxml",1200,700);
    }
    @FXML
    private void showAppointments(){
        MainApp.navigateTo("AppointmentView.fxml", 1200,700);
    }
    @FXML
    private void showRecords(){
        MainApp.navigateTo("MedicalRecords.fxml",1200,700);
    }
    @FXML
    private void showPrescriptions(){
        MainApp.navigateTo("PrescriptionView.fxml",1200,700);
    }
    @FXML
    private void showEmergency(){
        MainApp.navigateTo("EmergencyQueue.fxml",1200,700);
    }
    @FXML
    private void showBilling(){
        MainApp.navigateTo("BillingView.fxml",1200,700);
    }
    @FXML
    private void showRooms(){
        MainApp.navigateTo("RoomView.fxml",1200,700);
    }
    @FXML
    private void showDepartments(){
        MainApp.navigateTo("DepartmentView.fxml",1200,700);
    }
    @FXML private void showAuditLog(){
        MainApp.navigateTo("auditLog.fxml",1200,700);
    }
}