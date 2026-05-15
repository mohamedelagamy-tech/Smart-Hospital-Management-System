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
        sortBox.setStyle(
                "-fx-background-color: white;"+ "-fx-border-color: #cccccc;"+
                 "-fx-border-radius: 6;" + "-fx-background-radius: 6;" + "-fx-font-size: 13px;");
        Label sortLabel = new Label("sort By");
        sortLabel.setStyle("-fx-font-size: 11px; -fx-textfill: #888888");
        VBox sortBox2 = new VBox(2,sortLabel,sortBox);
        sortBox.setOnAction(e -> applyFilters);

        ToggleGroup filterGroup = new ToggleGroup();
        ToggleButton btnAll = makeFilterBtn("All",      filterGroup);
        ToggleButton btnPaid = makeFilterBtn("Paid",      filterGroup);
        ToggleButton btnPending = makeFilterBtn("Pending",      filterGroup);
        ToggleButton btnOverdue = makeFilterBtn("Overdue",      filterGroup);
        btnAll.setSelected(true);

        filterGroup.selectedToggleProperty().addListener((obs,o,n) -> applyFilters());

        HBox filterBar = new HBox (8, btnAll,btnPaid,btnPending,btnOverdue);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        HBox topControls = new HBox(16, searchField,sortBox2);
        topControls.setAlignment(Pos.CENTER_LEFT);
        topControls.setPadding(new Insets(0,0,16,0));

        TableColumn<Bill,String> colBillNum = new TableColumn<>("Bill Number");
        colBillNum.setCellValueFactory(new PropertyValueFactory<>("billNumber"));
        colBillNum.setPrefWidth(140);

        TableColumn<Bill , String> colPatient= new TableColumn<>("patient Name");
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colPatient.setPrefWidth(160);

        TableColumn<Bill , String> colService = new TableColumn<>("Service");
        colService.setCellValueFactory(new PropertyValueFactory<>("service"));
        colService.setPrefWidth(180);

        TableColumn<Bill , String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setPrefWidth(100);

        TableColumn<Bill , String> colAmount = new TableColumn<>("Amount");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setPrefWidth(110);
        colAmount.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<Bill , String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(100);

        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label badge = new Label(status);
                    badge.setPadding(new Insets(4, 12, 4, 12));
                    badge.setFont(Font.font("System", FontWeight.BOLD, 12));
                    badge.setStyle("-fx-background-radius: 20;");
                    switch (status) {
                        case "Paid":
                            badge.setTextFill(Color.WHITE);
                            badge.setStyle("-fx-background-color: #27ae60; -fx-background-radius: 20");
                            break;
                        case "Pending":
                            badge.setTextFill(Color.WHITE);
                            badge.setStyle("-fx-background-color: #e67e22; -fx-background-radius: 20;");
                            break;
                        case "Overdue":
                            badge.setTextFill(Color.WHITE);
                            badge.setStyle("-fx-background-color: #e74c3c; -fx-background-radius: 20;");
                            break;
                    }
                    setGraphic(badge);
                    setText(null);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });

}
