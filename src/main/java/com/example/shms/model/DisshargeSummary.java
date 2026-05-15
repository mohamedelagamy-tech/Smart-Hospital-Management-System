
package com.example.shms.model;
import java.util.List;
import java.io.FileWriter;
import java.io.IOExeption;
public class DisshargeSummary {

    private final int patientID;
    private final int doctorID;
    private int roomUsed;
    private List<prescription> prescriptions;
    private double totalBill;

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
    public double getTotalBill() {
        return tatalBill;
    }
public void writeToFile() {
    try {
        FileWriter writer = new FileWriter("discharge_" + patientID + ".txt");
        writer.write("=== DISCHARGE SUMMARY ===\n");
        writer.write("Patient ID: " + patientID + "\n");
        writer.write("Doctor ID: " + doctorID + "\n");
        writer.write("Rooms Used: " + roomUsed + "\n");
        writer.write("Total Bill: " + totalBill + "\n");
        writer.write("Prescriptions: \n");
        for (Prescription p : prescriptions) {
            writer.write(" -" + p.getMedicineName() + "|" + p.getDosage() + "|" + p.getDuration() + "\n");
        }
        writer.close();
        System.out.println("file saved!");
        }
    catch (IOException e){
        System.out.println("Error writing file");
    }
}
    
}
