package transportDev.src.main.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import transportDev.src.main.entities.*;
import transportDev.src.main.enums.ShippingZone;
import transportDev.src.main.enums.TransportStatus;

public class TransportService {
    private List<Transport> transports = new ArrayList<>();
    private TruckService truckService;
    private DriverService driverService;
    private SiteService siteService;
    private OrderService orderService;


    // Constructor
    public TransportService(TruckService truckService, DriverService driverService, SiteService siteService){
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
        Driver driver = driverService.getDriverById(driverId);
        Site source = siteService.getSiteById(sourceId);
        List<Site> destinations = new ArrayList<>();
        for (String destId : destinationIds) {
            Site site = siteService.getSiteById(destId);
            if (site != null) destinations.add(site);
        }

        if (truck == null || driver == null || source == null || destinations.isEmpty()) {
            return null;
        }

        Transport transport = new Transport(date, time, truck, driver, source, destinations);
        transports.add(transport);
        return transport;
    }
    public List<Transport> getAllTransports(){
        return new ArrayList<>(transports);
    }

    public Transport getTransportById(int id){
        for (Transport t : transports) {
            if (t.getId() == id) return t;
        }
        return null;
    }
    public List<Transport> getTransportsByDate(LocalDate date){
        List<Transport> result = new ArrayList<>();
        for (Transport t : transports) {
            if (t.getDate().equals(date)) result.add(t);
        }
        return result;
    }
    public List<Transport> getTransportsByStatus(TransportStatus status){
        List<Transport> result = new ArrayList<>();
        for (Transport t : transports) {
            if (t.getStatus() == status) result.add(t);
        }
        return result;
    }
    public List<Transport> getTransportsByZone(ShippingZone zone){
        List<Transport> result = new ArrayList<>();
        for (Transport t : transports) {
            boolean allInZone = t.getDestinations().stream()
                    .allMatch(s -> s.getShippingZone() == zone);
            if (allInZone) result.add(t);
        }
        return result;
    }

    public boolean updateTransportStatus(int id, TransportStatus newStatus){
        Transport t = getTransportById(id);
        if (t == null) return false;
        t.setStatus(newStatus);
        return true;
    }
    public boolean changeTruck(int transportId, String truckId){
        Transport t = getTransportById(transportId);
        Truck truck = truckService.getTruckByRegNumber(truckId);
        
        if (t == null || truck == null) return false;
        
        // ✅ בדיקה שאפשר לשנות משאית (רק בשלב תכנון)
        if (!t.canChangeTruck()) return false;
        
        t.setTruck(truck);
        return true;
    }
    public boolean changeDriver(int transportId, String driverId){
        Transport t = getTransportById(transportId);
        Driver driver = driverService.getDriverById(driverId);
        
        if (t == null || driver == null) return false;
        
        // ✅ בדיקה שאפשר לשנות נהג (רק בשלב תכנון)
        if (!t.canChangeDriver()) return false;
        
        t.setDriver(driver);
        return true;
    }
    public boolean addDestination(int transportId, String siteId) {
        Transport transport = getTransportById(transportId);
        Site site = siteService.getSiteById(siteId);
        
        if (transport == null || site == null) return false;

        List<Site> destinations = new ArrayList<>(transport.getDestinations());
        
        if (!destinations.isEmpty()) {
            ShippingZone zone = destinations.getFirst().getShippingZone();
            if (site.getShippingZone() != zone) return false;
        }
        
        if (!destinations.contains(site)) {
            destinations.add(site);
            transport.setDestinations(destinations);
        }
        
        return true;
    }

    public boolean removeDestination(int transportId, String siteId) {
        Transport transport = getTransportById(transportId);
        if (transport == null) return false;

        List<Site> destinations = transport.getDestinations();
        return destinations.removeIf(site -> site.getId().equals(siteId));
    }

    public boolean cancelTransport(int id){
        Transport t = getTransportById(id);
        if (t == null)
            return false;
        if (t.canBeCancelled()) {
            List<Order> ordersToCancel = orderService.getOrdersInTransport(id);
            for (Order o : ordersToCancel) {
                orderService.cancelOrder(o.getId());
            }
            t.setStatus(TransportStatus.CANCELLED);
            return true;
        }
        return false;
    }
    
    public boolean isTruckAvailable(String truckId) {
        Truck truck = truckService.getTruckByRegNumber(truckId);
        if (truck == null) return false;
        
        // Check if truck is marked as available
        if (!truck.isAvailable()) return false;
        
        // Check if truck is not assigned to any active transport
        for (Transport transport : transports) {
            if (transport.getTruck().getRegNumber().equals(truckId) && 
                transport.getStatus() != TransportStatus.COMPLETED &&
                transport.getStatus() != TransportStatus.CANCELLED) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isDriverAvailable(String driverId) {
        Driver driver = driverService.getDriverById(driverId);
        if (driver == null) return false;
        
        // Check if driver is marked as available
        if (!driver.isAvailable()) return false;
        
        // Check if driver is not assigned to any active transport
        for (Transport transport : transports) {
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
     * @return Estimated weight in kg
     */
    public double calculateEstimatedWeight(List<String> destinationIds) {
        double baseWeight = 500.0; // Base weight in kg
        int destinationCount = destinationIds.size();
        return baseWeight + (destinationCount * 100.0); // Add 100kg per destination
    }

}
