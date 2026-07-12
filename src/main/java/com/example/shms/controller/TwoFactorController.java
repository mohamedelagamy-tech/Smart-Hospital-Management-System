package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.utils.SessionManager;
import com.example.shms.utils.TwoFactorService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class TwoFactorController {

    @FXML private ImageView logoView;
    @FXML private ImageView backgroundView;
    @FXML private Label subtitleLabel;
    @FXML private TextField codeField;
    @FXML private Label timerLabel;
    @FXML private Label errorLabel;
    @FXML private Button verifyButton;
    @FXML private Button resendButton;

    private final TwoFactorService twoFactorService=new TwoFactorService();
    private final SessionManager session = SessionManager.getInstance();
    private Timeline countdownTimer;
    private int secondsRemaining=600;

    @FXML public void initialize(){
        try {
            Image logo=new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoView.setImage(logo);
            Image bg=new Image(getClass().getResourceAsStream("/images/background.png"));
            backgroundView.setImage(bg);
        }catch (Exception e){
            System.out.println("Image load failed: "+e.getMessage());
        }

        String email=session.getPendingEmail();
        String maskedEmail=maskEmail(email);
        subtitleLabel.setText("We sent a 6-digit code to "+maskedEmail);

        startCountdown();

        codeField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>(){
            @Override
            public void handle(javafx.scene.input.KeyEvent event){
                if(event.getCode()==javafx.scene.input.KeyCode.ENTER){
                    handleVerify();
                }
            }
        });
    }

    private void startCountdown(){
        if(countdownTimer !=null){
            countdownTimer.stop();
        }
        secondsRemaining=600;

        countdownTimer=new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                secondsRemaining--;
                int minutes=secondsRemaining/60;
                int seconds=secondsRemaining%60;
                timerLabel.setText(String.format("Code expires in %02d:%02d",minutes,seconds));

                if(secondsRemaining<=60){
                    timerLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #A32D2D;");
                }
                if(secondsRemaining<=0){
                    countdownTimer.stop();
                    timerLabel.setText("Code expired. Please request a new one.");
                    verifyButton.setDisable(true);
                }
            }
        }));
        countdownTimer.setCycleCount(Timeline.INDEFINITE);
        countdownTimer.play();
    }

    @FXML private void handleVerify(){
        String code=codeField.getText().trim();
        if(code.isEmpty()){
            errorLabel.setText("Please enter the verification code.");
            return;
        }

        String username=session.getPendingUsername();
        if(twoFactorService.verifyCode(username,code)){
            countdownTimer.stop();
            session.completePendingLogin();
            String role=session.getLoggedInRole();
            switch (role) {
                case "PATIENT":
                    MainApp.navigateTo("patientDashboard",1200,700);
                    break;
                default:
                    MainApp.navigateTo("dashboard", 1200, 700);
                    break;
            }
        }else {
            errorLabel.setText("Invalid or expired code. Please try again.");
            codeField.clear();
            codeField.requestFocus();
        }
    }

    @FXML private void handleResend(){
        String username=session.getPendingUsername();
        String email=session.getPendingEmail();
        twoFactorService.generateAndSendCode(username, email);
        startCountdown();
        errorLabel.setText("");
        timerLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888;");
        resendButton.setDisable(true);

        Timeline reEnable=new Timeline(new KeyFrame(Duration.seconds(30), new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                resendButton.setDisable(false);
            }
        }));
        reEnable.play();
    }

    @FXML private void handleBack(){
        if (countdownTimer != null) countdownTimer.stop();
        session.clearPendingLogin();
        MainApp.navigateTo("login",900,650);
    }

    private String maskEmail(String email){
        if(email==null||!email.contains("@")){
            return "your email";
        }
        String[] parts=email.split("@");
        String name=parts[0];
        String domain=parts[1];
        if(name.length() <= 2){
            return "**@" + domain;
        }
        return name.substring(0,2)+"***@"+domain;
    }
}