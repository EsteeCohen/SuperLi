package transportDev.src.main.repositories;

import java.util.List;
import transportDev.src.main.entities.Driver;
import transportDev.src.main.enums.LicenseType;

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