package src.main.services;
import java.time.LocalDate;
import java.time.LocalDateTime;
import transportDev.src.main.entities.Site;

public interface ITransportScheduleService {
    // :)
    boolean areThereArrivalsAtTheShift(LocalDateTime startTime, LocalDateTime endTime, Site site);
    int getNumberOfDeliveriesToSiteInDate(LocalDate date, Site site);
}
