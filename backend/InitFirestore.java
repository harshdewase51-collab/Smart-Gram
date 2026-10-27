package com.smartgram;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * SmartGram Firebase Firestore Data Initialization (Java)
 * Equivalent to backend/init_firestore.py.
 * Populates the Firestore database with sample village data.
 */
public class InitFirestore {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("🚀 SmartGram Firebase Data Initialization (Java)");
        System.out.println("=================================================");

        Firestore db = initializeFirebase();
        if (db == null) {
            System.err.println("❌ Could not connect to Firestore. Exiting.");
            System.exit(1);
        }

        uploadVillages(db);
    }

    private static Firestore initializeFirebase() {
        try {
            // Check for serviceAccountKey.json
            File keyFile = new File("backend/serviceAccountKey.json");
            if (!keyFile.exists()) {
                keyFile = new File("serviceAccountKey.json");
            }

            FirebaseOptions options;
            if (keyFile.exists()) {
                System.out.println("🔑 Found service account key: " + keyFile.getAbsolutePath());
                FileInputStream serviceAccount = new FileInputStream(keyFile);
                options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
            } else {
                System.out.println("⚠️  Using Google Application Default Credentials...");
                options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();
            }

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            System.out.println("✅ Connected to Firestore successfully!");
            return FirestoreClient.getFirestore();

        } catch (IOException e) {
            System.err.println("❌ Error connecting to Firestore: " + e.getMessage());
            System.err.println("\n⚠️  To use this script:");
            System.err.println("1. Download your Firebase service account key from Firebase Console");
            System.err.println("2. Save it as 'backend/serviceAccountKey.json'");
            System.err.println("3. Run this initialization script again\n");
            return null;
        }
    }

    private static void uploadVillages(Firestore db) {
        List<Map<String, Object>> villages = getSampleVillages();

        try {
            for (Map<String, Object> village : villages) {
                String id = (String) village.get("id");
                Map<String, Object> data = new HashMap<>(village);
                data.remove("id");

                WriteResult result = db.collection("Villages").document(id).set(data).get();
                System.out.println("✅ Uploaded: " + village.get("name") + " (ID: " + id + ") at " + result.getUpdateTime());
            }

            System.out.println("\n✅ Successfully uploaded " + villages.size() + " villages to Firestore!");
            System.out.println("   You can now see them in your Dashboard.");

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("❌ Error uploading villages: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static List<Map<String, Object>> getSampleVillages() {
        List<Map<String, Object>> list = new ArrayList<>();

        // Nagazari
        Map<String, Object> nagazari = new HashMap<>();
        nagazari.put("id", "nagazari");
        nagazari.put("name", "Nagazari");
        nagazari.put("population", 8500);
        nagazari.put("totalArea", "28800 sq meter");
        nagazari.put("houses", 1200);
        nagazari.put("mainCrops", List.of("Sugarcane", "Cotton", "Jowar"));
        nagazari.put("IncomesSources", List.of("Agriculture", "Sugar Mill", "Dairy"));
        nagazari.put("communityHall", 1);
        nagazari.put("healthFacility", 1);
        nagazari.put("bank", 1);
        nagazari.put("WaterSupply", Map.of("boreWell", 45, "well", 12, "lake", 1, "own", 2));
        nagazari.put("Power Supply", Map.of("agricultural", "8 hours", "domestic", "24 hours"));
        nagazari.put("Tanks", List.of("Navapur Tank", "Mutha Tank"));
        nagazari.put("location", "[21.08266, 78.60331]");
        nagazari.put("description", "A progressive village in Maharashtra with strong agricultural base");
        list.add(nagazari);

        // Ahmednagar
        Map<String, Object> ahmednagar = new HashMap<>();
        ahmednagar.put("id", "ahmednagar");
        ahmednagar.put("name", "Ahmednagar");
        ahmednagar.put("population", 12000);
        ahmednagar.put("totalArea", "35000 sq meter");
        ahmednagar.put("houses", 1800);
        ahmednagar.put("mainCrops", List.of("Wheat", "Cotton", "Onion"));
        ahmednagar.put("IncomesSources", List.of("Agriculture", "Trade", "Services"));
        ahmednagar.put("communityHall", 1);
        ahmednagar.put("healthFacility", 2);
        ahmednagar.put("bank", 2);
        ahmednagar.put("WaterSupply", Map.of("boreWell", 60, "well", 20, "lake", 2, "own", 5));
        ahmednagar.put("Power Supply", Map.of("agricultural", "10 hours", "domestic", "24 hours"));
        ahmednagar.put("Tanks", List.of("Ahmednagar Tank", "Mula Tank"));
        ahmednagar.put("location", "[19.0919, 75.3235]");
        ahmednagar.put("description", "Major agricultural hub in Ahmednagar district");
        list.add(ahmednagar);

        // Manjara
        Map<String, Object> manjara = new HashMap<>();
        manjara.put("id", "manjara");
        manjara.put("name", "Manjara");
        manjara.put("population", 5200);
        manjara.put("totalArea", "18000 sq meter");
        manjara.put("houses", 750);
        manjara.put("mainCrops", List.of("Rice", "Sugarcane", "Maize"));
        manjara.put("IncomesSources", List.of("Agriculture", "Animal Husbandry"));
        manjara.put("communityHall", 1);
        manjara.put("healthFacility", 1);
        manjara.put("bank", 0);
        manjara.put("WaterSupply", Map.of("boreWell", 25, "well", 8, "lake", 1, "own", 1));
        manjara.put("Power Supply", Map.of("agricultural", "6 hours", "domestic", "20 hours"));
        manjara.put("Tanks", List.of("Manjara Tank"));
        manjara.put("location", "[19.4, 75.5]");
        manjara.put("description", "Developing village with focus on sustainable farming");
        list.add(manjara);

        // Phaltan
        Map<String, Object> phaltan = new HashMap<>();
        phaltan.put("id", "phaltan");
        phaltan.put("name", "Phaltan");
        phaltan.put("population", 9800);
        phaltan.put("totalArea", "32000 sq meter");
        phaltan.put("houses", 1400);
        phaltan.put("mainCrops", List.of("Sugarcane", "Jowar", "Gram"));
        phaltan.put("IncomesSources", List.of("Agriculture", "Sugar Factory", "Petty Trade"));
        phaltan.put("communityHall", 2);
        phaltan.put("healthFacility", 2);
        phaltan.put("bank", 1);
        phaltan.put("WaterSupply", Map.of("boreWell", 50, "well", 15, "lake", 0, "own", 3));
        phaltan.put("Power Supply", Map.of("agricultural", "9 hours", "domestic", "22 hours"));
        phaltan.put("Tanks", List.of("Phaltan Tank", "Bhima Tank"));
        phaltan.put("location", "[18.0, 74.8]");
        phaltan.put("description", "Industrial and agricultural village in Satara district");
        list.add(phaltan);

        // Indapur
        Map<String, Object> indapur = new HashMap<>();
        indapur.put("id", "indapur");
        indapur.put("name", "Indapur");
        indapur.put("population", 15000);
        indapur.put("totalArea", "45000 sq meter");
        indapur.put("houses", 2200);
        indapur.put("mainCrops", List.of("Sugarcane", "Cotton", "Wheat"));
        indapur.put("IncomesSources", List.of("Agriculture", "Food Processing", "Commerce"));
        indapur.put("communityHall", 2);
        indapur.put("healthFacility", 3);
        indapur.put("bank", 2);
        indapur.put("WaterSupply", Map.of("boreWell", 80, "well", 25, "lake", 2, "own", 8));
        indapur.put("Power Supply", Map.of("agricultural", "12 hours", "domestic", "24 hours"));
        indapur.put("Tanks", List.of("Indapur Tank", "Bhima Left Bank Canal"));
        indapur.put("location", "[18.74, 75.49]");
        indapur.put("description", "Large developed village with strong infrastructure");
        list.add(indapur);

        return list;
    }
}
