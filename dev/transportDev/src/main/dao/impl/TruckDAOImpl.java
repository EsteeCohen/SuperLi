package src.main.dao.impl;

import src.main.dao.TruckDAO;
import src.main.entities.Truck;
import src.main.enums.LicenseType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TruckDAOImpl implements TruckDAO {
    private Map<String, Truck> trucks = new HashMap<>();
    
    @Override
    public void create(Truck truck) {
        trucks.put(truck.getRegNumber(), truck);
    }
    
    @Override
    public Truck read(String regNumber) {
        return trucks.get(regNumber);
    }
    
    @Override
    public void update(Truck truck) {
        trucks.put(truck.getRegNumber(), truck);
    }
    
    @Override
    public void delete(String regNumber) {
        trucks.remove(regNumber);
    }
    
    @Override
    public List<Truck> getAll() {
        return new ArrayList<>(trucks.values());
    }
    
    @Override
    public List<Truck> getByLicenseType(LicenseType licenseType) {
        return trucks.values().stream()
            .filter(truck -> truck.getLicenseType() == licenseType)
            .collect(java.util.stream.Collectors.toList());
    }
} 