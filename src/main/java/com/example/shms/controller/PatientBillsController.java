package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    @FXML private ListView<String> historyList;

    private final SessionManager session=SessionManager.getInstance();
    private final DatabaseManager db=DatabaseManager.getInstance();
    private int currentBillId= -1;
    private double totalAmount=0;
    private double amountAlreadyPaid=0;
    private int patientId= -1;

    @Override
    public void initialize(URL url,ResourceBundle rb){
        userLabel.setText(session.getLoggedInUser());
        setupPaymentMethods();
        loadPatientBills();
        setupSlider();
    }

    private void setupPaymentMethods(){
        List<String> methods= new ArrayList<>();
        methods.add("💳 Card");
        methods.add("🏦 Bank Transfer");
        methods.add("💵 Cash");
        methods.add("📱 Vodafone Cash");
        paymentMethodCombo.setItems(FXCollections.observableArrayList(methods));
        paymentMethodCombo.setValue("💳 Card");
    }

    private void loadPatientBills(){
        String username=session.getLoggedInUser();
        try(Statement st=db.getConnection().createStatement()){

            ResultSet patientRs=st.executeQuery("SELECT id FROM patients WHERE username = '"+username+"' LIMIT 1");
            if(!patientRs.next()){
                billIdLabel.setText("No patient record found");
                payButton.setDisable(true);
                paymentSlider.setDisable(true);
                return;
            }
            patientId=patientRs.getInt("id");

            ResultSet bills=db.getConnection().createStatement().executeQuery("SELECT * FROM bills WHERE patientID = "+patientId+" ORDER BY id DESC");

            List<String> historyItems= new ArrayList<>();
            boolean firstUnpaid=true;
            currentBillId= -1;

            while(bills.next()){
                String status=bills.getString("status");
                double amount=bills.getDouble("amount");
                String entry= "#B-"+String.format("%03d", bills.getInt("id"))+
                        "  |  "+bills.getString("doctorName")+
                        "  |  "+bills.getString("service")+
                        "  |  EGP "+String.format("%.0f",amount)+
                        "  |  "+status;
                historyItems.add(entry);

                if(firstUnpaid && !status.equals("Paid")){
                    currentBillId=bills.getInt("id");
                    totalAmount=amount;
                    amountAlreadyPaid=bills.getDouble("amountPaid");
                    double remaining=totalAmount-amountAlreadyPaid;

                    billIdLabel.setText("#B-"+String.format("%03d", currentBillId));
                    billDoctorLabel.setText(bills.getString("doctorName"));
                    billTreatmentLabel.setText(bills.getString("service"));
                    billStatusLabel.setText(status);
                    billTotalLabel.setText("EGP "+String.format("%.0f",totalAmount));
                    sliderMaxLabel.setText("EGP "+String.format("%.0f",remaining)+" (Remaining)");
                    paymentSlider.setMax(remaining);
                    paymentSlider.setValue(remaining);
                    amountDueLabel.setText("EGP "+String.format("%.0f",remaining));
                    payButton.setText("Pay EGP "+String.format("%.0f",remaining));
                    firstUnpaid=false;
                }
            }

            if(currentBillId== -1){
                billIdLabel.setText("No unpaid bills");
                billDoctorLabel.setText("—");
                billTreatmentLabel.setText("—");
                billStatusLabel.setText("All paid");
                billTotalLabel.setText("EGP 0");
                payButton.setDisable(true);
                paymentSlider.setDisable(true);
            }

            if(historyItems.isEmpty()){
                historyItems.add("No bills found");
            }

            historyList.setItems(FXCollections.observableArrayList(historyItems));

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
            double newTotalPaid=amountAlreadyPaid+amountToPay;

            if(amountToPay<=0){
                paymentMessage.setText("Please select an amount greater than 0.");
                paymentMessage.setStyle("-fx-text-fill:#A32D2D;");
                return;
            }

            if(newTotalPaid>=totalAmount){
                newStatus="Paid";
            }else{
                newStatus="Partially Paid";
            }

            st.execute("UPDATE bills SET status = '"+newStatus+"', paymentMethod = '"+method+"', amountPaid = "+newTotalPaid+" WHERE id = "+currentBillId);

            paymentMessage.setText("Payment successful! Status: "+newStatus);
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