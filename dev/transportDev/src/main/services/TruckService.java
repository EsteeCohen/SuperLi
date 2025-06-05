package transportDev.src.main.services;

import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.entities.Truck;
import transportDev.src.main.enums.LicenseType;
import transportDev.src.main.dataAccessLayer.TruckDAO;
import transportDev.src.main.mappers.TruckMapper;
import transportDev.src.main.dtos.TruckDTO;

public class TruckService {
    private TruckDAO truckDAO;
    
    //בנאי ליצירת שירות משאיות חדש
    public TruckService() {
        this.truckDAO = new TruckDAO();
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
        
        try {
            Truck truck = new Truck(regNumber, model, emptyWeight, maxWeight, licenseType);
            TruckDTO truckDTO = TruckMapper.toDTO(truck);
            truckDAO.insertTruck(truckDTO);
            return truck;
        } catch (Exception e) {
            System.err.println("Error adding truck to database: " + e.getMessage());
            return null;
        }
    }
    
    //מחזיר את כל המשאיות במערכת
    public List<Truck> getAllTrucks() {
        try {
            List<TruckDTO> truckDTOs = truckDAO.getAllTrucks();
            List<Truck> trucks = new ArrayList<>();
            for (TruckDTO dto : truckDTOs) {
                trucks.add(TruckMapper.fromDTO(dto));
            }
            return trucks;
        } catch (Exception e) {
            System.err.println("Error retrieving trucks from database: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    //מחזיר משאית לפי מספר רישוי
    public Truck getTruckByRegNumber(String regNumber) {
        if (regNumber == null || regNumber.trim().isEmpty()) {
            return null;
        }
        
        try {
            TruckDTO truckDTO = truckDAO.getTruckByLicensePlate(regNumber);
            if (truckDTO != null) {
                return TruckMapper.fromDTO(truckDTO);
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error retrieving truck from database: " + e.getMessage());
            return null;
        }
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
        
        List<Truck> allTrucks = getAllTrucks();
        List<Truck> result = new ArrayList<>();
        for (Truck truck : allTrucks) {
            if (truck.getLicenseType() == licenseType) {
                result.add(truck);
            }
        }
        
        return result;
    }
    
    //מחזיר משאיות שיכולות לשאת משקל מסוים
    public List<Truck> getTrucksWithCapacityForWeight(double weight) {
        List<Truck> allTrucks = getAllTrucks();
        List<Truck> result = new ArrayList<>();
        for (Truck truck : allTrucks) {
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
        
        try {
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
            
            TruckDTO truckDTO = TruckMapper.toDTO(truck);
            truckDAO.updateTruck(truckDTO);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating truck in database: " + e.getMessage());
            return false;
        }
    }
    
    //מוחק משאית מהמערכת
    public boolean deleteTruck(String regNumber) {
        Truck truck = getTruckByRegNumber(regNumber);
        
        if (truck == null) {
            return false;
        }
        
        try {
            truckDAO.deleteTruck(regNumber);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting truck from database: " + e.getMessage());
            return false;
        }
    }
    
    //מוחק את כל המשאיות מהמערכת - משמש לאתחול או לבדיקות
    public void clearAllTrucks() {
        try {
            List<Truck> allTrucks = getAllTrucks();
            for (Truck truck : allTrucks) {
                truckDAO.deleteTruck(truck.getRegNumber());
            }
        } catch (Exception e) {
            System.err.println("Error clearing trucks from database: " + e.getMessage());
        }
    }

    public List<Truck> getAvailableTrucksWithCapacity(double requiredCapacity, TransportService transportService) {
        return getAllTrucks().stream()
            .filter(truck -> truck.getMaxWeight() >= requiredCapacity)
            .filter(truck -> transportService.isTruckAvailable(truck.getRegNumber()))
            .collect(java.util.stream.Collectors.toList());
    }
}
