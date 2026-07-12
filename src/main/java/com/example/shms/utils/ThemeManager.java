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

        String cssFile;
        switch (theme) {
            case "dark": cssFile="/css/darkTheme.css";break;
            case "high-contrast": cssFile="/css/highContrast.css";break;
            case "ocean": cssFile="/css/oceanTheme.css";break;
            case "forest": cssFile="/css/forestTheme.css";break;
            case "purple": cssFile="/css/purpleTheme.css";break;
            case "rose": cssFile="/css/roseTheme.css";break;
            case "sand": cssFile="/css/sandTheme.css";break;
            default: cssFile="/css/lightTheme.css";break;
        }

        var url=ThemeManager.class.getResource(cssFile);
        if (url !=null){
            currentScene.getStylesheets().add(url.toExternalForm());
        }

        UserPreferences.setTheme(theme);

        String savedSize = UserPreferences.getFontSize();
        if(!savedSize.equals("medium")){
            applyFontSizeInternal(savedSize);
        }
    }

    private static void applyFontSizeInternal(String size){
        if(currentScene==null){
            return;
        }
        double fontSize;
        switch(size){
            case "small": fontSize=11;break;
            case "large": fontSize=15; break;
            default: fontSize=13;break;
        }
        currentScene.getRoot().setStyle(
                currentScene.getRoot().getStyle() != null
                        ? currentScene.getRoot().getStyle()
                        .replaceAll("-fx-font-size:[^;]+;","")
                        +" -fx-font-size: "+fontSize+"px;"
                        : "-fx-font-size: "+fontSize+"px;"
        );
    }

    public static void applyFontSize(String size){
        applyFontSizeInternal(size);
        UserPreferences.setFontSize(size);
    }

    public static void applyCurrentPreferences(Scene scene){
        currentScene = scene;
        applyTheme(UserPreferences.getTheme());
        String size=UserPreferences.getFontSize();
        if(!size.equals("medium")){
            applyFontSizeInternal(size);
        }
    }
}