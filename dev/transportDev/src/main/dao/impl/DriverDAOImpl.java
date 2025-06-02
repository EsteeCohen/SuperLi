package transportDev.src.main.dao.impl;

import transportDev.src.main.dao.DriverDAO;
import transportDev.src.main.entities.Driver;
import transportDev.src.main.enums.LicenseType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverDAOImpl implements DriverDAO {
    private Map<String, Driver> drivers = new HashMap<>();
    
    @Override
    public void create(Driver driver) {
        drivers.put(driver.getId(), driver);
    }
    
    @Override
    public Driver read(String id) {
        return drivers.get(id);
    }
    
    @Override
    public void update(Driver driver) {
        drivers.put(driver.getId(), driver);
    }
    
    @Override
    public void delete(String id) {
        drivers.remove(id);
    }
    
    @Override
    public List<Driver> getAll() {
        return new ArrayList<>(drivers.values());
    }
    
    @Override
    public List<Driver> getByLicenseType(LicenseType licenseType) {
        return drivers.values().stream()
            .filter(driver -> driver.getLicenseType() == licenseType)
            .collect(java.util.stream.Collectors.toList());
    }
} 