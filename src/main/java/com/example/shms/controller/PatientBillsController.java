package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PatientBillsController implements Initializable {

    @FXML private Label userLabel;
    @FXML private Label billIdLabel;
    @FXML private Label billDoctorLabel;
    @FXML private Label billTreatmentLabel;
    @FXML private Label billStatusLabel;
    @FXML private Label billTotalLabel;
    @FXML private Label amountDueLabel;
    @FXML private Label sliderMaxLabel;
    @FXML private Label paymentMessage;
    @FXML private Button payButton;
    @FXML private Slider paymentSlider;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TableView<ObservableList<String>> historyTable;
    @FXML private TableColumn<ObservableList<String>, String> colBillId;
    @FXML private TableColumn<ObservableList<String>, String> colDoctor;
    @FXML private TableColumn<ObservableList<String>, String> colTreatment;
    @FXML private TableColumn<ObservableList<String>, String> colAmount;
    @FXML private TableColumn<ObservableList<String>, String> colStatus;

    private final SessionManager session=SessionManager.getInstance();
    private final DatabaseManager db=DatabaseManager.getInstance();
    private int currentBillId= -1;
    private double totalAmount=0;
    private int patientId= -1;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        userLabel.setText(session.getLoggedInUser());
        setupPaymentMethods();
        setupTable();
        loadPatientBills();
        setupSlider();
    }

    private void setupPaymentMethods(){
        paymentMethodCombo.setItems(FXCollections.observableArrayList("💳 Card","🏦 Bank Transfer","💵 Cash","📱 Vodafone Cash"));
        paymentMethodCombo.setValue("💳 Card");
    }

    private void setupTable(){
        colBillId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> data) {
                return new SimpleStringProperty(data.getValue().get(0));
            }
        });
        colDoctor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> data){
                return new SimpleStringProperty(data.getValue().get(1));
            }
        });
        colTreatment.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> data){
                return new SimpleStringProperty(data.getValue().get(2));
            }
        });
        colAmount.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> data){
                return new SimpleStringProperty(data.getValue().get(3));
            }
        });
        colStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>,String> data){
                return new SimpleStringProperty(data.getValue().get(4));
            }
        });
    }

    private void loadPatientBills(){
        String username=session.getLoggedInUser();
        try(Statement st=db.getConnection().createStatement()){
            ResultSet patientRs = st.executeQuery("SELECT id FROM patients WHERE username = '"+username+"' LIMIT 1");
            if(!patientRs.next())
                return;
            patientId=patientRs.getInt("id");

            ResultSet bills = st.executeQuery("SELECT * FROM bills WHERE patientID = "+patientId+" ORDER BY id DESC");

            ObservableList<ObservableList<String>> data=FXCollections.observableArrayList();
            boolean firstUnpaid=true;

            while(bills.next()){
                String status = bills.getString("paymentStatus");
                ObservableList<String> row=FXCollections.observableArrayList();
                row.add("#B-"+String.format("%03d",bills.getInt("id")));
                row.add(bills.getString("doctorName"));
                row.add(bills.getString("treatment"));
                row.add("EGP "+String.format("%.0f",bills.getDouble("amount")));
                row.add(status);
                data.add(row);

                if(firstUnpaid && !status.equals("Paid")){
                    currentBillId=bills.getInt("id");
                    totalAmount=bills.getDouble("amount");
                    billIdLabel.setText("#B-"+String.format("%03d",currentBillId));
                    billDoctorLabel.setText(bills.getString("doctorName"));
                    billTreatmentLabel.setText(bills.getString("treatment"));
                    billStatusLabel.setText(status);
                    billTotalLabel.setText("EGP "+String.format("%.0f",totalAmount));
                    sliderMaxLabel.setText("EGP "+String.format("%.0f",totalAmount)+" (Full)");
                    paymentSlider.setMax(totalAmount);
                    paymentSlider.setValue(totalAmount);
                    amountDueLabel.setText("EGP "+String.format("%.0f",totalAmount));
                    payButton.setText("Pay EGP "+String.format("%.0f",totalAmount));
                    firstUnpaid=false;
                }
            }

            if(currentBillId== -1){
                billIdLabel.setText("No unpaid bills");
                payButton.setDisable(true);
                paymentSlider.setDisable(true);
            }

            historyTable.setItems(data);

        }catch(SQLException e){
            System.out.println("Bills load failed: "+e.getMessage());
        }
    }

    private void setupSlider(){
        paymentSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs,Number oldVal,Number newVal){
                double amount=newVal.doubleValue();
                amountDueLabel.setText("EGP "+String.format("%.0f",amount));
                payButton.setText("Pay EGP "+String.format("%.0f",amount));
            }
        });
    }

    @FXML private void handlePayment(){
        if(currentBillId== -1)
            return;

        double amountToPay=paymentSlider.getValue();
        String method=paymentMethodCombo.getValue();

        if(method==null){
            paymentMessage.setText("Please select a payment method.");
            paymentMessage.setStyle("-fx-text-fill:#A32D2D;");
            return;
        }

        try(Statement st=db.getConnection().createStatement()){
            String newStatus;
            if(amountToPay>=totalAmount){
                newStatus="Paid";
            }else if(amountToPay>0){
                newStatus="Partially Paid";
            }else{
                paymentMessage.setText("Please select an amount greater than 0");
                paymentMessage.setStyle("-fx-text-fill:#A32D2D;");
                return;
            }

            st.execute("UPDATE bills SET paymentStatus = '"+newStatus+"', paymentMethod = '"+method+"' WHERE id = "+currentBillId);

            paymentMessage.setText("Payment successful! Status updated to: "+newStatus);
            paymentMessage.setStyle("-fx-text-fill:#1D6A2E;");
            loadPatientBills();

        }catch(SQLException e){
            paymentMessage.setText("Payment failed: "+e.getMessage());
            paymentMessage.setStyle("-fx-text-fill:#A32D2D;");
        }
    }

    @FXML private void handleBack(){
        MainApp.navigateTo("patientDashboard",1200,700);
    }
}