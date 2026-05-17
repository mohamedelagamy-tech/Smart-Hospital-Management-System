package com.example.shms.model;

public class Prescription {

    private int PrescriptionNumber;
    private int patientId;
    private int doctorId;
    private String medicineName;
    private String dosage;
    private String duration;
    private String instructions;

    public Prescription(int PrescriptionNumber, int patientId, int doctorId,
                        String medicineName, String dosage,
                        String duration, String instructions) {
        this.PrescriptionNumber = PrescriptionNumber;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.duration = duration;
        this.instructions = instructions;
    }
    public int getId() { return PrescriptionNumber; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getMedicineName() { return medicineName; }
    public String getDosage() { return dosage; }
    public String getDuration() { return duration; }
    public String getInstructions() { return instructions; }

}
