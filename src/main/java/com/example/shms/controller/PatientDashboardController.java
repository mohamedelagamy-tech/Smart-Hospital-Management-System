package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PatientDashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label userLabel;
    @FXML private Label roleLabel;
    @FXML private Label clockLabel;
    @FXML private Label userInitials;
    @FXML private Label nextApptLabel;
    @FXML private Label nextApptDetail;
    @FXML private Label profileInitials;
    @FXML private Label profileName;
    @FXML private Label profileStatus;
    @FXML private Label profileBlood;
    @FXML private Label profileDept;
    @FXML private Label profilePhone;
    @FXML private Label profileAddress;
    @FXML private Label totalBillLabel;
    @FXML private Label paidBillLabel;
    @FXML private Label unpaidBillLabel;
    @FXML private ListView<String> appointmentsList;
    @FXML private ListView<String> billsList;
    @FXML private ListView<String> timelineList;
    @FXML private ImageView logoView;

    private final SessionManager session=SessionManager.getInstance();
    private final DatabaseManager db=DatabaseManager.getInstance();

    @Override
    public void initialize(URL url,ResourceBundle rb){
        setupLogo();
        setupUserInfo();
        setupClock();
        loadPatientData();
    }

    private void setupLogo(){
        try{
            Image logo= new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoView.setImage(logo);
        }catch(Exception e){
            System.out.println("Failed to load logo: "+e.getMessage());
        }
    }

    private void setupUserInfo(){
        String user=session.getLoggedInUser();
        userLabel.setText(user);
        roleLabel.setText("PATIENT");
        String initials=user.substring(0,Math.min(2,user.length())).toUpperCase();
        userInitials.setText(initials);
        profileInitials.setText(initials);

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
    }

    private void setupClock(){
        Timeline clock=new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                String time=LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE dd MMM  •  hh:mm a"));
                clockLabel.setText(time);
            }
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void loadPatientData(){
        String username=session.getLoggedInUser();
        try(Statement st=db.getConnection().createStatement()){

            ResultSet patientRs=st.executeQuery("SELECT * FROM patients WHERE username = '"+username+"' LIMIT 1");

            if(patientRs.next()){
                int patientId=patientRs.getInt("id");
                String name=patientRs.getString("name");
                profileName.setText(name);
                profileBlood.setText(patientRs.getString("bloodType"));
                profileDept.setText(patientRs.getString("department"));
                profilePhone.setText(patientRs.getString("phone"));
                profileAddress.setText(patientRs.getString("address"));

                loadAppointments(patientId);
                loadBills(patientId);
                loadTimeline(patientId,name);
            }

        }catch(SQLException e){
            System.out.println("Failed to load patient data: "+e.getMessage());
        }
    }


    private void loadAppointments(int patientId){
        List<String> items=new ArrayList<>();
        try(Statement st=db.getConnection().createStatement()){
            ResultSet rs=st.executeQuery(
                    "SELECT a.*, d.name as doctorName FROM appointments a "+
                            "JOIN doctors d ON a.doctorID = d.id "+
                            "WHERE a.patientID = "+patientId+
                            " AND a.status IN ('Scheduled','Completed') ORDER BY a.date LIMIT 5");

            boolean first=true;
            while(rs.next()){
                String entry="📅  "+rs.getString("date")+
                        " at "+rs.getString("time")+
                        "  —  "+rs.getString("doctorName")+
                        "  ["+rs.getString("status")+"]";
                items.add(entry);

                if(first){
                    nextApptLabel.setText("Next: "+rs.getString("date")+
                            " at "+rs.getString("time"));
                    nextApptDetail.setText("With "+rs.getString("doctorName"));
                    first=false;
                }
            }

            if(items.isEmpty()){
                items.add("No upcoming appointments");
                nextApptLabel.setText("No upcoming appointments");
                nextApptDetail.setText("Book one using the button above");
            }

        }catch(SQLException e){
            System.out.println("Failed to load appointments: "+e.getMessage());
        }
        appointmentsList.setItems(FXCollections.observableArrayList(items));
    }

    private void loadBills(int patientId){
        List<String> items=new ArrayList<>();
        double total=0,paid=0,unpaid=0;

        try(Statement st=db.getConnection().createStatement()){
            ResultSet rs=st.executeQuery("SELECT * FROM bills WHERE patientID = "+patientId);

            while(rs.next()){
                double amount=rs.getDouble("amount");
                String status=rs.getString("status");
                total+=amount;
                if(status.equals("Paid")){
                    paid+=amount;
                }else{
                    unpaid+=amount;
                }
                String icon;
                if(status.equals("Paid")){
                    icon="✅";
                }else if(status.equals("Partially Paid")){
                    icon="🔶";
                }else{
                    icon="❌";
                }
                String entry=icon+"  "+rs.getString("service")+
                        "  —  EGP "+String.format("%.0f",amount)+
                        "  ["+status+"]";
                items.add(entry);
            }

        }catch(SQLException e){
            System.out.println("Failed to load bills: "+e.getMessage());
        }

        totalBillLabel.setText("EGP "+String.format("%.0f",total));
        paidBillLabel.setText("EGP "+String.format("%.0f",paid));
        unpaidBillLabel.setText("EGP "+String.format("%.0f",unpaid));

        if(items.isEmpty())
            items.add("No bills found");
        billsList.setItems(FXCollections.observableArrayList(items));
    }

    private void loadTimeline(int patientId,String name){
        List<String> items=new ArrayList<>();
        try(Statement st=db.getConnection().createStatement()){

            ResultSet appts=st.executeQuery("SELECT a.date, a.status, d.name as doctorName "+
                    "FROM appointments a JOIN doctors d ON a.doctorId = d.id "+
                    "WHERE a.patientId = "+patientId+" ORDER BY a.date DESC");
            while(appts.next()){
                items.add("📅  Appointment — "+appts.getString("doctorName")+
                        "  •  "+appts.getString("date")+
                        "  ["+appts.getString("status")+"]");
            }

            ResultSet prescriptions=st.executeQuery("SELECT * FROM prescriptions WHERE patientID = "+patientId+" ORDER BY dateIssued DESC");
            while(prescriptions.next()){
                items.add("💊  Prescription — "+prescriptions.getString("medicineName")+
                        " "+prescriptions.getString("dosage")+
                        "  •  "+prescriptions.getString("dateIssued"));
            }
            items.add("🏥  Patient registered — Welcome to SHMS");

        }catch(SQLException e){
            System.out.println("Failed to load timeline: "+e.getMessage());
        }

        if(items.isEmpty())
            items.add("No history yet");
        timelineList.setItems(FXCollections.observableArrayList(items));
    }

    @FXML private void handleLogout(){
        session.stopSessionTimer();
        session.logout();
        MainApp.navigateTo("login",900,650);
    }

    @FXML private void showHome(){}
    @FXML private void showAppointments(){
        MainApp.navigateTo("appointment-management-view",1200,700);
    }
    @FXML private void showBookAppointments(){
        try{
            javafx.fxml.FXMLLoader loader=new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/bookAppointmentView.fxml"));
            javafx.scene.Parent root=loader.load();
            javafx.stage.Stage stage=new javafx.stage.Stage();
            stage.setTitle("Book Appointment");
            stage.setScene(new javafx.scene.Scene(root,420,580));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
        }catch(Exception e){
            System.out.println("Failed to open booking form: "+e.getMessage());
        }
    }
    @FXML private void showPrescriptions(){
        MainApp.navigateTo("PrescriptionScreen",1200,700);
    }
    @FXML private void showBills(){
        MainApp.navigateTo("patientBills",1200,700);
    }
}