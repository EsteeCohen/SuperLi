package src.main.controllers;

import src.main.entities.Transport;
import src.main.enums.ShippingZone;
import src.main.enums.TransportStatus;
import src.main.services.TransportService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TransportController {
    private TransportService transportService;

    public TransportController(TransportService transportService){
        this.transportService = transportService;
    }

    public Transport createTransport(LocalDate date, LocalTime time, String truckId, String driverId, String sourceId, List<String> destinationIds) {
        return transportService.createTransport(date, time, truckId, driverId, sourceId, destinationIds);
    }
    public List<Transport> getAllTransports() {
        return transportService.getAllTransports();
    }

    public Transport getTransportById(int id) {
        return transportService.getTransportById(id);
    }

    public List<Transport> getTransportsByDate(LocalDate date) {
        return transportService.getTransportsByDate(date);
    }

    public List<Transport> getTransportsByStatus(TransportStatus status) {
        return transportService.getTransportsByStatus(status);
    }

    public List<Transport> getTransportsByZone(ShippingZone zone) {
        return transportService.getTransportsByZone(zone);
    }

    public boolean updateTransportStatus(int id, TransportStatus newStatus) {
        return transportService.updateTransportStatus(id, newStatus);
    }

    public boolean changeTruck(int transportId, String truckId) {
        return transportService.changeTruck(transportId, truckId);
    }

    public boolean changeDriver(int transportId, String driverId) {
        return transportService.changeDriver(transportId, driverId);
    }

    public boolean addDestination(int transportId, String siteId){
        return transportService.addDestination(transportId, siteId);
    }

    public boolean removeDestination(int transportId, String siteId){
        return transportService.removeDestination(transportId, siteId);
    }

    public boolean cancelTransport(int id) {
        return transportService.cancelTransport(id);
    }


}
