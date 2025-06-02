package transportDev.src.main.dao;

import transportDev.src.main.entities.Driver;
import transportDev.src.main.enums.LicenseType;
import java.util.List;

public interface DriverDAO {
    void create(Driver driver);
    Driver read(String id);
    void update(Driver driver);
    void delete(String id);
    List<Driver> getAll();
    List<Driver> getByLicenseType(LicenseType licenseType);
} 