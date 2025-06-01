package src.main.repositories;

import src.main.entities.Driver;
import src.main.enums.LicenseType;
import java.util.List;

public interface DriverRepository {
    Driver add(String id, String name, String phone, LicenseType licenseType);
    Driver findById(String id);
    List<Driver> findAll();
    List<Driver> findAvailableDrivers();
    List<Driver> findByLicenseType(LicenseType licenseType);
    List<Driver> findAvailableByLicenseType(LicenseType licenseType);
    boolean updateDriver(String id, String name, String phone, LicenseType licenseType);
    boolean deleteDriver(String id);
} 