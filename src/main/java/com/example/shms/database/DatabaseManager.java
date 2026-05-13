package com.example.shms.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

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

    private void insertData(){
        try (Statement st=connection.createStatement()) {

            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.getInt(1) > 0){
                return;
            }

            st.execute("INSERT INTO users (username,password,role) VALUES ('Zeina','admin123','ADMIN')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('mariam','doctor123','DOCTOR')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('SalmaK','nurse123','NURSE')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('salmaA','recep123','RECEPTIONIST')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('Mohamed','patient123','PATIENT')");

            st.execute("INSERT INTO departments (depName) VALUES ('Cardiology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Pediatrics')");
            st.execute("INSERT INTO departments (depName) VALUES ('Emergency')");
            st.execute("INSERT INTO departments (depName) VALUES ('Neurology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Orthopedics')");
            st.execute("INSERT INTO departments (depName) VALUES ('Dermatology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Oncology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Radiology')");

            st.execute("INSERT INTO doctors (name,specialization,gender,schedule,status) VALUES ('Dr. Khaled Nour','Cardiologist','Male','Mon-Wed-Fri','Available')");
            st.execute("INSERT INTO doctors (name,specialization,gender,schedule,status) VALUES ('Dr. Mona Samir','Pediatrician','Female','Sun-Tue-Thu','Available')");
            st.execute("INSERT INTO doctors (name,specialization,gender,schedule,status) VALUES ('Dr. Ahmed Hassan','Emergency Specialist','Male','Mon-Tue-Wed','Available')");
            st.execute("INSERT INTO doctors (name,specialization,gender,schedule,status) VALUES ('Dr. Layla Farouk','Neurologist','Female','Sun-Mon-Wed','Available')");
            st.execute("INSERT INTO doctors (name,specialization,gender,schedule,status) VALUES ('Dr. Omar Sherif','Orthopedic Surgeon','Male','Tue-Thu-Sat','Available')");
            st.execute("INSERT INTO doctors (name,specialization,gender,schedule,status) VALUES ('Dr. Hana Adel','Dermatologist','Female','Mon-Wed-Fri','Available')");
            st.execute("INSERT INTO doctors (name,specialization,gender,schedule,status) VALUES ('Dr. Tarek Mansour','Oncologist','Male','Sun-Tue-Thu','Available')");
            st.execute("INSERT INTO doctors (name,specialization,gender,schedule,status) VALUES ('Dr. Yasmine Farid','Radiologist','Female','Mon-Wed-Sat','Busy')");
            st.execute("INSERT INTO doctors (name,specialization,gender,schedule,status) VALUES ('Dr. Mahmoud Gamal','General Surgeon','Male','Tue-Thu-Sat','Available')");

            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Sara Mohamed',34,'Female','01098765432','Cairo','A+')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Omar Ali',25,'Male','01012345678','Giza','B+')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Nour Hassan',45,'Female','01112223344','Alexandria','O+')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Karim Adel',30,'Male','01234567890','Cairo','AB+')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Layla Mahmoud',28,'Female','01098001122','Cairo','A-')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Youssef Tarek',52,'Male','01156789012','Giza','B-')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Dina Mostafa',38,'Female','01234509876','Cairo','O-')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Hassan Nabil',61,'Male','01198765432','Alexandria','A+')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Rania Khaled',29,'Female','01067890123','Cairo','AB-')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Amr Saeed',44,'Male','01145678901','Giza','B+')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Adam Khaled',7,'Male','01067890123','Cairo','AB-')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Lina Hassan',4,'Female','01145678901','Giza','B+')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType) VALUES ('Yara Samir',11,'Female','01198765432','Alexandria','A+')");

            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('101','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('102','Occupied')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('103','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('201','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('202','Occupied')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('301','Occupied')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('302','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('401','Available')");

            System.out.println("Data inserted!");

        }catch (SQLException e){
            System.out.println("Data insertion failed: "+e.getMessage());
        }
    }

    private void createTables(){
        try(Statement st=connection.createStatement()){
            st.execute("CREATE TABLE IF NOT EXISTS users ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "username TEXT NOT NULL UNIQUE,"+
                    "password TEXT NOT NULL,"+
                    "role TEXT NOT NULL)");

            st.execute("CREATE TABLE IF NOT EXISTS doctors (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "specialization TEXT NOT NULL," +
                    "gender TEXT NOT NULL," +
                    "schedule TEXT NOT NULL," +
                    "status TEXT DEFAULT 'Available'," +
                    "rating REAL DEFAULT 0.0)");

            st.execute("CREATE TABLE IF NOT EXISTS patients ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "name TEXT NOT NULL UNIQUE,"+
                    "age INTEGER NOT NULL,"+
                    "gender TEXT NOT NULL,"+
                    "phone TEXT NOT NULL,"+
                    "address TEXT NOT NULL,"+
                    "bloodType TEXT NOT NULL,"+
                    "department TEXT)");

            st.execute("CREATE TABLE IF NOT EXISTS departments ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "depName TEXT NOT NULL UNIQUE)");

            st.execute("CREATE TABLE IF NOT EXISTS rooms ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "roomNumber TEXT NOT NULL,"+
                    "status TEXT DEFAULT 'Available',"+
                    "assignedPatientID INTEGER)");

            st.execute("CREATE TABLE IF NOT EXISTS auditLog (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "role TEXT," +
                    "status TEXT," +
                    "timestamp TEXT)");

            st.execute("CREATE TABLE IF NOT EXISTS bills ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "patientID INTEGER NOT NULL,"+
                    "patientName TEXT NOT NULL,"+
                    "doctorName TEXT NOT NULL,"+
                    "treatment TEXT NOT NULL,"+
                    "amount REAL NOT NULL,"+
                    "paymentStatus TEXT NOT NULL,"+
                    "paymentMethod TEXT NOT NULL)");

            st.execute("CREATE TABLE IF NOT EXISTS appointments ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
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
                    "patientID INTEGER NOT NULL,"+
                    "doctorID INTEGER NOT NULL,"+
                    "medicineName TEXT NOT NULL,"+
                    "dosage TEXT NOT NULL,"+
                    "instructions TEXT NOT NULL,"+
                    "duration TEXT NOT NULL,"+
                    "dateIssued TEXT NOT NULL)");

            System.out.println("All tables created successfully!");
            insertData();

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
    public void addPatient(String name,int age, String gender,String phone,String address,String bloodType,String department){
        String sql="INSERT INTO patients(name,age,gender,phone,address,bloodType,department) VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1,name);
            ps.setInt(2,age);
            ps.setString(3,gender);
            ps.setString(4,phone);
            ps.setString(5,address);
            ps.setString(6,bloodType);
            ps.setString(7,department);
            ps.executeUpdate();
            System.out.println("Patient added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding patient "+e.getMessage());
        }
    }
    public void editPatient(int id,String name,int age,String gender,String phone,String address,String bloodType,String department){
        String sql="UPDATE patients SET name=?,age=?,gender=?,phone=?,address=?,bloodType=?,department=? WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1,name);
            ps.setInt(2,age);
            ps.setString(3,gender);
            ps.setString(4,phone);
            ps.setString(5,address);
            ps.setString(6,bloodType);
            ps.setString(7,department);
            ps.setInt(8,id);
            ps.executeUpdate();
            System.out.println("Patient edited successfully!");
        }catch (SQLException e) {
            System.out.println("Error editing patient "+e.getMessage());
        }
    }
    public void deletePatient(int id){
        String sql="DELETE FROM patients WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
            System.out.println("Patient deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting patient "+e.getMessage());
        }
    }
    public ResultSet selectPatient(String keyword){
        String sql="SELECT * FROM patients WHERE name LIKE ? OR phone LIKE ? OR id LIKE ?";
        try{
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,"%"+keyword+"%");
            ps.setString(2,"%"+keyword+"%");
            ps.setString(3,"%"+keyword+"%");
            return ps.executeQuery();
        }catch (SQLException e) {
            System.out.println("Error selecting patient "+e.getMessage());
            return null;
        }
    }
    public ResultSet getAllPatients(){
        String sql="SELECT * FROM patients";
        try{
            Statement st=connection.createStatement();
            return st.executeQuery(sql);
        }catch (SQLException e) {
            System.out.println("Error selecting patients "+e.getMessage());
            return null;
        }
    }
    public void updateStatus(int id,String status){
        String sql="UPDATE appointments SET status=? WHERE patientID=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1,status);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Patient updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating patient "+e.getMessage());
        }
    }
    public ResultSet getPatientByDepartment(String department){
        String sql="SELECT * FROM patients WHERE department=?";
        try{
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,department);
            return ps.executeQuery();
        }catch (SQLException e) {
            System.out.println("Error selecting patient by department "+e.getMessage());
            return null;
        }
    }
}
