package com.example.shms.controller;

import com.example.shms.MainApp;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.MedicalRecord;
import com.example.shms.utils.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.util.List;
import com.example.shms.utils.LanguageManager;

public class MedicalRecordsController {

    @FXML private TextField searchField;
    @FXML private Button backBtn;
    @FXML private Label titleLabel;
    @FXML private TableView<MedicalRecord> recordsTable;
    @FXML private TableColumn<MedicalRecord,String> colNumber;
    @FXML private TableColumn<MedicalRecord,String> colPatient;
    @FXML private TableColumn<MedicalRecord,String> colDoctor;
    @FXML private TableColumn<MedicalRecord,String> colDate;
    @FXML private TableColumn<MedicalRecord,String> colDiagnosis;
    @FXML private TableColumn<MedicalRecord,String> colTreatment;
    @FXML private TableColumn<MedicalRecord,String> colStatus;

    private final ObservableList<MedicalRecord> allRecords=FXCollections.observableArrayList();
    private final DatabaseManager db=DatabaseManager.getInstance();

    @FXML
    private void initialize() {

        if (LanguageManager.isArabic()) {

            titleLabel.setText("السجلات الطبية");
            backBtn.setText("→ رجوع");

            searchField.setPromptText(
                    "ابحث باسم المريض أو الطبيب أو التشخيص..."
            );

            colNumber.setText("#");
            colPatient.setText("المريض");
            colDoctor.setText("الطبيب");
            colDate.setText("التاريخ");
            colDiagnosis.setText("التشخيص");
            colTreatment.setText("العلاج");
            colStatus.setText("الحالة");
        }

        setupColumns();
        loadFromDatabase();

        recordsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        recordsTable.setFixedCellSize(52);
    }
    private void setupColumns(){
        colNumber.setCellValueFactory(cell -> {
            int index = recordsTable.getItems().indexOf(cell.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(index));
        });
        colPatient.setCellValueFactory(cell -> {
            String name = db.getPatientName(cell.getValue().getPatientId());
            return new SimpleStringProperty(name != null ? name : "Unknown");
        });
        colDoctor.setCellValueFactory(cell -> {
            String name = db.getDoctorName(cell.getValue().getDoctorId());
            return new SimpleStringProperty(name != null ? name : "—");
        });
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate()));
        colDiagnosis.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDiagnosis()));
        colTreatment.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTreatment()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNotes()));
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status,boolean empty){
                super.updateItem(status, empty);
                if(empty || status == null){
                    setText(null);
                    setGraphic(null);
                    return;
                }
                String text = status;

                if (LanguageManager.isArabic()) {

                    switch (status) {
                        case "Active":
                            text = "نشط";
                            break;

                        case "Completed":
                            text = "مكتمل";
                            break;

                        case "Follow-up":
                            text = "متابعة";
                            break;
                    }
                }

                Label badge = new Label(text);
                badge.setPadding(new Insets(3,10,3,10));
                badge.setStyle(badgeStyle(status));
                setGraphic(badge);
                setText(null);
                setAlignment(Pos.CENTER_LEFT);
            }
        });
        recordsTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(MedicalRecord item,boolean empty){
                super.updateItem(item,empty);
                setStyle((!empty && getIndex()%2 !=0)
                        ? "-fx-background-color: #f7f9fc;"
                        : "-fx-background-color: white;");
            }
        });
        recordsTable.setItems(allRecords);
    }
    private void loadFromDatabase(){
        allRecords.clear();
        List<MedicalRecord> list=db.getAllMedicalRecords();
        if(list != null){
            allRecords.addAll(list);
        }
    }
    private String badgeStyle(String status){
        String base="-fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 20;";
        return switch(status){
            case "Active"    -> base+"-fx-text-fill: #1a6fba; -fx-background-color: #dbeafe;";
            case "Completed" -> base+"-fx-text-fill: #16a34a; -fx-background-color: #dcfce7;";
            case "Follow-up" -> base+"-fx-text-fill: #d97706; -fx-background-color: #fef3c7;";
            default -> base+"-fx-text-fill: #555555; -fx-background-color: #eeeeee;";
        };
    }
    @FXML
    private void onSearch(KeyEvent event){
        String query=searchField.getText().trim().toLowerCase();
        if(query.isEmpty()){
            recordsTable.setItems(allRecords);
            return;
        }
        ObservableList<MedicalRecord> filtered=FXCollections.observableArrayList();
        for(MedicalRecord r : allRecords){
            if(db.getPatientName(r.getPatientId()).toLowerCase().contains(query)
                    || db.getDoctorName(r.getDoctorId()).toLowerCase().contains(query)
                    || r.getDiagnosis().toLowerCase().contains(query)
                    || r.getTreatment().toLowerCase().contains(query)
                    || r.getNotes().toLowerCase().contains(query)
                    || r.getDate().contains(query)) {
                filtered.add(r);
            }
        }
        recordsTable.setItems(filtered);
    }
    @FXML private void handleBack(){
        MainApp.navigateTo(SessionManager.getInstance().getDashboardName(),1200,700);
    }
}