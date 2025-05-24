package src.main.services;

import java.util.ArrayList;
import java.util.List;

import src.main.entities.Truck;
import src.main.enums.LicenseType;

public class TruckService {
    private List<Truck> trucks;
    
    //בנאי ליצירת שירות משאיות חדש
    public TruckService() {
        this.trucks = new ArrayList<>();
    }
    
    //יוצר משאית חדשה במערכת
    public Truck addTruck(String regNumber, String model, double emptyWeight, double maxWeight, LicenseType licenseType) {
        // אימות קלטים
        if (regNumber == null || regNumber.trim().isEmpty() || 
            model == null || model.trim().isEmpty() || 
            emptyWeight < 0 || maxWeight <= 0 || 
            emptyWeight >= maxWeight || licenseType == null) {
            return null;
        }
        
        // בדיקה אם משאית עם מספר רישוי זה כבר קיימת
        if (getTruckByRegNumber(regNumber) != null) {
            return null;
        }
        
        Truck truck = new Truck(regNumber, model, emptyWeight, maxWeight, licenseType);
        trucks.add(truck);
        return truck;
    }
    
    //מחזיר את כל המשאיות במערכת
    public List<Truck> getAllTrucks() {
        return new ArrayList<>(trucks);
    }
    
    //מחזיר משאית לפי מספר רישוי
    public Truck getTruckByRegNumber(String regNumber) {
        if (regNumber == null || regNumber.trim().isEmpty()) {
            return null;
        }
        
        for (Truck truck : trucks) {
            if (truck.getRegNumber().equals(regNumber)) {
                return truck;
            }
        }
        
        return null;
    }
    
    //מחזיר משאית לפי ID (אליאס ל-getTruckByRegNumber)
    public Truck getTruckById(String id) {
        return getTruckByRegNumber(id);
    }
    
    //מחזיר משאיות לפי סוג רישיון נדרש
    public List<Truck> getTrucksByLicenseType(LicenseType licenseType) {
        if (licenseType == null) {
            return new ArrayList<>();
        }
        
        List<Truck> result = new ArrayList<>();
        for (Truck truck : trucks) {
            if (truck.getLicenseType() == licenseType) {
                result.add(truck);
            }
        }
        
        return result;
    }
    
    //מחזיר משאיות שיכולות לשאת משקל מסוים
    public List<Truck> getTrucksWithCapacityForWeight(double weight) {
        List<Truck> result = new ArrayList<>();
        for (Truck truck : trucks) {
            if (truck.canCarryLoad(weight)) {
                result.add(truck);
            }
        }
        
        return result;
    }
    
    //מעדכן פרטי משאית קיימת
    public boolean updateTruck(String regNumber, String model, Double emptyWeight, Double maxWeight, LicenseType licenseType) {
        Truck truck = getTruckByRegNumber(regNumber);
        
        if (truck == null) {
            return false;
        }
        
        if (model != null && !model.trim().isEmpty()) {
            truck.setModel(model);
        }
        
        if (emptyWeight != null && emptyWeight >= 0) {
            truck.setEmptyWeight(emptyWeight);
        }
        
        if (maxWeight != null && maxWeight > 0 && maxWeight > truck.getEmptyWeight()) {
            truck.setMaxWeight(maxWeight);
        }
        
        if (licenseType != null) {
            truck.setLicenseType(licenseType);
        }
        
        return true;
    }
    
    //מוחק משאית מהמערכת
    public boolean deleteTruck(String regNumber) {
        Truck truck = getTruckByRegNumber(regNumber);
        
        if (truck == null) {
            return false;
        }
        
        return trucks.remove(truck);
    }
    
    //מוחק את כל המשאיות מהמערכת - משמש לאתחול או לבדיקות
    public void clearAllTrucks() {
        trucks.clear();
    }
}
