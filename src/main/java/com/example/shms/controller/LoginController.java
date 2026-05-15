package com.example.shms.controller;

import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.SessionManager;
import com.example.shms.utils.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.shms.MainApp;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final SessionManager session=SessionManager.getInstance();
    private final DatabaseManager db=DatabaseManager.getInstance();

    @FXML private void handleLogin(){
        String username=usernameField.getText().trim();
        String password=passwordField.getText().trim();

        if(Validator.checkEmpty(username,password)){
            showError("Please fill in all fields.","orange");
            return;
        }

        if(session.locked()){
            showError("Too many failed attempts. Please restart the app.","red");
            return;
        }

        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs=ps.executeQuery();

            if(rs.next()) {
                String role =rs.getString("role");
                session.login(username,role);
                logAudit(username, role,"SUCCESS");
                showError("Login successful!","green");
                MainApp.navigateTo("dashboard.fxml", 1200, 700);
            } else {
                session.addAttempts();
                logAudit(username,"UNKNOWN","FAILED");
                int remainingAttempts = 5 - session.getLoginAttempts();
                showError("Invalid credentials. "+remainingAttempts+" attempts remainingAttempts.","red");
            }

        } catch(SQLException e){
            showError("Database error: "+e.getMessage(),"red");
        }
    }

    private void showError(String message,String color){
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: "+color+";");
    }

    private void logAudit(String username, String role, String status){
        try {
            String sql="INSERT INTO auditLog (username, role,status,timestamp) VALUES (?, ?, ?, datetime('now'))";
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,role);
            ps.setString(3,status);
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println("Audit log failed: "+e.getMessage());
        }
    }
}