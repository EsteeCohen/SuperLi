package transportDev.src.main.services;

import java.sql.Driver;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import employeeDev.src.serviceLayer.DriverSL;
import employeeDev.src.serviceLayer.Interfaces.DriverInfoInterface;
import employeeDev.src.serviceLayer.Interfaces.SiteInfoInterface;
import transportDev.src.main.entities.*;
import transportDev.src.main.enums.ShippingZone;
import transportDev.src.main.enums.TransportStatus;
import transportDev.src.main.dataAccessLayer.TransportDAO;
import transportDev.src.main.mappers.TransportMapper;
import transportDev.src.main.dtos.TransportDTO;

public class TransportService {
    private TransportDAO transportDAO;
    private TruckService truckService;
    private DriverInfoInterface driverService;
    private SiteInfoInterface siteService;
    private OrderService orderService;

    // Constructor
    public TransportService(TruckService truckService, DriverInfoInterface driverService, SiteInfoInterface siteService){
        this.transportDAO = new TransportDAO();
        this.truckService = truckService;
        this.driverService = driverService;
        this.siteService = siteService;
    }
    
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
    
    // Methods
    public Transport createTransport(LocalDate date, LocalTime time, String truckId, String driverId, String sourceId, List<String> destinationIds){
        Truck truck = truckService.getTruckByRegNumber(truckId);
        DriverSL driver = driverService.getDriverById(driverId);
        Site source = siteService.getSiteByName(sourceId);
        List<Site> destinations = new ArrayList<>();
        for (String destId : destinationIds) {
            Site site = siteService.getSiteByName(destId);
            if (site != null) destinations.add(site);
        }

        if (truck == null || driver == null || source == null || destinations.isEmpty()) {
            return null;
        }

        try {
            Transport transport = new Transport(date, time, truck, driver, source, destinations);
            TransportDTO transportDTO = TransportMapper.toDTO(transport);
            transportDAO.insertTransport(transportDTO);
            return transport;
        } catch (Exception e) {
            System.err.println("Error creating transport: " + e.getMessage());
            return null;
        }
    }
    
    public List<Transport> getAllTransports(){
        try {
            List<TransportDTO> transportDTOs = transportDAO.getAllTransports();
            List<Transport> transports = new ArrayList<>();
            for (TransportDTO dto : transportDTOs) {
                // Get the required objects for the mapper
                Truck truck = truckService.getTruckByRegNumber(dto.getTruck().getLicensePlate());
                DriverSL driver = driverService.getDriverById(dto.getDriver().getId());
                Site source = siteService.getSiteByName(dto.getSourceSite().getName());
                
                // For now, create empty destinations list - we'll load them separately
                List<Site> destinations = new ArrayList<>();
                
                if (truck != null && driver != null && source != null) {
                    Transport transport = TransportMapper.fromDTO(dto, truck, driver, source, destinations);
                    transports.add(transport);
                }
            }
            return transports;
        } catch (Exception e) {
            System.err.println("Error retrieving transports: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Transport getTransportById(int id){
        try {
            TransportDTO transportDTO = transportDAO.getTransportById(id);
            if (transportDTO != null) {
                // Get the required objects for the mapper
                Truck truck = truckService.getTruckByRegNumber(transportDTO.getTruck().getLicensePlate());
                DriverSL driver = driverService.getDriverById(transportDTO.getDriver().getId());
                Site source = siteService.getSiteByName(transportDTO.getSourceSite().getName());
                
                // For now, create empty destinations list
                List<Site> destinations = new ArrayList<>();
                
                if (truck != null && driver != null && source != null) {
                    return TransportMapper.fromDTO(transportDTO, truck, driver, source, destinations);
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error retrieving transport by ID: " + e.getMessage());
            return null;
        }
    }
    
    public List<Transport> getTransportsByDate(LocalDate date){
        List<Transport> allTransports = getAllTransports();
        List<Transport> result = new ArrayList<>();
        for (Transport t : allTransports) {
            if (t.getDate().equals(date)) result.add(t);
        }
        return result;
    }
    
    public List<Transport> getTransportsByStatus(TransportStatus status){
        List<Transport> allTransports = getAllTransports();
        List<Transport> result = new ArrayList<>();
        for (Transport t : allTransports) {
            if (t.getStatus() == status) result.add(t);
        }
        return result;
    }
    
    public List<Transport> getTransportsByZone(ShippingZone zone){
        List<Transport> allTransports = getAllTransports();
        List<Transport> result = new ArrayList<>();
        for (Transport t : allTransports) {
            boolean allInZone = t.getDestinations().stream()
                    .allMatch(s -> s.getShippingZone() == zone);
            if (allInZone) result.add(t);
        }
        return result;
    }

    public boolean updateTransportStatus(int id, TransportStatus newStatus){
        try {
            Transport t = getTransportById(id);
            if (t == null) return false;
            t.setStatus(newStatus);
            TransportDTO transportDTO = TransportMapper.toDTO(t);
            transportDAO.updateTransport(transportDTO);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating transport status: " + e.getMessage());
            return false;
        }
    }
    
    public boolean changeTruck(int transportId, String truckId){
        try {
            Transport t = getTransportById(transportId);
            Truck truck = truckService.getTruckByRegNumber(truckId);
            
            if (t == null || truck == null) return false;
            
            // ✅ בדיקה שאפשר לשנות משאית (רק בשלב תכנון)
            if (!t.canChangeTruck()) return false;
            
            t.setTruck(truck);
            TransportDTO transportDTO = TransportMapper.toDTO(t);
            transportDAO.updateTransport(transportDTO);
            return true;
        } catch (Exception e) {
            System.err.println("Error changing truck: " + e.getMessage());
            return false;
        }
    }
    
    public boolean changeDriver(int transportId, String driverId){
        try {
            Transport t = getTransportById(transportId);
            DriverSL driver = driverService.getDriverById(driverId);
            
            if (t == null || driver == null) return false;
            
            // ✅ בדיקה שאפשר לשנות נהג (רק בשלב תכנון)
            if (!t.canChangeDriver()) return false;
            
            t.setDriver(driver);
            TransportDTO transportDTO = TransportMapper.toDTO(t);
            transportDAO.updateTransport(transportDTO);
            return true;
        } catch (Exception e) {
            System.err.println("Error changing driver: " + e.getMessage());
            return false;
        }
    }
    
    public boolean addDestination(int transportId, String siteId) {
        try {
            Transport transport = getTransportById(transportId);
            Site site = siteService.getSiteByName(siteId);
            
            if (transport == null || site == null) return false;

            List<Site> destinations = new ArrayList<>(transport.getDestinations());
            
            if (!destinations.isEmpty()) {
                ShippingZone zone = transport.getSourceSite().getShippingZone();
                if (site.getShippingZone() != zone) return false;
            }
            
            if (!destinations.contains(site)) {
                destinations.add(site);
                transport.setDestinations(destinations);
                // Update transport in database
                TransportDTO transportDTO = TransportMapper.toDTO(transport);
                transportDAO.updateTransport(transportDTO);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error adding destination: " + e.getMessage());
            return false;
        }
    }

    public boolean removeDestination(int transportId, String siteId) {
        try {
            Transport transport = getTransportById(transportId);
            if (transport == null) return false;

            List<Site> destinations = transport.getDestinations();
            boolean removed = destinations.removeIf(site -> site.getName().equals(siteId));
            
            if (removed) {
                transport.setDestinations(destinations);
                // Update transport in database
                TransportDTO transportDTO = TransportMapper.toDTO(transport);
                transportDAO.updateTransport(transportDTO);
            }
            
            return removed;
        } catch (Exception e) {
            System.err.println("Error removing destination: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelTransport(int id){
        try {
            Transport t = getTransportById(id);
            if (t == null)
                return false;
            if (t.canBeCancelled()) {
                List<Order> ordersToCancel = orderService.getOrdersInTransport(id);
                for (Order o : ordersToCancel) {
                    orderService.cancelOrder(o.getId());
                }
                t.setStatus(TransportStatus.CANCELLED);
                TransportDTO transportDTO = TransportMapper.toDTO(t);
                transportDAO.updateTransport(transportDTO);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error cancelling transport: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isTruckAvailable(String truckId) {
        Truck truck = truckService.getTruckByRegNumber(truckId);
        if (truck == null) return false;
        
        // Check if truck is marked as available
        if (!truck.isAvailable()) return false;
        
        // Check if truck is not assigned to any active transport
        List<Transport> allTransports = getAllTransports();
        for (Transport transport : allTransports) {
            if (transport.getTruck().getRegNumber().equals(truckId) && 
                transport.getStatus() != TransportStatus.COMPLETED &&
                transport.getStatus() != TransportStatus.CANCELLED) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isDriverAvailable(String driverId) {
        DriverSL driver = driverService.getDriverById(driverId);
        if (driver == null) return false;
        
        // Check if driver is marked as available
        if (!driver.isAvailableToDrive()) return false;
        
        // Check if driver is not assigned to any active transport
        List<Transport> allTransports = getAllTransports();
        for (Transport transport : allTransports) {
            if (transport.getDriver().getId().equals(driverId) && 
                transport.getStatus() != TransportStatus.COMPLETED &&
                transport.getStatus() != TransportStatus.CANCELLED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate estimated weight for transport planning
     * @param destinationIds List of destination site IDs
     */
    public double calculateEstimatedWeight(List<String> destinationIds) {
        // This method doesn't interact with the database
        return 0.0; // Implementation depends on business logic
    }
}
