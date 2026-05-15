package com.example.shms.model;

 public class Doctor extends Person {
     private String department;
     private String status;
     private double totalRating;
     private int ratingCount;

     public Doctor(int ID, String name, String email, String password, String department, String status) {
         super(ID, name, email, password, "Doctor");
         this.department = department;
         this.status = status;
         this.totalRating = 0;
         this.ratingCount = 0;
     }

     public Doctor(int ID, String name, String email, String password, String department, String status,
                   double totalRating, int ratingCount) {

         super(ID, name, email, password, "Doctor");

         this.department = department;
         this.status = status;
         this.totalRating = totalRating;
         this.ratingCount = ratingCount;
     }

     @Override
     public String getDetails() {
         return "Doctor ID: " + getID()
                 + ", Name: " + getName()
                 + ", Email: " + getEmail()
                 + ", Department: " + department
                 + ", Status: " + status
                 + ", Average Rating: " + getAverageRating();
     }

     public void addRating(double rating) {
         if (rating < 1 || rating > 5) {
             throw new IllegalArgumentException("Rating must be between 1 and 5");
         }
         totalRating += rating;
         ratingCount++;

     }

     public double getAverageRating() {
         if (ratingCount == 0) {
             return 0;
         }
         return totalRating/ratingCount;
     }

     public String getDepartment() {
         return department;
     }

     public void setDepartment(String department) {
         this.department = department;
     }

     public String getStatus() {
         return status;
     }

     public void setStatus(String status) {
         this.status = status;
     }

     public double getTotalRating() {
         return totalRating;
     }

     public void setTotalRating(double totalRating) {
         this.totalRating = totalRating;
     }

     public int getRatingCount() {
         return ratingCount;
     }

     public void setRatingCount(int ratingCount) {
         this.ratingCount = ratingCount;
     }

     @Override
     public String toString() {
         return "Doctor ID: " + getID()
                 + ", Name: " + getName()
                 + ", Role: " + getRole()
                 + ", Department: " + department
                 + ", Status: " + status
                 + ", Average Rating: " + String.format("%.2f", getAverageRating());
     }
 }
