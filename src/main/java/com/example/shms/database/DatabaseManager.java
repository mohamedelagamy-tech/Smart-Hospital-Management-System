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

            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Sara Mohamed',34,'Female','01098765432','Cairo','A+','Cardiology',1,'sara.mohamed@email.com','sara.mohamed')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Omar Ali',25,'Male','01012345678','Giza','B+','Orthopedics',2,'omar.ali@email.com','omar.ali')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Nour Hassan',45,'Female','01112223344','Alexandria','O+','Neurology',2,'nour.hassan@email.com','nour.hassan')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Karim Adel',30,'Male','01234567890','Cairo','AB+','Emergency',1,'karim.adel@email.com','karim.adel')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Layla Mahmoud',28,'Female','01098001122','Cairo','A-','Dermatology',3,'layla.mahmoud@email.com','layla.mahmoud')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Youssef Tarek',52,'Male','01156789012','Giza','B-','Oncology',2,'youssef.tarek@email.com','youssef.tarek')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Dina Mostafa',38,'Female','01234509876','Cairo','O-','Radiology',3,'dina.mostafa@email.com','dina.mostafa')");
            st.execute("INSERT INTO patients (name,age,gender,phone,address,bloodType,department,priority,email,username) VALUES ('Mohamed Fawzy',28,'Male','01116341931','Alexandria','A+','Cardiology',3,'mohamed.fawzy@email.com','mohamed.fawzy')");
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

            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('101','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('102','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('103','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('201','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('202','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('301','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('302','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('401','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('402','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('303','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('304','Available')");
            st.execute("INSERT INTO rooms (roomNumber,status) VALUES ('501','Available')");

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
    public List<Prescription> getPrescriptionsByPatient(int patientId) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription p : prescriptions) {
            if (p.getPatientId() == patientId) result.add(p);
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
    public Room getAvailableRoom(){
        String sql = "SELECT * FROM rooms WHERE status='Available' LIMIT 1";
        try(Statement st=connection.createStatement()){
            ResultSet rs=st.executeQuery(sql);
            if(rs.next()){
                return new Room(
                        rs.getInt("assignedPatientID"),
                        "",
                        rs.getInt("id"),
                        Integer.parseInt(rs.getString("roomNumber")),
                        rs.getString("status"),
                        ""
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting room: " + e.getMessage());
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
}