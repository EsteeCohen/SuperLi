package src.main.repositories;

import src.main.entities.Truck;
import src.main.enums.LicenseType;
import java.util.List;

public interface TruckRepository {
    Truck add(String regNumber, String model, double emptyWeight, double maxWeight, LicenseType licenseType);
    Truck findByRegNumber(String regNumber);
    List<Truck> findAll();
    List<Truck> findAvailableTrucks();
    List<Truck> findByLicenseType(LicenseType licenseType);
    List<Truck> findAvailableWithCapacity(double requiredCapacity);
    boolean updateTruck(Truck truck);
    boolean deleteTruck(String regNumber);
} 