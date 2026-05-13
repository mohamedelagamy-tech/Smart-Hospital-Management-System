module com.example.shms {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.shms to javafx.fxml;
    exports com.example.shms;
}