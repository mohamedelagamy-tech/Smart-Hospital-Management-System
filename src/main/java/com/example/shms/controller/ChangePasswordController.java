package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.PasswordEncryption;
import com.example.shms.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePasswordController {

    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private final DatabaseManager db=DatabaseManager.getInstance();
    private final SessionManager session=SessionManager.getInstance();

    @FXML private void handleChangePassword(){
        String current=currentPasswordField.getText().trim();
        String newPass=newPasswordField.getText().trim();
        String confirm=confirmPasswordField.getText().trim();

        if(current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()){
            showMessage("Please fill in all fields.",false);
            return;
        }

        if(newPass.length()<8){
            showMessage("New password must be at least 8 characters.",false);
            return;
        }

        if(!newPass.equals(confirm)){
            showMessage("New passwords do not match.",false);
            return;
        }

        if(newPass.equals(current)){
            showMessage("New password must be different from current password.",false);
            return;
        }

        String username = session.getLoggedInUser();
        String hashedCurrent = PasswordEncryption.hash(current);

        try (PreparedStatement ps = db.getConnection().prepareStatement(
                "SELECT id FROM users WHERE username = ? AND password = ?")) {
            ps.setString(1, username);
            ps.setString(2, hashedCurrent);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                showMessage("Current password is incorrect.",false);
                return;
            }

            String hashedNew = PasswordEncryption.hash(newPass);
            try(PreparedStatement update=db.getConnection().prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {
                update.setString(1,hashedNew);
                update.setString(2,username);
                update.executeUpdate();
                showMessage("Password updated successfully! Please log in again.",true);

                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        try {
                            Thread.sleep(2000);
                        }catch(InterruptedException e){ }
                        javafx.application.Platform.runLater(new Runnable() {
                            @Override
                            public void run(){
                                session.logout();
                                MainApp.navigateTo("login",900,650);
                            }
                        });
                    }
                }).start();
            }

        }catch(SQLException e){
            showMessage("Error updating password: "+e.getMessage(),false);
        }
    }

    private void showMessage(String message, boolean success){
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " +
                (success ? "#1D6A2E; -fx-background-color: #D6F0DC;" : "#A32D2D; -fx-background-color: #FCEBEB;") +
                " -fx-background-radius: 6; -fx-padding: 8 12;");
    }

    @FXML private void handleBack(){
        MainApp.navigateTo("settings",1200,700);
    }
}