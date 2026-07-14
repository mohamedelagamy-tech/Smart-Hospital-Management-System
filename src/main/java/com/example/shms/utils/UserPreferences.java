package com.example.shms.utils;

import java.io.*;
import java.util.Properties;

public class UserPreferences {
    private static final String prefsFile="shmsPreferences.properties";
    private static Properties props= new Properties();

    static {
        load();
    }

    private static void load(){
        try(FileInputStream fis= new FileInputStream(prefsFile)){
            props.load(fis);
        }catch(IOException e){
            props.setProperty("theme","light");
            props.setProperty("language","English");
            props.setProperty("fontSize","medium");
        }
    }

    private static void save(){
        try(FileOutputStream fos=new FileOutputStream(prefsFile)){
            props.store(fos,"SHMS user preferences");
        }catch(IOException e){
            System.out.println("Failed to save user preferences"+e.getMessage());
        }
    }
    public static String getTheme(){
        return props.getProperty("theme","light");
    }
    public static String getLanguage(){
        return props.getProperty("language","English");
    }

    public static void setTheme(String theme){
        props.setProperty("theme", theme);
        save();
    }
    public static void setLanguage(String lang){
        props.setProperty("language", lang);
        save();
    }
}