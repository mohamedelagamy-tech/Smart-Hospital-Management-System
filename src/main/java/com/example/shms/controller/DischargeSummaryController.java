package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Bill;
import com.example.shms.model.DischargeSummary;
import com.example.shms.model.Prescription;
import com.example.shms.model.Room;
import com.example.shms.utils.EmailService;
import com.example.shms.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DischargeSummaryController implements Initializable {

    @FXML private ComboBox<String> patientComboBox;
    @FXML private Label lblName;
    @FXML private Label lblPatientId;
    @FXML private Label lblAdmission;
    @FXML private Label lblDischarge;
    @FXML private Label lblRoom;
    @FXML private Label lblDoctor;
    @FXML private Label lblDiagnosis;
    @FXML private Label lblRoomCharges;
    @FXML private Label lblMedicineCharges;
    @FXML private Label lblConsultationCharges;
    @FXML private Label lblTotalBill;
    @FXML private TableView<Prescription> prescriptionsTable;
    @FXML private TableColumn<Prescription,String> colMedicine;
    @FXML private TableColumn<Prescription,String> colDosage;
    @FXML private TableColumn<Prescription,String> colDuration;

    private final DatabaseManager db=DatabaseManager.getInstance();
    private int roomId=-1;

    @Override
    public void initialize(URL location,ResourceBundle resources){
        setupTable();
        loadPatients();
        clearSummary();
    }
    public void initWithPatient(int patientId,int roomId){
        this.roomId=roomId;
        patientComboBox.setVisible(false);
        patientComboBox.setManaged(false);
        populateSummary(patientId);
    }
    private void setupTable(){
        colMedicine.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        colDosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }
    private void loadPatients(){
        try{
            ResultSet rs=db.getAllPatients();
            while(rs!=null && rs.next()){
                String entry=rs.getString("name")+" (P-"+rs.getInt("id")+")";
                patientComboBox.getItems().add(entry);
            }
        }catch(SQLException e){
            System.out.println("Error loading patients: "+e.getMessage());
        }
    }
    @FXML
    private void onPatientSelected(){
        String selected=patientComboBox.getValue();
        if(selected==null) return;
        int patientId=extractPatientId(selected);
        if(patientId==-1) return;
        populateSummary(patientId);
    }
    private void populateSummary(int patientId){
        try(java.sql.Statement st=db.getConnection().createStatement()){
            ResultSet rs=st.executeQuery("SELECT * FROM patients WHERE id = "+patientId+" LIMIT 1");
            if(rs.next()){
                lblName.setText(rs.getString("name"));
                lblPatientId.setText("P-"+patientId);
            }

            ResultSet apptRs=st.executeQuery(
                    "SELECT a.date, d.name as doctorName FROM appointments a "+
                            "JOIN doctors d ON a.doctorId=d.id "+
                            "WHERE a.patientId="+patientId+" ORDER BY a.date ASC LIMIT 1"
            );
            if(apptRs.next()){
                lblAdmission.setText(apptRs.getString("date"));
                lblDoctor.setText("Dr. "+apptRs.getString("doctorName"));
            }else{
                lblAdmission.setText("—");
                lblDoctor.setText("—");
            }

            lblDischarge.setText(LocalDate.now().toString());

            String roomInfo="N/A";
            List<Room> rooms=db.getAllRooms();
            if(rooms!=null){
                for(Room r:rooms){
                    if(r.getAssignedPatientId()==patientId){
                        roomInfo="Room "+r.getRoomNumber()+" - "+r.getRoomtype();
                        if(roomId==-1) roomId=r.getId();
                        break;
                    }
                }
            }
            lblRoom.setText(roomInfo);

            ResultSet diagRs=st.executeQuery(
                    "SELECT diagnosis FROM medical_records WHERE patientId="+patientId+" ORDER BY id DESC LIMIT 1"
            );
            lblDiagnosis.setText(diagRs.next() ? diagRs.getString("diagnosis") : "See medical records");

        }catch(Exception e){
            System.out.println("populateSummary error: "+e.getMessage());
        }

        List<Prescription> prescriptions=db.getPrescriptionsByPatient(patientId);
        ObservableList<Prescription> presObs=FXCollections.observableArrayList();
        if(prescriptions!=null) presObs.addAll(prescriptions);
        prescriptionsTable.setItems(presObs);

        double roomCharges=0,medicineCharges=0,consultationCharges=0,totalBill=0;
        List<Bill> bills=db.getBillsByPatient(patientId);
        if(bills!=null){
            for(Bill b:bills){
                String service=b.getService().toLowerCase();
                if(service.contains("room")) roomCharges+=b.getAmount();
                else if(service.contains("medicine")||service.contains("prescription")) medicineCharges+=b.getAmount();
                else consultationCharges+=b.getAmount();
                totalBill+=b.getAmount();
            }
        }

        lblRoomCharges.setText("EGP "+String.format("%.0f",roomCharges));
        lblMedicineCharges.setText("EGP "+String.format("%.0f",medicineCharges));
        lblConsultationCharges.setText("EGP "+String.format("%.0f",consultationCharges));
        lblTotalBill.setText("EGP "+String.format("%.0f",totalBill));
    }
    @FXML
    private void handlePrint(){
        String selected=patientComboBox.getValue();
        int patientId=-1;
        if(selected!=null){
            patientId=extractPatientId(selected);
        }else{
            try(java.sql.Statement st=db.getConnection().createStatement()){
                ResultSet rs=st.executeQuery("SELECT id FROM patients WHERE name='"+lblName.getText()+"' LIMIT 1");
                if(rs.next()) patientId=rs.getInt("id");
            }catch(Exception e){
                System.out.println("Patient ID lookup failed: "+e.getMessage());
            }
        }
        if(patientId==-1){ showAlert("Could not resolve patient."); return; }

        List<Prescription> prescriptions=db.getPrescriptionsByPatient(patientId);
        double totalBill=0;
        try(java.sql.Statement st=db.getConnection().createStatement()){
            ResultSet billRs=st.executeQuery("SELECT SUM(amount) FROM bills WHERE patientID="+patientId);
            if(billRs.next()) totalBill=billRs.getDouble(1);
        }catch(Exception e){
            System.out.println("Bills query failed: "+e.getMessage());
        }

        DischargeSummary summary=new DischargeSummary(
                0,patientId,
                prescriptions!=null ? prescriptions : List.of(),
                0,totalBill
        );
        summary.writeToFile();

        try(java.sql.Statement st=db.getConnection().createStatement()){
            st.execute("UPDATE patients SET status='Discharged' WHERE id="+patientId);
            if(roomId!=-1) db.dischargeRoom(roomId);
        }catch(Exception e){
            System.out.println("Discharge update failed: "+e.getMessage());
        }

        final int finalPatientId=patientId;
        final double finalTotal=totalBill;
        Thread emailThread=new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    String patientEmail=db.getPatientEmail(finalPatientId);
                    String patientName=db.getPatientName(finalPatientId);
                    if(patientEmail!=null && !patientEmail.isEmpty()){
                        EmailService.sendDischargeSummary(
                                patientEmail,patientName,
                                lblDoctor.getText(),lblRoom.getText(),
                                String.format("%.0f",finalTotal)
                        );
                    }
                }catch(Exception e){
                    System.out.println("Discharge email failed: "+e.getMessage());
                }
            }
        });
        emailThread.setDaemon(true);
        emailThread.start();

        showAlert("Patient discharged! Summary saved.");
        ((javafx.stage.Stage) prescriptionsTable.getScene().getWindow()).close();
    }
    private int extractPatientId(String entry){
        try{
            int start=entry.lastIndexOf("P-")+2;
            int end=entry.lastIndexOf(")");
            return Integer.parseInt(entry.substring(start,end).trim());
        }catch(Exception e){
            return -1;
        }
    }
    private void clearSummary(){
        lblName.setText("—");
        lblPatientId.setText("—");
        lblAdmission.setText("—");
        lblDischarge.setText("—");
        lblRoom.setText("—");
        lblDoctor.setText("—");
        lblDiagnosis.setText("—");
        lblRoomCharges.setText("EGP 0");
        lblMedicineCharges.setText("EGP 0");
        lblConsultationCharges.setText("EGP 0");
        lblTotalBill.setText("EGP 0");
        prescriptionsTable.setItems(FXCollections.emptyObservableList());
    }
    private void showAlert(String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Discharge Summary");
        alert.setContentText(message);
        alert.show();
    }
    @FXML private void handleBack(){
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }
}