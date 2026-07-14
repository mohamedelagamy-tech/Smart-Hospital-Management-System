package com.example.shms.controller;

import com.example.shms.MainApp;
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

    @FXML private ToggleButton btnSmall;
    @FXML private ToggleButton btnMedium;
    @FXML private ToggleButton btnLarge;
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
    @FXML private Label fontSizeLabel;
    @FXML private Label fontDescLabel;
    @FXML private Label languageLabel;
    @FXML private Label displayLanguageLabel;
    @FXML private Label languageDescLabel ;
    @FXML private Label accountLabel;
    @FXML private Label changePasswordLabel;
    @FXML private Label changePasswordDescLabel;
    @FXML private Label twoFactorLabel;
    @FXML private Label twoFactorDescLabel;




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
        switch(UserPreferences.getFontSize()){
            case "small":
                btnSmall.setSelected(true);
                break;

            case "large":
                btnLarge.setSelected(true);
                break;

            default:
                btnMedium.setSelected(true);
                break;
        }
        if(UserPreferences.getLanguage().equals("Arabic")){
            titleLabel.setText("⚙ الإعدادات");

            appearanceLabel.setText("🎨 المظهر");
            themeLabel.setText("السمة");
            themeDescLabel.setText("اختر مظهر النظام في جميع الصفحات");

            fontSizeLabel.setText("حجم الخط");
            fontDescLabel.setText("تغيير حجم الخط في النظام");

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
    private void handleFontChange() {

        String size = "medium";

        if (btnSmall.isSelected()) {
            size = "small";
        } else if (btnLarge.isSelected()) {
            size = "large";
        }

        ThemeManager.applyFontSize(size);
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