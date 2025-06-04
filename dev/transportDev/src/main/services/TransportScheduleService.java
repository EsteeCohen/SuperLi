package src.main.services;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import src.main.entities.Site;
import src.main.entities.Transport;
import src.main.services.TransportService;


public class TransportScheduleService implements ITransportScheduleService {
    private TransportService transportService;

    public TransportScheduleService(TransportService transportService) {
        this.transportService = transportService;
    }

    @Override
    public boolean areThereArrivalsAtTheShift (LocalDateTime startTime, LocalDateTime endTime, Site site) {
        List<Transport> transportsInDate = transportService.getTransportsByDate(startTime.toLocalDate());

        for (Transport transport : transportsInDate) {
            List<Site> destinations = transport.getDestinations();
            LocalDateTime currentArrivalTime = transport.getDate().atTime(transport.getTime());

            for (Site dest : destinations) {
                if (currentArrivalTime.isAfter(endTime)) break;
                if (dest.equals(site) && !currentArrivalTime.isBefore(startTime) && !currentArrivalTime.isAfter(endTime)) {
                    return true;
                }
                currentArrivalTime = currentArrivalTime.plusHours(1);
            }
        }

        return false;
    }

    @Override
    public int getNumberOfDeliveriesToSiteInDate(LocalDate date, Site site) {
        List<Transport> transportsInDate = transportService.getTransportsByDate(date);
        int count = 0;
        for (Transport transport : transportsInDate) {
            if (transport.getDestinations().contains(site)) {
                count++;
            }
        }
        return count;
    }
}
