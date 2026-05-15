
package com.example.shms.model;
import java.util.List;
import java.io.FileWriter;
import java.io.IOExeption;
public class DisshargeSummary {

    private int patientID;
    private int doctorID;
    private int roomUsed;
    private List<prescription> prescriptions;
    private double tatalBill;

    public DisshargeSummary(int doctorID, int patientID, List<prescription> prescriptions, int roomUsed, double tatalBill) {
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.prescriptions = prescriptions;
        this.roomUsed = roomUsed;
        this.tatalBill = tatalBill;
    }
    public int getPatientID() {
        return patientID;
    }
    public double getTatalBill() {
        return tatalBill;
    }

    
}
