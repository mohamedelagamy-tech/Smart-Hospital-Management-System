package com.example.shms.model;

public class ReorderRequest {
    private int id ;
    private int medicationId;
    private String medicationName;
    private int quantityRequested;
    private String supplier;
    private String requestDate;
    private String expectedDate;
    private String status;
    public ReorderRequest(){}
    public ReorderRequest (int id, int medicationId, String medicationName,
                           int quantityRequested, String supplier,
                           String requestDate, String expectedDate, String status) {
        this.id=id;
        this.medicationId=medicationId;
        this.medicationName=medicationName;
        this.quantityRequested=quantityRequested;
        this.supplier=supplier;
        this.requestDate=requestDate;
        this.expectedDate=expectedDate;
        this.status=status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

