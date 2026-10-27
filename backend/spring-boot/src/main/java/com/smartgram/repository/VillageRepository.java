package com.smartgram.repository;

import com.smartgram.model.Village;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class VillageRepository {

    private final Map<String, Village> database = new LinkedHashMap<>();

    public VillageRepository() {
        initDefaultVillages();
    }

    private void initDefaultVillages() {
        // Nagazari
        database.put("nagazari", new Village(
            "nagazari", "Nagazari", 8500, "28800 sq meter", 1200,
            List.of("Sugarcane", "Cotton", "Jowar"),
            List.of("Agriculture", "Sugar Mill", "Dairy"),
            1, 1, 1,
            Map.of("boreWell", 45, "well", 12, "lake", 1, "own", 2),
            Map.of("agricultural", "8 hours", "domestic", "24 hours"),
            List.of("Navapur Tank", "Mutha Tank"),
            "[21.08266, 78.60331]",
            "A progressive village in Maharashtra with strong agricultural base"
        ));

        // Ahmednagar
        database.put("ahmednagar", new Village(
            "ahmednagar", "Ahmednagar", 12000, "35000 sq meter", 1800,
            List.of("Wheat", "Cotton", "Onion"),
            List.of("Agriculture", "Trade", "Services"),
            1, 2, 2,
            Map.of("boreWell", 60, "well", 20, "lake", 2, "own", 5),
            Map.of("agricultural", "10 hours", "domestic", "24 hours"),
            List.of("Ahmednagar Tank", "Mula Tank"),
            "[19.0919, 75.3235]",
            "Major agricultural hub in Ahmednagar district"
        ));

        // Manjara
        database.put("manjara", new Village(
            "manjara", "Manjara", 5200, "18000 sq meter", 750,
            List.of("Rice", "Sugarcane", "Maize"),
            List.of("Agriculture", "Animal Husbandry"),
            1, 1, 0,
            Map.of("boreWell", 25, "well", 8, "lake", 1, "own", 1),
            Map.of("agricultural", "6 hours", "domestic", "20 hours"),
            List.of("Manjara Tank"),
            "[19.4, 75.5]",
            "Developing village with focus on sustainable farming"
        ));

        // Phaltan
        database.put("phaltan", new Village(
            "phaltan", "Phaltan", 9800, "32000 sq meter", 1400,
            List.of("Sugarcane", "Jowar", "Gram"),
            List.of("Agriculture", "Sugar Factory", "Petty Trade"),
            2, 2, 1,
            Map.of("boreWell", 50, "well", 15, "lake", 0, "own", 3),
            Map.of("agricultural", "9 hours", "domestic", "22 hours"),
            List.of("Phaltan Tank", "Bhima Tank"),
            "[18.0, 74.8]",
            "Industrial and agricultural village in Satara district"
        ));

        // Indapur
        database.put("indapur", new Village(
            "indapur", "Indapur", 15000, "45000 sq meter", 2200,
            List.of("Sugarcane", "Cotton", "Wheat"),
            List.of("Agriculture", "Food Processing", "Commerce"),
            2, 3, 2,
            Map.of("boreWell", 80, "well", 25, "lake", 2, "own", 8),
            Map.of("agricultural", "12 hours", "domestic", "24 hours"),
            List.of("Indapur Tank", "Bhima Left Bank Canal"),
            "[18.74, 75.49]",
            "Large developed village with strong infrastructure"
        ));
    }

    public List<Map<String, String>> findAllSummary() {
        List<Map<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, Village> entry : database.entrySet()) {
            list.add(Map.of("id", entry.getKey(), "name", entry.getValue().getName()));
        }
        return list;
    }

    public Optional<Village> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(database.get(id.toLowerCase().trim()));
    }

    public Map<String, Village> getAll() {
        return Collections.unmodifiableMap(database);
    }
}
