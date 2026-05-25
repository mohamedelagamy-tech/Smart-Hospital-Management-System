package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PatientBillsController implements Initializable {

    @FXML private Label  userLabel;
    @FXML private Label  billIdLabel;
    @FXML private Label  billDoctorLabel;
    @FXML private Label  billTreatmentLabel;
    @FXML private Label  billStatusLabel;
    @FXML private Label  billTotalLabel;
    @FXML private Label  amountPaidLabel;
    @FXML private Label  amountDueLabel;
    @FXML private Label  sliderMaxLabel;
    @FXML private Label  paymentMessage;
    @FXML private Button payButton;
    @FXML private Slider paymentSlider;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private ListView<String> historyList;

    private final SessionManager  session=SessionManager.getInstance();
    private final DatabaseManager db=DatabaseManager.getInstance();

    private int    currentBillId= -1;
    private double totalAmount=0;
    private double amountAlreadyPaid=0;
    private int    patientId= -1;

    @Override
    public void initialize(URL url,ResourceBundle rb){
        userLabel.setText(session.getLoggedInUser());
        setupPaymentMethods();
        setupSlider();
        loadPatientBills();
    }

    private void setupPaymentMethods(){
        paymentMethodCombo.setItems(FXCollections.observableArrayList("💳 Card", "🏦 Bank Transfer", "💵 Cash", "📱 Vodafone Cash"));
        paymentMethodCombo.setValue("💳 Card");
    }

    private void loadPatientBills(){
        String username=session.getLoggedInUser();
        currentBillId= -1;
        amountAlreadyPaid=0;

        try(Statement st=db.getConnection().createStatement()){

            ResultSet patientRs = st.executeQuery("SELECT id FROM patients WHERE username = '"+username+"' LIMIT 1");
            if(!patientRs.next()){
                setNoBillsState("No patient record found.");
                return;
            }
            patientId=patientRs.getInt("id");
            ResultSet bills = db.getConnection().createStatement().executeQuery("SELECT * FROM bills WHERE patientID = "+patientId+" ORDER BY id DESC");

            List<String> historyItems=new ArrayList<>();
            boolean firstUnpaid =true;

            while(bills.next()){
                String status=bills.getString("status");
                double amount=bills.getDouble("amount");
                double paid=bills.getDouble("amountPaid");

                String entry = "#B-"+String.format("%03d",bills.getInt("id"))+
                        "  |  "+bills.getString("doctorName")+
                        "  |  "+bills.getString("service")+
                        "  |  EGP "+String.format("%.0f",amount)+
                        "  |  "+status;
                historyItems.add(entry);

                if (firstUnpaid && !status.equals("Paid")){
                    currentBillId=bills.getInt("id");
                    totalAmount=amount;
                    amountAlreadyPaid=paid;
                    double remaining=totalAmount - amountAlreadyPaid;

                    billIdLabel.setText("#B-" + String.format("%03d",currentBillId));
                    billDoctorLabel.setText(bills.getString("doctorName"));
                    billTreatmentLabel.setText(bills.getString("service"));
                    billStatusLabel.setText(status);
                    billTotalLabel.setText("EGP "+String.format("%.0f",totalAmount));

                    if(amountAlreadyPaid > 0){
                        amountPaidLabel.setText("Already paid: EGP "+String.format("%.0f", amountAlreadyPaid));
                        amountPaidLabel.setVisible(true);
                    }else{
                        amountPaidLabel.setVisible(false);
                    }

                    paymentSlider.setMin(1);
                    paymentSlider.setMax(remaining);
                    paymentSlider.setValue(remaining);
                    paymentSlider.setDisable(false);
                    payButton.setDisable(false);

                    sliderMaxLabel.setText("EGP "+String.format("%.0f",remaining)+" (Full remaining)");
                    amountDueLabel.setText("EGP "+String.format("%.0f",remaining));
                    payButton.setText("Pay EGP "+String.format("%.0f",remaining));

                    firstUnpaid=false;
                }
            }

            if(currentBillId== -1){
                setNoBillsState("✅ All bills paid!");
            }

            historyList.setItems(FXCollections.observableArrayList(
                    historyItems.isEmpty() ? List.of("No bills found") : historyItems
            ));

        }catch(Exception e){
            System.out.println("Bills load failed: "+e.getMessage());
            paymentMessage.setText("Error loading bills.");
            paymentMessage.setStyle("-fx-text-fill:#A32D2D;");
        }
    }

    private void setupSlider(){
        paymentSlider.valueProperty().addListener((obs,oldVal,newVal) -> {
            double amount=newVal.doubleValue();
            amountDueLabel.setText("EGP "+String.format("%.0f",amount));
            payButton.setText("Pay EGP "+String.format("%.0f",amount));
        });
    }

    @FXML
    private void handlePayment(){
        if (currentBillId == -1)
            return;

        double paying=paymentSlider.getValue();
        String method=paymentMethodCombo.getValue();

        if(method==null){
            showMessage("Please select a payment method.",false);
            return;
        }
        if(paying<=0){
            showMessage("Please select an amount greater than 0.",false);
            return;
        }

        double newTotalPaid=amountAlreadyPaid+paying;
        double remaining=totalAmount-newTotalPaid;

        if (newTotalPaid>totalAmount+0.01){
            showMessage("Payment exceeds the remaining balance.",false);
            return;
        }

        String newStatus=(remaining<=0.01) ? "Paid":"Partially Paid";

        try {
            PreparedStatement ps = db.getConnection().prepareStatement("UPDATE bills SET status = ?,paymentMethod = ?,amountPaid = ? WHERE id = ?");
            ps.setString(1,newStatus);
            ps.setString(2,method);
            ps.setDouble(3,newTotalPaid);
            ps.setInt(4,currentBillId);
            ps.executeUpdate();

            String msg = newStatus.equals("Paid")
                    ? "✅ Bill fully paid! Status: Paid"
                    : "✅ Payment of EGP "+String.format("%.0f", paying)+
                    " recorded. Remaining: EGP "+String.format("%.0f",remaining);
            showMessage(msg,true);
            loadPatientBills();

        }catch (Exception e){
            showMessage("Payment failed: "+e.getMessage(),false);
        }
    }

    private void setNoBillsState(String msg){
        billIdLabel.setText(msg);
        billDoctorLabel.setText("—");
        billTreatmentLabel.setText("—");
        billStatusLabel.setText("—");
        billTotalLabel.setText("EGP 0");
        if (amountPaidLabel != null)
            amountPaidLabel.setVisible(false);
        payButton.setDisable(true);
        paymentSlider.setDisable(true);
    }

    private void showMessage(String msg,boolean success){
        paymentMessage.setText(msg);
        paymentMessage.setStyle(success
                ? "-fx-text-fill:#1D6A2E;"
                : "-fx-text-fill:#A32D2D;");
    }

    @FXML private void handleBack(){
        MainApp.navigateTo("patientDashboard",1200,700);
    }
}