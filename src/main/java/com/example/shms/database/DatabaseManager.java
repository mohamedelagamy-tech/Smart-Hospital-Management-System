package com.example.shms.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


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
