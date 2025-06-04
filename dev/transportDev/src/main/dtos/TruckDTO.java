package src.main.dtos;

import java.util.List;

public class TruckDTO {
    private String licensePlate;
    private String model;
    private double emptyWeight;
    private double maxWeight;
    private List<String> requiredLicenseTypes;
    private boolean available;

    public TruckDTO(String licensePlate, String model, double emptyWeight, double maxWeight, 
                   List<String> requiredLicenseTypes, boolean available) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.emptyWeight = emptyWeight;
        this.maxWeight = maxWeight;
        this.requiredLicenseTypes = requiredLicenseTypes;
        this.available = available;
    }

    // Getters
    public String getLicensePlate() {
        return licensePlate;
    }

    public String getModel() {
        return model;
    }

    public double getEmptyWeight() {
        return emptyWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public List<String> getRequiredLicenseTypes() {
        return requiredLicenseTypes;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setEmptyWeight(double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setRequiredLicenseTypes(List<String> requiredLicenseTypes) {
        this.requiredLicenseTypes = requiredLicenseTypes;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
} // :)