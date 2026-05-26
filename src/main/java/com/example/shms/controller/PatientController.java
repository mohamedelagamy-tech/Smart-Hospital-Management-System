package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Patient;
import com.example.shms.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;

public class PatientController {
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> idCol;
    @FXML private TableColumn<Patient, String> nameCol;
    @FXML private TableColumn<Patient, Integer> ageCol;
    @FXML private TableColumn<Patient, String> bloodTypeCol;
    @FXML private TableColumn<Patient, Integer> priorityCol;
    @FXML private TableColumn<Patient, String> statusCol;
    @FXML private TableColumn<Patient, Void> actionsCol;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> departmentFilter;
    @FXML private ComboBox<String> sortCombo;
    @FXML private Button allBtn,emergencyBtn,urgentBtn,normalBtn,resetBtn,addBtn;
    @FXML private Label statusLabel;
    private final DatabaseManager db= DatabaseManager.getInstance();
    private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private String currentPriorityFilter="all";
    @FXML
    private void initialize() {
        setupColumns();
        setupActions();
        setupDepartmentFilter();
        loadPatients();
        setupSearch();
        sortCombo.getItems().addAll("ID","Name","Age","Priority","Status");
        sortCombo.setValue("ID");
        sortCombo.setOnAction(e->sortBy(sortCombo.getValue()));
    }
    private void setupColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        bloodTypeCol.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        priorityCol.setCellFactory(col->new TableCell<Patient, Integer>(){
            @Override
            protected void updateItem(Integer priority, boolean empty) {
                super.updateItem(priority, empty);
                if(empty|| priority==null) {
                    setText(null);
                    setStyle("");
                }else{
                    switch(priority) {
                        case 1->{
                            setText("Emergency");
                            setStyle("-fx-text-fill: #DC2626; -fx-font-weight:bold; ");
                        }
                        case 2->{
                            setText("Urgent");
                            setStyle("-fx-text-fill: #B45309; -fx-font-weight:bold; ");
                        }
                        case 3->{
                            setText("Normal");
                            setStyle("-fx-text-fill: #166534;-fx-font-weight: bold;");
                        }
                        default ->{
                            setText(priority+"");
                            setStyle("");
                        }
                    }
                }
            }
        });
        statusCol.setCellFactory(col->new  TableCell<Patient, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if(empty || status == null) {
                    setText(null);
                    setStyle("");
                }else{
                    setText(status);
                    switch (status) {
                        case "Waiting"->setStyle("-fx-text-fill: #B45309; -fx-font-weight: bold;");
                        case "With Doctor"->setStyle("-fx-text-fill: #1D4ED8; -fx-font-weight: bold;");
                        case "Discharged"->setStyle("-fx-text-fill: #166534; -fx-font-weight: bold;");
                        default->setStyle("");
                    };
                }
            }
        });
    }
    private void setupActions() {
        actionsCol.setCellFactory(col->new TableCell<>(){
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons=new HBox(5,editBtn,deleteBtn);
            {
                editBtn.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11;");
                deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11;");
                editBtn.setOnAction((e)->{
                    Patient p=getTableView().getItems().get(getIndex());
                    handleEditPatient(p);
                });
                deleteBtn.setOnAction((e)->{
                    Patient p=getTableView().getItems().get(getIndex());
                    handleDeletePatient(p);
                });
            }
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                if(empty||getIndex()>=getTableView().getItems().size())
                    setGraphic(null);
                else
                    setGraphic(buttons);
            }
        });

    }
    private void setupDepartmentFilter() {
        departmentFilter.getItems().addAll("All","Cardiology", "Pediatrics", "Emergency","Neurology", "Orthopedics", "Dermatology", "Oncology", "Radiology");
        departmentFilter.setValue("All");
        departmentFilter.setOnAction(e->filterPatients());
    }
    public void loadPatients() {
        patientList.clear();
        try {
            ResultSet rs = db.getAllPatients();
            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientID(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                p.setPhone(rs.getString("phone"));
                p.setDepartment(rs.getString("department"));
                p.setBloodType(rs.getString("bloodType"));
                p.setAddress(rs.getString("address"));
                p.setPriority(rs.getInt("priority"));
                p.setStatus(rs.getString("status"));
                patientList.add(p);
            }
        } catch (SQLException e) {
            System.out.println("error in loading patient" + e.getMessage());
        }
        Collections.sort(patientList);
        filterPatients();
    }
    private void setupSearch(){
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterPatients());
    }
    private void filterPatients(){
        String keyword=searchField.getText().toLowerCase();
        String department=departmentFilter.getValue();
        ObservableList<Patient> filtered = FXCollections.observableArrayList();
        for(Patient p:patientList){
            boolean matchSearch=p.getName().toLowerCase().contains(keyword)||String.valueOf(p.getPatientID()).contains(keyword);
            boolean matchDepartment=department==null || department.equals("All")||department.equals(p.getDepartment());
            boolean matchPriority=currentPriorityFilter.equals("all")||String.valueOf(p.getPriority()).equals(getPriorityNumber(currentPriorityFilter));
            if(matchSearch&&matchDepartment&&matchPriority){
                filtered.add(p);
            }
        }
        patientTable.setItems(filtered);
        patientTable.refresh();
        statusLabel.setText("Showing: " + filtered.size()+" of "+patientList.size()+" patients");
    }
    private String getPriorityNumber(String filter){
        return switch (filter){
            case "emergency" -> "1";
            case "urgent" -> "2";
            case "normal" -> "3";
            default -> "";
        };
    }
    @FXML
    private void handleAll(){
        currentPriorityFilter="all";
        filterPatients();
    }
    @FXML
    private void handleEmergency(){
        currentPriorityFilter="emergency";
        filterPatients();
    }
    @FXML
    private void handleUrgent(){
        currentPriorityFilter="urgent";
        filterPatients();
    }
    @FXML
    private void handleNormal(){
        currentPriorityFilter="normal";
        filterPatients();
    }
    @FXML
    private void handleReset(){
        searchField.clear();
        departmentFilter.setValue("All");
        currentPriorityFilter="all";
        sortCombo.setValue("ID");
        Collections.sort(patientList);
        filterPatients();
    }
    @FXML
    private void handleAddPatient(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PatientForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Patient");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            PatientFormController formController=loader.getController();
            formController.setPatientController(this);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("error opening form "+e.getMessage());
        }
    }
    private void handleEditPatient(Patient p){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PatientForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Patient");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            PatientFormController formController=loader.getController();
            formController.setPatientController(this);
            formController.setPatient(p);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("error opening edit form "+e.getMessage());
        }
    }
    private  void handleDeletePatient(Patient p){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Patient");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Delete "+p.getName()+ " ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                db.deletePatient(p.getPatientID());
                loadPatients();
            }
        });
    }
    private void sortBy(String mode){
        switch(mode){
            case "ID"->Collections.sort(patientList);
            case "Name"->patientList.sort(Comparator.comparing(Patient::getName));
            case "Age"->patientList.sort(Comparator.comparingInt(Patient::getAge));
            case "Priority"->patientList.sort(Comparator.comparingInt(Patient::getPriority));
            case "Status"->patientList.sort(Comparator.comparing(Patient::getStatus));
        }
        filterPatients();
    }
    @FXML
    private void handleBack() {
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }
}