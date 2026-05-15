package com.example.shms.model;
public class Bill implements Billable {

    private final int billNumber;
    private int patientId;
    private int appointmentId;
    private double amount;
    private String status; 
    private String date;
    public Bill(int billNumber, int patientId, int appointmentId, String date) {
        this.billNumber = billNumber;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.date = date;
    }

    public int getBillNumber() { return billNumber; }
    public int getPatientId() { return patientId; }
    public int getAppointmentId() { return appointmentId; }
    public double getAmount() { return amount; }
    public String getBillStatus() { return status; }
    public String getBILLDate() { return date; }


    public void setBillStatus(String status) { this.status = status; }
    public void setAmount(double amount) { this.amount = amount; }

}
