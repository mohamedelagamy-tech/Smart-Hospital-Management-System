package com.example.shms.controller;

import javafx.fxml.FXML;

public class CalendarController {
    @FXML private javafx.scene.control.Label monthLabel;
    @FXML private javafx.scene.layout.GridPane calendarGrid;

    private com.example.shms.database.DatabaseManager db = com.example.shms.database.DatabaseManager.getInstance();
    private java.time.LocalDate currentMonth = java.time.LocalDate.now().withDayOfMonth(1);

    @FXML public void initialize() {
        loadCalendar();
    }
    private void loadCalendar(){
        calendarGrid.getChildren().clear();
        monthLabel.setText(currentMonth.getMonth()+" "+currentMonth.getYear());
        String[] days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        for ( int i=0;i<7;i++){
            javafx.scene.control.Label lbl = new javafx.scene.control.Label(days[i]);
            lbl.setStyle("-fx-font-weight:bold; -fx-padding:5;");
            calendarGrid.add(lbl,i,0);
        }
        int firstDay = currentMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentMonth.lengthOfMonth();
        java.util.List<com.example.shms.model.Appointment> all = db.getAppointmentsByPatient(0);
        int col =  firstDay , row = 1;
        for (int day = 1;day<= daysInMonth;day++){
            java.time.LocalDate date = currentMonth.withDayOfMonth(day);
            javafx.scene.layout.VBox cell = new javafx.scene.layout.VBox(2);
            cell.setStyle("-fx-border-color:#ccc; -fx-padding:4; -fx-min-width:80; -fx-min-height:60;");
            javafx.scene.control.Label dayLabel = new javafx.scene.control.Label(String.valueOf(day));
            dayLabel.setStyle(date.equals (java.time.LocalDate.now())
            ? "-fx-font-weight:bold; -fx-text-fill:#185FA5;"
                    : "-fx-font-weight:bold;");
            cell.getChildren().add(dayLabel);
            for (com.example.shms.model.Appointment a:all){
                if(a.getDate().equals(date)){
                    String color ="Scheduled".equals(a.getStatus()) ?"#E6F1FB"
                            : " Completed ".equals(a.getStatus()) ?"#EAF3DE"
                            : " Cancelled".equals(a.getStatus()) ?"#FCEBEB" : "#F1EFE8";
                    javafx.scene.control.Label al= new javafx.scene.control.Label((a.getTime()+""+a.getStatus()));
                    al.setStyle("-fx-background-color:"+color+";-fx-padding:2; -fx-font-size:10;");
                    cell.getChildren().add(al);

                }
            }
            calendarGrid.add(cell,col,row);
            col++;
            if (col == 7) {
                col=0;
                row++;
            }

        }
    }
    @FXML private void handlePrevMonth(){
        currentMonth = currentMonth.minusMonths(1);
        loadCalendar();
    }
    @FXML private void handleNextMonth()
    {
        currentMonth = currentMonth.plusMonths(1);
        loadCalendar();
    }
}
