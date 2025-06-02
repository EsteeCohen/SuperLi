package transportDev.src.main.dao.impl;

import transportDev.src.main.dao.TransportDAO;
import transportDev.src.main.entities.Transport;
import transportDev.src.main.enums.TransportStatus;
import transportDev.src.main.enums.ShippingZone;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportDAOImpl implements TransportDAO {
    private Map<Integer, Transport> transports = new HashMap<>();
    
    @Override
    public void create(Transport transport) {
        transports.put(transport.getId(), transport);
    }
    
    @Override
    public Transport read(int id) {
        return transports.get(id);
    }
    
    @Override
    public void update(Transport transport) {
        transports.put(transport.getId(), transport);
    }
    
    @Override
    public void delete(int id) {
        transports.remove(id);
    }
    
    @Override
    public List<Transport> getAll() {
        return new ArrayList<>(transports.values());
    }
    
    @Override
    public List<Transport> getByDate(LocalDate date) {
        return transports.values().stream()
            .filter(transport -> transport.getDate().equals(date))
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Transport> getByStatus(TransportStatus status) {
        return transports.values().stream()
            .filter(transport -> transport.getStatus() == status)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Transport> getByZone(ShippingZone zone) {
        return transports.values().stream()
            .filter(transport -> transport.getDestinations().stream()
                .allMatch(site -> site.getShippingZone() == zone))
            .collect(java.util.stream.Collectors.toList());
    }
} 