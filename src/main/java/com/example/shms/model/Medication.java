package com.example.shms.model;

public class Medication {
    private int id;
    private String name;
    private String category;
    private String form;
    private int stock;
    private int minStock;
    private double unitPrice;
    private String expiryDate;
    private String status;

    public Medication() {
    }

    public Medication(int id, String name, String category, String form, int stock, int minStock, double unitPrice, String expiryDate, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.form = form;
        this.stock = stock;
        this.minStock = minStock;
        this.unitPrice = unitPrice;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}