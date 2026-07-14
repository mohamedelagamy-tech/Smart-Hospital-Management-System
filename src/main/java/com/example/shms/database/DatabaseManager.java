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
            st.execute("INSERT INTO users (username,password,role) VALUES ('zeina.yasser','"+PasswordEncryption.hash("patient123")+"','PATIENT')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('mariam.elshafie','"+PasswordEncryption.hash("patient123")+"','PATIENT')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('salma.abdelsattar','"+PasswordEncryption.hash("patient123")+"','PATIENT')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('salma.abdelkhalek','"+PasswordEncryption.hash("patient123")+"','PATIENT')");
            st.execute("INSERT INTO users (username,password,role) VALUES ('mohamed.fawzy','"+PasswordEncryption.hash("patient123")+"','PATIENT')");

            st.execute("INSERT INTO departments (depName) VALUES ('Cardiology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Pediatrics')");
            st.execute("INSERT INTO departments (depName) VALUES ('Emergency')");
            st.execute("INSERT INTO departments (depName) VALUES ('Neurology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Orthopedics')");
            st.execute("INSERT INTO departments (depName) VALUES ('Dermatology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Oncology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Radiology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Psychiatry')");
            st.execute("INSERT INTO departments (depName) VALUES ('ENT')");
            st.execute("INSERT INTO departments (depName) VALUES ('Urology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Gastroenterology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Ophthalmology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Endocrinology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Nephrology')");
            st.execute("INSERT INTO departments (depName) VALUES ('Pulmonology')");
            st.execute("INSERT INTO departments (depName) VALUES ('General Surgery')");

            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Khaled Nour','khaled@hospital.com','doctor123','Cardiology','Available',15000,'Mon-Wed-Fri','8am-4pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Mona Samir','mona@hospital.com','doctor123','Pediatrics','Available',12000,'Sun-Tue-Thu','9am-5pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Ahmed Hassan','ahmed@hospital.com','doctor123','Emergency','Available',18000,'Mon-Tue-Wed','7am-3pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Layla Farouk','layla@hospital.com','doctor123','Neurology','Available',16000,'Sun-Mon-Wed','8am-4pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Omar Sherif','omar@hospital.com','doctor123','Orthopedics','Busy',14000,'Tue-Thu-Sat','10am-6pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Hana Adel','hana@hospital.com','doctor123','Dermatology','Available',11000,'Mon-Wed-Fri','9am-5pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Tarek Mansour','tarek@hospital.com','doctor123','Oncology','Available',20000,'Sun-Tue-Thu','8am-4pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Yasmine Farid','yasmine@hospital.com','doctor123','Radiology','Busy',13000,'Mon-Wed-Sat','7am-3pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Mohamed Sobhy','mohsobhy@hospital.com','doctor123','Cardiology','Available',15000,'Mon-Wed-Thu','8am-5pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Mahmoud Gamal','mahmoud@hospital.com','doctor123','Emergency','In Surgery',17000,'Tue-Thu-Sat','6am-2pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Karim Salah','karim@hospital.com','doctor123','Psychiatry','Available',14500,'Sun-Tue-Thu','9am-5pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Nour ElMadany','nour@hospital.com','doctor123','ENT','Busy',13500,'Mon-Wed-Fri','10am-6pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Salma Nabil','salma@hospital.com','doctor123','Urology','Available',15500,'Tue-Thu-Sat','8am-4pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Mostafa Adel','mostafa@hospital.com','doctor123','Gastroenterology','In Surgery',17500,'Sun-Mon-Wed','7am-3pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Reem Wael','reem@hospital.com','doctor123','Ophthalmology','Available',12500,'Mon-Tue-Thu','9am-5pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Hassan Fathy','hassan@hospital.com','doctor123','Endocrinology','Busy',15000,'Wed-Fri-Sat','8am-4pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Dina Mostafa','dina@hospital.com','doctor123','Nephrology','Available',16500,'Sun-Tue-Thu','10am-6pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Ali Samy','ali@hospital.com','doctor123','Pulmonology','Available',14000,'Mon-Wed-Fri','7am-3pm')");
            st.execute("INSERT INTO doctors (name,email,password,department,status,salary,workingDays,workingHours) VALUES ('Dr. Farah Emad','farah@hospital.com','doctor123','General Surgery','In Surgery',21000,'Tue-Thu-Sat','6am-2pm')");
            int[][] seedRatings={
                    {5,4,5,4,5},
                    {3,4,3,4,3},
                    {5,5,4,5,4},
                    {4,3,4,3,4},
                    {2,3,2,3,2},
                    {5,3,4,5,4},
                    {4,5,4,5,5},
                    {5,4,5,5,4},
                    {3,2,3,2,3},
                    {4,4,5,4,4},
                    {5,5,4,5,5},
                    {3,4,4,3,4},
                    {4,5,5,4,5},
                    {5,4,5,5,4},
                    {4,4,3,4,4},
                    {3,3,4,3,4},
                    {5,5,5,4,5},
                    {4,3,4,4,3},
                    {5,4,5,4,5}
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

            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sara Mohamed',34,'Female','01098765432','Cairo','A+','Cardiology',1,'sara.mohamed@email.com','sara.mohamed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Omar Ali',25,'Male','01012345678','Giza','B+','Orthopedics',2,'omar.ali@email.com','omar.ali')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nour Hassan',45,'Female','01112223344','Alexandria','O+','Neurology',2,'nour.hassan@email.com','nour.hassan')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Karim Adel',30,'Male','01234567890','Cairo','AB+','Emergency',1,'karim.adel@email.com','karim.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Layla Mahmoud',28,'Female','01098001122','Cairo','A-','Dermatology',3,'layla.mahmoud@email.com','layla.mahmoud')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Youssef Tarek',52,'Male','01156789012','Giza','B-','Oncology',2,'youssef.tarek@email.com','youssef.tarek')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Dina Mostafa',38,'Female','01234509876','Cairo','O-','Radiology',3,'dina.mostafa@email.com','dina.mostafa')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohamed Fawzy',28,'Male','01116341931','Alexandria','A+','Cardiology',3,'mfawzy2302@gmail.com','mohamed.fawzy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rania Khaled',29,'Female','01067890123','Cairo','AB-','Neurology',2,'rania.khaled@email.com','rania.khaled')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Amr Saeed',44,'Male','01145678901','Giza','B+','Orthopedics',2,'amr.saeed@email.com','amr.saeed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Adam Khaled',7,'Male','01023456789','Cairo','AB-','Pediatrics',3,'adam.khaled@email.com','adam.khaled')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Lina Hassan',4,'Female','01034567890','Giza','B+','Pediatrics',3,'lina.hassan@email.com','lina.hassan')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yara Samir',11,'Female','01045678901','Alexandria','A+','Pediatrics',3,'yara.samir@email.com','yara.samir')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mona Adel',55,'Female','01056789012','Cairo','O+','Cardiology',3,'mona.adel@email.com','mona.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Tarek Nabil',40,'Male','01067891234','Giza','B-','Emergency',1,'tarek.nabil@email.com','tarek.nabil')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hana Sherif',22,'Female','01078901234','Alexandria','A-','Dermatology',3,'hana.sherif@email.com','hana.sherif')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mahmoud Farid',67,'Male','01089012345','Cairo','AB+','Oncology',3,'mahmoud.farid@email.com','mahmoud.farid')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Salma Ibrahim',33,'Female','01090123456','Giza','O-','Radiology',3,'salma.ibrahim@email.com','salma.ibrahim')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Khaled Mostafa',48,'Male','01001234567','Cairo','A+','Neurology',3,'khaled.mostafa@email.com','khaled.mostafa')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nadia Gamal',36,'Female','01011234567','Alexandria','B+','Orthopedics',3,'nadia.gamal@email.com','nadia.gamal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Fares Mahmoud',9,'Male','01021234567','Cairo','O+','Pediatrics',3,'fares.mahmoud@email.com','fares.mahmoud')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rana Tarek',27,'Female','01031234567','Giza','AB-','Dermatology',3,'rana.tarek@email.com','rana.tarek')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Salma Abdelkhalek',19,'Female','01012778327','Alexandria','O-','Radiology',3,'Salmaabdelkhalek26@gmail.com','salma.abdelkhalek')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Salma Abdelsattar',19,'Female','01115119117','Alexandria','A+','Neurology',3,'engsalmaabdelsattar22108@gmail.com','salma.abdelsattar')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Zeina Yasser',19,'Female','01012127367','Alexandria','B+','Orthopedics',3,'zeinafarghaly@gmail.com','zeina.yasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohamed Mazen',18,'Male','01558887707','Alexandria','O+','Oncology',3,'m.mazenelagamy@gmail.com','Mohamed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mariam ElShafie',18,'Female','01009649895','Alexandria','AB-','Dermatology',3,'m.a.elshafei2007@gmail.com','mariam.elshafie')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ahmed Samy',31,'Male','01100001001','Cairo','A+','Cardiology',2,'ahmed.samy@email.com','ahmed.samy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Fatma Nour',42,'Female','01100001002','Giza','B+','Oncology',2,'fatma.nour@email.com','fatma.nour')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hassan Ali',58,'Male','01100001003','Alexandria','O+','Neurology',1,'hassan.ali@email.com','hassan.ali')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Noha Ramadan',26,'Female','01100001004','Cairo','AB+','Dermatology',3,'noha.ramadan@email.com','noha.ramadan')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sherif Kamal',47,'Male','01100001005','Giza','A-','Emergency',1,'sherif.kamal@email.com','sherif.kamal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yasmine Samir',23,'Female','01100001006','Alexandria','B-','Radiology',3,'yasmine.samir@email.com','yasmine.samir')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Walid Fathy',53,'Male','01100001007','Cairo','O-','Orthopedics',2,'walid.fathy@email.com','walid.fathy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Iman Lotfy',39,'Female','01100001008','Giza','AB-','Cardiology',2,'iman.lotfy@email.com','iman.lotfy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Tamer Ashraf',35,'Male','01100001009','Alexandria','A+','Neurology',3,'tamer.ashraf@email.com','tamer.ashraf')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Heba Wagdy',30,'Female','01100001010','Cairo','B+','Pediatrics',3,'heba.wagdy@email.com','heba.wagdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sameh Nasser',62,'Male','01100001011','Giza','O+','Oncology',1,'sameh.nasser@email.com','sameh.nasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Doaa Shawky',44,'Female','01100001012','Alexandria','AB+','Radiology',3,'doaa.shawky@email.com','doaa.shawky')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ayman Hamdy',50,'Male','01100001013','Cairo','A-','Emergency',1,'ayman.hamdy@email.com','ayman.hamdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Reham Fouad',37,'Female','01100001014','Giza','B-','Dermatology',3,'reham.fouad@email.com','reham.fouad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mostafa Gamal',29,'Male','01100001015','Alexandria','O-','Orthopedics',2,'mostafa.gamal@email.com','mostafa.gamal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Amira Hossam',21,'Female','01100001016','Cairo','AB-','Cardiology',3,'amira.hossam@email.com','amira.hossam')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hazem Magdy',46,'Male','01100001017','Giza','A+','Neurology',2,'hazem.magdy@email.com','hazem.magdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Passant Emad',32,'Female','01100001018','Alexandria','B+','Oncology',3,'passant.emad@email.com','passant.emad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Kareem Maher',24,'Male','01100001019','Cairo','O+','Pediatrics',3,'kareem.maher@email.com','kareem.maher')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Marwa Galal',41,'Female','01100001020','Giza','AB+','Radiology',2,'marwa.galal@email.com','marwa.galal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Bishoy Rafaat',27,'Male','01100001021','Alexandria','A-','Dermatology',3,'bishoy.rafaat@email.com','bishoy.rafaat')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ghada Saber',56,'Female','01100001022','Cairo','B-','Emergency',1,'ghada.saber@email.com','ghada.saber')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Wael Shehata',43,'Male','01100001023','Giza','O-','Orthopedics',2,'wael.shehata@email.com','wael.shehata')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Randa Hafez',38,'Female','01100001024','Alexandria','AB-','Cardiology',2,'randa.hafez@email.com','randa.hafez')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Samer Hamed',33,'Male','01100001025','Cairo','A+','Neurology',3,'samer.hamed@email.com','samer.hamed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nermeen Saad',49,'Female','01100001026','Giza','B+','Oncology',2,'nermeen.saad@email.com','nermeen.saad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Islam Abdallah',20,'Male','01100001027','Alexandria','O+','Pediatrics',3,'islam.abdallah@email.com','islam.abdallah')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Esraa Helmy',34,'Female','01100001028','Cairo','AB+','Radiology',3,'esraa.helmy@email.com','esraa.helmy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Adel Zaki',60,'Male','01100001029','Giza','A-','Emergency',1,'adel.zaki@email.com','adel.zaki')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mervat Nagi',45,'Female','01100001030','Alexandria','B-','Dermatology',3,'mervat.nagi@email.com','mervat.nagi')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Tarek Salem',51,'Male','01100001031','Cairo','O-','Orthopedics',2,'tarek.salem@email.com','tarek.salem')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Abeer Mansour',28,'Female','01100001032','Giza','AB-','Cardiology',3,'abeer.mansour@email.com','abeer.mansour')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ihab Youssef',36,'Male','01100001033','Alexandria','A+','Neurology',2,'ihab.youssef@email.com','ihab.youssef')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Lobna Fawzy',22,'Female','01100001034','Cairo','B+','Pediatrics',3,'lobna.fawzy@email.com','lobna.fawzy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Osama Ragab',57,'Male','01100001035','Giza','O+','Oncology',1,'osama.ragab@email.com','osama.ragab')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hend Badr',40,'Female','01100001036','Alexandria','AB+','Radiology',3,'hend.badr@email.com','hend.badr')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohsen Atef',48,'Male','01100001037','Cairo','A-','Emergency',1,'mohsen.atef@email.com','mohsen.atef')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Samar Wafaa',31,'Female','01100001038','Giza','B-','Dermatology',3,'samar.wafaa@email.com','samar.wafaa')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Amgad Sobhy',26,'Male','01100001039','Alexandria','O-','Orthopedics',3,'amgad.sobhy@email.com','amgad.sobhy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Shaimaa Reda',35,'Female','01100001040','Cairo','AB-','Cardiology',2,'shaimaa.reda@email.com','shaimaa.reda')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hossam Eldin',54,'Male','01100001041','Giza','A+','Neurology',2,'hossam.eldin@email.com','hossam.eldin')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yasmin Hamza',29,'Female','01100001042','Alexandria','B+','Oncology',3,'yasmin.hamza@email.com','yasmin.hamza')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mahmoud Soliman',6,'Male','01100001043','Cairo','O+','Pediatrics',3,'mahmoud.soliman@email.com','mahmoud.soliman')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Dalia Nabil',43,'Female','01100001044','Giza','AB+','Radiology',2,'dalia.nabil@email.com','dalia.nabil')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Fady Girgis',37,'Male','01100001045','Alexandria','A-','Emergency',1,'fady.girgis@email.com','fady.girgis')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Aya Osman',25,'Female','01100001046','Cairo','B-','Dermatology',3,'aya.osman@email.com','aya.osman')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ramadan Hassan',64,'Male','01100001047','Giza','O-','Orthopedics',2,'ramadan.hassan@email.com','ramadan.hassan')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hala Mohsen',50,'Female','01100001048','Alexandria','AB-','Cardiology',2,'hala.mohsen@email.com','hala.mohsen')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Alaa Badawy',32,'Male','01100001049','Cairo','A+','Neurology',3,'alaa.badawy@email.com','alaa.badawy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Maha Abdelhady',46,'Female','01100001050','Giza','B+','Oncology',2,'maha.abdelhady@email.com','maha.abdelhady')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Basel Adly',13,'Male','01100001051','Alexandria','O+','Pediatrics',3,'basel.adly@email.com','basel.adly')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Reem Hamdy',27,'Female','01100001052','Cairo','AB+','Radiology',3,'reem.hamdy@email.com','reem.hamdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Gamal Kamel',59,'Male','01100001053','Giza','A-','Emergency',1,'gamal.kamel@email.com','gamal.kamel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Noura Elgendi',33,'Female','01100001054','Alexandria','B-','Dermatology',3,'noura.elgendi@email.com','noura.elgendi')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Adham Wahby',41,'Male','01100001055','Cairo','O-','Orthopedics',2,'adham.wahby@email.com','adham.wahby')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nada Monir',24,'Female','01100001056','Giza','AB-','Cardiology',3,'nada.monir@email.com','nada.monir')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Khaled Anwar',55,'Male','01100001057','Alexandria','A+','Neurology',2,'khaled.anwar@email.com','khaled.anwar')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hadeer Shalaby',38,'Female','01100001058','Cairo','B+','Oncology',3,'hadeer.shalaby@email.com','hadeer.shalaby')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ziad Shaker',8,'Male','01100001059','Giza','O+','Pediatrics',3,'ziad.shaker@email.com','ziad.shaker')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ola Abdelnasser',47,'Female','01100001060','Alexandria','AB+','Radiology',2,'ola.abdelnasser@email.com','ola.abdelnasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sherif Mansour',44,'Male','01100001061','Cairo','A-','Emergency',1,'sherif.mansour@email.com','sherif.mansour')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Dina Kamal',20,'Female','01100001062','Giza','B-','Dermatology',3,'dina.kamal@email.com','dina.kamal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Medhat Fahmy',63,'Male','01100001063','Alexandria','O-','Orthopedics',2,'medhat.fahmy@email.com','medhat.fahmy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sahar Elnaggar',36,'Female','01100001064','Cairo','AB-','Cardiology',2,'sahar.elnaggar@email.com','sahar.elnaggar')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nader Younis',30,'Male','01100001065','Giza','A+','Neurology',3,'nader.younis@email.com','nader.younis')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ghada Abdou',52,'Female','01100001066','Alexandria','B+','Oncology',2,'ghada.abdou@email.com','ghada.abdou')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Seif Elislam',10,'Male','01100001067','Cairo','O+','Pediatrics',3,'seif.elislam@email.com','seif.elislam')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Azza Elsherbiny',42,'Female','01100001068','Giza','AB+','Radiology',3,'azza.elsherbiny@email.com','azza.elsherbiny')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hesham Ezz',49,'Male','01100001069','Alexandria','A-','Emergency',1,'hesham.ezz@email.com','hesham.ezz')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Basma Tawfik',26,'Female','01100001070','Cairo','B-','Dermatology',3,'basma.tawfik@email.com','basma.tawfik')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Emad Elhosieny',53,'Male','01100001071','Giza','O-','Orthopedics',2,'emad.elhosieny@email.com','emad.elhosieny')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rasha Bakry',31,'Female','01100001072','Alexandria','AB-','Cardiology',3,'rasha.bakry@email.com','rasha.bakry')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Abdelrahman Samy',23,'Male','01100001073','Cairo','A+','Neurology',3,'abdelrahman.samy@email.com','abdelrahman.samy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Manar Eldeeb',45,'Female','01100001074','Giza','B+','Oncology',2,'manar.eldeeb@email.com','manar.eldeeb')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Youssef Elsebaei',5,'Male','01100001075','Alexandria','O+','Pediatrics',3,'youssef.elsebaei@email.com','youssef.elsebaei')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sherin Fouad',39,'Female','01100001076','Cairo','AB+','Radiology',3,'sherin.fouad@email.com','sherin.fouad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yehia Badran',66,'Male','01100001077','Giza','A-','Emergency',1,'yehia.badran@email.com','yehia.badran')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Eman Rizk',28,'Female','01100001078','Alexandria','B-','Dermatology',3,'eman.rizk@email.com','eman.rizk')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sherif Zohdy',57,'Male','01100001079','Cairo','O-','Orthopedics',2,'sherif.zohdy@email.com','sherif.zohdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hend Elsayed',34,'Female','01100001080','Giza','AB-','Cardiology',2,'hend.elsayed@email.com','hend.elsayed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Kamal Elgohary',61,'Male','01100001081','Alexandria','A+','Neurology',2,'kamal.elgohary@email.com','kamal.elgohary')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Salwa Elkholy',48,'Female','01100001082','Cairo','B+','Oncology',2,'salwa.elkholy@email.com','salwa.elkholy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nabil Elmasry',12,'Male','01100001083','Giza','O+','Pediatrics',3,'nabil.elmasry@email.com','nabil.elmasry')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Inas Elnabawy',37,'Female','01100001084','Alexandria','AB+','Radiology',3,'inas.elnabawy@email.com','inas.elnabawy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mahmoud Elfiky',43,'Male','01100001085','Cairo','A-','Emergency',1,'mahmoud.elfiky@email.com','mahmoud.elfiky')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Zeinab Elrafei',22,'Female','01100001086','Giza','B-','Dermatology',3,'zeinab.elrafei@email.com','zeinab.elrafei')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sameh Elsharkawy',40,'Male','01100001087','Alexandria','O-','Orthopedics',2,'sameh.elsharkawy@email.com','sameh.elsharkawy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hoda Elgazar',55,'Female','01100001088','Cairo','AB-','Cardiology',2,'hoda.elgazar@email.com','hoda.elgazar')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ibrahim Elbanna',29,'Male','01100001089','Giza','A+','Neurology',3,'ibrahim.elbanna@email.com','ibrahim.elbanna')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Wafaa Elbialy',50,'Female','01100001090','Alexandria','B+','Oncology',2,'wafaa.elbialy@email.com','wafaa.elbialy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hazem Elboraei',3,'Male','01100001091','Cairo','O+','Pediatrics',3,'hazem.elboraei@email.com','hazem.elboraei')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Amal Elhusseiny',46,'Female','01100001092','Giza','AB+','Radiology',3,'amal.elhusseiny@email.com','amal.elhusseiny')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Maged Elewa',68,'Male','01100001093','Alexandria','A-','Emergency',1,'maged.elewa@email.com','maged.elewa')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nour Elfadl',24,'Female','01100001094','Cairo','B-','Dermatology',3,'nour.elfadl@email.com','nour.elfadl')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Tarek Elgammal',35,'Male','01100001095','Giza','O-','Orthopedics',2,'tarek.elgammal@email.com','tarek.elgammal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rehab Elhadad',41,'Female','01100001096','Alexandria','AB-','Cardiology',2,'rehab.elhadad@email.com','rehab.elhadad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Omar Elhamy',19,'Male','01100001097','Cairo','A+','Pediatrics',3,'omar.elhamy@email.com','omar.elhamy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mona Elhariry',58,'Female','01100001098','Giza','B+','Oncology',1,'mona.elhariry@email.com','mona.elhariry')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ramy Elnems',33,'Male','01100001099','Alexandria','O+','Neurology',3,'ramy.elnems@email.com','ramy.elnems')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Safia Elrashidy',62,'Female','01100001100','Cairo','AB+','Radiology',2,'safia.elrashidy@email.com','safia.elrashidy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Youssef Abdelrahman',27,'Male','01100001101','Alexandria','A+','Cardiology',2,'youssef.abdelrahman@email.com','youssef.abdelrahman')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Fatma Mahmoud',31,'Female','01100001102','Cairo','O+','Dermatology',3,'fatma.mahmoud@email.com','fatma.mahmoud')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ahmed Samir',45,'Male','01100001103','Giza','B+','Orthopedics',2,'ahmed.samir@email.com','ahmed.samir')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nourhan Ali',23,'Female','01100001104','Mansoura','AB+','Radiology',3,'nourhan.ali@email.com','nourhan.ali')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mostafa Hassan',52,'Male','01100001105','Alexandria','O-','Neurology',2,'mostafa.hassan@email.com','mostafa.hassan')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Salma Adel',18,'Female','01100001106','Tanta','A-','Pediatrics',3,'salma.adel@email.com','salma.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Karim Nasser',39,'Male','01100001107','Cairo','B-','General Surgery',2,'karim.nasser@email.com','karim.nasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Heba Fathy',47,'Female','01100001108','Alexandria','AB-','Oncology',2,'heba.fathy@email.com','heba.fathy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mahmoud Atef',63,'Male','01100001109','Giza','A+','Emergency',1,'mahmoud.atef@email.com','mahmoud.atef')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rana Mohamed',29,'Female','01100001110','Luxor','O+','ENT',3,'rana.mohamed@email.com','rana.mohamed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Amr Khaled',34,'Male','01100001111','Aswan','B+','Urology',2,'amr.khaled@email.com','amr.khaled')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Aya Tarek',25,'Female','01100001112','Port Said','A+','Cardiology',3,'aya.tarek@email.com','aya.tarek')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohamed Hegazy',56,'Male','01100001113','Ismailia','AB+','Neurology',2,'mohamed.hegazy@email.com','mohamed.hegazy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mariam Wael',21,'Female','01100001114','Alexandria','O-','Dermatology',3,'mariam.wael@email.com','mariam.wael')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sherif Hamdy',49,'Male','01100001115','Cairo','A-','Orthopedics',2,'sherif.hamdy@email.com','sherif.hamdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nesma Essam',37,'Female','01100001116','Damietta','B+','Radiology',2,'nesma.essam@email.com','nesma.essam')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Islam Salah',42,'Male','01100001117','Mansoura','O+','Oncology',2,'islam.salah@email.com','islam.salah')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Doaa Ibrahim',60,'Female','01100001118','Cairo','AB-','Emergency',1,'doaa.ibrahim@email.com','doaa.ibrahim')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Tamer Magdy',33,'Male','01100001119','Alexandria','B-','General Surgery',2,'tamer.magdy@email.com','tamer.magdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Farah Ashraf',15,'Female','01100001120','Cairo','A+','Pediatrics',3,'farah.ashraf@email.com','farah.ashraf')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Omar Fawzy',28,'Male','01100001121','Giza','O+','ENT',3,'omar.fawzy@email.com','omar.fawzy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yara Hossam',32,'Female','01100001122','Alexandria','B+','Cardiology',2,'yara.hossam@email.com','yara.hossam')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ayman Ragab',54,'Male','01100001123','Tanta','AB+','Neurology',2,'ayman.ragab@email.com','ayman.ragab')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Dina Salah',26,'Female','01100001124','Cairo','A-','Dermatology',3,'dina.salah@email.com','dina.salah')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Walid Naguib',67,'Male','01100001125','Alexandria','O-','Emergency',1,'walid.naguib@email.com','walid.naguib')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Reem Adel',38,'Female','01100001126','Giza','AB+','Radiology',2,'reem.adel@email.com','reem.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hossam Mostafa',43,'Male','01100001127','Cairo','B+','Orthopedics',2,'hossam.mostafa@email.com','hossam.mostafa')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nada Gamal',24,'Female','01100001128','Alexandria','O+','ENT',3,'nada.gamal@email.com','nada.gamal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Khaled Saber',58,'Male','01100001129','Mansoura','A+','Oncology',2,'khaled.saber@email.com','khaled.saber')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Habiba Yasser',20,'Female','01100001130','Cairo','B-','Dermatology',3,'habiba.yasser@email.com','habiba.yasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Adham Nasser',32,'Male','01048297153','Alexandria','A+','Cardiology',2,'adham.nasser@email.com','adham.nasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mariam Fathy',24,'Female','01176529481','Cairo','O+','Dermatology',3,'mariam.fathy@email.com','mariam.fathy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sherif Ashraf',57,'Male','01243871596','Giza','B-','Orthopedics',2,'sherif.ashraf@email.com','sherif.ashraf')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yasmine Tarek',19,'Female','01582134765','Alexandria','AB+','Pediatrics',3,'yasmine.tarek@email.com','yasmine.tarek')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohamed Hany',41,'Male','01134852679','Cairo','O-','Neurology',2,'mohamed.hany@email.com','mohamed.hany')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rana Wael',28,'Female','01093475218','Giza','A-','Radiology',3,'rana.wael@email.com','rana.wael')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ahmed Reda',65,'Male','01274618395','Alexandria','AB-','Emergency',1,'ahmed.reda@email.com','ahmed.reda')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Farah Samy',35,'Female','01548391267','Cairo','B+','Oncology',2,'farah.samy@email.com','farah.samy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Kareem Mostafa',27,'Male','01192753486','Alexandria','O+','Dermatology',3,'kareem.mostafa@email.com','kareem.mostafa')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nour Ibrahim',49,'Female','01067528149','Giza','A+','Cardiology',2,'nour.ibrahim@email.com','nour.ibrahim')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Tamer Saad',54,'Male','01219834756','Cairo','B+','Neurology',2,'tamer.saad@email.com','tamer.saad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Salma Hassan',12,'Female','01567483291','Alexandria','O+','Pediatrics',3,'salma.hassan@email.com','salma.hassan')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hossam Galal',45,'Male','01029384715','Giza','AB+','Radiology',2,'hossam.galal@email.com','hossam.galal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Menna Adel',31,'Female','01153472869','Cairo','A-','Oncology',2,'menna.adel@email.com','menna.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Wael Nabil',60,'Male','01286371945','Alexandria','O-','Emergency',1,'wael.nabil@email.com','wael.nabil')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Heba Emad',26,'Female','01537649821','Giza','B-','Dermatology',3,'heba.emad@email.com','heba.emad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Youssef Salah',37,'Male','01184276513','Cairo','A+','Orthopedics',2,'youssef.salah@email.com','youssef.salah')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Aya Mahmoud',23,'Female','01058492176','Alexandria','AB-','Radiology',3,'aya.mahmoud@email.com','aya.mahmoud')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Maher Fawzy',68,'Male','01293578164','Cairo','B+','Cardiology',2,'maher.fawzy@email.com','maher.fawzy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Doaa Khaled',34,'Female','01529481637','Giza','O+','Neurology',3,'doaa.khaled@email.com','doaa.khaled')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Bassem Ezz',51,'Male','01071836492','Alexandria','A-','Oncology',2,'bassem.ezz@email.com','bassem.ezz')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nadine Sherif',22,'Female','01165329784','Cairo','AB+','Dermatology',3,'nadine.sherif@email.com','nadine.sherif')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hazem Ragab',46,'Male','01257483916','Giza','O+','Orthopedics',2,'hazem.ragab@email.com','hazem.ragab')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Reem Atef',29,'Female','01591843726','Alexandria','B+','Radiology',3,'reem.atef@email.com','reem.atef')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Omar Gamal',39,'Male','01148736295','Cairo','A+','Cardiology',2,'omar.gamal@email.com','omar.gamal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hana Magdy',17,'Female','01083524679','Giza','O-','Pediatrics',3,'hana.magdy@email.com','hana.magdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Islam Anwar',56,'Male','01264971385','Alexandria','AB-','Emergency',1,'islam.anwar@email.com','islam.anwar')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Dalia Sobhy',43,'Female','01547268193','Cairo','B-','Oncology',2,'dalia.sobhy@email.com','dalia.sobhy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Amr Yasser',33,'Male','01127593864','Giza','O+','Neurology',3,'amr.yasser@email.com','amr.yasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Passant Ali',25,'Female','01094673182','Alexandria','A+','Dermatology',3,'passant.ali@email.com','passant.ali')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mahmoud Sherif',47,'Male','01184629573','Alexandria','O+','Orthopedics',2,'mahmoud.sherif@email.com','mahmoud.sherif')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nourhan Hossam',21,'Female','01053827194','Cairo','A+','Dermatology',3,'nourhan.hossam@email.com','nourhan.hossam')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ahmed Naguib',58,'Male','01547293618','Giza','B+','Cardiology',2,'ahmed.naguib@email.com','ahmed.naguib')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Salma Wael',14,'Female','01263847519','Alexandria','AB-','Pediatrics',3,'salma.wael@email.com','salma.wael')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hany Mahmoud',63,'Male','01139572846','Cairo','A-','Emergency',1,'hany.mahmoud@email.com','hany.mahmoud')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rania Adel',36,'Female','01072486135','Giza','O-','Radiology',2,'rania.adel@email.com','rania.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Karim Ehab',28,'Male','01258731469','Alexandria','B-','Neurology',3,'karim.ehab@email.com','karim.ehab')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Heba Samy',44,'Female','01568349275','Cairo','AB+','Oncology',2,'heba.samy@email.com','heba.samy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Youssef Adel',31,'Male','01081645392','Alexandria','A+','Cardiology',3,'youssef.adel@email.com','youssef.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Dina Fathy',27,'Female','01126495837','Giza','O+','Dermatology',3,'dina.fathy@email.com','dina.fathy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Amr Saber',53,'Male','01297145368','Cairo','AB-','Orthopedics',2,'amr.saber@email.com','amr.saber')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mona Khalil',48,'Female','01534591826','Alexandria','B+','Oncology',2,'mona.khalil@email.com','mona.khalil')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Tamer Saeed',40,'Male','01029376184','Giza','A+','Neurology',2,'tamer.saeed@email.com','tamer.saeed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Farah Ashour',19,'Female','01164839215','Cairo','O-','Radiology',3,'farah.ashour@email.com','farah.ashour')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mostafa Ragab',66,'Male','01248396571','Alexandria','AB+','Emergency',1,'mostafa.ragab@email.com','mostafa.ragab')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yara Gamal',30,'Female','01592647183','Giza','B-','Cardiology',2,'yara.gamal@email.com','yara.gamal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sherif Ibrahim',35,'Male','01175382649','Cairo','O+','Orthopedics',2,'sherif.ibrahim@email.com','sherif.ibrahim')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Aya Nasser',26,'Female','01058263974','Alexandria','A-','Dermatology',3,'aya.nasser@email.com','aya.nasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Khaled Hassan',55,'Male','01269473815','Giza','B+','Oncology',2,'khaled.hassan@email.com','khaled.hassan')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Reem Mohamed',33,'Female','01583174692','Cairo','AB-','Radiology',2,'reem.mohamed@email.com','reem.mohamed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Wael Emad',61,'Male','01147829635','Alexandria','O-','Emergency',1,'wael.emad@email.com','wael.emad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Menna Tarek',22,'Female','01039482657','Giza','A+','Pediatrics',3,'menna.tarek@email.com','menna.tarek')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Bassem Adel',46,'Male','01285719463','Cairo','B-','Neurology',2,'bassem.adel@email.com','bassem.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hagar Samir',37,'Female','01527496381','Alexandria','O+','Cardiology',2,'hagar.samir@email.com','hagar.samir')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Omar Fouad',29,'Male','01156294837','Giza','AB+','Dermatology',3,'omar.fouad@email.com','omar.fouad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Esraa Magdy',41,'Female','01081729364','Cairo','A-','Oncology',2,'esraa.magdy@email.com','esraa.magdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hesham Farouk',50,'Male','01293617482','Alexandria','B+','Orthopedics',2,'hesham.farouk@email.com','hesham.farouk')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nada Ashraf',18,'Female','01548361725','Giza','O+','Pediatrics',3,'nada.ashraf@email.com','nada.ashraf')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ayman Lotfy',59,'Male','01192836475','Cairo','AB-','Radiology',2,'ayman.lotfy@email.com','ayman.lotfy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Passant Sherif',27,'Female','01027594168','Alexandria','A+','Neurology',3,'passant.sherif@email.com','passant.sherif')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mina George',34,'Male','01068473195','Alexandria','A+','Cardiology',2,'mina.george@email.com','mina.george')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Habiba Ahmed',22,'Female','01592736481','Cairo','O+','Dermatology',3,'habiba.ahmed@email.com','habiba.ahmed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Bishoy Nabil',41,'Male','01247691835','Giza','AB-','Orthopedics',2,'bishoy.nabil@email.com','bishoy.nabil')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Malak Wael',16,'Female','01183572469','Alexandria','B+','Pediatrics',3,'malak.wael@email.com','malak.wael')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Marwan Samir',52,'Male','01025948713','Cairo','O-','Neurology',2,'marwan.samir@email.com','marwan.samir')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Jana Khaled',28,'Female','01291847365','Giza','A-','Radiology',3,'jana.khaled@email.com','jana.khaled')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mina Youssef',61,'Male','01534672891','Alexandria','AB+','Emergency',1,'mina.youssef@email.com','mina.youssef')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Shahd Mostafa',30,'Female','01164298573','Cairo','B-','Oncology',2,'shahd.mostafa@email.com','shahd.mostafa')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yehia Adel',26,'Male','01087264935','Alexandria','O+','Dermatology',3,'yehia.adel@email.com','yehia.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Raghad Mohamed',20,'Female','01556371842','Giza','A+','Cardiology',3,'raghad.mohamed@email.com','raghad.mohamed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Kerolos Atef',48,'Male','01238745916','Cairo','B+','Neurology',2,'kerolos.atef@email.com','kerolos.atef')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Lujain Hossam',9,'Female','01073418592','Alexandria','O-','Pediatrics',3,'lujain.hossam@email.com','lujain.hossam')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Bassem Farid',57,'Male','01145892673','Giza','AB-','Orthopedics',2,'bassem.farid@email.com','bassem.farid')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nadine George',35,'Female','01529486731','Cairo','A-','Radiology',2,'nadine.george@email.com','nadine.george')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Fady Mikhail',65,'Male','01263571948','Alexandria','O+','Emergency',1,'fady.mikhail@email.com','fady.mikhail')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Maha Gamal',39,'Female','01098723461','Giza','B+','Oncology',2,'maha.gamal@email.com','maha.gamal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Aly Tamer',24,'Male','01176418352','Cairo','A+','Cardiology',3,'aly.tamer@email.com','aly.tamer')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sandra Nader',31,'Female','01583746129','Alexandria','AB+','Dermatology',3,'sandra.nader@email.com','sandra.nader')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Maged Shaker',59,'Male','01254973861','Giza','O-','Orthopedics',2,'maged.shaker@email.com','maged.shaker')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rehab Naguib',46,'Female','01036498175','Cairo','A+','Radiology',2,'rehab.naguib@email.com','rehab.naguib')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Peter Adel',42,'Male','01152984617','Alexandria','B-','Neurology',2,'peter.adel@email.com','peter.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mennatallah Ali',27,'Female','01591862437','Giza','O+','Dermatology',3,'mennatallah.ali@email.com','mennatallah.ali')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Shady Ramy',36,'Male','01247193658','Cairo','AB-','Cardiology',2,'shady.ramy@email.com','shady.ramy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rita Magdy',25,'Female','01028674195','Alexandria','A-','Oncology',3,'rita.magdy@email.com','rita.magdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohab Hatem',50,'Male','01184652973','Giza','O+','Emergency',1,'mohab.hatem@email.com','mohab.hatem')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Lama Sherif',13,'Female','01562498713','Cairo','B+','Pediatrics',3,'lama.sherif@email.com','lama.sherif')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('John Nader',55,'Male','01283716495','Alexandria','A+','Orthopedics',2,'john.nader@email.com','john.nader')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Farida Saad',33,'Female','01047389162','Giza','AB+','Radiology',2,'farida.saad@email.com','farida.saad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Seif Ahmed',18,'Male','01197364528','Cairo','O-','Pediatrics',3,'seif.ahmed@email.com','seif.ahmed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Marina Fawzy',29,'Female','01573846195','Alexandria','B-','Dermatology',3,'marina.fawzy@email.com','marina.fawzy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Bassel Fathy',38,'Male','01162849715','Alexandria','A+','Cardiology',2,'bassel.fathy@email.com','bassel.fathy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Habiba Khalil',20,'Female','01047385192','Cairo','O+','Dermatology',3,'habiba.khalil@email.com','habiba.khalil')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Kerolos Mikhail',45,'Male','01298531764','Giza','B+','Orthopedics',2,'kerolos.mikhail@email.com','kerolos.mikhail')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Malak Sherif',17,'Female','01573942618','Alexandria','AB-','Pediatrics',3,'malak.sherif@email.com','malak.sherif')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yehia Khaled',54,'Male','01158274639','Cairo','O-','Neurology',2,'yehia.khaled@email.com','yehia.khaled')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Shahd Adel',28,'Female','01092648371','Giza','A-','Radiology',3,'shahd.adel@email.com','shahd.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('George Samir',63,'Male','01241687539','Alexandria','AB+','Emergency',1,'george.samir@email.com','george.samir')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nadine Ashraf',35,'Female','01586421935','Cairo','B-','Oncology',2,'nadine.ashraf@email.com','nadine.ashraf')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohab Nasser',31,'Male','01173495826','Alexandria','O+','Dermatology',3,'mohab.nasser@email.com','mohab.nasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Farah Ibrahim',22,'Female','01065192743','Giza','A+','Cardiology',3,'farah.ibrahim@email.com','farah.ibrahim')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Fady Aziz',57,'Male','01259317486','Cairo','B+','Neurology',2,'fady.aziz@email.com','fady.aziz')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Lina Magdy',10,'Female','01527846391','Alexandria','O-','Pediatrics',3,'lina.magdy@email.com','lina.magdy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sameh Tarek',48,'Male','01184739152','Giza','AB+','Radiology',2,'sameh.tarek@email.com','sameh.tarek')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Aya Saber',30,'Female','01038274615','Cairo','A-','Oncology',2,'aya.saber@email.com','aya.saber')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mina Fawzy',69,'Male','01296487135','Alexandria','O+','Emergency',1,'mina.fawzy@email.com','mina.fawzy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Esraa Mostafa',26,'Female','01561748392','Giza','B-','Dermatology',3,'esraa.mostafa@email.com','esraa.mostafa')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Bishoy Emad',40,'Male','01129487653','Cairo','A+','Orthopedics',2,'bishoy.emad@email.com','bishoy.emad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rahma Hany',23,'Female','01083926417','Alexandria','AB-','Radiology',3,'rahma.hany@email.com','rahma.hany')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Aly Reda',60,'Male','01245873916','Giza','B+','Cardiology',2,'aly.reda@email.com','aly.reda')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mariam Atef',32,'Female','01594281637','Cairo','O+','Neurology',3,'mariam.atef@email.com','mariam.atef')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Peter Hany',51,'Male','01167543892','Alexandria','A-','Oncology',2,'peter.hany@email.com','peter.hany')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Jana Fathy',20,'Female','01029461875','Giza','AB+','Dermatology',3,'jana.fathy@email.com','jana.fathy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Wael Ashraf',43,'Male','01287635149','Cairo','O-','Orthopedics',2,'wael.ashraf@email.com','wael.ashraf')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Marina Nader',36,'Female','01538462917','Alexandria','B+','Radiology',2,'marina.nader@email.com','marina.nader')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sherif Galal',56,'Male','01195648273','Giza','A+','Emergency',1,'sherif.galal@email.com','sherif.galal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nada Adel',27,'Female','01054723896','Cairo','O+','Cardiology',3,'nada.adel@email.com','nada.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('John Ibrahim',46,'Male','01261497385','Alexandria','AB-','Neurology',2,'john.ibrahim@email.com','john.ibrahim')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Menna Wael',18,'Female','01573826495','Giza','B-','Pediatrics',3,'menna.wael@email.com','menna.wael')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Karim Lotfy',34,'Male','01148729563','Cairo','O+','Oncology',2,'karim.lotfy@email.com','karim.lotfy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sandra Rafik',29,'Female','01087431526','Alexandria','A+','Dermatology',3,'sandra.rafik@email.com','sandra.rafik')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohamed Saif',37,'Male','01158294763','Alexandria','A+','Cardiology',2,'mohamed.saif@email.com','mohamed.saif')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Habiba Essam',25,'Female','01073619485','Cairo','O+','Dermatology',3,'habiba.essam@email.com','habiba.essam')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Boulos Nader',58,'Male','01264983571','Giza','AB-','Orthopedics',2,'boulos.nader@email.com','boulos.nader')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rawan Adel',18,'Female','01538472961','Alexandria','B+','Pediatrics',3,'rawan.adel@email.com','rawan.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ahmed Maher',43,'Male','01195824736','Cairo','O-','Neurology',2,'ahmed.maher@email.com','ahmed.maher')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Judy George',30,'Female','01082746395','Giza','A-','Radiology',2,'judy.george@email.com','judy.george')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Fadi Aziz',67,'Male','01258371649','Alexandria','AB+','Emergency',1,'fadi.aziz@email.com','fadi.aziz')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nada Emad',34,'Female','01564927381','Cairo','B-','Oncology',2,'nada.emad@email.com','nada.emad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Marwan Hatem',28,'Male','01147296583','Alexandria','O+','Dermatology',3,'marwan.hatem@email.com','marwan.hatem')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Yara Sameh',22,'Female','01059381427','Giza','A+','Cardiology',3,'yara.sameh@email.com','yara.sameh')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mina Fakhry',49,'Male','01274831695','Cairo','B+','Neurology',2,'mina.fakhry@email.com','mina.fakhry')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Hana Wael',11,'Female','01523769481','Alexandria','O-','Pediatrics',3,'hana.wael@email.com','hana.wael')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Khaled Farid',54,'Male','01168392547','Giza','AB+','Orthopedics',2,'khaled.farid@email.com','khaled.farid')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Marina Adel',32,'Female','01048195736','Cairo','A-','Radiology',2,'marina.adel@email.com','marina.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ayman Nabil',62,'Male','01291538476','Alexandria','O+','Emergency',1,'ayman.nabil@email.com','ayman.nabil')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Salma Hossam',27,'Female','01576492835','Giza','B+','Dermatology',3,'salma.hossam@email.com','salma.hossam')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Peter Mikhail',40,'Male','01152847369','Cairo','A+','Cardiology',2,'peter.mikhail@email.com','peter.mikhail')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Farida Mahmoud',23,'Female','01097361482','Alexandria','AB-','Radiology',3,'farida.mahmoud@email.com','farida.mahmoud')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Omar Hany',46,'Male','01239681475','Giza','B-','Oncology',2,'omar.hany@email.com','omar.hany')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Reem George',29,'Female','01542869317','Cairo','O+','Neurology',3,'reem.george@email.com','reem.george')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Tamer Fouad',51,'Male','01173495628','Alexandria','A-','Orthopedics',2,'tamer.fouad@email.com','tamer.fouad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mennatallah Yasser',26,'Female','01028496731','Cairo','B+','Cardiology',3,'mennatallah.yasser@email.com','mennatallah.yasser')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('George Fawzy',60,'Male','01264817539','Giza','O-','Emergency',1,'george.fawzy@email.com','george.fawzy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Malak Ashraf',20,'Female','01587362941','Alexandria','AB+','Pediatrics',3,'malak.ashraf@email.com','malak.ashraf')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sherif Tarek',39,'Male','01149632587','Cairo','A+','Neurology',2,'sherif.tarek@email.com','sherif.tarek')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sandra Samir',35,'Female','01085173642','Giza','O+','Dermatology',3,'sandra.samir@email.com','sandra.samir')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohab Aziz',44,'Male','01271593846','Alexandria','B-','Oncology',2,'mohab.aziz@email.com','mohab.aziz')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Habiba Gamal',24,'Female','01562841795','Cairo','A-','Radiology',3,'habiba.gamal@email.com','habiba.gamal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('John Naguib',57,'Male','01197384625','Alexandria','AB-','Cardiology',2,'john.naguib@email.com','john.naguib')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Rahma Ali',16,'Female','01039486172','Giza','O+','Pediatrics',3,'rahma.ali@email.com','rahma.ali')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Wael Saad',53,'Male','01258963417','Cairo','B+','Orthopedics',2,'wael.saad@email.com','wael.saad')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nour George',31,'Female','01546182973','Alexandria','A+','Dermatology',3,'nour.george@email.com','nour.george')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Bishoy Adel',48,'Male','01184527936','Giza','O-','Neurology',2,'bishoy.adel@email.com','bishoy.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mostafa Shoubeir',27,'Male','01062847195','Cairo','AB+','Oncology',2,'oufa.shoubeir@email.com','mostafa.shoubeir')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Karim Saber',33,'Male','01293158467','Alexandria','B+','Cardiology',2,'karim.saber@email.com','karim.saber')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Jana Nader',21,'Female','01578264319','Giza','A-','Radiology',3,'jana.nader@email.com','jana.nader')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Maged Hatem',65,'Male','01153728649','Cairo','O+','Emergency',1,'maged.hatem@email.com','maged.hatem')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Marian Fathy',28,'Female','01094517283','Alexandria','AB-','Dermatology',3,'marian.fathy@email.com','marian.fathy')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Ahmed Galal',42,'Male','01236487591','Giza','A+','Neurology',2,'ahmed.galal@email.com','ahmed.galal')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Maha Shaker',47,'Female','01569384725','Cairo','B-','Oncology',2,'maha.shaker@email.com','maha.shaker')");

            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('101','ICU','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('102','ICU','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('103','ICU','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('104','ICU','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('201','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('202','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('203','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('204','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('205','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('206','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('207','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('208','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('301','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('302','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('303','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('304','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('305','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('306','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('401','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('402','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('403','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('404','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('405','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('406','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('407','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('408','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('409','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('410','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('501','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('502','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('503','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('504','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('505','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('506','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('507','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('508','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('601','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('602','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('603','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('604','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('605','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('606','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('701','ICU','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('702','ICU','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('703','ICU','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('704','ICU','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('801','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('802','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('803','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('804','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('805','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('806','Emergency','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('901','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('902','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('903','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('904','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('905','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('906','General','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('1001','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('1002','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('1003','Private','Available')");
            st.execute("INSERT INTO rooms (roomNumber,roomtype,status) VALUES ('1004','Private','Available')");

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

            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (1, 1, 'Amlodipine', '5mg', 'Take once daily after meal', '3 months', '2026-05-20')");
            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (2, 5, 'Ibuprofen', '400mg', 'Take twice daily with food', '2 weeks', '2026-05-21')");
            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (3, 4, 'Sumatriptan', '50mg', 'Take at onset of migraine', '1 month', '2026-05-22')");
            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (4, 3, 'Amoxicillin', '500mg', 'Take three times daily', '1 week', '2026-05-22')");
            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (5, 6, 'Hydrocortisone', '1%', 'Apply to affected area twice daily', '2 weeks', '2026-05-23')");
            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (6, 7, 'Methotrexate', '15mg', 'Take once weekly', '6 months', '2026-05-23')");
            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (7, 8, 'Tamsulosin', '0.4mg', 'Take once daily at bedtime', '1 month', '2026-05-24')");
            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (8, 1, 'Metoprolol', '25mg', 'Take twice daily', '3 months', '2026-05-24')");
            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (9, 4, 'Levetiracetam', '500mg', 'Take twice daily', '6 months', '2026-05-10')");
            st.execute("INSERT INTO prescriptions (patientID, doctorID, medicineName, dosage, instructions, duration, dateIssued) VALUES (10, 5, 'Calcium', '500mg', 'Take once daily with vitamin D', '3 months', '2026-05-18')");

            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (1,'Sara Mohamed','Dr. Khaled Nour','Cardiology Consultation',850.00,'Paid','Credit Card','2026-05-20')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (2,'Omar Ali','Dr. Omar Sherif','Orthopedic Examination',600.00,'Partially Paid','Cash','2026-05-21')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (3,'Nour Hassan','Dr. Layla Farouk','Neurology Consultation',750.00,'Paid','Insurance','2026-05-22')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (4,'Karim Adel','Dr. Ahmed Hassan','Emergency Treatment',1200.00,'Overdue','Cash','2026-05-22')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (5,'Layla Mahmoud','Dr. Hana Adel','Dermatology Treatment',450.00,'Paid','Credit Card','2026-05-23')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (6,'Youssef Tarek','Dr. Tarek Mansour','Chemotherapy Session',3500.00,'Partially Paid','Insurance','2026-05-23')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (7,'Dina Mostafa','Dr. Yasmine Farid','Radiology Scan',900.00,'Overdue','Cash','2026-05-24')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (8,'Mohamed Fawzy','Dr. Khaled Nour','ECG and Consultation',700.00,'Paid','Credit Card','2026-05-24')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (9,'Rania Khaled','Dr. Layla Farouk','MRI Scan',1100.00,'Partially Paid','Insurance','2026-05-25')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (10,'Amr Saeed','Dr. Omar Sherif','Surgery Followup',500.00,'Paid','Cash','2026-05-25')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (26,'Mohamed Mazen','Dr. Tarek Mansour','Oncology Consultation',750,'Overdue','Cash','2026-05-22')");
            st.execute("INSERT INTO bills (patientID,patientName,doctorName,service,amount,status,paymentMethod,date) VALUES (26,'Mohamed Mazen','Dr. Tarek Mansour','Lab Tests',300,'Paid','Card','2026-05-20')");

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
                    "email TEXT," +
                    "username TEXT)");

            st.execute("CREATE TABLE IF NOT EXISTS departments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "depName TEXT NOT NULL UNIQUE)");

            st.execute("CREATE TABLE IF NOT EXISTS rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "roomNumber TEXT NOT NULL," +
                    "roomtype TEXT DEFAULT 'General'," +
                    "department TEXT DEFAULT 'General'," +
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
                    "service TEXT NOT NULL," +
                    "amount REAL NOT NULL," +
                    "status TEXT DEFAULT 'Pending'," +
                    "paymentMethod TEXT NOT NULL," +
                    "date TEXT)");

            st.execute("ALTER TABLE bills ADD COLUMN amountPaid REAL DEFAULT 0.0");
        } catch (SQLException ignoredAlter) {}
        try (Statement st = connection.createStatement()) {

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

            st.execute("CREATE TABLE IF NOT EXISTS medications (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "category TEXT," +
                    "form TEXT," +
                    "stock INTEGER DEFAULT 0," +
                    "minStock INTEGER DEFAULT 20," +
                    "unitPrice REAL DEFAULT 0.0," +
                    "expiryDate TEXT," +
                    "status TEXT DEFAULT 'In Stock')");

            st.execute("CREATE TABLE IF NOT EXISTS reorderRequests (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "medicationId INTEGER," +
                    "medicationName TEXT," +
                    "quantityRequested INTEGER," +
                    "supplier TEXT," +
                    "requestDate TEXT," +
                    "expectedDate TEXT," +
                    "status TEXT DEFAULT 'Pending')");

            st.execute("CREATE TABLE IF NOT EXISTS twoFactorCodes (" +
                    "username TEXT PRIMARY KEY," +
                    "code TEXT NOT NULL," +
                    "expiresAt TEXT NOT NULL)");

            st.execute("CREATE TABLE IF NOT EXISTS settings (" +
                    "username TEXT PRIMARY KEY," +
                    "twoFactorEnabled INTEGER DEFAULT 0," +
                    "theme TEXT DEFAULT 'light'," +
                    "language TEXT DEFAULT 'en')");

            st.execute("INSERT OR IGNORE INTO medications (name,category,form,stock,minStock,unitPrice,expiryDate,status) VALUES ('Amoxicillin 500mg','Antibiotic','Capsules',320,50,12.50,'2027-03-15','In Stock')");
            st.execute("INSERT OR IGNORE INTO medications (name,category,form,stock,minStock,unitPrice,expiryDate,status) VALUES ('Metformin 850mg','Diabetes','Tablets',18,50,8.00,'2026-08-20','Low Stock')");
            st.execute("INSERT OR IGNORE INTO medications (name,category,form,stock,minStock,unitPrice,expiryDate,status) VALUES ('Amlodipine 5mg','Cardiology','Tablets',12,40,15.75,'2026-07-01','Expiring Soon')");
            st.execute("INSERT OR IGNORE INTO medications (name,category,form,stock,minStock,unitPrice,expiryDate,status) VALUES ('Omeprazole 20mg','Gastrology','Capsules',0,30,22.00,'2027-11-30','Out of Stock')");
            st.execute("INSERT OR IGNORE INTO medications (name,category,form,stock,minStock,unitPrice,expiryDate,status) VALUES ('Paracetamol 500mg','Analgesic','Tablets',800,100,3.25,'2028-01-10','In Stock')");



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

    public List<Bill> getBillsByPatient(int patientId) {
        List<Bill> result = new ArrayList<>();
        for (Bill b : bills) {
            if (b.getPatientId() == patientId) result.add(b);
        }
        return result;
    }
    public void addBill(Bill bill){
        String sql="INSERT INTO bills(patientID,patientName,doctorName,service,amount,status,paymentMethod) VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement ps= connection.prepareStatement(sql)){
            ps.setInt(1,bill.getPatientId());
            ps.setString(2,bill.getPatientName());
            ps.setString(3,bill.getDoctorName());
            ps.setString(4,bill.getService());
            ps.setDouble(5,bill.getAmount());
            ps.setString(6, bill.getBillStatus());
            ps.setString(7,bill.getPaymentMethod());
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("error adding bill: " + e.getMessage());
        }
    }
    public List<Bill> getAllBills() {
        List<Bill> result = new ArrayList<>();
        String sql="SELECT * FROM bills";
        try(Statement st=connection.createStatement()){
            ResultSet rs= st.executeQuery(sql);
            while (rs.next()){
                result.add(new Bill(
                        rs.getInt("id"),
                        rs.getInt("patientID"),
                        rs.getString("patientName"),
                        rs.getString("doctorName"),
                        rs.getString("service"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getString("paymentMethod"),
                        rs.getString("date")!=null?rs.getString("date"):""
                ));
            }
        }catch (SQLException e){
            System.out.println("error getting bills: " + e.getMessage());
        }
        return result;
    }
    public List<Prescription> getAllPrescriptions() {
        List<Prescription> result = new ArrayList<>();
        String sql="SELECT * FROM prescriptions";
        try(Statement st=connection.createStatement()){
            ResultSet rs= st.executeQuery(sql);
            while (rs.next()){
                result.add(new Prescription(
                        rs.getInt("id"),
                        rs.getInt("patientID"),
                        rs.getInt("doctorID"),
                        rs.getString("medicineName"),
                        rs.getString("dosage"),
                        rs.getString("duration"),
                        rs.getString("instructions")
                ));
            }
        }catch (SQLException e){
            System.out.println("error getting prescriptions: " + e.getMessage());
        }
        return result;
    }
    public void addPrescription(Prescription p) {
        String sql="INSERT INTO prescriptions (patientID,doctorID,medicineName,dosage,instructions,duration,dateIssued) VALUES (?,?,?,?,?,?,?)";
        try(PreparedStatement ps= connection.prepareStatement(sql)){
            ps.setInt(1,p.getPatientId());
            ps.setInt(2,p.getDoctorId());
            ps.setString(3,p.getMedicineName());
            ps.setString(4,p.getDosage());
            ps.setString(5,p.getInstructions());
            ps.setString(6, p.getDuration());
            ps.setString(7,java.time.LocalDate.now().toString());
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("error adding prescription: " + e.getMessage());
        }
    }
    public List<Prescription> getPrescriptionsByPatient(int patientId){
        List<Prescription> result=new ArrayList<>();
        try(Statement st=connection.createStatement()){
            ResultSet rs=st.executeQuery("SELECT * FROM prescriptions WHERE patientID = "+patientId);
            while(rs.next()){
                result.add(new Prescription(
                        rs.getInt("id"),
                        rs.getInt("patientID"),
                        rs.getInt("doctorID"),
                        rs.getString("medicineName"),
                        rs.getString("dosage"),
                        rs.getString("duration"),
                        rs.getString("instructions")
                ));
            }
        }catch(SQLException e){
            System.out.println("Failed to get prescriptions by patient: "+e.getMessage());
        }
        return result;
    }

    public void assignRoom(int roomId, int patientId) {
        String sql="UPDATE rooms SET status='Occupied', assignedPatientID=? WHERE id=?";
        try(PreparedStatement ps= connection.prepareStatement(sql)){
            ps.setInt(1,patientId);
            ps.setInt(2,roomId);
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("error assigning room: " + e.getMessage());
        }
    }
    public void releaseRoom(int roomId) {
        String sql="UPDATE rooms SET status='Available', assignedPatientID=NULL WHERE id=?";
        try(PreparedStatement ps= connection.prepareStatement(sql)){
            ps.setInt(1,roomId);
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("error releasing room: " + e.getMessage());
        }
    }
    public List<Room> getAllRooms(){
        List<Room> result=new ArrayList<>();
        try {
            Statement st=connection.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM rooms");
            while(rs.next()){
                Room r = new Room(
                        rs.getInt("assignedPatientID"),
                        rs.getInt("id"),
                        Integer.parseInt(rs.getString("roomNumber")),
                        rs.getString("status"),
                        rs.getString("roomtype")
                );
                result.add(r);
            }
        }catch (SQLException e){
            System.out.println("Error loading rooms: "+e.getMessage());
        }
        return result;
    }
    public List<Room> getAvailableRooms() {
        List<Room> result = new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement(
                "SELECT * FROM rooms WHERE status ='Available'")){
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Room r=new Room(
                        rs.getInt("assignedPatientID"),
                        rs.getInt("id"),
                        Integer.parseInt(rs.getString("roomNumber")),
                        rs.getString("status"),
                        rs.getString("roomtype")
                );
                result.add(r);
            }
        }catch (SQLException e){
            System.out.println("Error getting available rooms"+e.getMessage());
            return null;
        }
        return result;
    }
    public Room getAvailableRoom(){
        String sql = "SELECT * FROM rooms WHERE status='Available' LIMIT 1";
        try(Statement st=connection.createStatement()){
            ResultSet rs=st.executeQuery(sql);
            if(rs.next()){
                return new Room(
                        rs.getInt("assignedPatientID"),
                        rs.getInt("id"),
                        Integer.parseInt(rs.getString("roomNumber")),
                        rs.getString("status"),
                        rs.getString("roomtype")
                );
            }
        }catch(SQLException e){
            System.out.println("Error getting room: "+e.getMessage());
        }
        return null;
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
    public void dischargeRoom(int roomId){
        try(Statement st=connection.createStatement()){
            st.execute("UPDATE rooms SET status='Cleaning',assignedPatientID=NULL WHERE id="+roomId);
        }catch(SQLException e){
            System.out.println("Discharge failed: "+e.getMessage());
        }
    }
    public void saveTwoFactorCode(String username,String code,String expiresAt){
        try(PreparedStatement ps=connection.prepareStatement("INSERT OR REPLACE INTO twoFactorCodes (username,code,expiresAt) VALUES (?,?,?)")){
            ps.setString(1,username);
            ps.setString(2,code);
            ps.setString(3,expiresAt);
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println("Save 2FA code failed: "+e.getMessage());
        }
    }

    public String getTwoFactorCode(String username){
        try(PreparedStatement ps=connection.prepareStatement("SELECT code,expiresAt FROM twoFactorCodes WHERE username = ?")){
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                String expiresAt=rs.getString("expiresAt");
                if (java.time.LocalDateTime.now().isBefore(java.time.LocalDateTime.parse(expiresAt))){
                    return rs.getString("code");
                }
            }
        }catch (SQLException e){
            System.out.println("Get 2FA code failed: "+e.getMessage());
        }
        return null;
    }

    public void deleteTwoFactorCode(String username){
        try(PreparedStatement ps = connection.prepareStatement("DELETE FROM twoFactorCodes WHERE username = ?")){
            ps.setString(1,username);
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("Delete 2FA code failed: "+e.getMessage());
        }
    }

    public boolean isTwoFactorEnabled(String username){
        try(PreparedStatement ps=connection.prepareStatement("SELECT twoFactorEnabled FROM settings WHERE username = ?")){
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return rs.getInt("twoFactorEnabled")==1;
            }
        }catch(SQLException e){
            System.out.println("2FA check failed: "+e.getMessage());
        }
        return false;
    }

    public void setTwoFactorEnabled(String username,boolean enabled){
        try(PreparedStatement ps=connection.prepareStatement("INSERT OR REPLACE INTO settings (username,twoFactorEnabled) VALUES (?,?)")){
            ps.setString(1,username);
            ps.setInt(2,enabled ? 1 : 0);
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("Set 2FA failed: "+e.getMessage());
        }
    }
}