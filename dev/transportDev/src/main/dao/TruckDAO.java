package src.main.dao;

import src.main.entities.Truck;
import src.main.enums.LicenseType;
import java.util.List;

public interface TruckDAO {
    void create(Truck truck);
    Truck read(String regNumber);
    void update(Truck truck);
    void delete(String regNumber);
    List<Truck> getAll();
    List<Truck> getByLicenseType(LicenseType licenseType);
} 