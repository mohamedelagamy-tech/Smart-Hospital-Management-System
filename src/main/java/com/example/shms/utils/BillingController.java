package com.example.shms.utils;

import com.example.shms.model.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.net.URL;
import java.util.ResourceBundle;

public class BillingController implements Initializable {
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortBox;

    @FXML private ToggleButton btnAll;
    @FXML private ToggleButton btnPaid;
    @FXML private ToggleButton btnPending;
    @FXML private ToggleButton btnOverdue;

    @FXML private TableView<Bill> billingTable;
    @FXML private TableColumn<Bill, String> colBillNum;
    @FXML private TableColumn<Bill, String> colPatient;
    @FXML private TableColumn<Bill, String> colService;
    @FXML private TableColumn<Bill, String> colDate;
    @FXML private TableColumn<Bill, Double> colAmount;
    @FXML private TableColumn<Bill, String> colStatus;

    private ObservableList<Bill> allBills = FXCollections.observableArrayList();
    private ToggleGroup filterGroup;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sortBox.getItems().addAll(
                "Date (Newest First)",
                "Date (Oldest First)",
                "Amount (High to Low)",
                "Amount (Low to High)"
        );
        sortBox.setValue("Date (Newest First)");

        filterGroup = new ToggleGroup();
        btnAll.setToggleGroup(filterGroup);
        btnPaid.setToggleGroup(filterGroup);
        btnPending.setToggleGroup(filterGroup);
        btnOverdue.setToggleGroup(filterGroup);
        btnAll.setSelected(true);

        styleFilterBtn(btnAll);
        styleFilterBtn(btnPaid);
        styleFilterBtn(btnPending);
        styleFilterBtn(btnOverdue);

        filterGroup.selectedToggleProperty().addListener((obs, o, n) -> applyFilters());

        colBillNum.setCellValueFactory(new PropertyValueFactory<>("billNumber"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colService.setCellValueFactory(new PropertyValueFactory<>("service"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { setText(null); setGraphic(null); return; }
                Label badge = new Label(status);
                badge.setPadding(new Insets(4, 12, 4, 12));
                badge.setFont(Font.font("System", FontWeight.BOLD, 12));
                badge.setTextFill(Color.WHITE);
                switch (status) {
                    case "Paid"    -> badge.setStyle("-fx-background-color: #27ae60; -fx-background-radius: 20;");
                    case "Pending" -> badge.setStyle("-fx-background-color: #e67e22; -fx-background-radius: 20;");
                    case "Overdue" -> badge.setStyle("-fx-background-color: #e74c3c; -fx-background-radius: 20;");
                }
                setGraphic(badge);
                setText(null);
                setAlignment(Pos.CENTER_LEFT);
            }
        });

        billingTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Bill item, boolean empty) {
                super.updateItem(item, empty);
                setStyle((!empty && getIndex() % 2 != 0)
                        ? "-fx-background-color: #fafafa;"
                        : "-fx-background-color: white;");
            }
        });

        billingTable.setFixedCellSize(52);
        billingTable.setItems(allBills);
    }

    @FXML
    private void openNewBillForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shms/NewBillForm.fxml"));
            Parent root = loader.load();

            NewBillController formCtrl = loader.getController();
            formCtrl.init(allBills, billingTable);

            Stage formStage = new Stage();
            formStage.setTitle("Generate New Bill");
            formStage.setScene(new Scene(root, 380, 480));
            formStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void applyFilters() {
        String search = searchField.getText().toLowerCase();
        String sort   = sortBox.getValue();

        String activeFilter = "All";
        if (filterGroup.getSelectedToggle() != null) {
            activeFilter = ((ToggleButton) filterGroup.getSelectedToggle()).getText();
        }
        final String filter = activeFilter;

        ObservableList<Bill> result = FXCollections.observableArrayList(allBills);

        result.removeIf(b ->
                !b.getPatientName().toLowerCase().contains(search) &&
                        !b.getBillNumber().toLowerCase().contains(search)
        );

        if (!filter.equals("All")) {
            result.removeIf(b -> !b.getBillStatus().equalsIgnoreCase(filter));
        }

        if (sort != null) switch (sort) {
            case "Amount (High to Low)" -> result.sort((a, b) -> Double.compare(b.getAmount(), a.getAmount()));
            case "Amount (Low to High)" -> result.sort((a, b) -> Double.compare(a.getAmount(), b.getAmount()));
        }

        billingTable.setItems(result);
    }

    private void styleFilterBtn(ToggleButton btn) {
        btn.selectedProperty().addListener((obs, was, isNow) ->
                btn.setStyle(isNow
                        ? "-fx-background-color: #1a1a2e; -fx-text-fill: white; -fx-border-radius: 20; -fx-background-radius: 20; -fx-font-size: 13px; -fx-cursor: hand;"
                        : "-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 20; -fx-background-radius: 20; -fx-font-size: 13px; -fx-cursor: hand;"
                )
        );
    }

    public void setBills(ObservableList<Bill> bills) {
        this.allBills = bills;
        billingTable.setItems(allBills);
    }
}

