module com.example.shms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;


    opens com.example.shms to javafx.fxml;
    exports com.example.shms;
}