package src.main.controllers;

import src.main.services.DriverService;

import java.util.List;

import src.main.entities.Driver;
import src.main.enums.LicenseType;

// אחראית על ניהול נהגים במערכת
// מספקת ממשק בין הלקוח לשירות ניהול נהגים
public class DriverController {
    private DriverService driverService;
    // constructor 
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    
    // יצירת נהג חדש
    public Driver addDriver(String id, String name, String phone, String licenseTypeStr) {
        LicenseType licenseType;
        try {
            licenseType = LicenseType.valueOf(licenseTypeStr.toUpperCase());
            return driverService.addDriver(id, name, phone, licenseType);
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating driver: " + e.getMessage());
            return null;
        }
    }

    // קבלת נהג לפי מזהה
    public Driver getDriverById(String id) {
        return driverService.getDriverById(id);
    }

    // קבלת נהג לפי סוג רישיון
    // מחזירה את כל הנהגים עם סוג רישיון מסוים
    public List<Driver> getDriversByLicenseType(String licenseTypeStr) {
        LicenseType licenseType;
        try {
            licenseType = LicenseType.valueOf(licenseTypeStr.toUpperCase());
            return driverService.getDriversByLicenseType(licenseType);
        } catch (IllegalArgumentException e) {
            System.out.println("Error getting drivers by license type: " + e.getMessage());
            return null;
        }
    }   

    // קבלת כל הנהגים
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    // עדכון פרטי נהג
    public void updateDriver(String id, String name, String phone, String licenseTypeStr) {
        LicenseType licenseType;
        if( licenseTypeStr != null){
            try {
                licenseType = LicenseType.valueOf(licenseTypeStr.toUpperCase());
                driverService.updateDriver(id, name, phone, licenseType);
            } catch (IllegalArgumentException e) {
                System.out.println("Error updating driver: " + e.getMessage());
            }
        }
    }
    // מחיקת נהג
    // מחזירה true אם הנהג נמחק בהצלחה, אחרת false
    public boolean deleteDriver(String id) {
        return driverService.deleteDriver(id);
    }
    
}
