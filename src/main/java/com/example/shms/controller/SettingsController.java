package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.SessionManager;
import com.example.shms.utils.ThemeManager;
import com.example.shms.utils.UserPreferences;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import com.example.shms.utils.LanguageManager;
import javafx.scene.control.Label;
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
    @FXML private Button backBtn;
    @FXML private Button changePasswordBtn;
    @FXML private Label settingsTitle;
    @FXML private Label titleLabel;
    @FXML private Label themeLabel;
    @FXML private Label appearanceLabel;
    @FXML private Label themeDescLabel;
    @FXML private Label languageLabel;
    @FXML private Label displayLanguageLabel;
    @FXML private Label languageDescLabel ;
    @FXML private Label accountLabel;
    @FXML private Label changePasswordLabel;
    @FXML private Label changePasswordDescLabel;
    @FXML private Label twoFactorLabel;
    @FXML private Label twoFactorDescLabel;

    private final DatabaseManager db = DatabaseManager.getInstance();


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
            titleLabel.setText("⚙ الإعدادات");

            appearanceLabel.setText("🎨 المظهر");
            themeLabel.setText("السمة");
            themeDescLabel.setText("اختر مظهر النظام في جميع الصفحات");

            languageLabel.setText("🌐 اللغة");
            displayLanguageLabel.setText("لغة العرض");
            languageDescLabel.setText("سيتم إعادة تحميل النظام بعد تغيير اللغة");

            accountLabel.setText("👤 الحساب");

            changePasswordLabel.setText("تغيير كلمة المرور");
            changePasswordDescLabel.setText("تحديث كلمة مرور الحساب");

            twoFactorLabel.setText("التحقق بخطوتين");
            twoFactorDescLabel.setText("إرسال رمز تحقق إلى بريدك الإلكتروني عند تسجيل الدخول");

            backBtn.setText("→ رجوع");
            changePasswordBtn.setText("تغيير ←");

            btnEnglish.setText("English");
            btnArabic.setText("العربية");

            btn2FA.setText("تفعيل");
            btnArabic.setSelected(true);
        }else {
            btnEnglish.setSelected(true);
        }
        if (LanguageManager.isArabic()) {
            titleLabel.setText("⚙ الإعدادات");
        }
        if (LanguageManager.isArabic()) {
            backBtn.setText("→ رجوع");
        }
        if (LanguageManager.isArabic()) {

            btn2FA.setText("تفعيل");

        }
        String username=SessionManager.getInstance().getLoggedInUser();
        boolean twoFAEnabled=db.isTwoFactorEnabled(username);
        btn2FA.setSelected(twoFAEnabled);
        if(twoFAEnabled){
            btn2FA.setText("Enabled ✓");
            btn2FA.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-size: 12px; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16; -fx-border-color: #4CAF50; -fx-border-width: 1;");
        }

        String role=SessionManager.getInstance().getLoggedInRole();
        if(!role.equals("PATIENT")){
            btn2FA.setVisible(false);
            btn2FA.setManaged(false);
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
        String username=SessionManager.getInstance().getLoggedInUser();
        boolean enabled=btn2FA.isSelected();
        db.setTwoFactorEnabled(username, enabled);
        if(enabled){
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