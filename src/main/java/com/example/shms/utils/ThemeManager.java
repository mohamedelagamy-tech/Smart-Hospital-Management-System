package com.example.shms.utils;

import javafx.scene.Scene;

public class ThemeManager {

    private static Scene currentScene;

    public static void setScene(Scene scene){
        currentScene=scene;
    }

    public static void applyTheme(String theme){
        if(currentScene==null){
            return;
        }
        currentScene.getStylesheets().clear();
        switch(theme){
            case "dark":
                currentScene.getStylesheets().add(ThemeManager.class.getResource("/css/darkTheme.css").toExternalForm());
                break;
            case "high-contrast":
                currentScene.getStylesheets().add(ThemeManager.class.getResource("/css/highContrast.css").toExternalForm());
                break;
            default:
                currentScene.getStylesheets().add(ThemeManager.class.getResource("/css/lightTheme.css").toExternalForm());
                break;
        }
        UserPreferences.setTheme(theme);
    }

    public static void applyFontSize(String size){
        if(currentScene==null)
            return;
        double fontSize;
        switch(size){
            case "small": fontSize=11;break;
            case "large": fontSize = 15;break;
            default: fontSize = 13;break;
        }
        currentScene.getRoot().setStyle("-fx-font-size: "+fontSize+"px;");
        UserPreferences.setFontSize(size);
    }

    public static void applyCurrentPreferences(Scene scene){
        currentScene=scene;
        applyTheme(UserPreferences.getTheme());
        applyFontSize(UserPreferences.getFontSize());
    }
}