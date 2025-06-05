package transportDev.src.main.mappers;

import employeeDev.src.serviceLayer.DriverSL;
import transportDev.src.main.dtos.TransportDTO;
import transportDev.src.main.dtos.SiteDTO;
import transportDev.src.main.dtos.DriverDTO;
import transportDev.src.main.entities.Transport;
import transportDev.src.main.entities.Truck;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.TransportStatus;
import java.util.ArrayList;
import java.util.List;

public class TransportMapper {

    public static TransportDTO toDTO(Transport transport) {
        if (transport == null) {
            return null;
        }

        List<SiteDTO> destinationDTOs = new ArrayList<>();
        for (Site destination : transport.getDestinations()) {
            destinationDTOs.add(SiteMapper.toDTO(destination));
        }

        return new TransportDTO(
                transport.getId(),
                transport.getDate(),
                transport.getTime(),
                TruckMapper.toDTO(transport.getTruck()),
                DriverMapper.toDTO(transport.getDriver()),
                SiteMapper.toDTO(transport.getSourceSite()),
                destinationDTOs,
                transport.getCurrentWeight(),
                transport.getStatus().toString()
        );
    }

    public static Transport fromDTO(TransportDTO transportDTO, Truck truck, DriverSL driver, Site sourceSite, List<Site> destinations) {
        if (transportDTO == null) {
            return null;
        }

        Transport transport = new Transport(
                transportDTO.getDate(),
                transportDTO.getTime(),
                truck,
                driver,
                sourceSite,
                destinations
        );

        transport.setCurrentWeight(transportDTO.getCurrentWeight());
        
        // Set status if it's not the default PLANNING status
        TransportStatus status = TransportStatus.getByName(transportDTO.getStatus());
        if (status != null) {
            transport.setStatus(status);
        }

        return transport;
    }
} 