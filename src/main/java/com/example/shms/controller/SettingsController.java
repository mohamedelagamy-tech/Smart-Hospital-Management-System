package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.utils.SessionManager;
import com.example.shms.utils.ThemeManager;
import com.example.shms.utils.UserPreferences;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

public class SettingsController {

    @FXML private ToggleButton btnLight;
    @FXML private ToggleButton btnDark;
    @FXML private ToggleButton btnHighContrast;
    @FXML private ToggleButton btnSmall;
    @FXML private ToggleButton btnMedium;
    @FXML private ToggleButton btnLarge;
    @FXML private ToggleButton btnEnglish;
    @FXML private ToggleButton btnArabic;
    @FXML private ToggleButton btn2FA;

    @FXML public void initialize(){
        switch(UserPreferences.getTheme()){
            case "dark": btnDark.setSelected(true);break;
            case "high-contrast": btnHighContrast.setSelected(true);break;
            default: btnLight.setSelected(true);break;
        }

        switch(UserPreferences.getFontSize()){
            case "small": btnSmall.setSelected(true);break;
            case "large": btnLarge.setSelected(true);break;
            default: btnMedium.setSelected(true);break;
        }

        if(UserPreferences.getLanguage().equals("Arabic")){
            btnArabic.setSelected(true);
        }else {
            btnEnglish.setSelected(true);
        }
    }

    @FXML private void handleThemeChange(){
        String theme="light";
        if(btnDark.isSelected()){
            theme="dark";
            btnDark.setStyle("-fx-background-color: #1F4E79; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 0; -fx-cursor: hand; -fx-padding: 8 16;");
            btnLight.setStyle("-fx-background-color: white; -fx-text-fill: #555; -fx-font-size: 12px; -fx-background-radius: 8 0 0 8; -fx-cursor: hand; -fx-padding: 8 16;");
            btnHighContrast.setStyle("-fx-background-color: white; -fx-text-fill: #555; -fx-font-size: 12px; -fx-background-radius: 0 8 8 0; -fx-cursor: hand; -fx-padding: 8 16;");
        }else if(btnHighContrast.isSelected()){
            theme="high-contrast";
            btnHighContrast.setStyle("-fx-background-color: #1F4E79; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 0 8 8 0; -fx-cursor: hand; -fx-padding: 8 16;");
            btnLight.setStyle("-fx-background-color: white; -fx-text-fill: #555; -fx-font-size: 12px; -fx-background-radius: 8 0 0 8; -fx-cursor: hand; -fx-padding: 8 16;");
            btnDark.setStyle("-fx-background-color: white; -fx-text-fill: #555; -fx-font-size: 12px; -fx-background-radius: 0; -fx-cursor: hand; -fx-padding: 8 16;");
        }else {
            btnLight.setStyle("-fx-background-color: #1F4E79; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 8 0 0 8; -fx-cursor: hand; -fx-padding: 8 16;");
            btnDark.setStyle("-fx-background-color: white; -fx-text-fill: #555; -fx-font-size: 12px; -fx-background-radius: 0; -fx-cursor: hand; -fx-padding: 8 16;");
            btnHighContrast.setStyle("-fx-background-color: white; -fx-text-fill: #555; -fx-font-size: 12px; -fx-background-radius: 0 8 8 0; -fx-cursor: hand; -fx-padding: 8 16;");
        }
        ThemeManager.applyTheme(theme);
    }

    @FXML private void handleFontChange(){
        String size="medium";
        if(btnSmall.isSelected()){
            size="small";
            btnSmall.setStyle("-fx-font-size: 11px; -fx-background-color: #1F4E79; -fx-text-fill: white; -fx-background-radius: 8 0 0 8; -fx-cursor: hand; -fx-padding: 8 16;");
            btnMedium.setStyle("-fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: #555; -fx-background-radius: 0; -fx-cursor: hand; -fx-padding: 8 16; -fx-border-color: #EEEEEE; -fx-border-width: 0 1 0 1;");
            btnLarge.setStyle("-fx-font-size: 15px; -fx-background-color: white; -fx-text-fill: #555; -fx-background-radius: 0 8 8 0; -fx-cursor: hand; -fx-padding: 8 16;");
        }else if(btnLarge.isSelected()){
            size="large";
            btnSmall.setStyle("-fx-font-size: 11px; -fx-background-color: white; -fx-text-fill: #555; -fx-background-radius: 8 0 0 8; -fx-cursor: hand; -fx-padding: 8 16;");
            btnMedium.setStyle("-fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: #555; -fx-background-radius: 0; -fx-cursor: hand; -fx-padding: 8 16; -fx-border-color: #EEEEEE; -fx-border-width: 0 1 0 1;");
            btnLarge.setStyle("-fx-font-size: 15px; -fx-background-color: #1F4E79; -fx-text-fill: white; -fx-background-radius: 0 8 8 0; -fx-cursor: hand; -fx-padding: 8 16;");
        }else {
            btnSmall.setStyle("-fx-font-size: 11px; -fx-background-color: white; -fx-text-fill: #555; -fx-background-radius: 8 0 0 8; -fx-cursor: hand; -fx-padding: 8 16;");
            btnMedium.setStyle("-fx-font-size: 13px; -fx-background-color: #1F4E79; -fx-text-fill: white; -fx-background-radius: 0; -fx-cursor: hand; -fx-padding: 8 16; -fx-border-color: #EEEEEE; -fx-border-width: 0 1 0 1;");
            btnLarge.setStyle("-fx-font-size: 15px; -fx-background-color: white; -fx-text-fill: #555; -fx-background-radius: 0 8 8 0; -fx-cursor: hand; -fx-padding: 8 16;");
        }
        ThemeManager.applyFontSize(size);
    }

    @FXML private void handleLanguageChange(){
        String lang=btnArabic.isSelected() ? "Arabic":"English";
        UserPreferences.setLanguage(lang);
        String dashboard=SessionManager.getInstance().getDashboardName();
        MainApp.navigateTo(dashboard,1200,700);
    }

    @FXML private void handleChangePassword(){
        MainApp.navigateTo("changePassword",900,600);
    }

    @FXML private void handle2FAToggle(){
        if(btn2FA.isSelected()){
            btn2FA.setText("Enabled ✓");
            btn2FA.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-size: 12px; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16; -fx-border-color: #4CAF50; -fx-border-width: 1;");
        }else {
            btn2FA.setText("Enable");
            btn2FA.setStyle("-fx-background-color: white; -fx-text-fill: #555; -fx-font-size: 12px; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16; -fx-border-color: #EEEEEE; -fx-border-width: 1;");
        }
    }

    @FXML private void handleBack(){
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }
}