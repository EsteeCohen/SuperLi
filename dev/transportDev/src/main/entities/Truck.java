package src.main.entities;

import src.main.enums.LicenseType;

public class Truck {
    private String regNumber;
    private String model;
    private double emptyWeight;
    private double maxWeight;
    private LicenseType licenseType;
    private boolean isAvailable;

    //----------------- Constructors -------------------
    public Truck(String regNumber, String model, double emptyWeight, double maxWeight, LicenseType licenseType) {
        this.regNumber = regNumber;
        this.model = model;
        this.emptyWeight = emptyWeight;
        this.maxWeight = maxWeight;
        this.licenseType = licenseType;
        this.isAvailable = true;
    }
    //----------------- Getters and Setters -------------------
    public String getRegNumber() {
        return regNumber;
    }
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public double getEmptyWeight() {
        return emptyWeight;
    }
    public void setEmptyWeight(double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }
    public double getMaxWeight() {
        return maxWeight;
    }
    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }
    public LicenseType getLicenseType() {
        return licenseType;
    }
    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void available() {
        this.isAvailable = true;
    }

    public void unavailable() {
        this.isAvailable = false;
    }
    //----------------- methods -------------------
    public boolean canCarryLoad(double cargoWeight) {
        return (emptyWeight + cargoWeight) <= maxWeight;
    }
    public double getAvailableCapacity(double cargoWeight) {
        return maxWeight - (emptyWeight + cargoWeight);
    }
    //----------------- toString -------------------
    @Override
    public String toString() {
        return "Truck{" +
                "regNumber='" + regNumber + '\'' +
                ", model='" + model + '\'' +
                ", emptyWeight=" + emptyWeight +
                ", maxWeight=" + maxWeight +
                ", licenseType=" + licenseType +
                '}';
    }
    //----------------- equals -------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Truck)) return false;

        Truck truck = (Truck) o;

        if (Double.compare(truck.emptyWeight, emptyWeight) != 0) return false;
        if (Double.compare(truck.maxWeight, maxWeight) != 0) return false;
        if (!regNumber.equals(truck.regNumber)) return false;
        if (!model.equals(truck.model)) return false;
        return licenseType == truck.licenseType;
    }
    // :)
}
