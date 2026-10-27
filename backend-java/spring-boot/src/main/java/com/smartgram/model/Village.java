package com.smartgram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class Village {
    private String id;
    private String name;
    private int population;
    private String totalArea;
    private int houses;
    private List<String> mainCrops;

    @JsonProperty("IncomesSources")
    private List<String> incomesSources;

    private int communityHall;
    private int healthFacility;
    private int bank;

    @JsonProperty("WaterSupply")
    private Map<String, Integer> waterSupply;

    @JsonProperty("Power Supply")
    private Map<String, String> powerSupply;

    @JsonProperty("Tanks")
    private List<String> tanks;

    private String location;
    private String description;

    public Village() {}

    public Village(String id, String name, int population, String totalArea, int houses,
                   List<String> mainCrops, List<String> incomesSources, int communityHall,
                   int healthFacility, int bank, Map<String, Integer> waterSupply,
                   Map<String, String> powerSupply, List<String> tanks, String location, String description) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.totalArea = totalArea;
        this.houses = houses;
        this.mainCrops = mainCrops;
        this.incomesSources = incomesSources;
        this.communityHall = communityHall;
        this.healthFacility = healthFacility;
        this.bank = bank;
        this.waterSupply = waterSupply;
        this.powerSupply = powerSupply;
        this.tanks = tanks;
        this.location = location;
        this.description = description;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }

    public String getTotalArea() { return totalArea; }
    public void setTotalArea(String totalArea) { this.totalArea = totalArea; }

    public int getHouses() { return houses; }
    public void setHouses(int houses) { this.houses = houses; }

    public List<String> getMainCrops() { return mainCrops; }
    public void setMainCrops(List<String> mainCrops) { this.mainCrops = mainCrops; }

    public List<String> getIncomesSources() { return incomesSources; }
    public void setIncomesSources(List<String> incomesSources) { this.incomesSources = incomesSources; }

    public int getCommunityHall() { return communityHall; }
    public void setCommunityHall(int communityHall) { this.communityHall = communityHall; }

    public int getHealthFacility() { return healthFacility; }
    public void setHealthFacility(int healthFacility) { this.healthFacility = healthFacility; }

    public int getBank() { return bank; }
    public void setBank(int bank) { this.bank = bank; }

    public Map<String, Integer> getWaterSupply() { return waterSupply; }
    public void setWaterSupply(Map<String, Integer> waterSupply) { this.waterSupply = waterSupply; }

    public Map<String, String> getPowerSupply() { return powerSupply; }
    public void setPowerSupply(Map<String, String> powerSupply) { this.powerSupply = powerSupply; }

    public List<String> getTanks() { return tanks; }
    public void setTanks(List<String> tanks) { this.tanks = tanks; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
