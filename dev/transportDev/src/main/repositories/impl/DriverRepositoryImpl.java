package transportDev.src.main.repositories.impl;

import transportDev.src.main.repositories.DriverRepository;
import transportDev.src.main.dao.DriverDAO;
import transportDev.src.main.entities.Driver;
import transportDev.src.main.enums.LicenseType;
import java.util.List;

public class DriverRepositoryImpl implements DriverRepository {
    private DriverDAO driverDAO;
    
    public DriverRepositoryImpl(DriverDAO driverDAO) {
        this.driverDAO = driverDAO;
    }
    
    @Override
    public Driver add(String id, String name, String phone, LicenseType licenseType) {
        if (driverDAO.read(id) != null) {
            return null; // Driver already exists
        }
        Driver driver = new Driver(id, name, phone, licenseType);
        driverDAO.create(driver);
        return driver;
    }
    
    @Override
    public Driver findById(String id) {
        return driverDAO.read(id);
    }
    
    @Override
    public List<Driver> findAll() {
        return driverDAO.getAll();
    }
    
    @Override
    public List<Driver> findAvailableDrivers() {
        return driverDAO.getAll().stream()
            .filter(Driver::isAvailable)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Driver> findByLicenseType(LicenseType licenseType) {
        return driverDAO.getByLicenseType(licenseType);
    }
    
    @Override
    public List<Driver> findAvailableByLicenseType(LicenseType licenseType) {
        return driverDAO.getByLicenseType(licenseType).stream()
            .filter(Driver::isAvailable)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public boolean updateDriver(String id, String name, String phone, LicenseType licenseType) {
        Driver driver = driverDAO.read(id);
        if (driver == null) return false;
        
        driver.setName(name);
        driver.setPhone(phone);
        driver.setLicenseType(licenseType);
        driverDAO.update(driver);
        return true;
    }
    
    @Override
    public boolean deleteDriver(String id) {
        if (driverDAO.read(id) == null) return false;
        driverDAO.delete(id);
        return true;
    }
} 