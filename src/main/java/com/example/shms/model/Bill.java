package com.example.shms.model;
public class Bill implements Billable {

    private int id;
    private int patientId;
    private int appointmentId;
    private double amount;
    private String status; 
    private String date;
    public Bill(int id, int patientId, int appointmentId,
                double amount, String status, String date) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getAppointmentId() { return appointmentId; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getDate() { return date; }

    public void setStatus(String status) { this.status = status; }
    public void setAmount(double amount) { this.amount = amount; }

}
