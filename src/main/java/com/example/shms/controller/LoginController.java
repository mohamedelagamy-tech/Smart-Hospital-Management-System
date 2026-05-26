package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.PasswordEncryption;
import com.example.shms.utils.SessionManager;
import com.example.shms.utils.Validator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.shms.MainApp;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private TextField passwordVisible;
    @FXML private Button showPassword;
    @FXML private ImageView logoView;
    @FXML private ImageView backgroundView;

    private boolean isPasswordVisible=false;

    private final SessionManager session=SessionManager.getInstance();
    private final DatabaseManager db=DatabaseManager.getInstance();

    @FXML public void initialize(){
        passwordField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>(){
            @Override
            public void handle(javafx.scene.input.KeyEvent event){
                if(event.getCode()==javafx.scene.input.KeyCode.ENTER){
                    handleLogin();
                }
            }
        });
        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
        logoView.setImage(logo);

        Image bg = new Image(getClass().getResourceAsStream("/images/background.png"));
        backgroundView.setImage(bg);
    }

    @FXML private void handleLogin(){
        String username=usernameField.getText().trim();
        String password=passwordField.getText().trim();

        if(Validator.checkEmpty(username,password)){
            showError("Please fill in all fields.","empty");
            return;
        }

        if(session.locked()){
            showError("Too many failed attempts. Please restart the app.","locked");
            return;
        }

        try{
            String sql="SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps=db.getConnection().prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,PasswordEncryption.hash(password));
            ResultSet rs=ps.executeQuery();

            if(rs.next()){
                String role =rs.getString("role");
                session.login(username,role);
                logAudit(username , role , "SUCCESS");
                showError("Login successful!","success");

                session.startSessionTimer(new Runnable(){
                    @Override
                    public void run(){
                        session.logout();
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run(){
                                MainApp.navigateTo("login", 900, 650);
                            }
                        });
                    }
                });

                switch(role){
                    case "ADMIN":
                    case "DOCTOR":
                    case "NURSE":
                    case "RECEPTIONIST":MainApp.navigateTo("dashboard",1200,700);break;
                    case "PATIENT": MainApp.navigateTo("patientDashboard",1200,700);break;
                    default:showError("Unknown role detected.","error");
                }
            }else{
                session.addAttempts();
                logAudit(username,"UNKNOWN","FAILED");
                int remainingAttempts = 5-session.getLoginAttempts();
                showError("Invalid credentials. "+remainingAttempts+" attempts.","invalid");
                shakeField(passwordField);
            }

        }catch(SQLException e){
            showError("Database error: "+e.getMessage(),"dbError");
        }
    }
    @FXML private void handleShowPassword(){
        isPasswordVisible=!isPasswordVisible;
        if(isPasswordVisible){
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            showPassword.setText("\uD83D\uDD76");
        }else{
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            showPassword.setText("\uD83D\uDC41");
        }
    }

    private void showError(String message,String type){
        errorLabel.setText(message);
        switch(type){
            case "empty": errorLabel.setStyle("-fx-text-fill: orange;-fx-font-size: 11;");
                break;
            case "invalid": errorLabel.setStyle("-fx-text-fill: #A32D2D;-fx-background-color: #FCEBEB;-fx-background-radius:4;-fx-padding: 6 10;-fx-font-size: 11; ");
                break;
            case "locked": errorLabel.setStyle("-fx-text-fill: #7B0000;-fx-background-color: #FFD0D0;-fx-background-radius:4;-fx-padding: 6 10;-fx-font-size: 11; ");
                break;
            case "success": errorLabel.setStyle("-fx-text-fill: #1D6A2E;-fx-background-color: #D6F0DC;-fx-background-radius:4;-fx-padding: 6 10;-fx-font-size: 11; ");
                break;
            case "dbError": errorLabel.setStyle("-fx-text-fill: #555555;-fx-background-color: #F0F0F0;-fx-background-radius:4;-fx-padding: 6 10;-fx-font-size: 11; ");
                break;
        }
    }

    private void shakeField(javafx.scene.Node node){
        javafx.animation.TranslateTransition shake= new javafx.animation.TranslateTransition(Duration.millis(80),node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(5);
        shake.setAutoReverse(true);
        shake.setOnFinished(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                node.setTranslateX(0);
            }
        });
        shake.play();
    }

    private void logAudit(String username, String role, String status){
        try {
            String sql="INSERT INTO auditLog (username, role,status,timestamp) VALUES (?, ?, ?, datetime('now','+3 hours'))";
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,role);
            ps.setString(3,status);
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println("Audit log failed: "+e.getMessage());
        }
    }

    private void fadeToScene(String fxmlFile, int width, int height) {
        FadeTransition fade = new FadeTransition(Duration.millis(500));
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setNode(usernameField.getScene().getRoot());
        fade.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainApp.navigateTo(fxmlFile, width, height);
            }
        });
        fade.play();
    }
}