package src.main.dtos;

import java.util.List;

public class DriverDTO {
    private String id;
    private String name;
    private String phoneNumber;
    private List<String> licenseTypes;
    private boolean available;

    public DriverDTO(String id, String name, String phoneNumber, List<String> licenseTypes, boolean available) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.licenseTypes = licenseTypes;
        this.available = available;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getLicenseTypes() {
        return licenseTypes;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLicenseTypes(List<String> licenseTypes) {
        this.licenseTypes = licenseTypes;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
} // :)