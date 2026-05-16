package com.example.shms;
import com.example.shms.model.Bill;
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


public class BillingScreen {

    private TableView<Bill> billingTable;
    private ObservableList<Bill> allBills;
    private TextField searchField;
    private ComboBox<String> sortBox;
    private ToggleGroup filterGroup;

    public void show(Stage stage){
        Label title = new Label("Billing Management");
        title.setFont(Font.font("Georgia",FontWeight.BOLD,28));

        Button newBillBtn = new Button("+ New Bill");
        newBillBtn.setPrefHeight(40);
        newBillBtn.setStyle( "-fx-background-color: #1a1a2e;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;");
        newBillBtn.setOnAction(e -> openNewBillForm());
        Region spacer = new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);
        HBox titlrBar = new HBox(title,spacer,newBillBtn);
        titleBar.setAlignment(Pos.CENTER_LEFT);


        searchField=new TextField();
        searchField.setPromptText("search by patient neme , bill number ,date ...");
        searchField.setPrefWidth(280);
        searchField.setPrefHeight(40);
        searchField.setStyle(
                "-fx-background-color: white"+ "-fx-border-color: #cccccc" +
                "-fx-border-radius: 6;" + "-fx-background-radius: 6;"+"-fx-padding: 0 12;"+
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
        sortBox.setOnAction(e -> applyFilters());

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
        billingTable = new TableView<>();
        billingTable.getColumns().addAll(colBillNum, colPatient, colService, colDate, colAmount, colStatus );
        billingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        billingTable.setStyle("-fx-background-color: white;" + "-fx-border-color: #eOeOe0;" + "-fx-border-radius: 8;" +"-fx-background-radius: 8;" + "-fx-font-size: 13px; ");
        billingTable.setFixedCellSize(52);

        billingTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Bill item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setStyle("-fx-background-color: white;");
                } else if (getIndex() % 2 == 0) {
                    setStyle("-fx-background-color: white;");
                } else {
                    setStyle("-fx-background-color: #fafafa;");
                }
            }
        });
        VBox  layout = new VBox(16,titleBar,topControls,filterBar,billingTable);
        layout.setPadding(new Insets(32));
        layout.setStyle("-fx-background-color: #f0f2f5; ");
        VBox.setVgrow(billingTable,Priority.ALWAYS);

        Scene scene = new Scene (layout,1000,620);
        stage.setTitle("Hospital- Billing Management");
        stage.setScene(scene);
        stage.show();
    }
    private ToggleButton makeFilterBtn (String text, ToggleGroup group){
            ToggleButton btn = new ToggleButton(text);
            btn.setToggleGroup(group);
            btn.setPrefHeight(34);
            btn.setPadding(new Insets(0,18,0,18));
            btn.setStyle( "-fx-background-color: white;"+ "-fx-border-color: #cccccc;"+
                    "-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-font-size: 16px;");
            btn.selectedProperty().addListener((obs, wasSelected, isSelected)->{
                if (isSelected){
                    btn.setStyle("-fx-background-color: #1a1a2e;" +"-fx-text-fill: white;" + "-fx-border-radius: 20;" + "-fx-background-radius: 20;" +
                    "-fx-font-size: 13px;" + "-fx-cursor: hand;");}
                else {
                    btn.setStyle("-fx-background-color: white;" +"-fx-border-color: #cccccc;" + "-fx-border-radius: 20;" + "-fx-background-radius: 20;" +
                            "-fx-font-size: 13px;" + "-fx-cursor: hand;");
                }
            });
            return btn;
        }
    private void applyFilters(){
            String search = searchField.getText().toLowerCase();
            String sort = sortBox.getValue();

            ObservableList<Bill> result = FXCollections.observableArrayList();
            for (Bill b : allBills){
                boolean matchesSearch = b.getPatientName().toLowerCase().contains(search)
                        || b.getBillNumber().toLowerCase().contains(search);
                result.add(b);
            }
            result.removeIf(b ->
                    !b.getPatientName().toLowerCase().contains(search) &&
                            !b.getBillNumber().toLowerCase().contains(search)
            );
            switch (sort) {
                case "Amount (High to Low)":
                    result.sort((a, b) -> Double.compare(b.getAmount(), a.getAmount()));
                    break;
                case "Amount (Low to High)":
                    result.sort((a, b) -> Double.compare(a.getAmount(), b.getAmount()));
                    break;
            }

            billingTable.setItems(result);
        }
    private void openNewBillForm(){
        Stage formStage = new Stage();
        formStage.setTitle("Generate New Bill");

        Label patientLabel = new Label("patient name ");
        ComboBox<String> patientBox = new ComboBox<>();
        patientBox.getItems().addAll(
                "...add patient names...",
                "...add more..."
        );
        patientBox.setPromptText("select a patient");
        patientBox.setPrefWidth(300);

        Label serviceLabel = new Label("service");
        TextField serviceField = new TextField();
        serviceField.setPromptText("enter service type");
        serviceField.setPrefWidth(300);

        Label amountLabel = new Label("cost:");
        TextField amountField = new TextField();
        amountField.setPromptText("enter amount");
        amountField.setPrefWidth(300);

        Label statusLabel = new Label("status");
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Pending","paid","overdue");
        statusBox.setValue("Pending");
        statusBox.setPrefWidth(300);

        Button submitBtn = new Button("submit");
        submitBtn.setPrefWidth(300);
        submitBtn.setPrefHeight(40);
        submitBtn.setStyle(submitBtn.setStyle("-fx-background-color: #0078D4;"+"-fx-text-fill: white;"+
                "-fx-font-size: 14px;"+" -fx-background-radius: 5;");

        submitBtn.setOnAction(e->{
                String selectedPatient = patientBox.getValue();
        String service         = serviceField.getText();
        String amountText      = amountField.getText();
        String status          = statusBox.getValue(););

        if (selectedPatient == null || service.isEmpty () || amountText.isEmpty()){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("missing fields");
        alert.setContentText("all fields are required");
        alert.show();
        return;  }
        double amount;
        try{

        }

    }


}
