package com.example.shms.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
//hi

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String db_URL="jdbc:sqlite:hospital.db";

    private void connect(){
        try{
            connection=DriverManager.getConnection(db_URL);
            System.out.println("Database connected!");
        }catch(SQLException e){
            System.out.println("Failed to connect"+e.getMessage());
        }
    }

    private void createTables(){
        try(Statement st=connection.createStatement()){
            st.execute("CREATE TABLE IF NOT EXISTS users ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "username TEXT NOT NULL UNIQUE,"+
                    "password TEXT NOT NULL,"+
                    "role TEXT NOT NULL)");
            st.execute("CREATE TABLE IF NOT EXISTS patients ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "name TEXT NOT NULL UNIQUE,"+
                    "age INTEGER NOT NULL,"+
                    "gender TEXT NOT NULL)"+
                    "phone TEXT NOT NULL"+
                    "address TEXT NOT NULL"+
                    "bloodType TEXT NOT NULL)");
            st.execute("CREATE TABLE IF NOT EXISTS doctors ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "name TEXT NOT NULL UNIQUE,"+
                    "age INTEGER NOT NULL,"+
                    "gender TEXT NOT NULL)"+
                    "phone TEXT NOT NULL"+
                    "address TEXT NOT NULL"+
                    "bloodType TEXT NOT NULL)");
            st.execute("CREATE TABLE IF NOT EXISTS bills ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "billId INTEGER NOT NULL UNIQUE,"+
                    "patientID INTEGER NOT NULL,"+
                    "patientName TEXT NOT NULL,"+
                    "doctorName TEXT NOT NULL,"+
                    "treatment TEXT NOT NULL,"+
                    "amount REAL NOT NULL,"+
                    "payementStatus TEXT NOT NULL,"+
                    "payementMethod TEXT NOT NULL)");
            st.execute("CREATE TABLE IF NOT EXISTS appointments ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "appointmentID INTEGER NOT NULL UNIQUE,"+
                    "patientID INTEGER NOT NULL,"+
                    "patientName TEXT NOT NULL,"+
                    "doctorID INTEGER NOT NULL,"+
                    "doctorName TEXT NOT NULL,"+
                    "appointmentDate TEXT NOT NULL,"+
                    "appointmentTime TEXT NOT NULL,"+
                    "status TEXT NOT NULL,"+
                    "priority TEXT NOT NULL)");
            st.execute("CREATE TABLE IF NOT EXISTS prescriptions ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "prescriptionID INTEGER NOT NULL UNIQUE,"+
                    "patientID INTEGER NOT NULL,"+
                    "doctorID INTEGER NOT NULL,"+
                    "medicineName TEXT NOT NULL,"+
                    "dosage TEXT NOT NULL,"+
                    "instructions TEXT NOT NULL,"+
                    "duration TEXT NOT NULL,"+
                    "dateIssued TEXT NOT NULL)");
        }catch (SQLException e) {
            System.out.println("Table creation failed: "+e.getMessage());
        }
    }

    public Connection getConnection(){
        return connection;
    }

    private DatabaseManager(){
        connect();
        createTables();
    }

    public static DatabaseManager getInstance(){
        if(instance==null){
            instance=new DatabaseManager();
        }
        return instance;
    }
}
