package com.example.shms;
import com.example.shms.model.Bill;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class BillingScreen extends Application {

    private TableView<Bill> billingTable;
    private ObservableList<Bill> allBills;
    private TextField searchField;
    private ComboBox<String> sortBox;

    public void show(Stage stage){
        Label title = new Label("Billing Management");
        title.setFont(Font.font("Georgia",FontWeight.BOLD,28));

        searchField=new TextField();
        searchField.setPromptText("search by patient neme , bill number ,date ...");
        searchField.setPrefWidth(280);
        searchField.setPrefHeight(40);
        searchField.setStyle(
                "-fx-background-color: white"+ "-fx-border-color: #cccccc" +
                "-fx-border-radius: 6;" + "-fx-background-radius: 6;"+"-fx-padding: 0 12;"
                "-fx-font-size: 13px;");
        searchField.textProperty().addListener((obs,oldVal,newVal)-> applyFilters());

        sortBox = new ComboBox<>();
        sortBox.getItems().addAll( "Date (Newest First",
                "Date (Oldest First)", "Amount (High to Low)" , "Amount (Low to High");
        sortBox.setValue("Date (Newest First");
        sortBox.setPrefHeight(40);
        sortBox.setStyle();
    }
}
