//private static final String db_URL = "jdbc:sqlite:hospital.db";
package com.example.shms.database;

import com.example.shms.model.*;
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

    private List<MedicalRecord> medicalRecords = new ArrayList<>();
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

            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Khaled Nour','khaled@hospital.com','doctor123','Cardiology','Available',15000,'Mon-Wed-Fri','8am-4pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Mona Samir','mona@hospital.com','doctor123','Pediatrics','Available',12000,'Sun-Tue-Thu','9am-5pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Ahmed Hassan','ahmed@hospital.com','doctor123','Emergency','Available',18000,'Mon-Tue-Wed','7am-3pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Layla Farouk','layla@hospital.com','doctor123','Neurology','Available',16000,'Sun-Mon-Wed','8am-4pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Omar Sherif','omar@hospital.com','doctor123','Orthopedics','Busy',14000,'Tue-Thu-Sat','10am-6pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Hana Adel','hana@hospital.com','doctor123','Dermatology','Available',11000,'Mon-Wed-Fri','9am-5pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Tarek Mansour','tarek@hospital.com','doctor123','Oncology','Available',20000,'Sun-Tue-Thu','8am-4pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Yasmine Farid','yasmine@hospital.com','doctor123','Radiology','Busy',13000,'Mon-Wed-Sat','7am-3pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Mahmoud Gamal','mahmoud@hospital.com','doctor123','Emergency','In Surgery',17000,'Tue-Thu-Sat','6am-2pm')");
            int[][] seedRatings={
                    {5,4,5,4,5},
                    {3,4,3,4,3},
                    {5,5,4,5,4},
                    {4,3,4,3,4},
                    {2,3,2,3,2},
                    {4,5,4,5,5},
                    {5,4,5,5,4},
                    {3,2,3,2,3},
                    {4,4,5,4,4}
            };
            String[] comments = {
                    "Excellent doctor, very professional",
                    "Good experience overall",
                    "Very knowledgeable and caring",
                    "Explained everything clearly",
                    "Would recommend to others"
            };
            for(int di=0;di<seedRatings.length;di++){
                int docId=di+1;
                int[] ratings=seedRatings[di];
                double total=0;
                for(int i=0;i<ratings.length;i++){
                    st.execute("INSERT INTO doctor_ratings (doctor_id, rating, comment) VALUES ("
                            + docId + ", " + ratings[i] + ", '" + comments[i] + "')");
                    total+=ratings[i];
                }
                st.execute("UPDATE doctors SET totalRating="+total+", ratingCount="+ratings.length+" WHERE id="+docId);
            }

            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Sara Mohamed',34,'Female','01098765432','Cairo','A+','Cardiology',1,'sara.mohamed@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Omar Ali',25,'Male','01012345678','Giza','B+','Orthopedics',2,'omar.ali@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Nour Hassan',45,'Female','01112223344','Alexandria','O+','Neurology',2,'nour.hassan@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Karim Adel',30,'Male','01234567890','Cairo','AB+','Emergency',1,'karim.adel@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Layla Mahmoud',28,'Female','01098001122','Cairo','A-','Dermatology',3,'layla.mahmoud@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Youssef Tarek',52,'Male','01156789012','Giza','B-','Oncology',2,'youssef.tarek@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Dina Mostafa',38,'Female','01234509876','Cairo','O-','Radiology',3,'dina.mostafa@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Mohamed Fawzy',28,'Male','01116341931','Alexandria','A+','Cardiology',3,'mohamed.fawzy@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Rania Khaled',29,'Female','01067890123','Cairo','AB-','Neurology',2,'rania.khaled@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Amr Saeed',44,'Male','01145678901','Giza','B+','Orthopedics',2,'amr.saeed@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Adam Khaled',7,'Male','01023456789','Cairo','AB-','Pediatrics',3,'adam.khaled@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Lina Hassan',4,'Female','01034567890','Giza','B+','Pediatrics',3,'lina.hassan@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Yara Samir',11,'Female','01045678901','Alexandria','A+','Pediatrics',3,'yara.samir@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Mona Adel',55,'Female','01056789012','Cairo','O+','Cardiology',3,'mona.adel@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Tarek Nabil',40,'Male','01067891234','Giza','B-','Emergency',1,'tarek.nabil@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Hana Sherif',22,'Female','01078901234','Alexandria','A-','Dermatology',3,'hana.sherif@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Mahmoud Farid',67,'Male','01089012345','Cairo','AB+','Oncology',3,'mahmoud.farid@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Salma Ibrahim',33,'Female','01090123456','Giza','O-','Radiology',3,'salma.ibrahim@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Khaled Mostafa',48,'Male','01001234567','Cairo','A+','Neurology',3,'khaled.mostafa@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Nadia Gamal',36,'Female','01011234567','Alexandria','B+','Orthopedics',3,'nadia.gamal@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Fares Mahmoud',9,'Male','01021234567','Cairo','O+','Pediatrics',3,'fares.mahmoud@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Rana Tarek',27,'Female','01031234567','Giza','AB-','Dermatology',3,'rana.tarek@email.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Salma Abdelkhalek',19,'Female','01012778327','Alexandria','O-','Radiology',3,'Salmaabdelkhalek26@gmail.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Salma Abdelsattar',19,'Female','01115119117','Alexandria','A+','Neurology',3,'engsalmaabdelsattar22108@gmail.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Zeina Yasser',19,'Female','01012127367','Alexandria','B+','Orthopedics',3,'zeinafarghaly@gmail.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Mohamed Mazen',18,'Male','01558887707','Alexandria','O+','Oncology',3,'m.mazenelagamy@gmail.com')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email) VALUES ('Mariam ElShafie',18,'Female','01009649895','Alexandria','AB-','Dermatology',3,'m.a.elshafei2007@gmail.com')");

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

            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (1, 1, '2026-05-20', '09:00', 'Completed', 'Regular checkup')");
            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (2, 5, '2026-05-21', '10:30', 'Completed', 'Follow-up visit')");
            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (3, 4, '2026-05-22', '11:00', 'Scheduled', 'First consultation')");
            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (4, 3, '2026-05-22', '08:00', 'Scheduled', 'Emergency follow-up')");
            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (5, 6, '2026-05-23', '13:00', 'Scheduled', 'Skin examination')");
            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (6, 7, '2026-05-23', '14:00', 'Scheduled', 'Oncology review')");
            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (7, 8, '2026-05-24', '09:30', 'Scheduled', 'Radiology scan')");
            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (8, 1, '2026-05-24', '11:00', 'Scheduled', 'Cardiology checkup')");
            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (9, 4, '2026-05-25', '10:00', 'Cancelled', 'Patient unavailable')");
            st.execute("INSERT INTO appointments (patientId, doctorId, date, time, status, notes) VALUES (10, 5, '2026-05-25', '15:00', 'Scheduled', 'Post-surgery review')");

            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (1, 1, '2026-05-20', 'Hypertension', 'Prescribed Amlodipine 5mg', 'Active')");
            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (2, 5, '2026-05-21', 'Knee Injury', 'Physical therapy recommended', 'Follow-up')");
            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (3, 4, '2026-05-22', 'Migraine', 'Prescribed Sumatriptan', 'Completed')");
            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (4, 3, '2026-05-22', 'Appendicitis', 'Emergency appendectomy performed', 'Completed')");
            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (5, 6, '2026-05-23', 'Eczema', 'Prescribed hydrocortisone cream', 'Active')");
            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (6, 7, '2026-05-23', 'Lymphoma', 'Chemotherapy session 3 of 6', 'Active')");
            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (7, 8, '2026-05-24', 'Kidney Stone', 'CT scan ordered', 'Follow-up')");
            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (8, 1, '2026-05-24', 'Arrhythmia', 'ECG performed, prescribed Metoprolol', 'Active')");
            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (9, 4, '2026-05-10', 'Epilepsy', 'Prescribed Levetiracetam 500mg', 'Completed')");
            st.execute("INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES (10, 5, '2026-05-18', 'Fracture - Left Arm', 'Cast applied for 6 weeks', 'Follow-up')");

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
                    "email TEXT,"+
                    "password TEXT," +
                    "department TEXT NOT NULL," +
                    "status TEXT DEFAULT 'Available'," +
                    "salary REAL default 0.0," +
                    "workingDays Text,"+
                    "workingHours Text," +
                    "totalRating REAL DEFAULT 0.0," +
                    "ratingCount INTEGER DEFAULT 0)");

            st.execute("CREATE TABLE IF NOT EXISTS doctor_ratings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "doctor_id INTEGER NOT NULL," +
                    "rating INTEGER NOT NULL," +
                    "comment TEXT)");

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
                    "status TEXT DEFAULT 'Waiting'," +
                    "email TEXT)");

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
                    "patientId INTEGER NOT NULL," +
                    "doctorId INTEGER NOT NULL," +
                    "date TEXT NOT NULL," +
                    "time TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "notes TEXT)");

            st.execute("CREATE TABLE IF NOT EXISTS prescriptions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patientID INTEGER NOT NULL," +
                    "doctorID INTEGER NOT NULL," +
                    "medicineName TEXT NOT NULL," +
                    "dosage TEXT NOT NULL," +
                    "instructions TEXT NOT NULL," +
                    "duration TEXT NOT NULL," +
                    "dateIssued TEXT NOT NULL)");

            st.execute("CREATE TABLE IF NOT EXISTS medical_records (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patientId INTEGER NOT NULL," +
                    "doctorId INTEGER NOT NULL," +
                    "date TEXT NOT NULL," +
                    "diagnoses TEXT," +
                    "treatment TEXT," +
                    "notes TEXT)");

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
        String sql = "UPDATE patients SET status=? WHERE id=?";
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
                "UPDATE appointments SET status='Cancelled' WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (java.sql.SQLException e) {
            System.out.println("cancelAppointment error: " + e.getMessage());
        }
    }
    public void markComplete(int id) {
        try (java.sql.PreparedStatement ps = connection.prepareStatement(
                "UPDATE appointments SET status='Completed' WHERE id=?")) {
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
                rs.getInt("id"),
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
    public List<Room> getAllRooms() {
        List<Room> result = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM rooms");
            while (rs.next()) {
                Room r = new Room(
                        rs.getInt("assignedPatientID"),
                        "",
                        rs.getInt("id"),
                        Integer.parseInt(rs.getString("roomNumber")),
                        rs.getString("status"),
                        ""
                );
                result.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        }
        return result;
    }
    public List<Room> getAvailableRooms() {
        List<Room> result = new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement(
                "SELECT * FROM rooms WHERE status ='Available'")){
                    ResultSet rs=ps.executeQuery();
                    while (rs.next()) {
                        Room r = new Room(
                        rs.getInt("assignedPatientID"),
                        "",
                        rs.getInt("id"),
                        Integer.parseInt(rs.getString("roomNumber")),
                        rs.getString("status"),
                        ""
                );
                result.add(r);
            }
        }catch (SQLException e){
            System.out.println("Error getting available rooms"+e.getMessage());
            return null;
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

    public void saveDoctorRating(int doctorId, int rating, String comment) {
        String sql = "INSERT INTO doctor_ratings (doctor_id, rating, comment) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, rating);
            ps.setString(3, comment);

            ps.executeUpdate();

            addRating(doctorId, rating);

            System.out.println("Doctor rating saved successfully!");

        } catch (SQLException e) {
            System.out.println("Error saving doctor rating: " + e.getMessage());
        }
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM medical_records");
            while (rs.next()) {
                records.add(new MedicalRecord(
                        rs.getInt("id"),
                        rs.getInt("patientId"),
                        rs.getInt("doctorId"),
                        rs.getString("date"),
                        rs.getString("diagnoses"),
                        rs.getString("treatment"),
                        rs.getString("notes")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return records;
    }

    public void addMedicalRecord(MedicalRecord record) {
        String sql="INSERT INTO medical_records (patientId, doctorId, date, diagnoses, treatment, notes) VALUES(?,?,?,?,?,?)";
        try(PreparedStatement ps=connection.prepareStatement(sql)) {
            ps.setInt(1,record.getPatientId());
            ps.setInt(2,record.getDoctorId());
            ps.setString(3,record.getDate());
            ps.setString(4,record.getDiagnosis());
            ps.setString(5,record.getTreatment());
            ps.setString(6,record.getNotes());
            ps.executeUpdate();
        }catch(SQLException e) {
            System.out.println("error adding  medical record: " + e.getMessage());
        }
    }

    public String getPatientEmail(int patientId){
        try(Statement st = connection.createStatement()){
            ResultSet rs = st.executeQuery("SELECT email FROM patients WHERE id = "+patientId);
            if(rs.next())
                return rs.getString("email");
        }catch(SQLException e){
            System.out.println("Get patient email failed: "+e.getMessage());
        }
        return null;
    }

    public String getPatientName(int patientId){
        try(Statement st=connection.createStatement()){
            ResultSet rs=st.executeQuery("SELECT name FROM patients WHERE id = "+patientId);
            if(rs.next())
                return rs.getString("name");
        }catch(SQLException e){
            System.out.println("Get patient name failed: "+e.getMessage());
        }
        return null;
    }

    public String getDoctorName(int doctorId){
        try(Statement st=connection.createStatement()) {
            ResultSet rs=st.executeQuery("SELECT name FROM doctors WHERE id = "+doctorId);
            if(rs.next())
                return rs.getString("name");
        }catch(SQLException e) {
            System.out.println("Get doctor name failed: "+e.getMessage());
        }
        return null;
    }


}


