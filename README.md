
---

# Culinary Academy ORM Final 🍴  
### Comprehensive JavaFX-Based Culinary Academy Management System  

Welcome to the **Culinary Academy ORM Final** project! This repository represents the final version of a management system designed to streamline operations for a culinary academy, utilizing JavaFX for the user interface and ORM principles for database interactions.  

---

## 📖 Overview  

This project is a Java-based management system designed for culinary academies, emphasizing simplicity and efficiency. The system integrates role-based access, with distinct privileges for administrators and coordinators. Key functionalities include student registration, program and course management, and secure payment handling.  

Key technologies and configurations used:  
- **Property Configuration** for managing application settings.  
- **Session Factory Configuration** for efficient ORM-based database interactions.  
- **MySQL** as the sole database solution.  

---

## ✨ Features  

- **Student Management:**  
  - Registration, updates, and deletion of student records.  
  - Dynamic retrieval of student data through optimized ORM queries.  

- **Program and Course Management:**  
  - Admins can add and update programs and courses.  
  - Programs and courses are seamlessly integrated within the system.  

- **Payment Handling:**  
  - Secure payment management for student fees.  
  - Track and update payment records.  

- **Role-Based Access Control:**  
  - **Admin:** Full access to all system features, including user and data management.  
  - **Coordinator:** Limited access to specific functionalities.  

- **JavaFX-Based UI:**  
  - Lightweight, responsive interface for user interaction.  

---

## 🛠️ Technologies Used  

- **Programming Language:**  
  - Pure Java (JDK 11 or later recommended).  

- **UI Framework:**  
  - JavaFX  

- **Database:**  
  - MySQL  

- **ORM:**  
  - Hibernate  

- **Configurations:**  
  - Property Configuration for centralized settings.  
  - Session Factory Configuration for database session management.  

---


## 📁 Project Structure  

```plaintext  
Culinary-Academy-ORM-Final/  
│  
├── src/  
│   ├── main/  
│   │   ├── java/  
│   │   │   └── com/culinaryacademy/  
│   │   │       ├── controllers/  
│   │   │       ├── models/  
│   │   │       ├── views/  
│   │   │       └── utils/  
│   │   └── resources/  
│   │       └── application.properties  
│  
├── database/  
│   └── database.sql  
└── README.md  
```  

---

## 🧪 Testing  

- **Unit Tests:** Validate functionality of key modules like student registration and payment handling.  
- **Integration Tests:** Ensure smooth interaction between the JavaFX UI, ORM, and MySQL database.  

Run the test suite using your IDE or a Java testing framework.  

---

## 📈 Future Enhancements  

- Add detailed reporting for student payments and course enrollments.  
- Expand user roles and permissions for finer control.  
- Integrate notifications for payments and course updates.  



---

### 📜 License  

This project is licensed under the MIT License. See the `LICENSE` file for details.  

---

