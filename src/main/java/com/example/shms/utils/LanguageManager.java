package com.example.shms.utils;

public class LanguageManager {
    private static java.util.ResourceBundle bundle;
    private static String currentLanguage = "English";

    public static void setLanguage(String language) {
        currentLanguage = language;
        java.util.Locale locale;
        if ("Arabic".equals(language)) {
            locale = new java.util.Locale("ar");
        } else {
            locale = java.util.Locale.ENGLISH;
        }
        bundle = java.util.ResourceBundle.getBundle(
                "i18n/strings", locale);
    }

    public static String get(String key) {
        try {
            if (bundle == null) setLanguage("English");
            return bundle.getString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public static boolean isArabic() {
        return "Arabic".equals(currentLanguage);
    }

    public static javafx.geometry.NodeOrientation getOrientation() {
        return isArabic()
                ? javafx.geometry.NodeOrientation.RIGHT_TO_LEFT
                : javafx.geometry.NodeOrientation.LEFT_TO_RIGHT;
    }
}
