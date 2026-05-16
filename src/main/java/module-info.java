module com.example.shms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;
    opens com.example.shms to javafx.fxml;
    opens com.example.shms.controller to javafx.fxml;
    opens com.example.shms.model to javafx.fxml, javafx.base;
    exports com.example.shms;
    exports com.example.shms.controller;
}