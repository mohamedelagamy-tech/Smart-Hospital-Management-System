package com.example.shms.utils;
import com.example.shms.database.DatabaseManager;
import com.example.shms.model.Appointment;
import java.util.Collections;
import java.util.List;
public class AppointmentHistory {
    private DatabaseManager db;
    public AppointmentHistory(DatabaseManager db){
        this.db=db;
    }
    public List<Appointment> getHistory(int patientId){
        List<Appointment> history=db.getAppointmentsByPatient(patientId);
                Collections.sort(history);
        return history;
    }
public List<Appointment> getCompletedOnly(int patientId){
    List<Appointment> all=getHistory(patientId);
    List<Appointment> completed = new java.util.ArrayList<>();
    for (Appointment a:all)
    {
        if ("Completed".equals(a.getStatus())){
            completed.add(a);
        }
    }
    return completed;
    }
    public  void printHistory(int patientId){
        List<Appointment> history = getHistory(patientId);
        if (history.isEmpty()){
            System.out.println("No appointments found for patient "+patientId);
            return;
        }
        System.out.println("Appointment History Patient:" +patientId);
        for (Appointment a : history){
            System.out.println(a);
        }
    }
}
