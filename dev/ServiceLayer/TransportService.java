package ServiceLayer;

import BussinessLayer.Transport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransportService {
    private List<Transport> transports = new ArrayList<>();
    private TruckService truckService;
    private DriverService driverService;
    private SiteService siteService;

    // Constructor
    public TransportService(TruckService truckService, DriverService driverService, SiteService siteService){
        this.truckService = truckService;
        this.driverService = driverService;
        this.siteService = siteService;
    }
    // Methods
    public Transport createTransport(LocalDate date, LocalTime time, String truckId, String driverId, String sourceId, List<String> destinationIds, double weight){
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

        Transport transport = new Transport(date, time, truck, driver, source, destinations, weight);
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
    public boolean changeTruck(int transportId, int truckId){
        Transport t = getTransportById(transportId);
        Truck truck = truckService.getTruckByRegNumber(truckId);
        if (t == null || truck == null) return false;
        t.setTruck(truck);
        return true;
    }
    public boolean changeDriver(int transportId, int driverId){
        Transport t = getTransportById(transportId);
        Driver driver = driverService.getDriverById(driverId);
        if (t == null || driver == null) return false;
        t.setDriver(driver);
        return true;
    }
    public boolean addDestination(int transportId, String siteId) {
        Transport transport = getTransportById(transportId);
        Site site = siteService.getSiteById(siteId);

        List<Site> destinations = transport.getDestinations();
        if (!destinations.isEmpty()) {
            ShippingZone zone = destinations.get(0).getShippingZone();
            if (site.getShippingZone() != zone) return false;
        }
        destinations.add(site);
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
        if (t == null) return false;
        if (t.canBeCancelled()) {
            t.setStatus(TransportStatus.CANCELLED);
            return true;
        }
        return false;
    }
    public boolean validateTransport(Transport transport){
        return transport.isWeightValid() && transport.isDriverLicenseValid() && transport.areDestinationsValid();
    }

}
