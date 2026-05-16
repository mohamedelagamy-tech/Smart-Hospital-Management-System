package com.example.shms.database;

import com.example.shms.model.Appointment;
import com.example.shms.model.Bill;
import com.example.shms.model.Prescription;
import com.example.shms.model.Room;
import com.example.shms.utils.PasswordEncryption;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
public class DatabaseManager {
    private List<Bill> bills = new ArrayList<>();
    private List<Prescription> prescriptions = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private static DatabaseManager instance;
    private Connection connection;
    private static final String db_URL = "jdbc:sqlite:hospital.db";

    private void connect() {
        try {
            connection = DriverManager.getConnection(db_URL);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            System.out.println("Failed to connect" + e.getMessage());
        }
    }

    private void insertData() {
        try (Statement st = connection.createStatement()) {

            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.getInt(1) > 0) {
                return;
            }

            st.execute("INSERT INTO users (username,password,role) VALUES ('Zeina','"+PasswordEncryption.hash("admin123")+"','ADMIN')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('Mariam','"+PasswordEncryption.hash("doctor123")+"','DOCTOR')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('SalmaK','"+PasswordEncryption.hash("nurse123")+"','NURSE')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('SalmaA','"+PasswordEncryption.hash("recep123")+"','RECEPTIONIST')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('Mohamed','"+PasswordEncryption.hash("patient123")+"','PATIENT')");

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

            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Sara Mohamed',34,'Female','01098765432','Cairo','A+','Cardiology',1)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Omar Ali',25,'Male','01012345678','Giza','B+','Orthopedics',2)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Nour Hassan',45,'Female','01112223344','Alexandria','O+','Neurology',2)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Karim Adel',30,'Male','01234567890','Cairo','AB+','Emergency',1)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Layla Mahmoud',28,'Female','01098001122','Cairo','A-','Dermatology',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Youssef Tarek',52,'Male','01156789012','Giza','B-','Oncology',2)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Dina Mostafa',38,'Female','01234509876','Cairo','O-','Radiology',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Mohamed Fawzy',28,'Male','01116341931','Alexandria','A+','Cardiology',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Rania Khaled',29,'Female','01067890123','Cairo','AB-','Neurology',2)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Amr Saeed',44,'Male','01145678901','Giza','B+','Orthopedics',2)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Adam Khaled',7,'Male','01023456789','Cairo','AB-','Pediatrics',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Lina Hassan',4,'Female','01034567890','Giza','B+','Pediatrics',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Yara Samir',11,'Female','01045678901','Alexandria','A+','Pediatrics',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Mona Adel',55,'Female','01056789012','Cairo','O+','Cardiology',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Tarek Nabil',40,'Male','01067891234','Giza','B-','Emergency',1)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Hana Sherif',22,'Female','01078901234','Alexandria','A-','Dermatology',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Mahmoud Farid',67,'Male','01089012345','Cairo','AB+','Oncology',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Salma Ibrahim',33,'Female','01090123456','Giza','O-','Radiology',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Khaled Mostafa',48,'Male','01001234567','Cairo','A+','Neurology',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Nadia Gamal',36,'Female','01011234567','Alexandria','B+','Orthopedics',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Fares Mahmoud',9,'Male','01021234567','Cairo','O+','Pediatrics',3)");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority) VALUES ('Rana Tarek',27,'Female','01031234567','Giza','AB-','Dermatology',3)");

            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('101','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('102','Occupied')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('103','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('201','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('202','Occupied')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('301','Occupied')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('302','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('401','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('402','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('303','Occupied')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('304','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('501','Occupied')");


            System.out.println("Data inserted!");

        } catch (SQLException e) {
            System.out.println("Data insertion failed: " + e.getMessage());
        }
    }

    private void createTables() {
        try (Statement st = connection.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL)");

            st.execute("CREATE TABLE IF NOT EXISTS doctors (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "specialization TEXT NOT NULL," +
                    "gender TEXT NOT NULL," +
                    "schedule TEXT NOT NULL," +
                    "status TEXT DEFAULT 'Available'," +
                    "rating REAL DEFAULT 0.0)");

            st.execute("CREATE TABLE IF NOT EXISTS patients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "age INTEGER NOT NULL," +
                    "gender TEXT NOT NULL," +
                    "phone TEXT NOT NULL," +
                    "address TEXT NOT NULL," +
                    "bloodType TEXT NOT NULL," +
                    "department TEXT NOT NULL," +
                    "priority INTEGER DEFAULT 3," +
                    "status TEXT DEFAULT 'Waiting')");

            st.execute("CREATE TABLE IF NOT EXISTS departments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "depName TEXT NOT NULL UNIQUE)");

            st.execute("CREATE TABLE IF NOT EXISTS rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "roomNumber TEXT NOT NULL," +
                    "status TEXT DEFAULT 'Available'," +
                    "assignedPatientID INTEGER)");

            st.execute("CREATE TABLE IF NOT EXISTS auditLog (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "role TEXT," +
                    "status TEXT," +
                    "timestamp TEXT)");

            st.execute("CREATE TABLE IF NOT EXISTS bills (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patientID INTEGER NOT NULL," +
                    "patientName TEXT NOT NULL," +
                    "doctorName TEXT NOT NULL," +
                    "treatment TEXT NOT NULL," +
                    "amount REAL NOT NULL," +
                    "paymentStatus TEXT NOT NULL," +
                    "paymentMethod TEXT NOT NULL)");

            st.execute("CREATE TABLE IF NOT EXISTS appointments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patientID INTEGER NOT NULL," +
                    "patientName TEXT NOT NULL," +
                    "doctorID INTEGER NOT NULL," +
                    "doctorName TEXT NOT NULL," +
                    "appointmentDate TEXT NOT NULL," +
                    "appointmentTime TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "priority TEXT NOT NULL)");

            st.execute("CREATE TABLE IF NOT EXISTS prescriptions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patientID INTEGER NOT NULL," +
                    "doctorID INTEGER NOT NULL," +
                    "medicineName TEXT NOT NULL," +
                    "dosage TEXT NOT NULL," +
                    "instructions TEXT NOT NULL," +
                    "duration TEXT NOT NULL," +
                    "dateIssued TEXT NOT NULL)");

            System.out.println("All tables created successfully!");
            insertData();

        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private DatabaseManager() {
        connect();
        createTables();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void addPatient(String name, int age, String gender, String phone, String address, String bloodType, String department, int priority) {
        String sql = "INSERT INTO patients(name,age,gender,phone,address,bloodType,department,priority) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.setString(6, bloodType);
            ps.setString(7, department);
            ps.setInt(8, priority);
            ps.executeUpdate();
            System.out.println("Patient added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding patient " + e.getMessage());
        }
    }

    public void editPatient(int id, String name, int age, String gender, String phone, String address, String bloodType, String department, int priority) {
        String sql = "UPDATE patients SET name=?,age=?,gender=?,phone=?,address=?,bloodType=?,department=?,priority=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.setString(6, bloodType);
            ps.setString(7, department);
            ps.setInt(8, priority);
            ps.setInt(9, id);
            ps.executeUpdate();
            System.out.println("Patient edited successfully!");
        } catch (SQLException e) {
            System.out.println("Error editing patient " + e.getMessage());
        }
    }

    public void deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Patient deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting patient " + e.getMessage());
        }
    }

    public ResultSet selectPatient(String keyword) {
        String sql = "SELECT * FROM patients WHERE name LIKE ? OR phone LIKE ? OR id LIKE ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error selecting patient " + e.getMessage());
            return null;
        }
    }
    public ResultSet getAllPatients() {
        String sql = "SELECT * FROM patients";
        try {
            Statement st = connection.createStatement();
            return st.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error selecting patients " + e.getMessage());
            return null;
        }
    }
    public void updateStatus(int id, String status) {
        String sql = "UPDATE patients SET status=? WHERE patientID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Patient updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating patient " + e.getMessage());
        }
    }
    public ResultSet getPatientByDepartment(String department) {
        String sql = "SELECT * FROM patients WHERE department=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, department);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error selecting patient by department " + e.getMessage());
            return null;
        }
    }
    public void addAppointment(Appointment appt) {
        String sql = "INSERT INTO appointments (patientId,doctorId,date,time,status,notes) VALUES (?,?,?,?,?,?)";
        try (java.sql.PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, appt.getPatientId());
            ps.setInt(2, appt.getDoctorId());
            ps.setString(3, appt.getDate().toString());
            ps.setString(4, appt.getTime().toString());
            ps.setString(5, appt.getStatus());
            ps.setString(6, appt.getNotes());
            ps.executeUpdate();
        } catch (java.sql.SQLException e) {
            System.out.println("addAppointment error: " + e.getMessage());
        }
    }
    public void cancelAppointment(int id) {
        try (java.sql.PreparedStatement ps = connection.prepareStatement(
                "UPDATE appointments SET status='Cancelled' WHERE appointmentId=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (java.sql.SQLException e) {
            System.out.println("cancelAppointment error: " + e.getMessage());
        }
    }
    public void markComplete(int id) {
        try (java.sql.PreparedStatement ps = connection.prepareStatement(
                "UPDATE appointments SET status='Completed' WHERE appointmentId=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (java.sql.SQLException e) {
            System.out.println("markComplete error: " + e.getMessage());
        }
    }
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        try (java.sql.PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM appointments WHERE patientId=? ORDER BY date,time")) {
            ps.setInt(1, patientId);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapAppointment(rs));
        } catch (java.sql.SQLException e) {
            System.out.println("getByPatient error: " + e.getMessage());
        }
        return list;
    }
    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        try (java.sql.PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM appointments WHERE doctorId=? ORDER BY date,time")) {
            ps.setInt(1, doctorId);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapAppointment(rs));
        } catch (java.sql.SQLException e) {
            System.out.println("getByDoctor error: " + e.getMessage());
        }
        return list;
    }
    public boolean hasConflict(int doctorId, java.time.LocalDate date, java.time.LocalTime time) {
        try (java.sql.PreparedStatement ps = connection.prepareStatement(
                "SELECT COUNT(*) FROM appointments WHERE doctorId=? AND date=? AND time=? AND status!='Cancelled'")) {
            ps.setInt(1, doctorId);
            ps.setString(2, date.toString());
            ps.setString(3, time.toString());
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (java.sql.SQLException e) {
            System.out.println("hasConflict error: " + e.getMessage());
        }
        return false;
    }
    private Appointment mapAppointment(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new Appointment(
                rs.getInt("appointmentId"),
                rs.getInt("patientId"),
                rs.getInt("doctorId"),
                java.time.LocalDate.parse(rs.getString("date")),
                java.time.LocalTime.parse(rs.getString("time")),
                rs.getString("status"),
                rs.getString("notes")
        );
    }

    public void addBill(Bill bill) {
        bills.add(bill);
    }
    public void updateBillStatus(String billId, String newStatus) {
        for (Bill b : bills) {
            if (b.getBillNumber().equals(billId))
                b.setBillStatus(newStatus);
        }
    }
    public List<Bill> getBillsByPatient(int patientId) {
        List<Bill> result = new ArrayList<>();
        for (Bill b : bills) {
            if (b.getPatientId() == patientId) result.add(b);
        }
        return result;
    }
    public List<Bill> getAllBills() { return bills; }

    public void addPrescription(Prescription p) { prescriptions.add(p); }
    public List<Prescription> getPrescriptionsByPatient(int patientId) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription p : prescriptions) {
            if (p.getPatientId() == patientId) result.add(p);
        }
        return result;
    }

    public void assignRoom(int roomId, int patientId) {
        for (Room r : rooms) {
            if (r.getId() == roomId) {
                r.setRoomStatus("Occupied");
                r.setAssignedPatientId(patientId);
            }
        }
    }
    public void releaseRoom(int roomId) {
        for (Room r : rooms) {
            if (r.getId() == roomId) {
                r.setRoomStatus("Available");
                r.setAssignedPatientId(0);
            }
        }
    }
    public List<Room> getAllRooms() { return rooms; }
    public List<Room> getAvailableRooms() {
        List<Room> result = new ArrayList<>();
        for (Room r : rooms) {
            if (r.getRoomStatus().equals("Available")) result.add(r);
        }
        return result;
    }
    public ResultSet getAvailableRoom(){
        String sql = "SELECT * FROM rooms WHERE status='Available' LIMIT 1";
        try{
            Statement st=connection.createStatement();
            return st.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error getting room: " + e.getMessage());
            return null;
        }
    }
    public int getNextPatientId(){
        try(Statement st= connection.createStatement()){
            ResultSet rs= st.executeQuery("SELECT MAX(id) AS maxID FROM patients");
            return rs.getInt("maxID")+1;
        }catch(SQLException e){
            return 1;
        }
    }
    public int getNextDoctorId(){
        try(Statement st= connection.createStatement()){
            ResultSet rs= st.executeQuery("SELECT MAX(id) AS maxID FROM doctors");
            return rs.getInt("maxID")+1;
        }catch(SQLException e){
            return 1;
        }

    }

    public void addDoctor(String name, String email, String password, String department, String status, double salary,
                          String workingDays, String workingHours){
        String sql = "Insert INTO doctors(name,email,password,department,status,salary,workingDays,workingHours) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, department);
            ps.setString(5, status);
            ps.setDouble(6, salary);
            ps.setString(7, workingDays);
            ps.setString(8, workingHours);
            ps.executeUpdate();
            System.out.println("Doctor added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding doctor: " + e.getMessage());
        }
    }

    public void editDoctor(int id, String name, String email, String password, String department, String status, double salary,
                           String workingDays, String workingHours){
        String sql = "UPDATE doctors SET name=?,email=?,password=?,department=?,status=?,salary=?,workingDays=?,workingHours=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, department);
            ps.setString(5, status);
            ps.setDouble(6, salary);
            ps.setString(7, workingDays);
            ps.setString(8, workingHours);
            ps.setInt(9, id);
            ps.executeUpdate();
            System.out.println("Doctor edited successfully!");
        } catch (SQLException e) {
            System.out.println("Error editing doctor: " + e.getMessage());
        }
    }

    public void removeDoctor(int id){
        String sql = "DELETE FROM doctors WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Doctor removed successfully!");
        } catch (SQLException e) {
            System.out.println("Error removing doctor: " + e.getMessage());
        }
    }

    public ResultSet getAllDoctors(){
        String sql = "SELECT * FROM doctors";
        try{
            Statement st = connection.createStatement();
            return st.executeQuery(sql);
        }
        catch(SQLException e){
            System.out.println("Error getting all doctors: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getDoctorsByDepartment(String department){
        String sql = "SELECT * FROM doctors WHERE department=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, department);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getting doctors by department: " + e.getMessage());
            return null;
        }
    }

    public void UpdateAvailability(int id, String status){
        String sql = "UPDATE doctors SET status=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Doctor availability updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating doctor availability: " + e.getMessage());
        }
    }

    public void addRating(int id, double newRating) {
        if (newRating < 1 || newRating > 5) {
            System.out.println("Rating must be between 1 and 5");
            return;
        }
        String selectSql = "SELECT totalRating, ratingCount FROM doctors WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double totalRating = rs.getDouble("totalRating");
                int ratingCount = rs.getInt("ratingCount");
                double updatedTotal = totalRating + newRating;
                int updatedCount = ratingCount + 1;

                String updateSql = "UPDATE doctors SET totalRating=?, ratingCount=? WHERE id=?";
                try (PreparedStatement ps2 = connection.prepareStatement(updateSql)) {
                    ps2.setDouble(1, updatedTotal);
                    ps2.setInt(2, updatedCount);
                    ps2.setInt(3, id);
                    ps2.executeUpdate();
                    System.out.println("Rating added successfully!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding rating: " + e.getMessage());
        }
    }
    public double getAverageRating(int id) {
        String sql = "SELECT totalRating, ratingCount FROM doctors WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double totalRating = rs.getDouble("totalRating");
                int ratingCount = rs.getInt("ratingCount");
                if (ratingCount == 0) return 0.0;
                return totalRating / ratingCount;
            }
        } catch (SQLException e) {
            System.out.println("Error getting average rating: " + e.getMessage());
        }
        return 0.0;
    }

}


