package transportDev.src.main.entities;

import transportDev.src.main.enums.LicenseType;

public class Driver {
    private String id;
    private String name;
    private String phone;
    private LicenseType licenseType;
    private boolean isAvailable;
    
    //----------------- Constructors -------------------
    public Driver(String id, String name, String phone, LicenseType licenseType) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.licenseType = licenseType;
        this.isAvailable = true;
    }

    //----------------- Getters and Setters -------------------
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
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
    public boolean canDrive(Truck truck){
        return this.licenseType.ordinal() >= truck.getLicenseType().ordinal();
    }

    //----------------- toString -------------------
    @Override
    public String toString() {
        return "DriverService{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", licenseType=" + licenseType +
                '}';
    }
    //----------------- equals -------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        return id.equals(((Driver) o).id); //id is unique

    }
} // :)
