package src.main.entities;

import src.main.enums.LicenseType;

public class Driver {
    private String id;
    private String name;
    private String phone;
    private LicenseType licenseType;
    
    //----------------- Constructors -------------------
    public Driver(String id, String name, String phone, LicenseType licenseType) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.licenseType = licenseType;
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
    //----------------- methods -------------------
    public boolean canDrive(Truck truck){
        if (truck==null){
            return false;
        }
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

        Driver that = (Driver) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!phone.equals(that.phone)) return false;
        return licenseType == that.licenseType;
    }
}
