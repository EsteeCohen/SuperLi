package src.main.repositories;

import src.main.entities.Transport;
import src.main.enums.TransportStatus;
import src.main.enums.ShippingZone;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TransportRepository {
    Transport createTransport(LocalDate date, LocalTime time, String truckId, String driverId, 
                             String sourceId, List<String> destinationIds);
    Transport findById(int id);
    List<Transport> findAll();
    List<Transport> findByDate(LocalDate date);
    List<Transport> findByStatus(TransportStatus status);
    List<Transport> findByZone(ShippingZone zone);
    List<Transport> findActiveTransports();
    boolean updateStatus(int id, TransportStatus status);
    boolean changeTruck(int transportId, String truckId);
    boolean changeDriver(int transportId, String driverId);
    boolean changeDateTime(int transportId, LocalDate newDate, LocalTime newTime);
    boolean cancelTransport(int id);
} 