package com.example.shms.model;

import java.util.Comparator;

public class Doctor extends Person implements Comparable<Doctor>, Schedulable  {
     private String department;
     private String status;
     private double salary;
     private String workingDays;
     private String workingHours;
     private double totalRating;
     private int ratingCount;

     public static final Comparator<Doctor> By_NAME =
             Comparator.comparing(Person::getName);
     public static final Comparator<Doctor> By_DEPARTMENT =
            Comparator.comparing(Doctor::getDepartment);
     public static final Comparator<Doctor> By_AVERAGE_RATING =
            Comparator.comparingDouble(Doctor::getAverageRating).reversed();
     public static final Comparator<Doctor> By_SALARY=
            Comparator.comparingDouble(Doctor::getSalary).reversed();


     public Doctor(int ID, String name, String email, String password, String department, String status, double salary,String workingDays, String workingHours) {
         super(ID, name, email, password, "Doctor");
         this.department = department;
         this.status = status;
         this.salary= salary;
         this.workingDays = workingDays;
         this.workingHours = workingHours;
         this.totalRating = 0;
         this.ratingCount = 0;
     }

     public Doctor(int ID, String name, String email, String password, String department, String status,
                   double salary, String workingDays, String workingHours, double totalRating, int ratingCount) {

         super(ID, name, email, password, "Doctor");

         this.department = department;
         this.status = status;
         this.salary=salary;
         this.workingDays = workingDays;
         this.workingHours = workingHours;
         this.totalRating = totalRating;
         this.ratingCount = ratingCount;
     }

    @Override
    public int compareTo(Doctor other) {
        return Double.compare(other.getAverageRating(), this.getAverageRating());
    }

    @Override
     public String getDetails() {
         return "Doctor ID: " + getID()
                 + ", Name: " + getName()
                 + ", Email: " + getEmail()
                 + ", Department: " + department
                 + ", Status: " + status
                 + ", Salary: " + salary
                 + ", Working Days: " + workingDays
                 + ", Working Hours: " + workingHours
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

     public double getSalary(){ return salary; }

     public void setSalary(double salary){ this.salary = salary; }

    @Override
    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    @Override
    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
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
                 + ", Salary: " + String.format("%.2f", salary)
                 + ", Working Days: " + workingDays
                 + ", Working Hours: " + workingHours
                 + ", Average Rating: " + String.format("%.2f", getAverageRating());
     }
 }
