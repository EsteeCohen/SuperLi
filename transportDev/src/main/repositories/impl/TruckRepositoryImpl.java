package src.main.repositories.impl;

import src.main.repositories.TruckRepository;
import src.main.dao.TruckDAO;
import src.main.entities.Truck;
import src.main.enums.LicenseType;
import java.util.List;

public class TruckRepositoryImpl implements TruckRepository {
    private TruckDAO truckDAO;
    
    public TruckRepositoryImpl(TruckDAO truckDAO) {
        this.truckDAO = truckDAO;
    }
    
    @Override
    public Truck add(String regNumber, String model, double emptyWeight, double maxWeight, LicenseType licenseType) {
        if (truckDAO.read(regNumber) != null) {
            return null; // Truck already exists
        }
        Truck truck = new Truck(regNumber, model, emptyWeight, maxWeight, licenseType);
        truckDAO.create(truck);
        return truck;
    }
    
    @Override
    public Truck findByRegNumber(String regNumber) {
        return truckDAO.read(regNumber);
    }
    
    @Override
    public List<Truck> findAll() {
        return truckDAO.getAll();
    }
    
    @Override
    public List<Truck> findAvailableTrucks() {
        return truckDAO.getAll().stream()
            .filter(Truck::isAvailable)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Truck> findByLicenseType(LicenseType licenseType) {
        return truckDAO.getByLicenseType(licenseType);
    }
    
    @Override
    public List<Truck> findAvailableWithCapacity(double requiredCapacity) {
        return truckDAO.getAll().stream()
            .filter(Truck::isAvailable)
            .filter(truck -> (truck.getMaxWeight() - truck.getEmptyWeight()) >= requiredCapacity)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public boolean updateTruck(Truck truck) {
        if (truckDAO.read(truck.getRegNumber()) == null) return false;
        truckDAO.update(truck);
        return true;
    }
    
    @Override
    public boolean deleteTruck(String regNumber) {
        if (truckDAO.read(regNumber) == null) return false;
        truckDAO.delete(regNumber);
        return true;
    }
} 