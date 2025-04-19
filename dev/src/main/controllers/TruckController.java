package src.main.controllers;

import src.main.services.TruckService;

import java.util.List;

import src.main.entities.Truck;
import src.main.enums.LicenseType;

//בקר משאיות
public class TruckController {
    private TruckService truckService;
    
    //constructor
    public TruckController(TruckService truckService) {
        this.truckService = truckService;
    }
    
    // יצירת משאית חדשה
    public Truck addTruck(String regNumber, String model, String emptyWeightStr, String maxWeightStr, String licenseTypeStr) {
        // אימות קלטים
        if (regNumber == null || regNumber.trim().isEmpty() || 
            model == null || model.trim().isEmpty() || 
            emptyWeightStr == null || emptyWeightStr.trim().isEmpty() || 
            maxWeightStr == null || maxWeightStr.trim().isEmpty() || 
            licenseTypeStr == null || licenseTypeStr.trim().isEmpty()) {
            return null;
        }
        
        // פירוש ערכי משקל
        double emptyWeight, maxWeight;
        try {
            emptyWeight = Double.parseDouble(emptyWeightStr);
            maxWeight = Double.parseDouble(maxWeightStr);
        } catch (NumberFormatException e) {
            return null;
        }
        
        // המרת מחרוזת סוג רישיון ל-enum
        LicenseType licenseType;
        try {
            licenseType = LicenseType.valueOf(licenseTypeStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
        
        return truckService.addTruck(regNumber, model, emptyWeight, maxWeight, licenseType);
    }
    
    // מחזיר את כל המשאיות במערכת
    public List<Truck> getAllTrucks() {
        return truckService.getAllTrucks();
    }
    
    // מחזיר משאית לפי מספר רישוי
    public Truck getTruckByRegNumber(String regNumber) {
        return truckService.getTruckByRegNumber(regNumber);
    }
    
    // מחזיר משאיות לפי סוג רישיון
    public List<Truck> getTrucksByLicenseType(String licenseTypeStr) {
        if (licenseTypeStr == null || licenseTypeStr.trim().isEmpty()) {
            return List.of();
        }
        
        try {
            LicenseType licenseType = LicenseType.valueOf(licenseTypeStr);
            return truckService.getTrucksByLicenseType(licenseType);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }
    
    // מחזיר משאיות שיכולות לשאת משקל מסוים
    // אם המשקל לא חוקי או ריק, מחזיר רשימה ריקה
    public List<Truck> getTrucksWithCapacityForWeight(String weightStr) {
        if (weightStr == null || weightStr.trim().isEmpty()) {
            return List.of();
        }
        
        try {
            double weight = Double.parseDouble(weightStr);
            return truckService.getTrucksWithCapacityForWeight(weight);
        } catch (NumberFormatException e) {
            return List.of();
        }
    }
    
    // מעדכן פרטי משאית
    public boolean updateTruck(String regNumber, String model, String emptyWeightStr, String maxWeightStr, String licenseTypeStr) {
        Double emptyWeight = null;
        if (emptyWeightStr != null && !emptyWeightStr.trim().isEmpty()) {
            try {
                emptyWeight = Double.parseDouble(emptyWeightStr);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        Double maxWeight = null;
        if (maxWeightStr != null && !maxWeightStr.trim().isEmpty()) {
            try {
                maxWeight = Double.parseDouble(maxWeightStr);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        LicenseType licenseType = null;
        if (licenseTypeStr != null && !licenseTypeStr.trim().isEmpty()) {
            try {
                licenseType = LicenseType.valueOf(licenseTypeStr);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        
        return truckService.updateTruck(regNumber, model, emptyWeight, maxWeight, licenseType);
    }
    
    // מוחק משאית לפי מספר רישוי
    public boolean deleteTruck(String regNumber) {
        return truckService.deleteTruck(regNumber);
    }
}