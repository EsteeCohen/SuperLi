package src.main.repositories.impl;

import src.main.repositories.TransportRepository;
import src.main.dao.TransportDAO;
import src.main.dao.TruckDAO;
import src.main.dao.DriverDAO;
import src.main.dao.SiteDAO;
import src.main.entities.*;
import src.main.enums.TransportStatus;
import src.main.enums.ShippingZone;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class TransportRepositoryImpl implements TransportRepository {
    private TransportDAO transportDAO;
    private TruckDAO truckDAO;
    private DriverDAO driverDAO;
    private SiteDAO siteDAO;
    
    public TransportRepositoryImpl(TransportDAO transportDAO, TruckDAO truckDAO, 
                                  DriverDAO driverDAO, SiteDAO siteDAO) {
        this.transportDAO = transportDAO;
        this.truckDAO = truckDAO;
        this.driverDAO = driverDAO;
        this.siteDAO = siteDAO;
    }
    
    @Override
    public Transport createTransport(LocalDate date, LocalTime time, String truckId, String driverId, 
                                   String sourceId, List<String> destinationIds) {
        Truck truck = truckDAO.read(truckId);
        Driver driver = driverDAO.read(driverId);
        Site source = siteDAO.read(sourceId);
        
        if (truck == null || driver == null || source == null) return null;
        
        List<Site> destinations = new ArrayList<>();
        for (String destId : destinationIds) {
            Site site = siteDAO.read(destId);
            if (site != null) destinations.add(site);
        }
        
        if (destinations.isEmpty()) return null;
        
        Transport transport = new Transport(date, time, truck, driver, source, destinations);
        transportDAO.create(transport);
        return transport;
    }
    
    @Override
    public Transport findById(int id) {
        return transportDAO.read(id);
    }
    
    @Override
    public List<Transport> findAll() {
        return transportDAO.getAll();
    }
    
    @Override
    public List<Transport> findByDate(LocalDate date) {
        return transportDAO.getByDate(date);
    }
    
    @Override
    public List<Transport> findByStatus(TransportStatus status) {
        return transportDAO.getByStatus(status);
    }
    
    @Override
    public List<Transport> findByZone(ShippingZone zone) {
        return transportDAO.getByZone(zone);
    }
    
    @Override
    public List<Transport> findActiveTransports() {
        return transportDAO.getAll().stream()
            .filter(transport -> transport.getStatus() != TransportStatus.COMPLETED && 
                               transport.getStatus() != TransportStatus.CANCELLED)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public boolean updateStatus(int id, TransportStatus status) {
        Transport transport = transportDAO.read(id);
        if (transport == null) return false;
        
        transport.setStatus(status);
        transportDAO.update(transport);
        return true;
    }
    
    @Override
    public boolean changeTruck(int transportId, String truckId) {
        Transport transport = transportDAO.read(transportId);
        Truck truck = truckDAO.read(truckId);
        
        if (transport == null || truck == null) return false;
        if (!transport.canChangeTruck()) return false;
        
        transport.setTruck(truck);
        transportDAO.update(transport);
        return true;
    }
    
    @Override
    public boolean changeDriver(int transportId, String driverId) {
        Transport transport = transportDAO.read(transportId);
        Driver driver = driverDAO.read(driverId);
        
        if (transport == null || driver == null) return false;
        if (!transport.canChangeDriver()) return false;
        
        transport.setDriver(driver);
        transportDAO.update(transport);
        return true;
    }
    
    @Override
    public boolean changeDateTime(int transportId, LocalDate newDate, LocalTime newTime) {
        Transport transport = transportDAO.read(transportId);
        
        if (transport == null) return false;
        if (transport.getStatus() != TransportStatus.PLANNING) return false;
        
        try {
            transport.setDate(newDate);
            transport.setTime(newTime);
            transportDAO.update(transport);
            return true;
        } catch (IllegalArgumentException e) {
            return false; // Invalid date/time
        }
    }
    
    @Override
    public boolean cancelTransport(int id) {
        Transport transport = transportDAO.read(id);
        if (transport == null || !transport.canBeCancelled()) return false;
        
        transport.setStatus(TransportStatus.CANCELLED);
        transportDAO.update(transport);
        return true;
    }
} 