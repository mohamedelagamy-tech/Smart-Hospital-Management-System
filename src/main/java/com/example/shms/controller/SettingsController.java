package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.utils.SessionManager;
import com.example.shms.utils.ThemeManager;
import com.example.shms.utils.UserPreferences;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ToggleButton;

public class SettingsController {

    @FXML private ToggleButton btnLight;
    @FXML private ToggleButton btnDark;
    @FXML private ToggleButton btnHighContrast;
    @FXML private ToggleButton btnOcean;
    @FXML private ToggleButton btnForest;
    @FXML private ToggleButton btnPurple;
    @FXML private ToggleButton btnRose;
    @FXML private ToggleButton btnSand;
    @FXML private ToggleButton btnEnglish;
    @FXML private ToggleButton btnArabic;
    @FXML private ToggleButton btn2FA;

    @FXML public void initialize() {
        switch(UserPreferences.getTheme()){
            case "dark": btnDark.setSelected(true);highlightTheme(btnDark);break;
            case "high-contrast": btnHighContrast.setSelected(true);highlightTheme(btnHighContrast);break;
            case "ocean": btnOcean.setSelected(true);highlightTheme(btnOcean);break;
            case "forest": btnForest.setSelected(true);highlightTheme(btnForest);break;
            case "purple": btnPurple.setSelected(true);highlightTheme(btnPurple);break;
            case "rose": btnRose.setSelected(true);highlightTheme(btnRose);break;
            case "sand": btnSand.setSelected(true);highlightTheme(btnSand);break;
            default: btnLight.setSelected(true);highlightTheme(btnLight);break;
        }

        if(UserPreferences.getLanguage().equals("Arabic")){
            btnArabic.setSelected(true);
        }else {
            btnEnglish.setSelected(true);
        }

    }

    private void highlightTheme(ToggleButton selected){
        ToggleButton[] all={btnLight,btnDark,btnHighContrast,btnOcean,btnForest,btnPurple,btnRose,btnSand};
        for(ToggleButton btn:all){
            btn.setStyle(btn.getStyle().replace(
                    "-fx-border-color: #1F4E79; -fx-border-width: 3;", "")
                    + "; -fx-border-color: #DDDDDD; -fx-border-width: 2;");
        }
        selected.setStyle(selected.getStyle().replace(
                "-fx-border-color: #DDDDDD; -fx-border-width: 2;", "")
                + "; -fx-border-color: #1F4E79; -fx-border-width: 3;");
    }

    @FXML private void handleThemeChange(){
        String theme="light";
        if(btnDark.isSelected()){
            theme="dark";
            highlightTheme(btnDark);
        }else if(btnHighContrast.isSelected()){
            theme="high-contrast";
            highlightTheme(btnHighContrast);
        }else if(btnOcean.isSelected()){
            theme="ocean";
            highlightTheme(btnOcean);
        }else if(btnForest.isSelected()){
            theme="forest";
            highlightTheme(btnForest);
        }else if(btnPurple.isSelected()){
            theme="purple";
            highlightTheme(btnPurple);
        }else if(btnRose.isSelected()){
            theme="rose";
            highlightTheme(btnRose);
        }else if(btnSand.isSelected()){
            theme="sand";
            highlightTheme(btnSand);
        }else {
            highlightTheme(btnLight);
        }
        ThemeManager.applyTheme(theme);
    }

    @FXML
    private void handleLanguageChange() {
        boolean isArabic = btnArabic.isSelected();
        String lang = isArabic ? "Arabic" : "English";

        com.example.shms.utils.UserPreferences.setLanguage(lang);
        com.example.shms.utils.LanguageManager.setLanguage(lang);

        javafx.stage.Stage stage = (javafx.stage.Stage) btnArabic.getScene().getWindow();
        stage.getScene().getRoot().setNodeOrientation(
                com.example.shms.utils.LanguageManager.getOrientation());

        com.example.shms.MainApp.navigateTo(
                com.example.shms.utils.SessionManager.getInstance().getDashboardName(),
                1200, 700);
    }

    @FXML private void handleChangePassword(){
        MainApp.navigateTo("changePassword",900,600);
    }

    @FXML private void handle2FAToggle() {
        if(btn2FA.isSelected()){
            btn2FA.setText("Enabled ✓");
        }else {
            btn2FA.setText("Enable");
        }
    }

    @FXML private void handleBack(){
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }
}