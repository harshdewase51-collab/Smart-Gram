# ☕ SmartGram Java Backend

This directory contains the **pure Java backend** of SmartGram (JDK 21 LTS & Spring Boot 3).

---

## 📁 Structure

```
SmartGram/
├── run-java.bat                   # 1-click batch launcher for Windows
├── run-java.ps1                   # PowerShell launcher
└── backend/
    ├── SmartGramServer.java       # Standalone Java 21 Server (Zero external dependencies!)
    ├── InitFirestore.java         # Firebase Firestore seeder in Java
    ├── bin/                       # Compiled bytecode
    └── spring-boot/               # Enterprise Spring Boot 3 Project
        ├── pom.xml
        └── src/main/java/com/smartgram/
            ├── SmartGramApplication.java
            ├── model/Village.java
            ├── repository/VillageRepository.java
            ├── controller/VillageApiController.java
            ├── controller/WebViewController.java
            └── config/WebMvcConfig.java
```

---

## ⚡ Option 1: Instant Standalone Java Server (Recommended)

Requires **only JDK 21+** (no Maven, Gradle, or external libraries needed).

### How to Run:
From the project root (`D:\SmartGram-main`):
```powershell
# Using the launcher:
.\run-java.ps1

# Or directly with javac/java:
javac -d backend/bin backend/SmartGramServer.java
java -cp backend/bin com.smartgram.SmartGramServer 5002
```

### Endpoints:
- **Homepage:** `http://localhost:5002`
- **Dashboard:** `http://localhost:5002/dashboard`
- **Villages API:** `http://localhost:5002/api/villages`
- **Single Village:** `http://localhost:5002/api/villages/nagazari`

---

## 🍃 Option 2: Enterprise Spring Boot 3

If you prefer using Maven with Spring Boot:

```bash
cd backend/spring-boot
mvn spring-boot:run
```

---

## ☁️ Firestore Seeding (`InitFirestore.java`)

To populate Cloud Firestore with Java:
1. Ensure your `serviceAccountKey.json` is located in `backend/` or project root.
2. Run with Firebase Admin Java SDK on classpath:
```bash
java -cp "backend/bin;lib/*" com.smartgram.InitFirestore
```
