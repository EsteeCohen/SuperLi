package src.main.dao;

import src.main.entities.Transport;
import src.main.enums.TransportStatus;
import src.main.enums.ShippingZone;
import java.time.LocalDate;
import java.util.List;

public interface TransportDAO {
    void create(Transport transport);
    Transport read(int id);
    void update(Transport transport);
    void delete(int id);
    List<Transport> getAll();
    List<Transport> getByDate(LocalDate date);
    List<Transport> getByStatus(TransportStatus status);
    List<Transport> getByZone(ShippingZone zone);
} 