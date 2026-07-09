package com.example.shms.utils;

import javafx.scene.Scene;

public class ThemeManager {

    private static Scene currentScene;

    public static void setScene(Scene scene){
        currentScene=scene;
    }

    public static void applyTheme(String theme){
        if(currentScene==null) {
            return;
        }

        currentScene.getStylesheets().clear();

        String cssFile;
        switch(theme){
            case "dark": cssFile = "/css/darkTheme.css";break;
            case "high-contrast": cssFile = "/css/highContrast.css";break;
            default: cssFile = "/css/lightTheme.css";break;
        }

        var url=ThemeManager.class.getResource(cssFile);
        if(url !=null) {
            currentScene.getStylesheets().add(url.toExternalForm());
        }

        UserPreferences.setTheme(theme);
    }

    public static void applyFontSize(String size){
        if(currentScene==null){
            return;
        }

        double fontSize;
        switch(size){
            case "small": fontSize=11;break;
            case "large": fontSize=15;break;
            default: fontSize=13;break;
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